package com.example.iiitb_connects;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

public class FragmentSearch2 extends Fragment {
    TabLayout tabLayout1;
    ViewPager viewPager1;
    AppBarLayout appBarLayout1;
    SearchView searchView1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view1 = inflater.inflate(R.layout.fragment_search_2,container,false);

        tabLayout1 = view1.findViewById(R.id.tabLayout1);
        viewPager1 = view1.findViewById(R.id.viewPager1);
        appBarLayout1 = view1.findViewById(R.id.appBarLayout1);

        return view1;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(viewPager1);
        tabLayout1.setupWithViewPager(viewPager1);

        tabLayout1.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

    public void setUpViewPager(ViewPager viewPager1){
        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new SearchProfile(), "Profiles");
        adapter.addFragment(new SearchQuestion(),"Questions");
    //    Toast.makeText(getContext(), "SearchQuestion Called", Toast.LENGTH_SHORT).show();
        viewPager1.setAdapter(adapter);
    }
}
