package com.example.familygallery.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.familygallery.R;

public class GalleryActivity extends SingleFragmentActivity {

    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_gallery;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.fragment_container);
        if (mFragment == null) {
            mFragment = GalleryFragment.newInstance();
            mFragmentManager.beginTransaction().add(R.id.fragment_container, mFragment).commit();
        }
    }
}
