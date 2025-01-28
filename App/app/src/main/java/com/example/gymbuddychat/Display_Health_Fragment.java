package com.example.gymbuddychat;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Display_Health_Fragment extends Fragment {

    private RecyclerView healthReportRecyclerView;
    private HealthReportAdapter adapter;
    private List<HealthReport> healthReports;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display__health_, container, false);

        healthReportRecyclerView = view.findViewById(R.id.healthReportRecyclerView);
        healthReports = new ArrayList<>();
        adapter = new HealthReportAdapter(getContext(), healthReports);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        healthReportRecyclerView.setLayoutManager(layoutManager);
        healthReportRecyclerView.setAdapter(adapter);

        // Call method to fetch health reports from Firebase
        fetchHealthReports();

        return view;
    }

    private void fetchHealthReports() {
        // Assuming you have a method to fetch health reports from Firebase
        // and populate the healthReports list
        // For example:
        DatabaseReference healthReportsRef = FirebaseDatabase.getInstance().getReference("health_reports");
        healthReportsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HealthReport report = snapshot.getValue(HealthReport.class);
                    healthReports.add(report);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to fetch health reports", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
