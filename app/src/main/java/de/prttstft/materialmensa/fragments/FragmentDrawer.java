package de.prttstft.materialmensa.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.prttstft.materialmensa.R;
import de.prttstft.materialmensa.activities.ActivityMain;
import de.prttstft.materialmensa.views.Drawer;
import de.prttstft.materialmensa.adapters.AdapterDrawer;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDrawer extends android.support.v4.app.Fragment {
    public static final String PREF_FILE_NAME = "testpref";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private View containerView;


    public FragmentDrawer() {
        // Required empty public constructor
    }

    public List<Drawer> getData() {
        List<Drawer> data = new ArrayList<>();
        int[] icons = {R.drawable.ic_mensaacademica, R.drawable.ic_mensaforum, R.drawable.ic_cafete, R.drawable.ic_grillcafe, R.drawable.ic_campusdoener, R.drawable.ic_onewaysnack, R.drawable.ic_mensula};
        String[] titles = getResources().getStringArray(R.array.drawer_tabs);
        for (int i = 0; i < titles.length; i++) {
            Drawer current = new Drawer();
            current.iconId = icons[i];
            current.title = titles[i];
            data.add(current);

        }
        return data;
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
        return inflater.inflate(R.layout.fragment_drawer, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstaceState) {
        RecyclerView mRecyclerDrawer = (RecyclerView) view.findViewById(R.id.drawerList);
        AdapterDrawer mAdapter = new AdapterDrawer(getActivity(), getData());
        mRecyclerDrawer.setAdapter(mAdapter);
        mRecyclerDrawer.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecyclerDrawer.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecyclerDrawer, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                ((ActivityMain) getActivity()).onDrawerItemClicked(position - 1);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerClosed(drawerView);

                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer + "");
                }

                getActivity().invalidateOptionsMenu();

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().supportInvalidateOptionsMenu();

            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

        };


        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
                if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
                    mDrawerLayout.openDrawer(containerView);
                }
            }
        });
    }

    public static interface ClickListener {
        public void onClick(View view, int position);


        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean b) {

        }

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

}