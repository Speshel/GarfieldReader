package me.loganfuller.garfieldreader;

import android.app.DatePickerDialog;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ImageView imgComic;

    Calendar minDate = Calendar.getInstance();
    int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Register onTouch event
        Point size = new Point();
        this.getWindowManager().getDefaultDisplay().getSize(size);
        screenWidth = size.x;

        // Minimum month is June (6) - 1 because it uses 0th notation
        minDate.set(1978, 6-1, 19);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imgComic = (ImageView) findViewById(R.id.imgComic);
        imgComic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getX() < screenWidth/2) {
                    Log.d("SCREEN_SIDE", event.getX() + "/" + screenWidth + "Left side tapped");
                    return true;
                } else if (event.getX() >= screenWidth/2) {
                    Log.d("SCREEN_SIDE", event.getX() + "/" + screenWidth+ "Right side tapped");
                    return true;
                } else {
                    return false;
                }
            }
        });

        /*
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            View decorView = getWindow().getDecorView();
            // Hide both the navigation bar and the status bar.
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
            // a general rule, you should design your app to hide the status bar whenever you
            // hide the navigation bar.
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
        */

        Glide.with(this)
                .load("https://d1ejxu6vysztl5.cloudfront.net/comics/garfield/2017/2017-07-12.gif")
                .into(imgComic);
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
                openDatePicker();
                return true;
            case R.id.icRandom:
                displayRandomComic();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayRandomComic() {
        long UPPER_RANGE = System.currentTimeMillis() / 1000;
        long LOWER_RANGE = minDate.getTimeInMillis();
        Random rand = new Random();
        long unixRandomDate = LOWER_RANGE +
                (long)(rand.nextDouble()*(UPPER_RANGE - LOWER_RANGE));
        Log.d("displayRandomComic", UPPER_RANGE + "-" + LOWER_RANGE + "---------Timestamp: " + unixRandomDate);
    }

    private void openDatePicker() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar cal = Calendar.getInstance();
                        cal.set(year, monthOfYear, dayOfMonth);
                        String comicDate = formatter.format(cal.getTime());
                        Log.d("OnDateSet", "https://d1ejxu6vysztl5.cloudfront.net/comics/garfield/" + year + "/" + comicDate + ".gif");
                        Glide.with(MainActivity.this)
                                .load("https://d1ejxu6vysztl5.cloudfront.net/comics/garfield/" + year + "/" + comicDate + ".gif")
                                .into(imgComic);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        datePickerDialog.show();
    }
}
