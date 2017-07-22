package me.loganfuller.garfieldreader;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ComicFragment extends Fragment {

    ImageButton btnNextComic, btnPreviousComic;
    SimpleDraweeView comicView;

    SimpleDateFormat formatter;

    Calendar currentComicDate = Calendar.getInstance();
    Calendar minDate = Calendar.getInstance();
    Calendar maxDate = Calendar.getInstance();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            currentComicDate = (Calendar) savedInstanceState.getSerializable("comicDate");
        }
        loadComic();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_comic, container, false);

        btnNextComic = (ImageButton) rootView.findViewById(R.id.btnNextComic);
        btnPreviousComic = (ImageButton) rootView.findViewById(R.id.btnPreviousComic);
        comicView = (SimpleDraweeView) rootView.findViewById(R.id.imgComic);

        btnNextComic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentComicDate.add(Calendar.DATE, 1);
                loadComic();
            }
        });

        btnPreviousComic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentComicDate.add(Calendar.DATE, -1);
                loadComic();
            }
        });

        // The minimum date for viewing comics is June 19th.
        minDate.set(1978, 6-1, 19);
        maxDate.setTime(Calendar.getInstance().getTime());

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("comicDate", currentComicDate);
    }

    private void loadComic() {
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(currentComicDate.getTime());

        Log.d("comicFragment/loadComic", "Loading comic from date: " + formattedDate);
        Uri uri = Uri.parse("https://d1ejxu6vysztl5.cloudfront.net/comics/garfield/" + currentComicDate.get(Calendar.YEAR) + "/" + formattedDate + ".gif");
        comicView.setImageURI(uri);
    }
}
