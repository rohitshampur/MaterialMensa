package de.prttstft.materialmensa.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import de.prttstft.materialmensa.R;

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

        // Adding the TextViews
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}