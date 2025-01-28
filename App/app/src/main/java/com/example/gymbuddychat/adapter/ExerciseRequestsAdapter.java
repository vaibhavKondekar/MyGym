package com.example.gymbuddychat.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gymbuddychat.R;
import com.example.gymbuddychat.model.ExerciseRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ExerciseRequestsAdapter extends ArrayAdapter<ExerciseRequest> {
    private Context mContext;
    private ArrayList<ExerciseRequest> mRequestsList;

    public ExerciseRequestsAdapter(Context context, ArrayList<ExerciseRequest> requestsList) {
        super(context, 0, requestsList);
        mContext = context;
        mRequestsList = requestsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_request, parent, false);
        }

        ExerciseRequest currentRequest = mRequestsList.get(position);

        TextView senderUsernameTextView = listItem.findViewById(R.id.senderUsernameTextView);
        senderUsernameTextView.setText("From: " + currentRequest.getSenderUsername());

        TextView exerciseTypeTextView = listItem.findViewById(R.id.exerciseTypeTextView);
        exerciseTypeTextView.setText("Exercise Type: " + currentRequest.getExerciseType());

        TextView timeTextView = listItem.findViewById(R.id.timeTextView);
        timeTextView.setText("Time: " + currentRequest.getTime());

        TextView dateTextView = listItem.findViewById(R.id.dateTextView);
        dateTextView.setText("Date: " + currentRequest.getDate());

        Button acceptButton = listItem.findViewById(R.id.acceptButton);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptRequest(currentRequest);
            }
        });

        return listItem;
    }

    private void acceptRequest(ExerciseRequest request) {
        String requestId = request.getExerciseId();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("exerciseInfo").child(requestId);

        // Update accepted state and acceptedByUserId in Firebase
        request.setAccepted(true);
        request.setAcceptedByUserId(userId);
        request.setAcceptedByUsername(username);

        databaseReference.setValue(request)
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Request accepted successfully
                            Toast.makeText(mContext, "Request accepted", Toast.LENGTH_SHORT).show();
                        } else {
                            // Failed to accept request
                            Toast.makeText(mContext, "Failed to accept request", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}

