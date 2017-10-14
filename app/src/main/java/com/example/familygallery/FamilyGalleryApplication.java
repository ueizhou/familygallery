package com.example.familygallery;


import android.app.Application;
import android.content.Context;

/**
 * Created by micky on 2017/10/13.
 */

public class FamilyGalleryApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
