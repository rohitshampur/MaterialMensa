package de.prttstft.materialmensa.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.prttstft.materialmensa.BuildConfig;
import de.prttstft.materialmensa.R;
import de.prttstft.materialmensa.logging.L;


public class ActivityAbout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Adding the Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        if (Build.VERSION.SDK_INT >= 21) {
            toolbar.setElevation(4);
        }
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String today = df.format(c.getTime());

        Calendar d = Calendar.getInstance();
        d.add(Calendar.DAY_OF_YEAR, 1);
        SimpleDateFormat dfd = new SimpleDateFormat("yyyy-MM-dd");
        String tomorrow = dfd.format(d.getTime());

        TextView about_about = (TextView) findViewById(R.id.about_about);
        TextView about_aboutText = (TextView) findViewById(R.id.about_aboutText);

        TextView icons = (TextView) findViewById(R.id.about_icons);
        TextView iconsText = (TextView) findViewById(R.id.about_iconsText);

        TextView libraries = (TextView) findViewById(R.id.about_libraries);
        TextView librariesText = (TextView) findViewById(R.id.about_librariesText);

        TextView api = (TextView) findViewById(R.id.about_api);
        TextView apiText = (TextView) findViewById(R.id.about_apiText);

        about_aboutText.setMovementMethod(LinkMovementMethod.getInstance());
        librariesText.setMovementMethod(LinkMovementMethod.getInstance());
        iconsText.setMovementMethod(LinkMovementMethod.getInstance());
        apiText.setMovementMethod(LinkMovementMethod.getInstance());
}




    // Options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

}