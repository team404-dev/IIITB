package com.example.iiitb_connects;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    //views
    private Button shoutoutButton;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView homeFeedRCV;

    //list of recycler view items
    private List<HomeFeedItems> homeFeedItemsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Init views
        shoutoutButton = view.findViewById(R.id.shoutoutButton);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        homeFeedRCV = view.findViewById(R.id.homeFeedRCV);

        //onClick func of shoutout button only visible when shoutouts available
        //show shoutouts as alert dialog

        //setting up layout managers
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        homeFeedRCV.setLayoutManager(layoutManager);

        //post feed lists
        loadData();
        HomeFeedItemAdapter adapter = new HomeFeedItemAdapter(homeFeedItemsList);
        homeFeedRCV.setAdapter(adapter);

        //refresh layout
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                HomeFeedItemAdapter adapter = new HomeFeedItemAdapter(homeFeedItemsList);
                homeFeedRCV.setAdapter(adapter);
            }
        });

        return view;
    }

    private void loadData() {
        homeFeedItemsList = new ArrayList<>();
        homeFeedItemsList.add(new HomeFeedItems(R.drawable.img2, "team404dev", R.drawable.img1, "First post, from the developer's side. Enjoy!"));
        homeFeedItemsList.add(new HomeFeedItems(R.drawable.img1, "codame_codingClub", R.drawable.img3, "First post, from the developer's side. Enjoy!"));
        homeFeedItemsList.add(new HomeFeedItems(R.drawable.img3, "mridangam_culturalClub", R.drawable.img2, "First post, from the developer's side. Enjoy!"));
        homeFeedItemsList.add(new HomeFeedItems(R.drawable.img2, "team404dev", R.drawable.img1, "First post, from the developer's side. Enjoy!"));
        homeFeedItemsList.add(new HomeFeedItems(R.drawable.img1, "codame_codingClub", R.drawable.img3, "First post, from the developer's side. Enjoy!"));
        homeFeedItemsList.add(new HomeFeedItems(R.drawable.img3, "mridangam_culturalClub", R.drawable.img2, "First post, from the developer's side. Enjoy!"));
        homeFeedItemsList.add(new HomeFeedItems(R.drawable.img2, "team404dev", R.drawable.img1, "First post, from the developer's side. Enjoy!"));
        homeFeedItemsList.add(new HomeFeedItems(R.drawable.img1, "codame_codingClub", R.drawable.img3, "First post, from the developer's side. Enjoy!"));
        homeFeedItemsList.add(new HomeFeedItems(R.drawable.img3, "mridangam_culturalClub", R.drawable.img2, "First post, from the developer's side. Enjoy!"));
        refreshLayout.setRefreshing(false);
    }
}
