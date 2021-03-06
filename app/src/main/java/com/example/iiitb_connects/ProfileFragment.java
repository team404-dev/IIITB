package com.example.iiitb_connects;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    //Init views
    private TextView username;
    private TextView fullName;
    private ImageView profilePhoto,iIV;
    private Button editProfileButton;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ProgressBar PPProgressBar;
    private androidx.appcompat.widget.Toolbar toolbar;

    //Init tab items
    private List<Fragment> tabs;
    private List<String> tabsTitle;

    //Init pager adapter
    private TabPagerAdapter pagerAdapter;

    //Firebase
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //Init views
        username = view.findViewById(R.id.username);
        fullName = view.findViewById(R.id.fullName);
        profilePhoto = view.findViewById(R.id.profilePicture);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPagerProfile);
        toolbar = view.findViewById(R.id.toolbar);
        PPProgressBar = view.findViewById(R.id.PPProgressBar);
        iIV = view.findViewById(R.id.iIV);

        //edit profile onClick method
        editProfileButton = view.findViewById(R.id.editProfileButton);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditProfileActivity.class));
            }
        });

        //toolbar item onClick method
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener);

        //profile photo clicker method
        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater imgPopupInflater = LayoutInflater.from(getActivity());
                View imgPopupView = imgPopupInflater.inflate(R.layout.image_popup, null);
                ImageView imagePopupIv = imgPopupView.findViewById(R.id.imagePopupIv);
                TextView imgPopupBio = imgPopupView.findViewById(R.id.imgPopupBio);
                if(profilePhotoUrl != null) {
                    Picasso.get().load(profilePhotoUrl).into(imagePopupIv);
                }
                if(mBio!=null)
                    imgPopupBio.setText(mBio);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(imgPopupView).create().show();
            }
        });

        iIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),InfoActivity.class));
            }
        });

        return view;
    }

    private void setUpTabs() {
        tabs = new ArrayList<>();
        tabsTitle = new ArrayList<>();
        tabs.add(new ChallengeFragment());tabsTitle.add("Challenges");
        tabs.add(new QuestionFragment());tabsTitle.add("Questions");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(MainActivity.sharedPreferences.getString("username", null)!=null){
            username.setText(MainActivity.sharedPreferences.getString("username", null));
            fullName.setText(MainActivity.sharedPreferences.getString("fullName", null));
        }

        //showing profile info
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if(username.getText()=="Some error occurred!!") {
                    username.setText(dataSnapshot.child("username").getValue().toString());
                    fullName.setText(dataSnapshot.child("fullName").getValue().toString());
                }
                if(dataSnapshot.hasChild("bio") && dataSnapshot.child("bio").getValue()!=null) {
                    mBio = dataSnapshot.child("bio").getValue().toString();
                }
                if(dataSnapshot.hasChild("realProfilePhoto") && dataSnapshot.child("realProfilePhoto").getValue()!=null) {
                    profilePhotoUrl = dataSnapshot.child("realProfilePhoto").getValue().toString();
                    Picasso.get().load(dataSnapshot.child("realProfilePhoto").getValue().toString()).networkPolicy(NetworkPolicy.OFFLINE).into(profilePhoto, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(dataSnapshot.child("realProfilePhoto").getValue().toString()).into(profilePhoto);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            //    Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //setting up tabs
        setUpTabs();
        pagerAdapter = new TabPagerAdapter(getChildFragmentManager(), tabs, tabsTitle);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    String mBio, profilePhotoUrl;
    androidx.appcompat.widget.Toolbar.OnMenuItemClickListener onMenuItemClickListener =
            new androidx.appcompat.widget.Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch(item.getItemId()) {
                        case R.id.signOut:
                            FirebaseAuth.getInstance().signOut();
                            MainActivity.sharedPreferences.edit().clear().apply();
                            Toast.makeText(getActivity(), "Signed out!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                            getActivity().finish();
                            return true;
                    }
                    return false;
                }
            };

    /*public class ImgLoader extends AsyncTask<String, Void, Bitmap> {

        ImageView iv;
        ProgressBar pb;
        Bitmap bmp;

        public ImgLoader(ImageView iv) {
            this.iv = iv;
        }
        public ImgLoader(ImageView iv, ProgressBar pb) {
            this.iv = iv;
            this.pb = pb;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(pb != null) {
                pb.setVisibility(View.VISIBLE);
                iv.setImageBitmap(null);
            }
        }

        @Override
        protected Bitmap doInBackground(String... ImgUrl) {
            try {
                URL url = new URL(ImgUrl[0]);
                InputStream is = url.openStream();
                bmp = BitmapFactory.decodeStream(is);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            iv.setImageBitmap(bitmap);
            if(pb !=null)
                pb.setVisibility(View.GONE);
            super.onPostExecute(bitmap);
        }
    }*/
}