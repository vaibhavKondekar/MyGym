package com.example.gymbuddychat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {


    // Function to convert timestamp to real-time string
// Function to convert timestamp to real-time string
    // Function to convert timestamp to real-time string
    private String convertTimestampToTime(Long timestamp) {
        if (timestamp == null || timestamp == 0) {
            return "-"; // Return "-" if timestamp is null or 0 (indicating no check-out time)
        } else {
            try {
                // Convert timestamp to Date object
                Date date = new Date(timestamp);

                // Create a SimpleDateFormat object with desired format
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.getDefault());

                // Format the Date object to desired format
                return sdf.format(date);
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
    }


}
