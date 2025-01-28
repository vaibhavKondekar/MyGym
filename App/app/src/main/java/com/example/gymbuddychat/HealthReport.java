package com.example.gymbuddychat;


public class HealthReport {
    private String userId;
    private String username;
    private int heartRateAverage;
    private int bloodOxygenAverage;
    private String bloodPressureAverage;

    public HealthReport() {
        // Default constructor required for Firebase
    }

    public HealthReport(String username , String userId, int heartRateAverage, int bloodOxygenAverage, String bloodPressureAverage) {
        this.userId = userId;
        this.username = username;
        this.heartRateAverage = heartRateAverage;
        this.bloodOxygenAverage = bloodOxygenAverage;
        this.bloodPressureAverage = bloodPressureAverage;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public int getHeartRateAverage() {
        return heartRateAverage;
    }

    public int getBloodOxygenAverage() {
        return bloodOxygenAverage;
    }

    public String getBloodPressureAverage() {
        return bloodPressureAverage;
    }
}

