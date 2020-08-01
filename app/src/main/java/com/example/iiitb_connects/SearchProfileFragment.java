package com.example.iiitb_connects;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class SearchProfileFragment extends Fragment {

    ProfileAdapter adapter;
    ArrayList<ProfileInfo> profileList;
    ArrayList<ProfileInfo> oldProfileList;
    ArrayList<String> profileListFullName;

    //Views
    SearchView searchView;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    EditText searchBar;


    RecyclerView.LayoutManager layoutManager;

    //Firebase
    DatabaseReference mRef;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_profile, container, false);

        //Init Views
        searchView = view.findViewById(R.id.searchViewProfile);

    //    searchBar = view.findViewById(R.id.searchBar);
        recyclerView = view.findViewById(R.id.searchProfileRecyclerview);
        refreshLayout = view.findViewById(R.id.refreshLayout);

        //Init Firebase
        mRef = FirebaseDatabase.getInstance().getReference("Users");
        mRef.keepSynced(true);
        mAuth = FirebaseAuth.getInstance();

        //Init Vars
        profileList = new ArrayList<ProfileInfo>();
        profileListFullName = new ArrayList<String>();
        oldProfileList = new ArrayList<ProfileInfo>();

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new ProfileAdapter(profileList, getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        //new things added to make ui less laggy
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);


        retrieveProfiles();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                profileList.clear();
                profileListFullName.clear();
                oldProfileList.clear();
                retrieveProfiles();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //    adapter.getFilter().filter(newText);
                search(newText);
                return false;
            }
        });

        adapter.setOnItemClickListener(new ProfileAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String Uid = profileList.get(position).getUserUid();
                Intent intent = new Intent(getContext(),StalkingActivity.class);
                intent.putExtra("User Uid",Uid);
                startActivity(intent);
            }
        });

        return view;
    }

    public void retrieveProfiles() {
        oldProfileList = profileList;
        profileList.clear();
        profileListFullName.clear();

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()){
                        if (!ds.child("isVerified").getValue().toString().equals("true")){
                            continue;
                        }
                        String fullName = ds.child("fullName").getValue().toString();
                        String username = ds.child("username").getValue().toString();
                        String userDP=null;
                        if(ds.child("templateProfilePhoto").getValue()!=null)
                        userDP = ds.child("templateProfilePhoto").getValue().toString();
                        String uid = ds.getKey();
                        profileList.add(new ProfileInfo(uid,username,fullName,userDP));
                        profileListFullName.add(fullName);
                    }
                Collections.reverse(profileList);
                if(profileList.size()>=oldProfileList.size())
                    adapter.notifyItemRangeInserted(0, profileList.size()-oldProfileList.size());
                else {
                    adapter.notifyDataSetChanged();
                }
                adapter.notifyDataSetChanged();
                refreshLayout.setEnabled(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        refreshLayout.setRefreshing(false);
    }

    public void search(String str) {
        ArrayList<ProfileInfo> myList = new ArrayList<>();
        for (ProfileInfo q : profileList){
            if (q.getFullName().toLowerCase().contains(str.toLowerCase())){
                myList.add(q);
            }
        }
        ProfileAdapter adapter = new ProfileAdapter(myList, getActivity());
        recyclerView.setAdapter(adapter);

    }
}
