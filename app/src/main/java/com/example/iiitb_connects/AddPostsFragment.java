package com.example.iiitb_connects;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_posts, container, false);
        //init views
        acceptBtn = view.findViewById(R.id.acceptButton);
        previewImg = view.findViewById(R.id.previewImg);
        choosePhoto = view.findViewById(R.id.choosePhoto);

        //init onClick methods
        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
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

}

