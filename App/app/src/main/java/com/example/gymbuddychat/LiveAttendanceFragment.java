package com.example.gymbuddychat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LiveAttendanceFragment extends Fragment {

    private RecyclerView recyclerView;
    private LiveAttendanceAdapter adapter;
    private List<AttendanceEntry> liveAttendanceList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_live_attendance, container, false);

        // Initialize RecyclerView
        recyclerView = rootView.findViewById(R.id.recycler_view_live_attendance);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        liveAttendanceList = new ArrayList<>();
        adapter = new LiveAttendanceAdapter(liveAttendanceList);
        recyclerView.setAdapter(adapter);

        // Fetch live attendance data
        fetchLiveAttendanceData();

        return rootView;
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
                Toast.makeText(getContext(), "Failed to fetch live attendance data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
