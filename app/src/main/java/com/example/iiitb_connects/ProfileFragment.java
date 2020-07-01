package com.example.iiitb_connects;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    //Init views
    private TextView username;
    private TextView fullName;
    private ImageView profilePhoto;
    private Button editProfileButton;
    private TabLayout tabLayout;
    private ViewPager viewPager;
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
                    Picasso.with(getActivity()).load(profilePhotoUrl).into(imagePopupIv);
                }
                if(mBio!=null)
                    imgPopupBio.setText(mBio);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(imgPopupView).create().show();
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
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(username.getText()==null) {
                    username.setText(dataSnapshot.child("username").getValue().toString());
                    fullName.setText(dataSnapshot.child("fullName").getValue().toString());
                }
                if(dataSnapshot.hasChild("bio") && dataSnapshot.child("bio").getValue()!=null) {
                    mBio = dataSnapshot.child("bio").getValue().toString();
                }
                if(dataSnapshot.hasChild("profilePhoto") && dataSnapshot.child("profilePhoto").getValue()!=null) {
                    profilePhotoUrl = dataSnapshot.child("profilePhoto").getValue().toString();
                    Picasso.with(getActivity()).load(dataSnapshot.child("profilePhoto").getValue().toString()).into(profilePhoto);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
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

}