package com.example.iiitb_connects;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChallengeFragment extends Fragment {

    //Views
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<ChallengeItems> challengeItemsList;
    private ChallengeItemAdapter adapter;
    private RelativeLayout nothingToShow;

    //Firebase
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Challenges");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenge, container, false);

        recyclerView = view.findViewById(R.id.challengeRCV);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        nothingToShow = view.findViewById(R.id.nothingToShow);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        challengeItemsList = new ArrayList<>();
        adapter = new ChallengeItemAdapter(challengeItemsList, getActivity());
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
    }

    String clubName, challengeName, description, templateImg;
    private void loadData() {
        challengeItemsList.clear();
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    if(ds.hasChild("optedBy") && ds.child("optedBy").hasChild(uid) && ds.child("optedBy").child(uid).getValue().toString().equals("1")) {
                        challengeName=null;clubName=null;templateImg=null;description=null;

                        if(ds.hasChild("clubName") && ds.child("clubName").getValue()!=null)
                            clubName = ds.child("clubName").getValue().toString();
                        if(ds.hasChild("templateImg") && ds.child("templateImg").getValue()!=null)
                            templateImg = ds.child("templateImg").getValue().toString();
                        if(ds.hasChild("challengeName") && ds.child("challengeName").getValue()!=null)
                            challengeName = ds.child("challengeName").getValue().toString();
                        if(ds.hasChild("description") && ds.child("description").getValue()!=null)
                            description = ds.child("description").getValue().toString();

                        challengeItemsList.add(new ChallengeItems(clubName, templateImg, challengeName, description, ds.getKey()));
                    }
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    if(challengeItemsList.size() == 0)
                        nothingToShow.setVisibility(View.VISIBLE);
                    else
                        nothingToShow.setVisibility(View.GONE);
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}