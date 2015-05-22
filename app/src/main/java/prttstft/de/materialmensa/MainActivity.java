package prttstft.de.materialmensa;

import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;


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
                (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer,(DrawerLayout)findViewById(R.id.drawer_layout),toolbar);











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
}