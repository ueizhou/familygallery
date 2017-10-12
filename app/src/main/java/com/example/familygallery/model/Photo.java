package com.example.familygallery.model;

/**
 * Created by micky on 2017/10/11.
 */

public class Photo {
    private String mId;

    public Photo(String id) {
        mId = id;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }
}
