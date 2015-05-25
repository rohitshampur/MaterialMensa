package de.prttstft.materialmensa.activities;

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
import android.view.View;

import com.software.shell.fab.ActionButton;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import de.prttstft.materialmensa.fragments.FragmentToday;
import de.prttstft.materialmensa.fragments.FragmentTomorrow;
import de.prttstft.materialmensa.R;
import de.prttstft.materialmensa.fragments.FragmentDrawer;


public class MainActivity extends AppCompatActivity implements MaterialTabListener {

    private Toolbar toolbar;
    private MaterialTabHost tabHost;
    private ViewPager viewPager;
    private ActionButton actionButton;
    public static final int DAYS_TODAY = 0;
    public static final int DAYS_TOMORROW = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Adding the toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
       if (Build.VERSION.SDK_INT >= 21) {
            toolbar.setElevation(4);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        // Adding the Floating Action Button
        initActionButton();
        actionButton.show();



        // Adding the NavigationDrawer Fragment
        FragmentDrawer drawerFragment =
                (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);



        // Adding the Tabs
        tabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        if (Build.VERSION.SDK_INT >= 21) {
            tabHost.setElevation(4);
        }

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


    }

    // This initiates the Floating Action Button
    public void initActionButton() {
        actionButton = (ActionButton) findViewById(R.id.action_button);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               shareIntent();
            }
        });
    }

    private void shareIntent() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        // Build String to Share
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Willst du mit mir Mensen? Heute gibt es ");
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Share"));
    }

    // This hides the Floating Action Button
    public void hideActionButton() {
        if (actionButton.isShown()) {
            actionButton.hide();
        }
    }

    // This shows the Floating Action Button
    public void showActionButton() {
        if (!actionButton.isShown()) {
            actionButton.show();
        }
    }

    // This Hides the Floating Action Button on the first start of the Application
    public void hideActionButtonOnFirstStart() {
        actionButton.removeHideAnimation();
        actionButton.hide();
        actionButton.setHideAnimation(ActionButton.Animations.FADE_OUT);
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