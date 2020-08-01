package com.example.iiitb_connects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InfoActivity extends AppCompatActivity {
    RelativeLayout feedback,aboutUs,rateUs;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        rateUs = findViewById(R.id.relLayout1);
        feedback = findViewById(R.id.relLayout2);
        aboutUs = findViewById(R.id.relLayout3);
        backBtn = findViewById(R.id.back);

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String[] recipients={"team404dev@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail"));
            }
        });

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AboutUsActivity.class));
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogBox();
            }
        });
    }

    AlertDialog alert;
    public void showDialogBox(){
        LayoutInflater inflater = InfoActivity.this.getLayoutInflater();
        View v = inflater.inflate(R.layout.rating_dialog_box,null);
        //Initialising Views
        Button submitButton = v.findViewById(R.id.positiveButton);
        Button cancelBtn = v.findViewById(R.id.negativeButton);
        final TextView avgRating = v.findViewById(R.id.avgRatingTV);
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Ratings");
        final ImageView star1,star2,star3,star4,star5,star10,star20,star30,star40,star50;
        star1 = v.findViewById(R.id.star1);
        star2 = v.findViewById(R.id.star2);
        star3 = v.findViewById(R.id.star3);
        star4 = v.findViewById(R.id.star4);
        star5 = v.findViewById(R.id.star5);
        star10 = v.findViewById(R.id.star10);
        star20 = v.findViewById(R.id.star20);
        star30 = v.findViewById(R.id.star30);
        star40 = v.findViewById(R.id.star40);
        star50 = v.findViewById(R.id.star50);
        final int[] count = {0};

        final AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this);
        builder.setView(v).setCancelable(false);
        alert = builder.create();
        alert.show();

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sum = 0;
                int num = 0;
                float avg = 0;
                if (snapshot.hasChildren()){
                for (DataSnapshot ds : snapshot.getChildren()){
                    sum = sum + Integer.parseInt(ds.getValue().toString());
                    num = num + 1;
                }
                avg = sum/num;
                avgRating.setText(String.valueOf(avg));}
                else{
                    avgRating.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star10.setVisibility(View.GONE);
                star20.setVisibility(View.GONE);
                star30.setVisibility(View.GONE);
                star40.setVisibility(View.GONE);
                star50.setVisibility(View.GONE);
                star10.setVisibility(View.VISIBLE);
                star1.setVisibility(View.GONE);
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
                star4.setVisibility(View.VISIBLE);
                star5.setVisibility(View.VISIBLE);
                count[0] =1;
            }
        });
        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star10.setVisibility(View.GONE);
                star20.setVisibility(View.GONE);
                star30.setVisibility(View.GONE);
                star40.setVisibility(View.GONE);
                star50.setVisibility(View.GONE);
                star10.setVisibility(View.VISIBLE);
                star20.setVisibility(View.VISIBLE);
                star1.setVisibility(View.GONE);
                star2.setVisibility(View.GONE);
                star3.setVisibility(View.VISIBLE);
                star4.setVisibility(View.VISIBLE);
                star5.setVisibility(View.VISIBLE);
                count[0] =2;
            }
        });
        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star10.setVisibility(View.GONE);
                star20.setVisibility(View.GONE);
                star30.setVisibility(View.GONE);
                star40.setVisibility(View.GONE);
                star50.setVisibility(View.GONE);
                star10.setVisibility(View.VISIBLE);
                star20.setVisibility(View.VISIBLE);
                star30.setVisibility(View.VISIBLE);
                star1.setVisibility(View.GONE);
                star2.setVisibility(View.GONE);
                star3.setVisibility(View.GONE);
                star4.setVisibility(View.VISIBLE);
                star5.setVisibility(View.VISIBLE);
                count[0] =3;

            }
        });
        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star10.setVisibility(View.GONE);
                star20.setVisibility(View.GONE);
                star30.setVisibility(View.GONE);
                star40.setVisibility(View.GONE);
                star50.setVisibility(View.GONE);
                star10.setVisibility(View.VISIBLE);
                star20.setVisibility(View.VISIBLE);
                star30.setVisibility(View.VISIBLE);
                star40.setVisibility(View.VISIBLE);
                star1.setVisibility(View.GONE);
                star2.setVisibility(View.GONE);
                star3.setVisibility(View.GONE);
                star4.setVisibility(View.GONE);
                star5.setVisibility(View.VISIBLE);
                count[0] =4;

            }
        });
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star10.setVisibility(View.GONE);
                star20.setVisibility(View.GONE);
                star30.setVisibility(View.GONE);
                star40.setVisibility(View.GONE);
                star50.setVisibility(View.GONE);
                star10.setVisibility(View.VISIBLE);
                star20.setVisibility(View.VISIBLE);
                star30.setVisibility(View.VISIBLE);
                star40.setVisibility(View.VISIBLE);
                star50.setVisibility(View.VISIBLE);
                star1.setVisibility(View.GONE);
                star2.setVisibility(View.GONE);
                star3.setVisibility(View.GONE);
                star4.setVisibility(View.GONE);
                star5.setVisibility(View.GONE);
                count[0] =5;

            }
        });
        star10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star10.setVisibility(View.GONE);
                star20.setVisibility(View.GONE);
                star30.setVisibility(View.GONE);
                star40.setVisibility(View.GONE);
                star50.setVisibility(View.GONE);
                star10.setVisibility(View.VISIBLE);
                star1.setVisibility(View.GONE);
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
                star4.setVisibility(View.VISIBLE);
                star5.setVisibility(View.VISIBLE);
                count[0] =1;


            }
        });
        star20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star10.setVisibility(View.GONE);
                star20.setVisibility(View.GONE);
                star30.setVisibility(View.GONE);
                star40.setVisibility(View.GONE);
                star50.setVisibility(View.GONE);
                star10.setVisibility(View.VISIBLE);
                star20.setVisibility(View.VISIBLE);
                star1.setVisibility(View.GONE);
                star2.setVisibility(View.GONE);
                star3.setVisibility(View.VISIBLE);
                star4.setVisibility(View.VISIBLE);
                star5.setVisibility(View.VISIBLE);
                count[0] =2;


            }
        });star30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star10.setVisibility(View.GONE);
                star20.setVisibility(View.GONE);
                star30.setVisibility(View.GONE);
                star40.setVisibility(View.GONE);
                star50.setVisibility(View.GONE);
                star10.setVisibility(View.VISIBLE);
                star20.setVisibility(View.VISIBLE);
                star30.setVisibility(View.VISIBLE);
                star1.setVisibility(View.GONE);
                star2.setVisibility(View.GONE);
                star3.setVisibility(View.GONE);
                star4.setVisibility(View.VISIBLE);
                star5.setVisibility(View.VISIBLE);
                count[0] =3;

            }
        });
        star40.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star10.setVisibility(View.GONE);
                star20.setVisibility(View.GONE);
                star30.setVisibility(View.GONE);
                star40.setVisibility(View.GONE);
                star50.setVisibility(View.GONE);
                star10.setVisibility(View.VISIBLE);
                star20.setVisibility(View.VISIBLE);
                star30.setVisibility(View.VISIBLE);
                star40.setVisibility(View.VISIBLE);
                star1.setVisibility(View.GONE);
                star2.setVisibility(View.GONE);
                star3.setVisibility(View.GONE);
                star4.setVisibility(View.GONE);
                star5.setVisibility(View.VISIBLE);
                count[0] =4;

            }
        });
        star50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star10.setVisibility(View.GONE);
                star20.setVisibility(View.GONE);
                star30.setVisibility(View.GONE);
                star40.setVisibility(View.GONE);
                star50.setVisibility(View.GONE);
                star10.setVisibility(View.VISIBLE);
                star20.setVisibility(View.VISIBLE);
                star30.setVisibility(View.VISIBLE);
                star40.setVisibility(View.VISIBLE);
                star50.setVisibility(View.VISIBLE);
                star1.setVisibility(View.GONE);
                star2.setVisibility(View.GONE);
                star3.setVisibility(View.GONE);
                star4.setVisibility(View.GONE);
                star5.setVisibility(View.GONE);
                count[0] =5;

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Ratings");
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mRef.child(mAuth.getCurrentUser().getUid()).setValue(String.valueOf(count[0]));
                alert.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
    }
}
