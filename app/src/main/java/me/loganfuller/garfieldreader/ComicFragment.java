package me.loganfuller.garfieldreader;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ComicFragment extends Fragment {

    ImageButton btnNextComic, btnPreviousComic;
    Button btnToday;
    SimpleDraweeView comicView;

    SimpleDateFormat formatter;

    Date dateToLoad;

    Calendar calendar = Calendar.getInstance();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            calendar = (Calendar) savedInstanceState.getSerializable("comicDate");
        }
        loadComic();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_comic, container, false);

        btnNextComic = rootView.findViewById(R.id.btnNextComic);
        btnPreviousComic = rootView.findViewById(R.id.btnPreviousComic);
        btnToday = rootView.findViewById(R.id.btnToday);

        comicView = rootView.findViewById(R.id.imgComic);
        comicView.getHierarchy().setFailureImage(R.drawable.ic_refresh_black_24dp);
        comicView.getHierarchy().setProgressBarImage(new ProgressBarDrawable());

        formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        dateToLoad = calendar.getTime();

        btnNextComic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, 1);
                dateToLoad.setTime(calendar.getTimeInMillis());
                loadComic();
            }
        });

        btnPreviousComic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, -1);
                dateToLoad.setTime(calendar.getTimeInMillis());
                loadComic();
            }
        });

        btnToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateToLoad = Calendar.getInstance().getTime();
                calendar.setTime(dateToLoad);
                loadComic();
            }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("comicDate", calendar);
    }

    private void loadComic() {
        String formattedDate = formatter.format(dateToLoad.getTime());

        Log.d("comicFragment/loadComic", "Loading comic from date: " + formattedDate);
        Uri uri = Uri.parse("https://d1ejxu6vysztl5.cloudfront.net/comics/garfield/" + calendar.get(Calendar.YEAR) + "/" + formattedDate + ".gif");
        comicView.setImageURI(uri);
    }

    public void showDatePicker() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setTargetFragment(ComicFragment.this, 1);
        if (getFragmentManager() != null) {
            datePickerFragment.show(getFragmentManager(), "DatePicker");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        if(resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            try {
                if (bundle != null) {
                    calendar.setTime(formatter.parse(bundle.getString("selectedDate", "error")));
                    dateToLoad = calendar.getTime();
                }
                Log.d("onActivityResult", "Received " + calendar.getTime() + " from DatePickerDialog.");
                loadComic();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
