package com.example.iiitb_connects;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;


public class SearchFragment extends Fragment {
    //Views
    TabLayout tabLayout;
    AppBarLayout appBarLayout;
    SearchView searchView;
    ViewPager viewPager;
    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        tabLayout = view.findViewById(R.id.searchTabLayout);
        appBarLayout = view.findViewById(R.id.appBar);
        viewPager = view.findViewById(R.id.viewPagerSearch);
        toolbar = view.findViewById(R.id.toolbarInSearch);

        //Popping up of dialog box
        showAlert();

        toolbar.setOnMenuItemClickListener(onMenuItemClickListener);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void setUpViewPager(ViewPager viewPager){
        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new SearchProfileFragment(),"Profiles");
        adapter.addFragment(new SearchQuestionFragment(),"Questions");
        viewPager.setAdapter(adapter);
    }
    AlertDialog alert;
    public void showAlert(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.search_question_alert,null);
        //Initialising Views
        Button okayButton = v.findViewById(R.id.positiveButton);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v).setCancelable(false);
        alert = builder.create();
        alert.show();

        //on Okay click
        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
    }



    androidx.appcompat.widget.Toolbar.OnMenuItemClickListener onMenuItemClickListener =
            new androidx.appcompat.widget.Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch(item.getItemId()) {
                        case R.id.addQuestionMenuItem:
                            startActivity(new Intent(getActivity(), AddQuestionActivity.class));
                            return true;
                    }
                    return false;
                }
            };



}
