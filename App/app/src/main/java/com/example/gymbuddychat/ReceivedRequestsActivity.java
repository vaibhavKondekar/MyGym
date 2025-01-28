package com.example.gymbuddychat;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gymbuddychat.adapter.ExerciseRequestsAdapter;
import com.example.gymbuddychat.model.ExerciseRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReceivedRequestsActivity extends AppCompatActivity {
    private DatabaseReference mDatabaseReference;
    private ArrayList<ExerciseRequest> mRequestsList;
    private ExerciseRequestsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_requests);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("exerciseInfo");
        mRequestsList = new ArrayList<>();
        mAdapter = new ExerciseRequestsAdapter(this, mRequestsList);

        ListView requestsListView = findViewById(R.id.requestsListView);
        requestsListView.setAdapter(mAdapter);

        fetchRequests();
    }

    private void fetchRequests() {
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mRequestsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ExerciseRequest request = snapshot.getValue(ExerciseRequest.class);
                    if (request != null) {
                        mRequestsList.add(request);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}
