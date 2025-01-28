package com.example.gymbuddychat;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class LiveAttendanceActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LiveAttendanceAdapter adapter;
    private List<AttendanceEntry> liveAttendanceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_attendance);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view_live_attendance);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        liveAttendanceList = new ArrayList<>();
        adapter = new LiveAttendanceAdapter(liveAttendanceList);
        recyclerView.setAdapter(adapter);

        // Fetch live attendance data
        fetchLiveAttendanceData();
    }

    private void fetchLiveAttendanceData() {
        DatabaseReference attendanceRef = FirebaseDatabase.getInstance().getReference("attendance");
        attendanceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                liveAttendanceList.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot entrySnapshot : userSnapshot.getChildren()) {
                        AttendanceEntry attendanceEntry = entrySnapshot.getValue(AttendanceEntry.class);
                        // Debugging statement
                        Log.d("LiveAttendance", "AttendanceEntry: " + attendanceEntry);
                        // Check if attendanceEntry is not null and checkOutDate is empty or null
                        if (attendanceEntry != null && (attendanceEntry.getCheckOutDate() == null || attendanceEntry.getCheckOutDate().isEmpty())) {
                            liveAttendanceList.add(attendanceEntry);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LiveAttendanceActivity.this, "Failed to fetch live attendance data.", Toast.LENGTH_SHORT).show();
            }
        });


    }



}