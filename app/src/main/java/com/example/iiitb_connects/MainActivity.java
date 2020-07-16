package com.example.iiitb_connects;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView mBottomNavigation;

    public static SharedPreferences sharedPreferences;
    public static boolean returnStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sharedPreferences = getSharedPreferences("com.example.sampleproject", MODE_PRIVATE);

        mBottomNavigation = findViewById(R.id.bottomNavigation);
        mBottomNavigation.setVisibility(View.VISIBLE);

        mBottomNavigation.setOnNavigationItemSelectedListener(bottomNavigationListener);

        if(returnStatus) {
            returnStatus = false;
            Fragment newFragment = new ProfileFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, newFragment,"profile");
            getSupportFragmentManager().beginTransaction().show(newFragment).commit();
            lastFragmentTag = "profile";
            mBottomNavigation.setSelectedItemId(R.id.profile);
        }
        else if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, new HomeFragment(), "home").commit();
            lastFragmentTag = "home";
        }

    }

    String lastFragmentTag;
    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Fragment lastFragment = fragmentManager.findFragmentByTag(lastFragmentTag);
                    if(lastFragment!=null)
                        fragmentTransaction.hide(lastFragment);
                    Fragment newFragment;
                    switch (item.getItemId()) {
                        case R.id.home :
                            newFragment = fragmentManager.findFragmentByTag("home");
                            lastFragmentTag = "home";
                            if(newFragment==null) {
                                newFragment = new HomeFragment();
                                fragmentTransaction.add(R.id.frameLayout, newFragment, lastFragmentTag);
                            }
                            break;
                        case R.id.search :
                            newFragment = fragmentManager.findFragmentByTag("search");
                            lastFragmentTag = "search";
                            if(newFragment==null) {
                                newFragment = new SearchFragment();
                                fragmentTransaction.add(R.id.frameLayout, newFragment, lastFragmentTag);
                            }
                            break;
                        case R.id.add :
                            newFragment = fragmentManager.findFragmentByTag("add");
                            lastFragmentTag = "add";
                            if(newFragment==null) {
                                newFragment = new AddFragment();
                                fragmentTransaction.add(R.id.frameLayout, newFragment, lastFragmentTag);
                            }
                            mBottomNavigation.setVisibility(View.GONE);
                            break;
                        case R.id.activity :
                            newFragment = fragmentManager.findFragmentByTag("activity");
                            lastFragmentTag = "activity";
                            if(newFragment==null) {
                                newFragment = new ActivityFragment();
                                fragmentTransaction.add(R.id.frameLayout, newFragment, lastFragmentTag);
                            }
                            break;
                        case R.id.profile :
                            newFragment = fragmentManager.findFragmentByTag("profile");
                            lastFragmentTag = "profile";
                            if(newFragment==null) {
                                newFragment = new ProfileFragment();
                                fragmentTransaction.add(R.id.frameLayout, newFragment, lastFragmentTag);
                            }
                            break;
                        default:
                            return false;
                    }
                    fragmentTransaction.show(newFragment).commit();
                    return true;
                }
            };
}
