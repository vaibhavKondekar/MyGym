package com.example.gymbuddychat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
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
import java.util.Calendar;
import java.util.List;

public class AttendanceFragment extends Fragment {

    private TextView currentDateTextView;
    private RecyclerView recyclerView;
    private AttendanceAdapter adapter;
    private List<AttendanceEntry> attendanceList;

    public AttendanceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);

        // Initialize views
        currentDateTextView = view.findViewById(R.id.text_view_current_date);
        recyclerView = view.findViewById(R.id.recycler_view_attendance);

        // Initialize RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        attendanceList = new ArrayList<>();
        adapter = new AttendanceAdapter(attendanceList);
        recyclerView.setAdapter(adapter);

        // Set current date initially
        setCurrentDate();

        view.findViewById(R.id.live_attendance_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start LiveAttendanceActivity
                Intent intent = new Intent(requireContext(), LiveAttendanceActivity.class);
                startActivity(intent);
            }
        });




        // Set click listener for choose date button
        view.findViewById(R.id.choose_date_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        return view;
    }

    private void setCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Months are zero-based
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String currentDate = dayOfMonth + " " + getMonthName(month) + " " + year;
        currentDateTextView.setText(currentDate);
        fetchAttendanceData(currentDate);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        selectedMonth++; // Months are zero-based, so increment by 1
                        String selectedDate = selectedDayOfMonth + " " + getMonthName(selectedMonth) + " " + selectedYear;
                        currentDateTextView.setText(selectedDate);
                        fetchAttendanceData(selectedDate);
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    private void fetchAttendanceData(final String selectedDate) {
        DatabaseReference attendanceRef = FirebaseDatabase.getInstance().getReference("attendance");
        attendanceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                attendanceList.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot entrySnapshot : userSnapshot.getChildren()) {
                        AttendanceEntry attendanceEntry = entrySnapshot.getValue(AttendanceEntry.class);
                        if (attendanceEntry != null && attendanceEntry.getCheckInDate().equals(selectedDate)) {
                            attendanceList.add(attendanceEntry);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(requireContext(), "Failed to fetch attendance data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getMonthName(int month) {
        switch (month) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
            default:
                return ""; // Handle invalid month values
        }
    }
}
