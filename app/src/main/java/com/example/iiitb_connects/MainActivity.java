package com.example.iiitb_connects;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView mBottomNavigation;

    public static SharedPreferences sharedPreferences;
    public static boolean returnStatus;
    private  boolean check;

    FirebaseAuth mAuth;
    DatabaseReference mRef;
    String userUid;

    /*private BottomSheetBehavior bottomSheetBehavior;
    private LinearLayout shoutout_bottom_sheet;
    private ImageView close;*/

    private Intent startActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity = getIntent();

        sharedPreferences = getSharedPreferences("com.example.sampleproject", MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference("Users");
        userUid = mAuth.getCurrentUser().getUid();

        check = false;
        if (!mAuth.getCurrentUser().isEmailVerified()){
            showVerificationAlert();
        }

        //Shoutout
        /*shoutout_bottom_sheet = findViewById(R.id.shoutout_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(shoutout_bottom_sheet);
        bottomSheetBehavior.setFitToContents(false);
        bottomSheetBehavior.setHalfExpandedRatio(0.4f);
        close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });*/

        /*OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();*/

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

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    if (ds.getKey().toString().equals(userUid)){
                        check = false;
                        return;
                    } else {
                        check = true;
                    }
                }
                if (check == true){
                    showAlert();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

    AlertDialog alert;
    public void showAlert(){
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        View v = inflater.inflate(R.layout.user_id_has_been_deleted,null);
        //Initialising Views
        Button okayButton = v.findViewById(R.id.positiveButton);


        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(v).setCancelable(false);
        alert = builder.create();
        alert.show();

        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                MainActivity.sharedPreferences.edit().clear().apply();
                Toast.makeText(getApplicationContext(), "Signed out!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }

    public void showVerificationAlert(){
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        View v = inflater.inflate(R.layout.verification_check_popup,null);
        //Initialising Views
        Button doneButton = v.findViewById(R.id.positiveButton);
        Button registerAgainBtn = v.findViewById(R.id.negativeButton);


        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(v).setCancelable(false);
        alert = builder.create();
        alert.show();

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        if (mAuth.getCurrentUser().isEmailVerified()){
                            alert.dismiss();
                        } else{
                            Toast.makeText(MainActivity.this, "Click on the link sent to your mail to verify yourself!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/

                mAuth.getCurrentUser().reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (mAuth.getCurrentUser().isEmailVerified()){
                            final String uid = mAuth.getCurrentUser().getUid();
                            DatabaseReference mRefVerif = FirebaseDatabase.getInstance().getReference("Users");
                            mRefVerif.child(uid).child("isVerified").setValue(true);
                            alert.dismiss();
                        } else{
                            Toast.makeText(MainActivity.this, "Click on the link sent to your mail to verify yourself!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        registerAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                MainActivity.sharedPreferences.edit().clear().apply();
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(mBottomNavigation.getSelectedItemId() == R.id.home){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Exit?").setMessage("Do you want to exit?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.super.onBackPressed();
                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            }).create().show();
        } else {
            if(mBottomNavigation.getSelectedItemId()==R.id.add){
                startActivity(startActivity);
            }
            else {
                getSupportFragmentManager().beginTransaction().show(getSupportFragmentManager().findFragmentByTag("home"));
                mBottomNavigation.setSelectedItemId(R.id.home);
            }
        }
    }
}
