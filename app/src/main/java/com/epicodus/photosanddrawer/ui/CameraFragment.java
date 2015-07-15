package com.epicodus.photosanddrawer.ui;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.epicodus.photosanddrawer.R;

import java.io.File;
import java.io.IOException;


public class CameraFragment extends Fragment {
    public static String TAG = "Camera Fragment";

    private static final int REQUEST_CODE = 1;
    private Bitmap mImage;
    private Uri mImageUri;
    private ImageView mImageView;

    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_camera, container, false);
        Button cameraButton = (Button) rootView.findViewById(R.id.button_camera);
        mImageView = (ImageView) rootView.findViewById(R.id.results);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto(v);
            }
        });

        return rootView;
    }

    private void takePhoto(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                                                                    System.currentTimeMillis() + "_pic.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        mImageUri = Uri.fromFile(photo);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = mImageUri;
                ContentResolver contentResolver = getActivity().getContentResolver();
                contentResolver.notifyChange(selectedImage, null);

                try {
                    mImage = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage);
                    mImageView.setImageBitmap(mImage);
                    Toast.makeText(getActivity(), selectedImage.toString(), Toast.LENGTH_LONG).show();
                    galleryAddPic(selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "failed to load", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void galleryAddPic(Uri selectedImage) {
        Log.v(TAG, "adding image to gallery");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(selectedImage);
        getActivity().sendBroadcast(mediaScanIntent);
    }
}