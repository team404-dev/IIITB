package com.example.iiitb_connects;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SetupPost extends AppCompatActivity {

    //views
    ImageView closeBtn;
    ImageView postImg;
    TextInputEditText description;
    Button uploadBtn;
    RelativeLayout darkLayout;
    LinearLayout progressBarLayout;

    Uri postImgUri;
    String mDescription;
    String mUsername;
    String mUserDp;
    String mUid;

    //firebase
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference posts = FirebaseDatabase.getInstance().getReference("posts");
    DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
    StorageReference mSRef = FirebaseStorage.getInstance().getReference("posts");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_post);

        Intent intent = getIntent();
        String mPostImgUri = intent.getStringExtra("postImgUri");
        postImgUri = Uri.parse(mPostImgUri);

        //init views
        closeBtn = findViewById(R.id.closeBtn);
        postImg = findViewById(R.id.postImg);
        description = findViewById(R.id.description);
        uploadBtn = findViewById(R.id.uploadBtn);
        darkLayout = findViewById(R.id.darkLayout);
        progressBarLayout = findViewById(R.id.progressBarLayout);

        //setting up the UI
        postImg.setImageURI(postImgUri);

        //onClick methods
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uplaodPost();
            }
        });
    }

    private void uplaodPost(){

        if(description.getText()!=null && !description.getText().toString().trim().equals(""))
            mDescription = description.getText().toString().trim();
        else
            mDescription = "";

        mUid = mAuth.getCurrentUser().getUid();
        mDatabaseRef.child(mUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("profilePhoto") && snapshot.child("profilePhoto").getValue()!=null)
                    mUserDp = snapshot.child("profilePhoto").getValue().toString();
                mUsername = snapshot.child("username").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        try {

            final String postId = String.valueOf(System.currentTimeMillis());
            Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), postImgUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            //uploading
            final UploadTask uploadTask = mSRef.child(postId).putBytes(data);
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    mSRef.child(postId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            DatabaseReference mDRef = posts.push();
                            mDRef.child("userInfo").child("username").setValue(mUsername);
                            mDRef.child("userInfo").child("userDp").setValue(mUserDp);
                            mDRef.child("postsInfo").child("Img").setValue(uri.toString());
                            mDRef.child("postsInfo").child("description").setValue(mDescription);

                            Intent intent = new Intent(SetupPost.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
                    darkLayout.setVisibility(View.VISIBLE);
                    progressBarLayout.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    darkLayout.setVisibility(View.GONE);
                    progressBarLayout.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SetupPost.this, "uploadFailure", Toast.LENGTH_SHORT).show();
                    darkLayout.setVisibility(View.GONE);
                    progressBarLayout.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "UriNotFound", Toast.LENGTH_SHORT).show();
        }
    }
}
