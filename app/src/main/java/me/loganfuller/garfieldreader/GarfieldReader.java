package me.loganfuller.garfieldreader;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class GarfieldReader extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}