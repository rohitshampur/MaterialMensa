package de.prttstft.materialmensa.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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

import de.prttstft.materialmensa.extras.DateHelper;
import de.prttstft.materialmensa.logging.L;
import de.prttstft.materialmensa.services.MyService;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import de.prttstft.materialmensa.fragments.FragmentToday;
import de.prttstft.materialmensa.fragments.FragmentTomorrow;
import de.prttstft.materialmensa.R;
import de.prttstft.materialmensa.fragments.FragmentDrawer;
import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;

public class ActivityMain extends AppCompatActivity implements MaterialTabListener {

    private static final int JOB_ID = 100;
    private MaterialTabHost tabHost;
    private ViewPager mPager;
    private JobScheduler mJobScheduler;
    public static final int DAYS_ONE = 0;
    public static final int DAYS_TWO = 1;
    public static final int DAYS_THREE = 2;
    public static final int DAYS_FOUR = 3;
    public static final int DAYS_FIVE = 4;
    public static final int DAYS_SIX = 5;
    public static final int DAYS_SEVEN = 6;
    public static final int DAYS_EIGHT = 7;
    public static int mensaID = 0;
    public static String today;
    public static String tomorrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mJobScheduler = JobScheduler.getInstance(this);
        constructJob();

        //Calendar Test
        DateHelper dateHelper = new DateHelper();

        today = dateHelper.getDate();
        tomorrow = dateHelper.getDate(1);

        // Adding the Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
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

    private void constructJob() {
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(this, MyService.class));
        //builder.setPeriodic(1000)
        builder.setPeriodic(86400000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true);
        mJobScheduler.schedule(builder.build());
    }

    // Options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        if (index == 0 && mensaID != 0) {
            mensaID = 0;
            Intent refresh = new Intent(this, ActivityMain.class);
            startActivity(refresh);
            finish();
        } else if (index == 1 && mensaID != 1) {
            mensaID = 1;
            Intent refresh = new Intent(this, ActivityMain.class);
            startActivity(refresh);
            finish();
        } else if (index == 2 && mensaID != 2) {
            mensaID = 2;
            Intent refresh = new Intent(this, ActivityMain.class);
            startActivity(refresh);
            finish();
        } else if (index == 3 && mensaID != 3) {
            mensaID = 3;
            Intent refresh = new Intent(this, ActivityMain.class);
            startActivity(refresh);
            finish();
        } else if (index == 4 && mensaID != 4) {
            mensaID = 4;
            Intent refresh = new Intent(this, ActivityMain.class);
            startActivity(refresh);
            finish();
        } else if (index == 5 && mensaID != 5) {
            mensaID = 5;
            Intent refresh = new Intent(this, ActivityMain.class);
            startActivity(refresh);
            finish();
        } else if (index == 6 && mensaID != 6) {
            mensaID = 6;
            Intent refresh = new Intent(this, ActivityMain.class);
            startActivity(refresh);
            finish();
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

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private FragmentToday createFragment(int newTab) {
            Bundle bundle = new Bundle();
            bundle.putInt("KEY", newTab);
            FragmentToday fragmentToday = new FragmentToday();
            fragmentToday.setArguments(bundle);
            return fragmentToday;
        }

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public Fragment getItem(int num) {
            //Fragment fragment = null;
            //fragment = FragmentToday.newInstance();
            switch (num) {
                default:
                    //FragmentToday fragment0 = new FragmentToday(0);
                    return createFragment(0);
                case DAYS_TWO:
                    return createFragment(1);
                case DAYS_THREE:
                    //FragmentToday fragment2 = new FragmentToday(2);
                    //return fragment2;
                    return createFragment(2);
                case DAYS_FOUR:
                    return createFragment(3);
                case DAYS_FIVE:
                    return createFragment(4);
                case DAYS_SIX:
                    return createFragment(5);
                case DAYS_SEVEN:
                    return createFragment(6);
                case DAYS_EIGHT:
                    //fragment = FragmentTomorrow.newInstance("", "");
                    return createFragment(7);
            }
        }

        @Override
        public int getCount() {
            return 8;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //return getResources().getStringArray(R.array.tabs)[position];
            DateHelper dateHelper = new DateHelper();
            return dateHelper.getDay(position);
        }
    }
}