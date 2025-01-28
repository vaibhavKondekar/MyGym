package com.example.gymbuddychat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HealthReportAdapter extends RecyclerView.Adapter<HealthReportAdapter.ViewHolder> {
    private List<HealthReport> healthReports;
    private Context context;

    public HealthReportAdapter(Context context, List<HealthReport> healthReports) {
        this.context = context;
        this.healthReports = healthReports;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.health_report_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HealthReport report = healthReports.get(position);


        holder.usernameTextView.setText(report.getUsername());
        holder.heartRateTextView.setText("Heart Rate: " + report.getHeartRateAverage() + " bpm");
        holder.bloodOxygenTextView.setText("Blood Oxygen: " + report.getBloodOxygenAverage() + "%");
        holder.bloodPressureTextView.setText("Blood Pressure: " + report.getBloodPressureAverage());

        boolean hasIssue = hasParameterIssue(report);

        if (hasIssue) {
            // Highlight background color in red for reports with issues
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.my_primary));
            // Trigger alert for gym owner
            triggerAlert();
        } else {
            // Set background color to white for reports without issues
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_green_light));
        }

    }

    @Override
    public int getItemCount() {
        return healthReports.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView heartRateTextView;
        TextView bloodOxygenTextView;
        TextView bloodPressureTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            heartRateTextView = itemView.findViewById(R.id.heartRateTextView);
            bloodOxygenTextView = itemView.findViewById(R.id.bloodOxygenTextView);
            bloodPressureTextView = itemView.findViewById(R.id.bloodPressureTextView);
        }
    }

    private boolean hasParameterIssue(HealthReport healthReport) {
        boolean heartRateIssue = !isWithinHeartRateRange(healthReport.getHeartRateAverage());
        boolean oxygenLevelIssue = !isWithinOxygenLevelRange(healthReport.getBloodOxygenAverage());
        boolean bloodPressureIssue = !isWithinBloodPressureRange(healthReport.getBloodPressureAverage());
        return heartRateIssue || oxygenLevelIssue || bloodPressureIssue;
    }

    private boolean isWithinHeartRateRange(int heartRate) {
        return heartRate >= 60 && heartRate <= 100;
    }

    private boolean isWithinOxygenLevelRange(int oxygenLevel) {
        return oxygenLevel >= 95 && oxygenLevel <= 100;
    }

    private boolean isWithinBloodPressureRange(String bloodPressure) {
        // Implement logic to check blood pressure range
        return true; // Placeholder logic
    }

    private void triggerAlert() {
        // Implement alert mechanism, such as sending a notification to the gym owner
//        Toast.makeText(context, "Health report has parameter issue! Alert gym owner.", Toast.LENGTH_SHORT).show();
    }
}
