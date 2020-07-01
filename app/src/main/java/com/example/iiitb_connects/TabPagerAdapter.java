package com.example.iiitb_connects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> tabs = new ArrayList<>();
    private List<String> tabsTitle = new ArrayList<>();

    public TabPagerAdapter(FragmentManager fm, List<Fragment> tabs, List<String> tabsTitle) {
        super(fm);
        this.tabs = tabs;
        this.tabsTitle = tabsTitle;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return tabs.get(position);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabsTitle.get(position);
    }
}
