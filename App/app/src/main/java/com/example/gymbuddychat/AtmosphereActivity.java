package com.example.gymbuddychat;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AtmosphereActivity extends AppCompatActivity {

    private TextView oxygenLevelText, humidityLevelText;
    private ProgressBar oxygenProgressBar, humidityProgressBar;
    private LineChart oxygenLineChart, humidityLineChart;
    private Handler handler = new Handler();
    private Random random = new Random();
    private List<Entry> oxygenEntries = new ArrayList<>();
    private List<Entry> humidityEntries = new ArrayList<>();
    private int time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atmosphere_fragment);

        // Bind UI elements
        oxygenLevelText = findViewById(R.id.oxygenLevelText);
        humidityLevelText = findViewById(R.id.humidityLevelText);
        oxygenProgressBar = findViewById(R.id.oxygenProgressBar);
        humidityProgressBar = findViewById(R.id.humidityProgressBar);
        oxygenLineChart = findViewById(R.id.oxygenLineChart);
        humidityLineChart = findViewById(R.id.humidityLineChart);

        // Start the simulation
        startSimulatingOxygenAndHumidityLevels();
    }

    private void startSimulatingOxygenAndHumidityLevels() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Simulate oxygen level between 20% and 30%
                int oxygenLevel = 20 + random.nextInt(11);
                // Simulate humidity level between 40% and 60%
                int humidityLevel = 40 + random.nextInt(21);

                // Update oxygen level text and progress bar
                oxygenLevelText.setText(oxygenLevel + "%");
                oxygenProgressBar.setProgress(oxygenLevel);
                if (oxygenLevel < 21) {
                    oxygenProgressBar.setProgressTintList(ContextCompat.getColorStateList(AtmosphereActivity.this, R.color.red));
                } else {
                    oxygenProgressBar.setProgressTintList(ContextCompat.getColorStateList(AtmosphereActivity.this, R.color.green));
                }

                // Update humidity level text and progress bar
                humidityLevelText.setText(humidityLevel + "%");
                humidityProgressBar.setProgress(humidityLevel);
                if (humidityLevel >= 40 && humidityLevel <= 60) {
                    humidityProgressBar.setProgressTintList(ContextCompat.getColorStateList(AtmosphereActivity.this, R.color.green));
                } else {
                    humidityProgressBar.setProgressTintList(ContextCompat.getColorStateList(AtmosphereActivity.this, R.color.red));
                }

                // Update both charts
                updateOxygenChart(oxygenLevel);
                updateHumidityChart(humidityLevel);

                // Repeat the simulation every second
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private void updateOxygenChart(int oxygenLevel) {
        oxygenEntries.add(new Entry(time, oxygenLevel));
        time++;

        LineDataSet dataSet = new LineDataSet(oxygenEntries, "Oxygen Level");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);

        LineData lineData = new LineData(dataSet);
        oxygenLineChart.setData(lineData);
        oxygenLineChart.invalidate(); // Refresh the chart
    }

    private void updateHumidityChart(int humidityLevel) {
        humidityEntries.add(new Entry(time, humidityLevel));

        LineDataSet dataSet = new LineDataSet(humidityEntries, "Humidity Level");
        dataSet.setColor(Color.GREEN); // You can choose a different color for humidity
        dataSet.setValueTextColor(Color.BLACK);

        LineData lineData = new LineData(dataSet);
        humidityLineChart.setData(lineData);
        humidityLineChart.invalidate(); // Refresh the chart
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null); // Stop updating when the activity is destroyed
    }
}
