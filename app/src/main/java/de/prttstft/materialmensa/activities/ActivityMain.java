package de.prttstft.materialmensa.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.prttstft.materialmensa.database.DBMeals;
import de.prttstft.materialmensa.database.DatabaseHandler;
import de.prttstft.materialmensa.logging.L;
import de.prttstft.materialmensa.materialmensa.MyApplication;
import de.prttstft.materialmensa.pojo.Contact;
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
import me.tatarka.support.os.PersistableBundle;

public class ActivityMain extends AppCompatActivity implements MaterialTabListener {

    private static final int JOB_ID = 100;
    private MaterialTabHost tabHost;
    private ViewPager mPager;
    private JobScheduler mJobScheduler;
    public static final int DAYS_TODAY = 0;
    public static final int DAYS_TOMORROW = 1;
    public static int mensaID = 0;
    public static String today;
    public static String tomorrow;
    Context context;

    List<Contact> Contacts = new ArrayList<Contact>();
    DatabaseHandler dbHandler;
    List<String> allergensList = new ArrayList<String>();
    List<String> allergensFullList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mJobScheduler = JobScheduler.getInstance(this);
        constructJob();
        this.deleteDatabase("mealDatabase.db");
        this.deleteDatabase("mealDatabase");
        dbHandler = new DatabaseHandler(this);

        allergensList.add("1");
        allergensList.add("2");
        allergensList.add("3");

        allergensFullList.add("Fleisch");
        allergensFullList.add("Fisch");
        allergensFullList.add("Käse");

        Contact contact1 = new Contact(2, "Schnitzel", "Hauptspeise", "3,80", "4,80", "5,80", allergensList, allergensFullList);
        dbHandler.createMeal(contact1);
//        Contacts.add(contact1);

        //Contact contact2 = new Contact(dbHandler.getMealsCount(), "Hans", 77);
        //dbHandler.createMeal(contact2);

        L.t(this, String.valueOf(dbHandler.getMealsCount()));
        Contacts.add(dbHandler.getMeal(1));
        L.t(this, Contacts.toString());
/*
        if (dbHandler.getMealsCount() != 0) {
            Contacts.add(dbHandler.getMeal(1));
        }


        if (Contacts.size() > 0) {
            L.t(this, Contacts.toString());
        }
*/
        // Calendar
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        today = df.format(c.getTime());
        //today = "2015-06-15";

        Calendar d = Calendar.getInstance();
        d.add(Calendar.DAY_OF_YEAR, 1);
        SimpleDateFormat dfd = new SimpleDateFormat("yyyy-MM-dd");
        tomorrow = dfd.format(d.getTime());

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
        //builder.setPeriodic(2000)
        builder.setPeriodic(86400000)
                //.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
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

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public Fragment getItem(int num) {
            Fragment fragment = null;
            switch (num) {
                case DAYS_TODAY:
                    fragment = FragmentToday.newInstance();
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