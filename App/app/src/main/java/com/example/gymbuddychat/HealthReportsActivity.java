package com.example.gymbuddychat;


import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HealthReportsActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button uploadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_reports);

        emailEditText = findViewById(R.id.emailEditText);
        uploadButton = findViewById(R.id.uploadButton);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the email content from the EditText
                String emailContent = emailEditText.getText().toString().trim();

                // Extract important health data from the email content
                HealthReport healthReport = extractHealthReport(emailContent);

                // If health report is extracted successfully, upload it to Firebase
                if (healthReport != null) {
                    uploadDataToFirebase(healthReport);
                } else {
                    Toast.makeText(HealthReportsActivity.this, "Failed to extract health report", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private HealthReport extractHealthReport(String emailContent) {
        // Use regular expressions to extract the required health data from the email content
        String heartRatePattern = "Heart Rate.*?Average: (\\d+) bpm.*?Highest recorded: (\\d+) bpm.*?Lowest recorded: (\\d+) bpm";
        String bloodOxygenPattern = "Blood Oxygen Level.*?Average: (\\d+)%.*?Highest recorded: (\\d+)%.*?Lowest recorded: (\\d+)%";
        String bloodPressurePattern = "Blood Pressure.*?Average: (\\d+/\\d+) mmHg.*?Highest recorded \\(Systolic/Diastolic\\): (\\d+/\\d+) mmHg.*?Lowest recorded \\(Systolic/Diastolic\\): (\\d+/\\d+) mmHg";

        Pattern heartRateRegex = Pattern.compile(heartRatePattern, Pattern.DOTALL);
        Pattern bloodOxygenRegex = Pattern.compile(bloodOxygenPattern, Pattern.DOTALL);
        Pattern bloodPressureRegex = Pattern.compile(bloodPressurePattern, Pattern.DOTALL);

        Matcher heartRateMatcher = heartRateRegex.matcher(emailContent);
        Matcher bloodOxygenMatcher = bloodOxygenRegex.matcher(emailContent);
        Matcher bloodPressureMatcher = bloodPressureRegex.matcher(emailContent);

        if (heartRateMatcher.find() && bloodOxygenMatcher.find() && bloodPressureMatcher.find()) {
            int heartRateAverage = Integer.parseInt(heartRateMatcher.group(1));
            int bloodOxygenAverage = Integer.parseInt(bloodOxygenMatcher.group(1));
            String bloodPressureAverage = bloodPressureMatcher.group(1);

            // Get the current user
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                String userId = currentUser.getUid();
                String username = currentUser.getDisplayName(); // Assuming you have the username available

                return new HealthReport(username, userId, heartRateAverage, bloodOxygenAverage, bloodPressureAverage);
            } else {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
                return null;
            }
        } else {
            return null;
        }
    }

    private void uploadDataToFirebase(HealthReport healthReport) {
        // Get a reference to the Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference healthDataRef = database.getReference("health_reports");

        // Store the health report under the user's ID
        healthDataRef.child(healthReport.getUserId()).setValue(healthReport);

        Toast.makeText(this, "Health report uploaded successfully", Toast.LENGTH_SHORT).show();
    }
}
