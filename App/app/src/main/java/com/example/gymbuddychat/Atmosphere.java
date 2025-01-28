package com.example.gymbuddychat;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.view.View;

public class Atmosphere extends Fragment {

    private TextView oxygenLevelText, co2LevelText, humidityLevelText;
    private PieChart oxygenPieChart, co2PieChart, humidityPieChart;
    private Handler handler = new Handler();
    private Random random = new Random();
    private DatabaseReference databaseReference;

    // Data model class
    public static class AtmosphereData {
        public int oxygenLevel;
        public float co2Level;
        public int humidityLevel;

        public AtmosphereData() {
            // Default constructor required for calls to DataSnapshot.getValue(AtmosphereData.class)
        }

        public AtmosphereData(int oxygenLevel, float co2Level, int humidityLevel) {
            this.oxygenLevel = oxygenLevel;
            this.co2Level = co2Level;
            this.humidityLevel = humidityLevel;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_atmosphere, container, false);

        // Initialize views
        oxygenLevelText = rootView.findViewById(R.id.oxygenLevelText);
        co2LevelText = rootView.findViewById(R.id.co2LevelText);
        humidityLevelText = rootView.findViewById(R.id.humidityLevelText);
        oxygenPieChart = rootView.findViewById(R.id.oxygenPieChart);
        co2PieChart = rootView.findViewById(R.id.co2PieChart);
        humidityPieChart = rootView.findViewById(R.id.humidityPieChart);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("atmosphere");

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Start simulating data immediately when the fragment becomes visible
        startSimulatingAtmosphereLevels();
    }

    private void startSimulatingAtmosphereLevels() {
        // Immediately generate and update values when the fragment is opened or resumes
        updateAtmosphereData();

        // Schedule updates every 1 minute (60 seconds)
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateAtmosphereData(); // Generate and update values

                // Continue updating every minute while the fragment is visible
                handler.postDelayed(this, 5000); // Schedule next update in 1 minute
            }
        }, 1000); // Initial delay of 1 minute
    }

    // This method generates random data and pushes it to Firebase
    private void updateAtmosphereData() {
        // Generate random values
        int oxygenLevel = 20 + random.nextInt(11); // Between 20% and 30%
        float co2Level = 0.03f + random.nextFloat() * (0.06f - 0.03f); // Between 0.03% and 0.06%
        int humidityLevel = 40 + random.nextInt(21); // Between 40% and 60%

        // Update TextViews with the new values
        oxygenLevelText.setText("Oxygen Level: " + oxygenLevel + "%");
        updatePieChart(oxygenPieChart, oxygenLevel, 100 - oxygenLevel);

        co2LevelText.setText("CO2 Level: " + String.format("%.2f%%", co2Level));
        updatePieChart(co2PieChart, (int) (co2Level * 100), 100 - (int) (co2Level * 100));

        humidityLevelText.setText("Humidity Level: " + humidityLevel + "%");
        updatePieChart(humidityPieChart, humidityLevel, 100 - humidityLevel);

        // Create and store data in Firebase (overwrite existing values)
        AtmosphereData atmosphereData = new AtmosphereData(oxygenLevel, co2Level, humidityLevel);
        databaseReference.setValue(atmosphereData); // Overwrites data at the "atmosphere" reference
    }

    private void updatePieChart(PieChart pieChart, int value1, int value2) {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(value1, "Level"));
        entries.add(new PieEntry(value2, "Remaining"));

        PieDataSet dataSet = new PieDataSet(entries, null);
        dataSet.setColors(new int[]{Color.GREEN, Color.GRAY}); // Customize colors
        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.invalidate(); // Refresh the chart
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        // Stop handler when fragment is no longer visible
//        handler.removeCallbacksAndMessages(null);
//    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        // Stop handler when fragment is destroyed
//        handler.removeCallbacksAndMessages(null);
//    }
}
