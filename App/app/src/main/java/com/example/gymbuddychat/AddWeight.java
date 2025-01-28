package com.example.gymbuddychat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddWeight extends AppCompatActivity {

    private EditText editTextWeight;
    private DatePicker datePicker;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_weight);

        editTextWeight = findViewById(R.id.editTextWeight);
        datePicker = findViewById(R.id.datePicker);

        db = FirebaseFirestore.getInstance();

        Button buttonAddWeight = findViewById(R.id.buttonAddWeight);
        buttonAddWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWeightToFirestore();
            }
        });
    }

    private void addWeightToFirestore() {
        String weightString = editTextWeight.getText().toString();
        if (weightString.isEmpty()) {
            editTextWeight.setError("Please enter your weight");
            return;
        }

        double weight = Double.parseDouble(weightString);

        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int dayOfMonth = datePicker.getDayOfMonth();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        // Convert date to milliseconds
        long milliseconds = calendar.getTimeInMillis();

        Map<String, Object> weightData = new HashMap<>();
        weightData.put("weight", weight);
        weightData.put("date", milliseconds);

        db.collection("weights")
                .add(weightData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("AddWeightActivity","Weight data added successfully");
                        Toast.makeText(AddWeight.this, "Weight added successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("AddWeightActivity","Failed to add weight data", e);
                        Toast.makeText(AddWeight.this, "Failed to add weight", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
