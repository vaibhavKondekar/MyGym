package com.example.gymbuddychat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WeightTrackerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weight_tracker, container, false);

        Button buttonAddWeight = view.findViewById(R.id.buttonAddWeight);
        buttonAddWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddWeight.class);
                startActivity(intent);
            }
        });

        Button buttonShowWeightHistory = view.findViewById(R.id.buttonShowWeightHistory);
        buttonShowWeightHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShowWeight.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
