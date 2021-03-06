package me.loganfuller.garfieldreader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            Log.d("MainActivity/onCreate", "Loaded ComicFragment into activity!");
            ComicFragment comic = new ComicFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, comic, "comicFragment").commit();
        } else {
            Log.d("MainActivity/onCreate", "Saved ComicFragment!");
            ComicFragment comic = (ComicFragment) getSupportFragmentManager().findFragmentByTag("comicFragment");
        }


        // Initialize the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.icDate:
                ComicFragment comicFragment = (ComicFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                comicFragment.showDatePicker();
                return true;
            case R.id.icRandom:
                Toast.makeText(this, "In Development", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_licenses:
                new LibsBuilder()
                        //provide a style (optional) (LIGHT, DARK, LIGHT_DARK_TOOLBAR)
                        .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                        //start the activity
                        .start(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
