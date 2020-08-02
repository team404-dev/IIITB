package com.example.iiitb_connects;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddFragment extends Fragment {

    //init views
    private TabLayout tabLayout;
    private ViewPager viewPager;

    //Init tab items
    private List<Fragment> tabs;
    private List<String> tabsTitle;
    ImageView closeBtn;

    //Master key
    String MASTER_KEY;

    //Init pager adapter
    private TabPagerAdapter pagerAdapter;

    //firebase
    DatabaseReference mRefMasterKey;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        //init views
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPagerProfile);
        closeBtn = view.findViewById(R.id.closeBtn);

        mRefMasterKey = FirebaseDatabase.getInstance().getReference("Key Details");
        mRefMasterKey.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("Master Key")){
                    MASTER_KEY = snapshot.child("Master Key").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        //onClick
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        //show master key finder
        showMasterKeyFinder();

        return view;
    }

    private void setUpTabs() {
        tabs = new ArrayList<>();
        tabsTitle = new ArrayList<>();
        tabs.add(new AddPostsFragment());tabsTitle.add("Posts");
        tabs.add(new AddChallengeFragment());tabsTitle.add("Challenges");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //setting up tabs
        setUpTabs();
        pagerAdapter = new TabPagerAdapter(getChildFragmentManager(), tabs, tabsTitle);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    AlertDialog alert;
    private void showMasterKeyFinder() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.master_key_finder, null);

        //init views
        final TextInputEditText masterKey = v.findViewById(R.id.masterKey);
        Button positiveButton = v.findViewById(R.id.positiveButton);
        Button negativeButton = v.findViewById(R.id.negativeButton);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v).setCancelable(false);
        alert = builder.create();
        alert.show();

        //onClick methods
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(masterKey.getText()!=null && masterKey.getText().toString().equals(MASTER_KEY))
                    alert.dismiss();
                else
                    Toast.makeText(getActivity(), "Master key authentication failed!", Toast.LENGTH_SHORT).show();
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}