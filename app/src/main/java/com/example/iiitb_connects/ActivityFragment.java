package com.example.iiitb_connects;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActivityFragment extends Fragment {

    //Views
    private RecyclerView recyclerView;
    private List<ChallengeItems> challengeItemsList;
    private ChallengeItemAdapter adapter;

    //Strings
    String clubName, challengeName, description, templateImg;

    //Firebase
    DatabaseReference challenges = FirebaseDatabase.getInstance().getReference("Challenges");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, container, false);

        challenges.keepSynced(true);

        recyclerView = view.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        challengeItemsList = new ArrayList<>();
        adapter = new ChallengeItemAdapter(challengeItemsList, getActivity());
        recyclerView.setAdapter(adapter);

        if(savedInstanceState==null){
            loadData();
        }

        return view;
    }

    private void loadData() {
        challengeItemsList.clear();
        Query recentChallenges = challenges.limitToLast(20);
        recentChallenges.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    challengeName=null;clubName=null;templateImg=null;description=null;

                    if(ds.hasChild("clubName") && ds.child("clubName").getValue()!=null)
                        clubName = ds.child("clubName").getValue().toString();
                    if(ds.hasChild("templateImg") && ds.child("templateImg").getValue()!=null)
                        templateImg = ds.child("templateImg").getValue().toString();
                    if(ds.hasChild("challengeName") && ds.child("challengeName").getValue()!=null)
                        challengeName = ds.child("challengeName").getValue().toString();
                    if(ds.hasChild("description") && ds.child("description").getValue()!=null)
                        description = ds.child("description").getValue().toString();

                    challengeItemsList.add(new ChallengeItems(clubName, templateImg, challengeName, description));
                }
                Collections.reverse(challengeItemsList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
