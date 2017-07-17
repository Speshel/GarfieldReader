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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ImageView imgComic;

    ProgressBar imageLoadPercent;

    Calendar minDate = Calendar.getInstance();
    Calendar maxDate = Calendar.getInstance();
    Calendar currentComicDate = Calendar.getInstance();
    int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the progressbar
        imageLoadPercent = (ProgressBar) findViewById(R.id.progressBar);

        // Register onTouch event
        Point size = new Point();
        this.getWindowManager().getDefaultDisplay().getSize(size);
        screenWidth = size.x;

        // The minimum date for viewing comics is June 19th.
        minDate.set(1978, 6-1, 19);
        maxDate.setTime(Calendar.getInstance().getTime());

        // Initialize the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize the ImageView to display comics and set an onTouchListener to detect when a user wants to navigate to a previous/next comic.
        imgComic = (ImageView) findViewById(R.id.imgComic);
        imgComic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getX() < screenWidth / 2) {
                        currentComicDate.add(Calendar.DATE, -1);
                        loadComic();
                    }
                    else if (event.getX() >= screenWidth / 2) {
                        currentComicDate.add(Calendar.DATE, 1);
                        loadComic();
                    }
                }
                return false;
            }
        });
        if(savedInstanceState != null) {
            Calendar comicDate = (Calendar) savedInstanceState.getSerializable("comicDate");
            currentComicDate = comicDate;
            loadComic();
        } else {
            loadComic();
        }
    }

    private void loadComic() {
        if(imageLoadPercent.getVisibility() == View.GONE) {
            imageLoadPercent.setVisibility(View.VISIBLE);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(currentComicDate.getTime());

        Log.d("onCreate", "Loading comic from date: " + formattedDate);
        Glide.with(this)
                .load("https://d1ejxu6vysztl5.cloudfront.net/comics/garfield/" + currentComicDate.get(Calendar.YEAR) + "/" + formattedDate + ".gif")
                .error(R.drawable.ic_error_black_24dp)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        // log exception
                        Log.e("TAG", "Error loading image", e);
                        imageLoadPercent.setVisibility(View.GONE);
                        return false; // important to return false so the error placeholder can be placed
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        imageLoadPercent.setVisibility(View.GONE);
                        return false;
                    }
                })
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
                Toast.makeText(this, "In Development", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openDatePicker() {
        // Get Current Date
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        currentComicDate.set(year, monthOfYear, dayOfMonth);
                        loadComic();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        datePickerDialog.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("comicDate", currentComicDate);
        super.onSaveInstanceState(outState);
    }


}
