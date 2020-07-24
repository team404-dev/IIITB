package com.example.iiitb_connects;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ChallengeDetails extends AppCompatActivity {

    private ImageView templateImg;
    private TextView challengeName;
    private TextView clubName;
    private TextView description;
    private ImageButton closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_details);

        Intent intent = getIntent();
        String mTemplateImg = intent.getStringExtra("templateImg");
        String mChallengeName = intent.getStringExtra("challengeName");
        String mClubName = intent.getStringExtra("clubName");
        String mDescription = intent.getStringExtra("description");

        //init views
        templateImg = findViewById(R.id.templateImg);
        challengeName = findViewById(R.id.challengeName);
        clubName = findViewById(R.id.clubName);
        description = findViewById(R.id.description);
        closeButton = findViewById(R.id.closeButton);

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
    }
}
