package com.example.iiitb_connects;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    //views
    private Button shoutoutButton;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView homeFeedRCV;
    private RelativeLayout loadScreen;

    //list of recycler view items
    private List<HomeFeedItems> homeFeedItemsList;
    private HomeFeedItemAdapter adapter;

    //Firebase
    private DatabaseReference posts = FirebaseDatabase.getInstance().getReference("Posts");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Init views
        shoutoutButton = view.findViewById(R.id.shoutoutButton);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        homeFeedRCV = view.findViewById(R.id.homeFeedRCV);
        loadScreen = view.findViewById(R.id.loadScreen);

        homeFeedItemsList = new ArrayList<>();

        //onClick func of shoutout button only visible when shoutouts available
        //show shoutouts as alert dialog

        //setting up layout managers
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        homeFeedRCV.setLayoutManager(layoutManager);

        adapter = new HomeFeedItemAdapter(homeFeedItemsList, getContext());
        homeFeedRCV.setAdapter(adapter);

        //post feed lists
        if(savedInstanceState==null) {
            refreshLayout.setEnabled(false);
            loadScreen.setVisibility(View.VISIBLE);
            loadData();
        }

        //refresh layout
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                homeFeedItemsList.clear();
                loadData();
            }
        });

        return view;
    }


    String Uid, username, userDp, Img, description;
    private void loadData() {
        posts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    Uid=null;Img=null;description=null;
                    if(ds.hasChild("postsInfo")) {
                        if(ds.child("postsInfo").hasChild("Img") && ds.child("postsInfo").child("Img").getValue()!=null)
                            Img = ds.child("postsInfo").child("Img").getValue().toString();
                        if(ds.child("postsInfo").hasChild("description") && ds.child("postsInfo").child("description").getValue()!=null)
                            description = ds.child("postsInfo").child("description").getValue().toString();
                    }
                    if(ds.hasChild("userInfo")) {
                        if(ds.child("userInfo").hasChild("username") && ds.child("userInfo").child("username").getValue()!=null)
                            username = ds.child("userInfo").child("username").getValue().toString();
                        if(ds.child("userInfo").hasChild("userDp") && ds.child("userInfo").child("userDp").getValue()!=null)
                            userDp = ds.child("userInfo").child("userDp").getValue().toString();
                    }
                    homeFeedItemsList.add(new HomeFeedItems(username, userDp, Img, description, ds.getKey()));
                    Collections.reverse(homeFeedItemsList);
                    adapter.notifyDataSetChanged();
                    loadScreen.setVisibility(View.GONE);
                    refreshLayout.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        refreshLayout.setRefreshing(false);
    }
}
