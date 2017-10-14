package com.example.familygallery.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;

import com.example.familygallery.FamilyGalleryApplication;
import com.example.familygallery.R;
import com.example.familygallery.model.Photo;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {
    private static final String TAG = "GalleryFragment";

    private List<Photo> mPhotos = new ArrayList<>();

    private View mView;

    // bluetooth
    private BluetoothAdapter mBluetoothAdapter;
    private List<BluetoothDevice> mBluetoothDevices;

    // gallery view
    private RecyclerView mGalleryRecyclerView;
    private GalleryAdaptor mGalleryAdaptor;

    // share button
    private Button mShareButton;

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_gallery, container, false);

        initBluetooth();

        initGalleryRecyclerView();
        initShareButton();

        initData();

        return mView;
    }

    private void initBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Log.w(TAG, "initBluetooth: bluetooth adaptor is null.");
            Toast.makeText(FamilyGalleryApplication.getContext(), "This device doesnot support bluetooth.", Toast.LENGTH_LONG).show();
            return;
        }

        mBluetoothAdapter.getProfileProxy(FamilyGalleryApplication.getContext(), new BluetoothProfile.ServiceListener() {
            @Override
            public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
                if (bluetoothProfile != null) {
                    mBluetoothDevices = bluetoothProfile.getConnectedDevices();
                }
            }

            @Override
            public void onServiceDisconnected(int i) {

            }
        }, BluetoothProfile.A2DP);
    }

    private void initGalleryRecyclerView() {
        mGalleryRecyclerView = mView.findViewById(R.id.gallery_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mView.getContext());
        mGalleryRecyclerView.setLayoutManager(layoutManager);
        mGalleryAdaptor = new GalleryAdaptor(mPhotos);
        mGalleryRecyclerView.setAdapter(mGalleryAdaptor);
    }

    private void initShareButton() {
        mShareButton = mView.findViewById(R.id.share_btn);
        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBluetoothDevices == null || mBluetoothDevices.size() == 0) {
                    Toast.makeText(FamilyGalleryApplication.getContext(), "no bluetooth device", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (BluetoothDevice remoteDevice : mBluetoothDevices) {
                    Method method = null;
                    try {
                        method = remoteDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }

                    BluetoothSocket socket = null;
                    try {
                        socket = (BluetoothSocket) method.invoke(remoteDevice, 1);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                    try {
                        socket.connect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String filePath = Environment.getExternalStorageDirectory() + File.separator + "FamilyGallery" + File.separator + "example.png";

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("uri", filePath);
                    contentValues.put("destination", remoteDevice.getAddress());
                    contentValues.put("direction", 0);
                    contentValues.put("timestamp", System.currentTimeMillis());
                    getActivity().getContentResolver().insert(Uri.parse("content://com.android.bluetooth.opp/btopp"), contentValues);

                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void initData() {
        mPhotos.add(new Photo("1"));
        mGalleryAdaptor.notifyDataSetChanged();
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
