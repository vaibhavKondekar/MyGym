package com.example.gymbuddychat;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ShowWeight extends AppCompatActivity {

    private LineChart lineChartWeight;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_weight);

        lineChartWeight = findViewById(R.id.lineChartWeight);
        setupLineChart(lineChartWeight);

        db = FirebaseFirestore.getInstance();

        fetchWeightDataFromFirebase();
    }


    private void setupLineChart(LineChart lineChart) {
        // Customize line chart appearance and behavior here
        lineChart.getDescription().setEnabled(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getAxisLeft().setAxisMinimum(0f);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new DateValueFormatter());
        xAxis.setLabelRotationAngle(-45); // Rotate x-axis labels to prevent overlap
        xAxis.setGranularity(1f); // Set minimum interval between labels

    }
    private void fetchWeightDataFromFirebase() {
        db.collection("weights")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Entry> entries = new ArrayList<>();
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            Map<String, Object> data = document.getData();
                            if (data != null) {
                                double weight = (double) data.get("weight");
                                long milliseconds = (long) data.get("date"); // Retrieve date as milliseconds
                                entries.add(new Entry(milliseconds, (float) weight));
                            }
                        }
                        // Sort entries by date
                        Collections.sort(entries, new Comparator<Entry>() {
                            @Override
                            public int compare(Entry entry1, Entry entry2) {
                                return Long.compare((long) entry1.getX(), (long) entry2.getX());
                            }
                        });
                        renderChart(entries);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("ShowWeightActivity", "Failed to fetch weight data", e);
                        // Handle failure
                    }
                });
    }




    private void renderChart(List<Entry> entries) {
        LineDataSet dataSet = new LineDataSet(entries, "Weight");
        LineData lineData = new LineData(dataSet);
        lineChartWeight.setData(lineData);

        // Add weight gain/loss annotations
        for (int i = 1; i < entries.size(); i++) {
            Entry currentEntry = entries.get(i);
            Entry previousEntry = entries.get(i - 1);
            float weightDifference = currentEntry.getY() - previousEntry.getY();
            String annotation = "";
            if (weightDifference > 0) {
                annotation = "Weight gain: " + weightDifference;
            } else if (weightDifference < 0) {
                annotation = "Weight loss: " + Math.abs(weightDifference);
            }
            if (!annotation.isEmpty()) {
                lineChartWeight.getAxisLeft().addLimitLine(new LimitLine(currentEntry.getY(), annotation));
            }
        }

        lineChartWeight.invalidate(); // refresh chart og
    }

//
//    private void renderChart(List<Entry> entries) {
//        LineDataSet dataSet = new LineDataSet(entries, "Weight");
//        LineData lineData = new LineData(dataSet);
//        lineChartWeight.setData(lineData);
//
//        // Add weight gain/loss annotations
//        for (int i = 1; i < entries.size(); i++) {
//            Entry currentEntry = entries.get(i);
//            Entry previousEntry = entries.get(i - 1);
//            float weightDifference = currentEntry.getY() - previousEntry.getY();
//            String annotation = "";
//            if (weightDifference > 0) {
//                annotation = "Weight gain: " + weightDifference;
//            } else if (weightDifference < 0) {
//                annotation = "Weight loss: " + Math.abs(weightDifference);
//            }
//
//            // Check if current weight is same as previous weight
//            if (currentEntry.getY() == previousEntry.getY()) {
//                annotation = ""; // Avoid duplicate annotations
//            }
//
//            // Add annotation if not empty
//            if (!annotation.isEmpty()) {
//                LimitLine limitLine = new LimitLine(currentEntry.getY(), annotation);
//                limitLine.setTextSize(10f); // Set text size
//                lineChartWeight.getAxisLeft().addLimitLine(limitLine);
//            }
//        }
//
//        lineChartWeight.invalidate(); // refresh chart
//    }










    private static class DateValueFormatter extends ValueFormatter {
        private final SimpleDateFormat dateFormat;

        public DateValueFormatter() {
            this.dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        }

        @Override
        public String getFormattedValue(float value) {
            // Convert timestamp to date and format it
            Date date = new Date((long) value);
            return dateFormat.format(date);
        }
    }
}
