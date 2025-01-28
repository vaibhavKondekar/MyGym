package com.example.gymbuddychat;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gymbuddychat.model.ExerciseRequest;
import com.example.gymbuddychat.model.UserModel;
import com.example.gymbuddychat.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Locale;

public class SendRequestToAll extends AppCompatActivity {
    private EditText exerciseTypeEditText;
    private TimePicker timePicker;
    private Button sendNotificationButton;
    private DatabaseReference databaseReference;
    private DatePicker datePicker;
    private UserModel currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request_to_all);
        ImageView backButton = findViewById(R.id.icon_back);

        backButton.setOnClickListener((v) -> {
            onBackPressed();
        });
        databaseReference = FirebaseDatabase.getInstance().getReference();

        exerciseTypeEditText = findViewById(R.id.exerciseTypeEditText);
        timePicker = findViewById(R.id.timePicker);
        datePicker = findViewById(R.id.datePicker);
        sendNotificationButton = findViewById(R.id.sendNotificationButton);

        // Get current user information
        getCurrentUser();

        sendNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExerciseInfo();
            }
        });
    }

    private void getCurrentUser() {
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    UserModel user = task.getResult().toObject(UserModel.class);
                    if (user != null) {
                        currentUser = user;
                    } else {
                        // Handle the case where user data is not available
                    }
                } else {
                    // Handle exceptions
                }
            }
        });
    }

    private void saveExerciseInfo() {
        // Get exercise type, time, and date
        String exerciseType = exerciseTypeEditText.getText().toString().trim();
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();

        // Get selected date from DatePicker
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1; // Month is zero-based
        int year = datePicker.getYear();

        // Construct a String representing the time
        String time = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);

        // Construct a String representing the date
        String date = String.format(Locale.getDefault(), "%02d-%02d-%04d", day, month, year);

        // Generate a unique key for the exercise entry
        String exerciseId = databaseReference.child("exerciseInfo").push().getKey();

        if (exerciseId != null && currentUser != null) {
            // Get sender's username
            String senderUsername = currentUser.getUsername();

            // Create an ExerciseRequest object
            ExerciseRequest exerciseRequest = new ExerciseRequest(exerciseId, exerciseType, time, date, currentUser.getUserId(), senderUsername, null, null, false, null, null);

            // Save the exercise data to Firebase
            databaseReference.child("exerciseInfo").child(exerciseId).setValue(exerciseRequest)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Request saved successfully
                                Toast.makeText(SendRequestToAll.this, "Exercise info saved successfully", Toast.LENGTH_SHORT).show();

                                // After saving the request, navigate to MainActivity
                                Intent intent = new Intent(SendRequestToAll.this, MainActivity.class);
                                startActivity(intent);
                                finish(); // Finish the current activity
                            } else {
                                // Failed to save request
                                Toast.makeText(SendRequestToAll.this, "Failed to save exercise info", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

}
