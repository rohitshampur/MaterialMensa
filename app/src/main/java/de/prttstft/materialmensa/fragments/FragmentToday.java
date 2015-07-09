package de.prttstft.materialmensa.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.prttstft.materialmensa.R;
import de.prttstft.materialmensa.activities.ActivityMain;
import de.prttstft.materialmensa.adapters.Adapter;
import de.prttstft.materialmensa.adapterExtras.DividerItemDecoration;
import de.prttstft.materialmensa.database.DatabaseHandlerMeals;
import de.prttstft.materialmensa.events.MealsLoadedEvent;
import de.prttstft.materialmensa.extras.Constants;
import de.prttstft.materialmensa.extras.MealSorter;
import de.prttstft.materialmensa.extras.URLBuilder;
import de.prttstft.materialmensa.json.JSONHelper;
import de.prttstft.materialmensa.logging.L;
import de.prttstft.materialmensa.materialmensa.MyApplication;
import de.prttstft.materialmensa.network.VolleySingleton;
import de.prttstft.materialmensa.pojo.Meal;
import de.prttstft.materialmensa.services.MyService;
import de.prttstft.materialmensa.tasks.MyManualTask;

import static de.prttstft.materialmensa.extras.Keys.EndpointToday.*;

public class FragmentToday extends Fragment implements Adapter.ViewHolder.ClickListener {
    private static final String STATE_MEALS = "state_meals";
    private RequestQueue requestQueue;
    public ArrayList<Meal> listMeals = new ArrayList<>();
    private TextView textVolleyError;
    private MealSorter mSorter = new MealSorter();
    private Adapter adapter;
    private ActionModeCallback actionModeCallback = new ActionModeCallback();
    private ActionMode actionMode;
    private Bus bus;

    DatabaseHandlerMeals dbHandler;

    public static FragmentToday newInstance() {
        return new FragmentToday();
    }

    public FragmentToday() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_MEALS, listMeals);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bus = MyApplication.getBus();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        textVolleyError = (TextView) view.findViewById(R.id.textVolleyError);
        JSONHelper jsonHelper = new JSONHelper();
        adapter = new Adapter(this);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        dbHandler = new DatabaseHandlerMeals(getActivity());


        if (savedInstanceState != null) {
            listMeals = savedInstanceState.getParcelableArrayList(STATE_MEALS);
        } else {
            listMeals = MyApplication.getWritableDatabase().getAllMeals();
            if (listMeals.size() == 0) {
                new MyManualTask(getActivity()).execute();
            }
        }

        adapter.setMealList(jsonHelper.setUpAndFilterMealList(listMeals,getActivity()));

        return view;
    }

    @Override
    public void onItemClicked(int position) {
        if (actionMode != null) {
            toggleSelection(position);
        }
    }

    @Override
    public boolean onItemLongClicked(int position) {
        if (actionMode == null) {
            actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
        }

        toggleSelection(position);

        return true;
    }

    private void toggleSelection(int position) {
        adapter.toggleSelection(position);
        int count = adapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @SuppressWarnings("unused")
        private final String TAG = ActionModeCallback.class.getSimpleName();

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_cam, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {

                case R.id.share:
                    shareIntent();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.clearSelection();
            actionMode = null;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @Subscribe
    public void onMealsLoaded(MealsLoadedEvent event){
        //if fragment is visible update ui
        if(getUserVisibleHint()){
            adapter.setMealList(event.meals);
        }
        //otherwise we don't care
    }


    // Share Intent
    private void shareIntent() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Willst du mit mir Mensen? Heute gibt es" + adapter.buildSelectedMealNamesString());
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Share"));
        adapter.emptySelectedMealNameList();
    }


}