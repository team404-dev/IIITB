package com.example.iiitb_connects;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

public class EditProfileActivity extends AppCompatActivity {

    //Firebase
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child("profilePhotos");

    //Layout views
    ImageButton closeButton;
    Button changeProfilePhoto, apply;
    ImageView profilePhoto;
    TextInputEditText username, bio;
    LinearLayout progressBarLayout;
    RelativeLayout darkLayout;
    ImageView instagram;
    ImageView linkedIn;

    //User details
    String mUsername;
    String mFullName;
    String mBio;

    //Uri of profile photo picked
    private Uri profilePhotoUri;

    //Permission constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_REQUEST_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_REQUEST_CODE = 400;
    //Array of permissions to be requested
    String cameraPermissions[];
    String storagePermissions[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //Init arrays of permissions
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //Init views
        profilePhoto = findViewById(R.id.profilePhoto);
        username = findViewById(R.id.username);
        //fullName = findViewById(R.id.fullName);
        bio = findViewById(R.id.bio);
        apply = findViewById(R.id.apply);
        instagram = findViewById(R.id.instagram);
        linkedIn = findViewById(R.id.linkedin);
        progressBarLayout = findViewById(R.id.progressBarLayout);
        darkLayout = findViewById(R.id.darkLayout);

        setInfo();

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInfo()) {
                    updateSharedPreferences();
                    mDatabaseRef.child("username").setValue(username.getText().toString());
                //    mDatabaseRef.child("fullName").setValue(fullName.getText().toString());
                    if(bio.getText()!=null && !bio.getText().toString().trim().equals("")) {
                        mDatabaseRef.child("bio").setValue(bio.getText().toString().trim());
                    }
                    MainActivity.returnStatus = true;
                    if(profilePhotoUri != null){
                        try {
                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            final StorageReference mRef = mStorageRef.child(mAuth.getCurrentUser().getUid());
                            Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), profilePhotoUri);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                            bmp.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                            bmp.compress(Bitmap.CompressFormat.JPEG, 0, baos2);
                            byte[] data = baos.toByteArray();
                            byte[] data2 = baos2.toByteArray();
                            //uploading
                            final UploadTask uploadTask = mRef.child("realImg.jpg").putBytes(data);
                            final UploadTask uploadTask2 = mRef.child("templateImg.jpg").putBytes(data2);
                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(EditProfileActivity.this, "Changes applied!", Toast.LENGTH_SHORT).show();
                                    mRef.child("realImg.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            mDatabaseRef.child("realProfilePhoto").setValue(uri.toString());
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            mDatabaseRef.child("realProfilePhoto").setValue(null);
                                        }
                                    });
                                    progressBarLayout.setVisibility(View.GONE);
                                    darkLayout.setVisibility(View.GONE);
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    progressBarLayout.setVisibility(View.GONE);
                                    darkLayout.setVisibility(View.GONE);
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                }
                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressBarLayout.setVisibility(View.VISIBLE);
                                    darkLayout.setVisibility(View.VISIBLE);
                                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                }
                            });
                            uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    mRef.child("templateImg.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            mDatabaseRef.child("templateProfilePhoto").setValue(uri.toString());
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            mDatabaseRef.child("templateProfilePhoto").setValue(null);
                                        }
                                    });
                                }
                            });
                        } catch (Exception e) {
                            Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        mDatabaseRef.child("profilePhoto").setValue(null);
                        Toast.makeText(EditProfileActivity.this, "Changes applied!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(EditProfileActivity.this, "Changes not applied!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        changeProfilePhoto = findViewById(R.id.changeProfilePhoto);
        changeProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String options[] = {"Camera", "Gallery", "Remove photo"};
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                builder.setTitle("Choose from");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0) {
                            //Choose from camera
                            if(!checkCameraPermissions())
                                requestCameraPermissions();
                            else
                                pickFromCamera();
                        }
                        else if(which == 1) {
                            //Choose from gallery
                            if(!checkStoragePermissions())
                                requestStoragePermissions();
                            else
                                pickFromGallery();
                        }
                        else if(which == 2) {
                            //Remove photo
                            profilePhoto.setImageResource(R.drawable.person);
                            mDatabaseRef.child("realProfilePhoto").setValue(null);
                            mDatabaseRef.child("templateProfilePhoto").setValue(null);
                        }
                    }
                }).create().show();
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkAccountDialog("Instagram");
            }
        });

        linkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkAccountDialog("LinkedIn");
            }
        });
    }

    private boolean checkStoragePermissions() {
        boolean result = (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) ==
                (PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private void requestStoragePermissions() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermissions() {
        boolean result = (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)) ==
                (PackageManager.PERMISSION_GRANTED);
        boolean result1 = (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) ==
                (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }
    private void requestCameraPermissions() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    private void setInfo() {
        if(MainActivity.sharedPreferences.getString("username", null) != null)
            username.setText(MainActivity.sharedPreferences.getString("username", null));
        /*if(MainActivity.sharedPreferences.getString("fullName", null) != null)
            fullName.setText(MainActivity.sharedPreferences.getString("fullName", null));*/
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("bio").getValue()!=null)
                    bio.setText(snapshot.child("bio").getValue().toString());
                if(snapshot.child("realProfilePhoto").getValue()!=null)
                    Picasso.get().load(snapshot.child("realProfilePhoto").getValue().toString()).into(profilePhoto);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileActivity.this, "Cannot load data\n"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInfo() {
        /*if( username.getText().toString().trim().equals("") ||
                fullName.getText().toString().trim().equals(""))
            return false;*/

        mUsername = username.getText().toString().trim();
    //    mFullName = fullName.getText().toString().trim();
        if(bio.getText()!=null)
            mBio = bio.getText().toString().trim();
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if(grantResults.length>0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && writeStorageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "Enable camera and storage permissions", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            case STORAGE_REQUEST_CODE: {
                if(grantResults.length>0) {
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(writeStorageAccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Enable storage permissions", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void pickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp desc");

        profilePhotoUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, profilePhotoUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_REQUEST_CODE);
    }

    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == IMAGE_PICK_GALLERY_REQUEST_CODE) {
                if(data != null && data.getData()!=null) {
                    profilePhotoUri = data.getData();
                    Picasso.get().load(profilePhotoUri).into(profilePhoto);
                } else
                    Toast.makeText(this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
            }
            if(requestCode == IMAGE_PICK_CAMERA_REQUEST_CODE) {
                Picasso.get().load(profilePhotoUri).into(profilePhoto);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void updateSharedPreferences() {
        if(username.getText()!=null && !username.getText().toString().trim().equals(""))
            MainActivity.sharedPreferences.edit().putString("username", username.getText().toString().trim()).apply();

    /*    if(fullName.getText()!=null && !fullName.getText().toString().trim().equals(""))
            MainActivity.sharedPreferences.edit().putString("fullName", fullName.getText().toString().trim()).apply();*/

        if(bio.getText()!=null && !bio.getText().toString().trim().equals(""))
            MainActivity.sharedPreferences.edit().putString("bio", bio.getText().toString().trim()).apply();
        else
            MainActivity.sharedPreferences.edit().putString("bio",null).apply();
    }

    AlertDialog alert;
    private void linkAccountDialog(final String accountType) {
        LayoutInflater inflater = LayoutInflater.from(EditProfileActivity.this);
        View v = inflater.inflate(R.layout.link_accounts, null);

        //init views
        final TextInputEditText url = v.findViewById(R.id.url);
        TextView message = v.findViewById(R.id.message);
        Button positiveButton = v.findViewById(R.id.positiveButton);

        String messageText = accountType+" account:";
        message.setText(messageText);

        final AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setView(v);
        alert = builder.create();
        alert.show();

        //onClick methods
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(url.getText()!=null && !url.getText().toString().equals("")) {
                    mDatabaseRef.child(accountType).setValue(url.getText().toString());
                    Toast.makeText(EditProfileActivity.this, "Account linked!", Toast.LENGTH_SHORT).show();
                    alert.dismiss();
                }
                else
                    Toast.makeText(EditProfileActivity.this, "Field empty!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}