package de.prttstft.materialmensa.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import de.prttstft.materialmensa.logging.L;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import de.prttstft.materialmensa.fragments.FragmentToday;
import de.prttstft.materialmensa.fragments.FragmentTomorrow;
import de.prttstft.materialmensa.R;
import de.prttstft.materialmensa.fragments.FragmentDrawer;


public class ActivityMain extends AppCompatActivity implements MaterialTabListener {

    private Toolbar toolbar;
    private MaterialTabHost tabHost;
    private ViewPager mPager;
    public static final int DAYS_TODAY = 0;
    public static final int DAYS_TOMORROW = 1;
    public static int mensaID = 0;
    public static int testt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Adding the Toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        if (Build.VERSION.SDK_INT >= 21) {
            toolbar.setElevation(4);
        }
        setSupportActionBar(toolbar);

        // Adding the NavigationDrawer Fragment
        FragmentDrawer drawerFragment =
                (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);


        // Adding the Tabs
        tabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);
        mPager = (ViewPager) findViewById(R.id.viewPager);
        if (Build.VERSION.SDK_INT >= 21) {
            tabHost.setElevation(4);
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tabHost.setSelectedNavigationItem(position);
            }
        });
        for (int i = 0; i < adapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setText(adapter.getPageTitle(i))
                            .setTabListener(this));
        }

    }

    // Share Intent
    private void shareIntent() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        // Build String to Share
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Willst du mit mir Mensen? Heute gibt es ");
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Share"));
    }


    // Options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(this, ActivityPreferences.class);
            startActivity(i);
            return true;
        }

        if (id == R.id.action_about) {
            Intent i = new Intent(this, ActivityAbout.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onDrawerItemClicked(int index) {
        //mPager.setCurrentItem(index);
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());


        /*String lifestyleChoice = SP.getString("prefLifestyle","0");
        if (lifestyleChoice.equals("1")) {
            L.t(this, "YOU HAVE NO PREFERENCE");
        }*/

        if (index == 0 && mensaID != 0) {
            mensaID = 0;
            testt = index;
            Intent refresh = new Intent(this, ActivityMain.class);
            startActivity(refresh);//Start the same Activity
            finish(); //finish Activity.
        } else if (index == 1 && mensaID != 1) {
            mensaID = 1;
            Intent refresh = new Intent(this, ActivityMain.class);
            startActivity(refresh);//Start the same Activity
            finish(); //finish Activity.
        } else if (index == 2 && mensaID != 2) {
            mensaID = 2;
            Intent refresh = new Intent(this, ActivityMain.class);
            startActivity(refresh);//Start the same Activity
            finish(); //finish Activity.
        } else if (index == 3 && mensaID != 3) {
            mensaID = 3;
            Intent refresh = new Intent(this, ActivityMain.class);
            startActivity(refresh);//Start the same Activity
            finish(); //finish Activity.
        } else if (index == 4 && mensaID != 4) {
            mensaID = 4;
            Intent refresh = new Intent(this, ActivityMain.class);
            startActivity(refresh);//Start the same Activity
            finish(); //finish Activity.
        } else if (index == 5 && mensaID != 5) {
            mensaID = 5;
            Intent refresh = new Intent(this, ActivityMain.class);
            startActivity(refresh);//Start the same Activity
            finish(); //finish Activity.
        } else if (index == 6 && mensaID != 6) {
            mensaID = 6;
            Intent refresh = new Intent(this, ActivityMain.class);
            startActivity(refresh);//Start the same Activity
            finish(); //finish Activity.
        }


    }

    // Tabs Stuff
    @Override
    public void onTabSelected(MaterialTab materialTab) {
        mPager.setCurrentItem(materialTab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {
        mPager.setCurrentItem(materialTab.getPosition());
    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }

    public void onDrawerSlide(float slideOffset) {

    }


    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public Fragment getItem(int num) {
            Fragment fragment = null;
            switch (num) {
                case DAYS_TODAY:
                    fragment = FragmentToday.newInstance("", "");
                    break;
                case DAYS_TOMORROW:
                    fragment = FragmentTomorrow.newInstance("", "");
                    break;
            }
            return fragment;

        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getStringArray(R.array.tabs)[position];
        }
    }
}