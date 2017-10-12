package com.example.familygallery.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.familygallery.R;
import com.example.familygallery.model.Photo;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {
    private static final String TAG = "GalleryFragment";

    private List<Photo> mPhotos = new ArrayList<>();

    private RecyclerView mGalleryRecyclerView;
    private GalleryAdaptor mGalleryAdaptor;

    private Button mShareButton;

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        mShareButton = view.findViewById(R.id.share_btn);
        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                mPhotos.add(new Photo("2"));
                mGalleryAdaptor.notifyDataSetChanged();
            }
        });

        mPhotos.add(new Photo("1"));

        mGalleryRecyclerView = view.findViewById(R.id.gallery_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        mGalleryRecyclerView.setLayoutManager(layoutManager);
        mGalleryAdaptor = new GalleryAdaptor(mPhotos);
        mGalleryRecyclerView.setAdapter(mGalleryAdaptor);

        return view;
    }

    private class GalleryAdaptor extends RecyclerView.Adapter<GalleryViewHolder> {
        private static final String TAG = "GalleryAdaptor";
        private List<Photo> mPhotos;

        public GalleryAdaptor(@NonNull List<Photo> photos) {
            mPhotos = photos;
        }

        @Override
        public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_recycler_view_item, parent, false);
            return new GalleryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(GalleryViewHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder: ");
            holder.mImageView.setImageResource(R.mipmap.ic_launcher);
        }

        @Override
        public int getItemCount() {
            return mPhotos.size();
        }
    }

    private class GalleryViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;

        public GalleryViewHolder(View view) {
            super(view);
            mImageView = view.findViewById(R.id.photo_image);
        }
    }
}
