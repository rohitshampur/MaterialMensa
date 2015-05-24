package de.prttstft.materialmensa.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.software.shell.fab.ActionButton;

import java.util.ArrayList;
import java.util.List;

import de.prttstft.materialmensa.R;
import de.prttstft.materialmensa.activities.MainActivity;
import de.prttstft.materialmensa.views.NavDraw;
import de.prttstft.materialmensa.adapters.AdapterDrawer;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDrawer extends android.support.v4.app.Fragment implements AdapterDrawer.ClickListener {

    private RecyclerView recyclerView;
    public static final String PREF_FILE_NAME = "testpref";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private AdapterDrawer adapter;

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private View containerView;


    public FragmentDrawer() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));
        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_drawer, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        adapter = new AdapterDrawer(getActivity(), getData());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return layout;
    }

    public List<NavDraw> getData() {
        List<NavDraw> data = new ArrayList<>();
        int[] icons = {R.drawable.ic_ndrawer_icon1, R.drawable.ic_ndrawer_icon2, R.drawable.ic_ndrawer_icon3, R.drawable.ic_ndrawer_icon4, R.drawable.ic_ndrawer_icon1, R.drawable.ic_ndrawer_icon2, R.drawable.ic_ndrawer_icon3};
        String[] titles = getResources().getStringArray(R.array.drawer_tabs);
        for (int i = 0; i < titles.length; i++) {
            // && i < icons.length
            NavDraw current = new NavDraw();
            current.iconId = icons[i];
            //current.iconId = icons[i%icons.length];
            current.title = titles[i];
            //current.title = titles[i%icons.length];
            data.add(current);
        }
        return data;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerClosed(drawerView);
                ((MainActivity)getActivity()).hideActionButton();

                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer + "");
                }

                getActivity().invalidateOptionsMenu();

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                ((MainActivity)getActivity()).showActionButton();

            }

        };
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            ((MainActivity)getActivity()).hideActionButtonOnFirstStart();
            mDrawerLayout.openDrawer(containerView);

        }

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPrefrences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefrences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceName, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }

    @Override
    public void itemClicked(View view, int position) {
        startActivity(new Intent(getActivity(), MainActivity.class));
        /*
         if(position==0){
            Log.d("something","did something");
            startActivity(new Intent(getActivity(), FirstActivity.class));
        }else{
            Log.d("nothing", "do nothing");
        }
        if(position==1){
            Log.d("something","did something");
            startActivity(new Intent(getActivity(), SecondActivity.class));
        }else{
            Log.d("nothing", "do nothing");
        }
        if(position==2){
            Log.d("something","did something");
            startActivity(new Intent(getActivity(), ThirdActivity.class));
        }else{
            Log.d("nothing", "do nothing");
        }
        if(position==3){
            Log.d("something","did something");
            startActivity(new Intent(getActivity(), FourthActivity.class));
        }else{
            Log.d("nothing", "do nothing");
        }
         */
    }
}