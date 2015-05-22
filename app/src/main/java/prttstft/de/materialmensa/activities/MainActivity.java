package prttstft.de.materialmensa.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import prttstft.de.materialmensa.fragments.FragmentDayAfterTomorrow;
import prttstft.de.materialmensa.fragments.FragmentToday;
import prttstft.de.materialmensa.fragments.FragmentTomorrow;
import prttstft.de.materialmensa.fragments.MyFragment;
import prttstft.de.materialmensa.R;
import prttstft.de.materialmensa.fragments.NavigationDrawerFragment;


public class MainActivity extends AppCompatActivity implements MaterialTabListener {

    private Toolbar toolbar;
    private MaterialTabHost tabHost;
    private ViewPager viewPager;
    public static final int DAYS_TODAY = 0;
    public static final int DAYS_TOMORROW = 1;
    public static final int DAYS_DAYAFTERTOMORROW = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Adding the toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Adding the NavigationDrawer Fragment
        NavigationDrawerFragment drawerFragment =
                (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        // Adding the Tabs
        tabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
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
/*
        //RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // Populating our data set
        List<data_row_meals> dataItems = new ArrayList<data_row_meals>();
        dataItems.add(new data_row_meals("Sigir eti börek - Rinderhack im orientalischen Blätterteig", "Price", "Contents"));
        dataItems.add(new data_row_meals("Big Summer Chickenburger Menü\n" +
                "Hähnchen | Rucola | Ananas | Mangosauce | Pommes Frites", "Price 2", "Contents 2"));

        // Creating new adapter Object
        data_row_meals_adapter myAdapter = new data_row_meals_adapter(dataItems);
        recyclerView.setAdapter(myAdapter);

        // Setting the layoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(MaterialTab materialTab) {
        viewPager.setCurrentItem(materialTab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {
        viewPager.setCurrentItem(materialTab.getPosition());
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
                    fragment = FragmentToday.newInstance("", "");
                    break;
                case DAYS_TOMORROW:
                    fragment = FragmentTomorrow.newInstance("", "");
                    break;
                case DAYS_DAYAFTERTOMORROW:
                    fragment = FragmentDayAfterTomorrow.newInstance("", "");
                    break;
            }
            return fragment;

        }

        /*public Fragment getItem(int num) {
            MyFragment myFragment = MyFragment.getInstance(num);
            switch (num) {

            }
            return myFragment;
        }*/

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getStringArray(R.array.tabs)[position];
        }
    }
}