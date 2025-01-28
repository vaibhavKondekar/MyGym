package com.example.gymbuddychat;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.WindowManager;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

// Inside DisplayHealthReportActivity.java
public class DisplayHealthReport extends AppCompatActivity {
    private RecyclerView healthReportRecyclerView;
    private HealthReportAdapter adapter;
    private List<HealthReport> healthReports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_health_report);

        healthReportRecyclerView = findViewById(R.id.healthReportRecyclerView);
        healthReports = new ArrayList<>();
        adapter = new HealthReportAdapter(this,healthReports);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        healthReportRecyclerView.setLayoutManager(layoutManager);
        healthReportRecyclerView.setAdapter(adapter);

        // Call method to fetch health reports from Firebase
        fetchHealthReports();
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
                Toast.makeText(DisplayHealthReport.this, "Failed to fetch health reports", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
