package com.example.iiitb_connects;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChallengeFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<ChallengeItems> challengeItemsList;
    private TextView nothingToShow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenge, container, false);

        recyclerView = view.findViewById(R.id.challengeRCV);
        nothingToShow = view.findViewById(R.id.nothingToShow);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        challengeItemsList = new ArrayList<>();
        challengeItemsList.add(new ChallengeItems(R.drawable.img1, "HackForWorld 1.0"));
        challengeItemsList.add(new ChallengeItems(R.drawable.img2, "TheTimeTeasers 1.0"));
        challengeItemsList.add(new ChallengeItems(R.drawable.img3, "BreakingBad 1.0"));
        challengeItemsList.add(new ChallengeItems(R.drawable.img1, "HackForWorld 1.0"));
        challengeItemsList.add(new ChallengeItems(R.drawable.img2, "TheTimeTeasers 1.0"));
        challengeItemsList.add(new ChallengeItems(R.drawable.img3, "BreakingBad 1.0"));
        challengeItemsList.add(new ChallengeItems(R.drawable.img1, "HackForWorld 1.0"));
        challengeItemsList.add(new ChallengeItems(R.drawable.img2, "TheTimeTeasers 1.0"));
        challengeItemsList.add(new ChallengeItems(R.drawable.img3, "BreakingBad 1.0"));

        ChallengeItemAdapter adapter = new ChallengeItemAdapter(challengeItemsList);
        recyclerView.setAdapter(adapter);

        if(challengeItemsList.size() == 0)
            nothingToShow.setVisibility(View.VISIBLE);
        else
            nothingToShow.setVisibility(View.GONE);

        return view;
    }
}
