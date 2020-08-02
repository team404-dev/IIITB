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
import android.widget.RatingBar;
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

import java.text.DecimalFormat;

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
        RatingBar ratingBar;
        ratingBar = v.findViewById(R.id.stars);
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Ratings");
        final float[] count = {0};

        final AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this);
        builder.setView(v).setCancelable(false);
        alert = builder.create();
        alert.show();

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float sum = 0;
                float num = 0;
                float avg = 0;
                if (snapshot.hasChildren()){
                for (DataSnapshot ds : snapshot.getChildren()){
                    sum = sum + Float.parseFloat(ds.getValue().toString());
                    num = num + 1;
                }
                avg = sum/num;
                DecimalFormat df = new DecimalFormat("#.#");
                avgRating.setText(String.valueOf(df.format(avg)));}
                else{
                    avgRating.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String message = null;
                switch ((int) rating){
                    case 1:
                        message = "Sorry to hear that :(";
                        break;
                    case 2:
                        message = "Your suggestions are always welcome";
                        break;
                    case 3:
                        message = "Seems as we are doing good enough!";
                        break;
                    case 4:
                        message = "Great! Thank You";
                        break;
                    case 5:
                        message = "Thanks for showing this much support!";
                        break;
                }
                Toast.makeText(InfoActivity.this, message, Toast.LENGTH_SHORT).show();
                count[0] = ratingBar.getRating();
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
