package com.example.iiitb_connects;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    //views
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView homeFeedRCV;
    private RelativeLayout loadScreen;
    ImageView infoIV;
    RelativeLayout nothingToShow;

    //list of recycler view items
    private List<HomeFeedItems> homeFeedItemsList;
    List<HomeFeedItems> oldHomeFeedItems;
    private HomeFeedItemAdapter adapter;

    //Firebase
    private DatabaseReference posts;

    //Picasso
    public static Picasso postLoader;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Firebase
        posts = FirebaseDatabase.getInstance().getReference("Posts");
        posts.keepSynced(true);

        //Picasso
        postLoader = Picasso.get();

        //Init views
        refreshLayout = view.findViewById(R.id.refreshLayout);
        homeFeedRCV = view.findViewById(R.id.homeFeedRCV);
        loadScreen = view.findViewById(R.id.loadScreen);
        nothingToShow = view.findViewById(R.id.nothingToShow);
        //infoIV = view.findViewById(R.id.infoBtn);
        homeFeedItemsList = new ArrayList<>();
        oldHomeFeedItems = new ArrayList<>();

        //new things added to make ui less laggy
        homeFeedRCV.setHasFixedSize(true);
        /*homeFeedRCV.setItemViewCacheSize(20);
        homeFeedRCV.setDrawingCacheEnabled(true);
        homeFeedRCV.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);*/

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
            homeFeedItemsList.clear();
            oldHomeFeedItems.clear();
            loadData();
        }

        /*homeFeedRCV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(recyclerView.getScrollState()==RecyclerView.SCROLL_STATE_IDLE){
                    postLoader.resumeTag("postMedia");
                } else {
                    postLoader.pauseTag("postMedia");
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });*/

        //refresh layout
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                homeFeedItemsList.clear();
                oldHomeFeedItems.clear();
                loadData();
            }
        });

        /*infoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),InfoActivity.class));
            }
        });*/

        return view;
    }


    String Uid, username, userDp, Img, description, postId;
    private void loadData() {
        oldHomeFeedItems = homeFeedItemsList;
        homeFeedItemsList.clear();
        Query recentPosts = posts.limitToLast(100);
        recentPosts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    postId=null;Uid=null;Img=null;description=null;username=null;
                    if(ds.hasChild("postsInfo")) {
                        if(ds.child("postsInfo").hasChild("Img") && ds.child("postsInfo").child("Img").getValue()!=null) {
                            Img = ds.child("postsInfo").child("Img").getValue().toString();
                            postId = ds.getKey();
                        }
                        if(ds.child("postsInfo").hasChild("description") && ds.child("postsInfo").child("description").getValue()!=null)
                            description = ds.child("postsInfo").child("description").getValue().toString();
                    }
                    if(ds.hasChild("userInfo")) {
                        /*if(ds.child("userInfo").hasChild("username") && ds.child("userInfo").child("username").getValue()!=null)
                            username = ds.child("userInfo").child("username").getValue().toString();
                        if(ds.child("userInfo").hasChild("userDp") && ds.child("userInfo").child("userDp").getValue()!=null)
                            userDp = ds.child("userInfo").child("userDp").getValue().toString();*/
                        if(ds.child("userInfo").hasChild("uid") && ds.child("userInfo").child("uid").getValue()!=null)
                            Uid = ds.child("userInfo").child("uid").getValue().toString();
                    }
                    //homeFeedItemsList.add(new HomeFeedItems(username, userDp, Img, description, postId));
                    homeFeedItemsList.add(new HomeFeedItems(Uid, Img, description, postId));
                }
                Collections.reverse(homeFeedItemsList);
                /*if(homeFeedItemsList.size()>=oldHomeFeedItems.size())
                    adapter.notifyItemRangeInserted(0, homeFeedItemsList.size()-oldHomeFeedItems.size());
                else {
                    adapter.notifyDataSetChanged();
                }*/
                adapter.notifyDataSetChanged();
                loadScreen.setVisibility(View.GONE);
                if (homeFeedItemsList.size() == 0){
                    nothingToShow.setVisibility(View.VISIBLE);
                } else {
                    nothingToShow.setVisibility(View.GONE);
                }
                refreshLayout.setEnabled(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        refreshLayout.setRefreshing(false);
    }
}
