package com.example.iiitb_connects;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

public class AddPostsFragment extends Fragment {

    //img request code
    private final int PICK_IMAGE_REQUEST = 1;

    //Views
    ImageView previewImg;
    Button choosePhoto;
    Button acceptBtn;

    //preview img
    Uri previewImgUri;

    //Permission constants

    private static final int STORAGE_REQUEST_CODE = 200;
    //Array of permissions to be requested
    String storagePermissions[];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_posts, container, false);
        //init views
        acceptBtn = view.findViewById(R.id.acceptButton);
        previewImg = view.findViewById(R.id.previewImg);
        choosePhoto = view.findViewById(R.id.choosePhoto);

        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //init onClick methods
        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkStoragePermissions())
                    requestStoragePermissions();
                else
                    pickFromGallery();
            }
        });
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SetupPost.class);
                intent.putExtra("postImgUri", previewImgUri.toString());
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK && requestCode==PICK_IMAGE_REQUEST) {
            if(data!=null && data.getData()!=null) {
                previewImgUri = data.getData();
                Picasso.get().load(previewImgUri).into(previewImg);
                acceptBtn.setVisibility(View.VISIBLE);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean checkStoragePermissions() {
        boolean result = (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) ==
                (PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private void requestStoragePermissions() {
        ActivityCompat.requestPermissions(getActivity(), storagePermissions, STORAGE_REQUEST_CODE);
    }
    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case STORAGE_REQUEST_CODE: {
                if(grantResults.length>0) {
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(writeStorageAccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(getActivity(), "Enable storage permissions", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}

