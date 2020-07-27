package com.example.iiitb_connects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ChallengeDetails extends AppCompatActivity {

    //Views
    private ImageView templateImg;
    private TextView challengeName;
    private TextView clubName;
    private TextView description;
    private ImageButton closeButton;
    private Button optStatus;

    //Firebase
    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Challenges");
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_details);

        Intent intent = getIntent();
        String mTemplateImg = intent.getStringExtra("templateImg");
        String mChallengeName = intent.getStringExtra("challengeName");
        String mClubName = intent.getStringExtra("clubName");
        String mDescription = intent.getStringExtra("description");
        String mChallengeID = intent.getStringExtra("challengeID");

        //init views
        templateImg = findViewById(R.id.templateImg);
        challengeName = findViewById(R.id.challengeName);
        clubName = findViewById(R.id.clubName);
        description = findViewById(R.id.description);
        closeButton = findViewById(R.id.closeButton);
        optStatus = findViewById(R.id.optStatus);

        assert mChallengeID != null;
        mRef = mRef.child(mChallengeID);
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("optedBy") && snapshot.child("optedBy").hasChild(uid)){
                    if(snapshot.child("optedBy").child(uid).getValue().toString().equals("1")){
                        optStatus.setText("OPT OUT");
                    }
                }else {
                    optStatus.setText("OPT IN");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //load data into views
        Picasso.get().load(mTemplateImg).into(templateImg);
        challengeName.setText(mChallengeName);
        clubName.setText(mClubName);
        description.setText(mDescription);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        optStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(optStatus.getText().equals("OPT IN")) {
                    //do opt in task
                    optStatus.setText("OPT OUT");
                    mRef.child("optedBy").child(uid).setValue("1");
                    Toast.makeText(ChallengeDetails.this, "Challenge added to your profile!", Toast.LENGTH_SHORT).show();
                } else {
                    // do opt out task
                    optStatus.setText("OPT IN");
                    mRef.child("optedBy").child(uid).setValue("0");
                }
            }
        });
    }
}
