package com.example.gymbuddychat;
public class AttendanceEntry {
    private String name;
    private String checkInTime;
    private String checkOutTime;
    private String checkInDate;
    private String checkOutDate;

    // No-argument constructor required for Firebase
    public AttendanceEntry() {
        // Default constructor required for Firebase
    }

    public AttendanceEntry(String name, String checkInTime, String checkOutTime, String checkInDate, String checkOutDate) {
        this.name = name;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public String getName() {
        return name;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }
}
