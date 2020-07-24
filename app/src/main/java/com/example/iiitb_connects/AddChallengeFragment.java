package com.example.iiitb_connects;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;

public class AddChallengeFragment extends Fragment {

    //Views
    private ImageView templateImg;
    private TextInputEditText challengeName;
    private TextInputEditText description;
    private RelativeLayout darkLayout;
    private LinearLayout progressBarLayout;
    private Button addChallengeButton;

    //Firebase
    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("challengeTemplates");
    DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("Challenges");

    Uri postImgUri;

    //String
    String mChallengeName, mDescription, templateImgURI, templateImgLogo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_challenge, container, false);

        //init views
        templateImg = view.findViewById(R.id.templateImg);
        challengeName = view.findViewById(R.id.challengeName);
        description = view.findViewById(R.id.description);
        darkLayout = view.findViewById(R.id.darkLayout);
        progressBarLayout = view.findViewById(R.id.progressBarLayout);
        addChallengeButton = view.findViewById(R.id.addChallengeButton);

        mDatabaseRef = mDatabaseRef.push();

        templateImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 100);
            }
        });

        addChallengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(challengeName.getText()!=null && !challengeName.getText().toString().trim().equals(""))
                    mChallengeName = challengeName.getText().toString().trim();
                if(description.getText()!=null && !description.getText().toString().trim().equals(""))
                    mDescription = description.getText().toString().trim();
                addChallenge();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("realProfilePhoto") && snapshot.child("realProfilePhoto").getValue()!=null) {
                    templateImgLogo = snapshot.child("realProfilePhoto").getValue().toString();
                    Picasso.get().load(snapshot.child("realProfilePhoto").getValue().toString()).into(templateImg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK && requestCode==100) {
            if(data!=null && data.getData()!=null) {
                postImgUri = data.getData();
                templateImgURI = postImgUri.toString();
                templateImg.setImageURI(postImgUri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addChallenge() {
        if(mChallengeName==null || mDescription==null)
            Toast.makeText(getActivity(), "Error adding challenge!", Toast.LENGTH_SHORT).show();
        else {
            if(postImgUri!=null) {
                try {
                    final String postIdStore = String.valueOf(System.currentTimeMillis());
                    Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), postImgUri);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                    byte[] data = baos.toByteArray();
                    //uploading
                    final UploadTask uploadTask = mStorageRef.child(postIdStore).putBytes(data);
                    uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            mStorageRef.child(postIdStore).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    mDatabaseRef.child("templateImg").setValue(uri.toString());
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    e.printStackTrace();
                                }
                            });
                            darkLayout.setVisibility(View.VISIBLE);
                            progressBarLayout.setVisibility(View.VISIBLE);
                            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            darkLayout.setVisibility(View.GONE);
                            progressBarLayout.setVisibility(View.GONE);
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "uploadFailure", Toast.LENGTH_SHORT).show();
                            darkLayout.setVisibility(View.GONE);
                            progressBarLayout.setVisibility(View.GONE);
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(getContext(), "UriNotFound", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                mDatabaseRef.child("templateImg").setValue(templateImgLogo);
            }

            mDatabaseRef.child("clubName").setValue(MainActivity.sharedPreferences.getString("username", "Error loading username"));
            mDatabaseRef.child("challengeName").setValue(mChallengeName);
            mDatabaseRef.child("description").setValue(mDescription);
            Toast.makeText(getActivity(), "Successfully added", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
