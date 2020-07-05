package com.example.iiitb_connects;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class FragmentSearch2 extends Fragment {
    TabLayout tabLayout1;
    ViewPager viewPager1;
    AppBarLayout appBarLayout1;
    SearchView searchView1;

    //Init tab items
    private List<Fragment> tabs;
    private List<String> tabsTitle;

    //Init pager adapter
    private TabPagerAdapter pagerAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view1 = inflater.inflate(R.layout.fragment_search_2,container,false);

        tabLayout1 = view1.findViewById(R.id.tabLayout1);
        viewPager1 = view1.findViewById(R.id.viewPager1);
        appBarLayout1 = view1.findViewById(R.id.appBarLayout1);

        return view1;
    }

    private void setUpTabs() {
        tabs = new ArrayList<>();
        tabsTitle = new ArrayList<>();
        tabs.add(new SearchProfile());tabsTitle.add("Profiles");
        tabs.add(new SearchQuestion());tabsTitle.add("Questions");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //setting up tabs
        setUpTabs();
        pagerAdapter = new TabPagerAdapter(getChildFragmentManager(), tabs, tabsTitle);
        viewPager1.setAdapter(pagerAdapter);
        tabLayout1.setupWithViewPager(viewPager1);
    }

}
