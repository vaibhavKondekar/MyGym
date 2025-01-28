//package com.example.gymbuddychat;
//
//import androidx.appcompat.widget.Toolbar; // Add this import statement
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.ActionBarDrawerToggle;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.drawerlayout.widget.DrawerLayout;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.format.DateFormat;
//import android.util.Log;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.android.material.navigation.NavigationView;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.zxing.integration.android.IntentIntegrator;
//import com.google.zxing.integration.android.IntentResult;
//
//import java.util.HashMap;
//
//
//public class UserMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
//
//    private static final String TAG = "UserMenu";
//    private DrawerLayout drawerLayout;
//    private BottomNavigationView bottomNavigationView;
//    private FragmentManager fragmentManager;
//    private Toolbar toolbar;
//    private Button ScanQR;
//    private FirebaseAuth auth;
//    private FirebaseUser user;
//    // Define the stored QR code
//    private String storedQR = "abcd"; // Replace with your stored QR code content
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_user_menu);
//        Log.d(TAG, "onCreate: ");
//        toolbar = findViewById(R.id.toolbar2);
//        setSupportActionBar(toolbar);
//        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
//
//        drawerLayout = findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = findViewById(R.id.navigation_drawer);
//        navigationView.setNavigationItemSelectedListener(this);
//
//        bottomNavigationView = findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setBackground(null);
//
//        fragmentManager = getSupportFragmentManager();
//
//        replaceFragment(new LiveAttendanceFragment());
//        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                int itemId = menuItem.getItemId();
//                if (itemId == R.id.bottom_healthreport) {
//                    replaceFragment(new Health_ReportFragment());
//                }
//                else if (itemId == R.id.bottom_liveattendance) {
//                    replaceFragment(new LiveAttendanceFragment());
//                }
//                else if (itemId == R.id.bottom_weighttracker) {
//                    replaceFragment(new WeightTrackerFragment());
//                }
//                else if (itemId == R.id.bottom_atmosphere) {
//                    replaceFragment(new atmosphere());
//
//                }
//                return true; // Return true to indicate that the item is selected
//            }
//        });
//
//        auth = FirebaseAuth.getInstance();
//        user = auth.getCurrentUser();
//
//        if (user == null) {
//            Intent intent = new Intent(UserMenu.this, Login1.class);
//            startActivity(intent);
//            finish();
//        }
//
//        ScanQR = findViewById(R.id.scanQRButton);
//        // Add the atmosphere button and set its OnClickListener
//
//
////        View atmosphereButton = findViewById(R.id.atmosphereButton); // Reference the atmosphere button
////        atmosphereButton.setOnClickListener(v -> {
////            Toast.makeText(UserMenu.this, "Atmosphere button clicked!", Toast.LENGTH_SHORT).show();
////            Intent intent = new Intent(UserMenu.this, AtmosphereActivity.class); // Change to your activity class
////            startActivity(intent);
////        });
//
//
//
//
//
//
//
//
//        ScanQR.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                scanCode();
//            }
//        });
//    }
//
//    // Method to start QR code scanning
//    private void scanCode() {
//        IntentIntegrator integrator = new IntentIntegrator(this);
//        integrator.setOrientationLocked(false);
//        integrator.setPrompt("Scan a QR Code");
//        integrator.initiateScan();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);
//        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (intentResult != null) {
//            String contents = intentResult.getContents();
//            Log.d(TAG, "onActivityResult: scanned contents=" + contents);
//            if (contents != null) {
//                // Compare scanned QR with stored QR
//                if(contents.equals(storedQR)) {
//                    // Show success dialog
//                    showSuccessDialog();
//                } else {
//                    // Show failure dialog
//                    showFailureDialog();
//                }
//            }
//        }
//    }
//
//    // Method to show dialog for successful attendance
//    private void showSuccessDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(UserMenu.this);
//        builder.setTitle("Attendance Marked");
//        builder.setMessage("Mark Your Attendance");
//
//        // Set the positive button (Check In)
//        builder.setPositiveButton("Check In", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                Toast.makeText(UserMenu.this, "Attendance Marked for Check In.", Toast.LENGTH_SHORT).show();
//                // Add your check-in logic here
//                markAttendance("Check In", FirebaseDatabase.getInstance().getReference("attendance").child(user.getUid()));
//            }
//        });
//
//        // Set the negative button (Check Out)
//        builder.setNegativeButton("Check Out", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                Toast.makeText(UserMenu.this, "Attendance Marked for Check Out.", Toast.LENGTH_SHORT).show();
//                // Add your check-out logic here
//                markAttendance("Check Out", FirebaseDatabase.getInstance().getReference("attendance").child(user.getUid()));
//            }
//        });
//
//        builder.show();
//    }
//
//    // Method to show dialog for failed attendance
//    private void showFailureDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(UserMenu.this);
//        builder.setTitle("Attendance Not Marked");
//        builder.setMessage("Failed to mark attendance. QR code does not match.");
//        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        }).show();
//    }
//
//    // Function to mark attendance
//    private void markAttendance(String attendanceType, DatabaseReference attendanceRef) {
//        if (user != null) {
//            // Retrieve the last attendance entry
//            attendanceRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        AttendanceEntry lastEntry = dataSnapshot.getChildren().iterator().next().getValue(AttendanceEntry.class);
//                        if (lastEntry != null) {
//                            if (attendanceType.equals("Check In")) {
//                                if (lastEntry.getCheckOutDate() == null) {
//                                    Toast.makeText(UserMenu.this, "You are already checked in. Please check out first.", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    performCheckIn(attendanceRef, user.getUid());
//                                }
//                            } else if (attendanceType.equals("Check Out")) {
//                                if (lastEntry.getCheckOutDate() != null) {
//                                    Toast.makeText(UserMenu.this, "You are already checked out. Please check in first.", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    performCheckOut(attendanceRef);
//                                }
//                            }
//                        }
//                    } else {
//                        // No previous attendance entry found, proceed with check-in
//                        if (attendanceType.equals("Check In")) {
//                            performCheckIn(attendanceRef, user.getUid());
//                        } else {
//                            Toast.makeText(UserMenu.this, "No previous attendance entry found. Please check in first.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    Log.e(TAG, "Error checking attendance status", databaseError.toException());
//                    Toast.makeText(UserMenu.this, "Failed to check attendance status. Please try again.", Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else {
//            Log.e(TAG, "Current user is null");
//            Toast.makeText(UserMenu.this, "User not logged in. Please log in again.", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    // Perform check-in logic
//    private void performCheckIn(DatabaseReference attendanceRef, String userID) {
//        // Retrieve username from Firestore
//        FirebaseFirestore.getInstance().collection("users").document(userID)
//                .get()
//                .addOnSuccessListener(documentSnapshot -> {
//                    if (documentSnapshot.exists()) {
//                        String userName = documentSnapshot.getString("Username");
//                        if (userName != null && !userName.isEmpty()) {
//                            // Proceed with check-in
//                            String date = DateFormat.format("yyyy-MM-dd", System.currentTimeMillis()).toString();
//                            String time = DateFormat.format("HH:mm:ss", System.currentTimeMillis()).toString();
//                            HashMap<String, Object> attendanceData = new HashMap<>();
//                            attendanceData.put("name", userName); // Store user's name
//                            attendanceData.put("checkInDate", date);
//                            attendanceData.put("checkInTime", time);
//
//                            // Push the data to Firebase under a unique key
//                            attendanceRef.push().setValue(attendanceData)
//                                    .addOnSuccessListener(aVoid -> {
//                                        // Attendance marked successfully
//                                        Log.d(TAG, "Check-in marked successfully");
//                                        Toast.makeText(UserMenu.this, "Check-in marked successfully.", Toast.LENGTH_SHORT).show();
//                                    })
//                                    .addOnFailureListener(e -> {
//                                        // Error occurred while marking attendance
//                                        Log.e(TAG, "Error marking check-in", e);
//                                        Toast.makeText(UserMenu.this, "Failed to mark check-in. Please try again.", Toast.LENGTH_SHORT).show();
//                                    });
//                        } else {
//                            Log.e(TAG, "User name is null or empty");
//                            Toast.makeText(UserMenu.this, "Failed to retrieve user name. Please try again.", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Log.e(TAG, "User document does not exist");
//                        Toast.makeText(UserMenu.this, "User document not found. Please try again.", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    Log.e(TAG, "Error fetching user data", e);
//                    Toast.makeText(UserMenu.this, "Failed to fetch user data. Please try again.", Toast.LENGTH_SHORT).show();
//                });
//    }
//
//
//    // Function to perform check-out
//    private void performCheckOut(DatabaseReference attendanceRef) {
//        // Retrieve the last check-in entry from Firebase
//        attendanceRef.orderByChild("checkOutDate").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    // Get the key of the last check-in entry
//                    String checkInKey = snapshot.getKey();
//                    if (checkInKey != null) {
//                        // Update the check-out time for the last check-in entry
//                        String date = DateFormat.format("yyyy-MM-dd", System.currentTimeMillis()).toString();
//                        String time = DateFormat.format("HH:mm:ss", System.currentTimeMillis()).toString();
//                        attendanceRef.child(checkInKey).child("checkOutDate").setValue(date);
//                        attendanceRef.child(checkInKey).child("checkOutTime").setValue(time)
//                                .addOnSuccessListener(aVoid -> {
//                                    // Check-out time updated successfully
//                                    Log.d(TAG, "performCheckOut: Check-out marked successfully");
//                                    Toast.makeText(UserMenu.this, "Check-out marked successfully.", Toast.LENGTH_SHORT).show();
//                                })
//                                .addOnFailureListener(e -> {
//                                    // Error occurred while updating check-out time
//                                    Log.e(TAG, "performCheckOut: Error marking check-out", e);
//                                    Toast.makeText(UserMenu.this, "Failed to mark check-out. Please try again.", Toast.LENGTH_SHORT).show();
//                                });
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e(TAG, "performCheckOut: Error retrieving last check-in entry", databaseError.toException());
//                Toast.makeText(UserMenu.this, "Failed to mark check-out. Please try again.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//        int itemId = menuItem.getItemId();
//
//        if (itemId == R.id.Drawer_exit) {
//            FirebaseAuth.getInstance().signOut();
//            Intent intent = new Intent(UserMenu.this, Login1.class);
//            startActivity(intent);
//            finish();
//            return true; // Return true to indicate that the item is selected
//        }
//
//        return false;
//    }
//
//    // Method to replace fragment
//    private void replaceFragment(Fragment fragment) {
//        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
//    }
//}
//
//
//
////package com.example.gymbuddychat;
////
////import android.content.Intent;
////import android.os.Bundle;
////import android.view.MenuItem;
////import android.view.WindowManager;
////import android.widget.Button;
////
////import androidx.annotation.NonNull;
////import androidx.appcompat.app.ActionBarDrawerToggle;
////import androidx.appcompat.app.AppCompatActivity;
////import androidx.appcompat.widget.Toolbar;
////import androidx.core.view.GravityCompat;
////import androidx.drawerlayout.widget.DrawerLayout;
////import androidx.fragment.app.Fragment;
////import androidx.fragment.app.FragmentManager;
////
////import com.google.android.material.bottomnavigation.BottomNavigationView;
////import com.google.android.material.navigation.NavigationView;
////import com.google.firebase.auth.FirebaseAuth;
////
////public class UserMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
////    DrawerLayout drawerLayout;
////    BottomNavigationView bottomNavigationView;
////    FragmentManager fragmentManager;
////    Toolbar toolbar;
////    Button logout;
////
////    @Override
////    public void onBackPressed() {
////        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
////            drawerLayout.closeDrawer(GravityCompat.START);
////        } else {
////            super.onBackPressed();
////        }
////    }
////
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_user_menu);
////        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
////
////        toolbar = findViewById(R.id.toolbar2);
////        setSupportActionBar(toolbar);
////        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
////
////        drawerLayout = findViewById(R.id.drawer_layout);
////        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
////        drawerLayout.addDrawerListener(toggle);
////        toggle.syncState();
////
////        NavigationView navigationView = findViewById(R.id.navigation_drawer);
////        navigationView.setNavigationItemSelectedListener(this);
////
////        bottomNavigationView = findViewById(R.id.bottom_navigation);
////        bottomNavigationView.setBackground(null);
////
////        fragmentManager = getSupportFragmentManager(); // Initialize fragmentManager
////
////        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
////            @Override
////            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
////                int itemId = menuItem.getItemId();
////                if (itemId == R.id.bottom_scan) {
////                    replaceFragment(new ScanFragment());
////                } else if (itemId == R.id.bottom_healthreport) {
////                    replaceFragment(new Health_ReportFragment());
////                }
////                 else if (itemId == R.id.bottom_liveattendance) {
////                    replaceFragment(new LiveAttendanceFragment());
////                }
////                else if (itemId == R.id.bottom_weighttracker) {
////                    replaceFragment(new WeightTrackerFragment());
////                }
////                else if (itemId == R.id.bottom_gymbuddy) {
////                    Intent intent = new Intent(UserMenu.this, LoginPhoneNumberActivity.class);
////                    startActivity(intent);
////                }
////                return true; // Return true to indicate that the item is selected
////            }
////        });
////
////        // Replace the initial fragment with HomeFragment
////        replaceFragment(new ScanFragment());
////    }
////
////
////    @Override
////    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
////        int itemId = menuItem.getItemId();
////
////        if (itemId == R.id.Drawer_exit) {
////            FirebaseAuth.getInstance().signOut();
////            Intent intent = new Intent(UserMenu.this, Login1.class);
////            startActivity(intent);
////            finish();
////            return true; // Return true to indicate that the item is selected
////        }
////
////        return false;
////    }
////
////    // Method to replace fragment
////    private void replaceFragment(Fragment fragment) {
////        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
////    }
////}






package com.example.gymbuddychat;

import androidx.appcompat.widget.Toolbar; // Add this import statement
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;


public class UserMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "UserMenu";
    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;
    private Button ScanQR;
    private FirebaseAuth auth;
    private FirebaseUser user;
    // Define the stored QR code
    private String storedQR = "abcd"; // Replace with your stored QR code content

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_menu);
        Log.d(TAG, "onCreate: ");
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setBackground(null);

        fragmentManager = getSupportFragmentManager();

        replaceFragment(new LiveAttendanceFragment());
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.bottom_healthreport) {
                    replaceFragment(new Health_ReportFragment());
                }
                else if (itemId == R.id.bottom_liveattendance) {
                    replaceFragment(new LiveAttendanceFragment());
                }
                else if (itemId == R.id.bottom_weighttracker) {
                    replaceFragment(new WeightTrackerFragment());
                }
                else if (itemId == R.id.bottom_atmosphere) {
                    replaceFragment(new Atmosphere());
                }

//                else if (itemId == R.id.bottom_gymbuddy) {
//                    Intent intent = new Intent(UserMenu.this, LoginPhoneNumberActivity.class);
//                    startActivity(intent);
//                }
                return true; // Return true to indicate that the item is selected
            }
        });

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(UserMenu.this, Login1.class);
            startActivity(intent);
            finish();
        }

        ScanQR = findViewById(R.id.scanQRButton);

        ScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }
        });
    }

    // Method to start QR code scanning
    private void scanCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.setPrompt("Scan a QR Code");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            String contents = intentResult.getContents();
            Log.d(TAG, "onActivityResult: scanned contents=" + contents);
            if (contents != null) {
                // Compare scanned QR with stored QR
                if(contents.equals(storedQR)) {
                    // Show success dialog
                    showSuccessDialog();
                } else {
                    // Show failure dialog
                    showFailureDialog();
                }
            }
        }
    }

    // Method to show dialog for successful attendance
    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserMenu.this);
        builder.setTitle("Attendance Marked");
        builder.setMessage("Mark Your Attendance");

        // Set the positive button (Check In)
        builder.setPositiveButton("Check In", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(UserMenu.this, "Attendance Marked for Check In.", Toast.LENGTH_SHORT).show();
                // Add your check-in logic here
                markAttendance("Check In", FirebaseDatabase.getInstance().getReference("attendance").child(user.getUid()));
            }
        });

        // Set the negative button (Check Out)
        builder.setNegativeButton("Check Out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(UserMenu.this, "Attendance Marked for Check Out.", Toast.LENGTH_SHORT).show();
                // Add your check-out logic here
                markAttendance("Check Out", FirebaseDatabase.getInstance().getReference("attendance").child(user.getUid()));
            }
        });

        builder.show();
    }

    // Method to show dialog for failed attendance
    private void showFailureDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserMenu.this);
        builder.setTitle("Attendance Not Marked");
        builder.setMessage("Failed to mark attendance. QR code does not match.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    // Function to mark attendance
    private void markAttendance(String attendanceType, DatabaseReference attendanceRef) {
        if (user != null) {
            // Retrieve the last attendance entry
            attendanceRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        AttendanceEntry lastEntry = dataSnapshot.getChildren().iterator().next().getValue(AttendanceEntry.class);
                        if (lastEntry != null) {
                            if (attendanceType.equals("Check In")) {
                                if (lastEntry.getCheckOutDate() == null) {
                                    Toast.makeText(UserMenu.this, "You are already checked in. Please check out first.", Toast.LENGTH_SHORT).show();
                                } else {
                                    performCheckIn(attendanceRef, user.getUid());
                                }
                            } else if (attendanceType.equals("Check Out")) {
                                if (lastEntry.getCheckOutDate() != null) {
                                    Toast.makeText(UserMenu.this, "You are already checked out. Please check in first.", Toast.LENGTH_SHORT).show();
                                } else {
                                    performCheckOut(attendanceRef);
                                }
                            }
                        }
                    } else {
                        // No previous attendance entry found, proceed with check-in
                        if (attendanceType.equals("Check In")) {
                            performCheckIn(attendanceRef, user.getUid());
                        } else {
                            Toast.makeText(UserMenu.this, "No previous attendance entry found. Please check in first.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, "Error checking attendance status", databaseError.toException());
                    Toast.makeText(UserMenu.this, "Failed to check attendance status. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e(TAG, "Current user is null");
            Toast.makeText(UserMenu.this, "User not logged in. Please log in again.", Toast.LENGTH_SHORT).show();
        }
    }
    private void performCheckIn(DatabaseReference attendanceRef, String userID) {
        // Retrieve username from Firestore
        FirebaseFirestore.getInstance().collection("users").document(userID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String userName = documentSnapshot.getString("Username");
                        if (userName != null && !userName.isEmpty()) {
                            String date = DateFormat.format("yyyy-MM-dd", System.currentTimeMillis()).toString();
                            String time = DateFormat.format("HH:mm:ss", System.currentTimeMillis()).toString();
                            HashMap<String, Object> attendanceData = new HashMap<>();
                            attendanceData.put("name", userName);
                            attendanceData.put("checkInDate", date);
                            attendanceData.put("checkInTime", time);
                            attendanceData.put("checkOutDate", null);  // Ensure checkOutDate is null for new check-ins

                            attendanceRef.push().setValue(attendanceData)
                                    .addOnSuccessListener(aVoid -> {
                                        // Increment the live attendance count
                                        incrementLiveAttendanceCount();
                                        Toast.makeText(UserMenu.this, "Check-in marked successfully.", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e(TAG, "Error marking check-in", e);
                                        Toast.makeText(UserMenu.this, "Failed to mark check-in. Please try again.", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Log.e(TAG, "User name is null or empty");
                            Toast.makeText(UserMenu.this, "Failed to retrieve user name. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e(TAG, "User document does not exist");
                        Toast.makeText(UserMenu.this, "User document not found. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching user data", e);
                    Toast.makeText(UserMenu.this, "Failed to fetch user data. Please try again.", Toast.LENGTH_SHORT).show();
                });
    }



    private void performCheckOut(DatabaseReference attendanceRef) {
        attendanceRef.orderByChild("checkOutDate").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String checkInKey = snapshot.getKey();
                    if (checkInKey != null) {
                        String date = DateFormat.format("yyyy-MM-dd", System.currentTimeMillis()).toString();
                        String time = DateFormat.format("HH:mm:ss", System.currentTimeMillis()).toString();
                        attendanceRef.child(checkInKey).child("checkOutDate").setValue(date);
                        attendanceRef.child(checkInKey).child("checkOutTime").setValue(time)
                                .addOnSuccessListener(aVoid -> {
                                    decrementLiveAttendanceCount();
                                    Toast.makeText(UserMenu.this, "Check-out marked successfully.", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e(TAG, "performCheckOut: Error marking check-out", e);
                                    Toast.makeText(UserMenu.this, "Failed to mark check-out. Please try again.", Toast.LENGTH_SHORT).show();
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "performCheckOut: Error retrieving last check-in entry", databaseError.toException());
                Toast.makeText(UserMenu.this, "Failed to mark check-out. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void incrementLiveAttendanceCount() {
        DatabaseReference attendanceCounterRef = FirebaseDatabase.getInstance().getReference("attendanceCounters/liveattendancecnt");
        attendanceCounterRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Long currentCount = mutableData.getValue(Long.class);
                if (currentCount == null) {
                    currentCount = 0L;  // Initialize if null
                }
                mutableData.setValue(currentCount + 1);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                if (error != null) {
                    Log.e(TAG, "Failed to increment live attendance count", error.toException());
                } else {
                    Log.d(TAG, "Live attendance count incremented successfully");
                }
            }
        });
    }

    private void decrementLiveAttendanceCount() {
        DatabaseReference attendanceCounterRef = FirebaseDatabase.getInstance().getReference("attendanceCounters/liveattendancecnt");
        attendanceCounterRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Long currentCount = mutableData.getValue(Long.class);
                if (currentCount == null || currentCount <= 0) {
                    currentCount = 0L;  // Handle underflow
                } else {
                    currentCount--;
                }
                mutableData.setValue(currentCount);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                if (error != null) {
                    Log.e(TAG, "Failed to decrement live attendance count", error.toException());
                }
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();

        if (itemId == R.id.Drawer_exit) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(UserMenu.this, Login1.class);
            startActivity(intent);
            finish();
            return true; // Return true to indicate that the item is selected
        }

        return false;
    }

    // Method to replace fragment
    private void replaceFragment(Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }
}



// Perform check-in logic
//    private void performCheckIn(DatabaseReference attendanceRef, String userID) {
//        // Retrieve username from Firestore
//        FirebaseFirestore.getInstance().collection("users").document(userID)
//                .get()
//                .addOnSuccessListener(documentSnapshot -> {
//                    if (documentSnapshot.exists()) {
//                        String userName = documentSnapshot.getString("Username");
//                        if (userName != null && !userName.isEmpty()) {
//                            // Proceed with check-in
//                            String date = DateFormat.format("yyyy-MM-dd", System.currentTimeMillis()).toString();
//                            String time = DateFormat.format("HH:mm:ss", System.currentTimeMillis()).toString();
//                            HashMap<String, Object> attendanceData = new HashMap<>();
//                            attendanceData.put("name", userName); // Store user's name
//                            attendanceData.put("checkInDate", date);
//                            attendanceData.put("checkInTime", time);
//
//                            // Push the data to Firebase under a unique key
//                            attendanceRef.push().setValue(attendanceData)
//                                    .addOnSuccessListener(aVoid -> {
//                                        // Attendance marked successfully
//                                        Log.d(TAG, "Check-in marked successfully");
//                                        Toast.makeText(UserMenu.this, "Check-in marked successfully.", Toast.LENGTH_SHORT).show();
//                                    })
//                                    .addOnFailureListener(e -> {
//                                        // Error occurred while marking attendance
//                                        Log.e(TAG, "Error marking check-in", e);
//                                        Toast.makeText(UserMenu.this, "Failed to mark check-in. Please try again.", Toast.LENGTH_SHORT).show();
//                                    });
//                        } else {
//                            Log.e(TAG, "User name is null or empty");
//                            Toast.makeText(UserMenu.this, "Failed to retrieve user name. Please try again.", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Log.e(TAG, "User document does not exist");
//                        Toast.makeText(UserMenu.this, "User document not found. Please try again.", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    Log.e(TAG, "Error fetching user data", e);
//                    Toast.makeText(UserMenu.this, "Failed to fetch user data. Please try again.", Toast.LENGTH_SHORT).show();
//                });
//    }


// Function to perform check-out
//    private void performCheckOut(DatabaseReference attendanceRef) {
//        // Retrieve the last check-in entry from Firebase
//        attendanceRef.orderByChild("checkOutDate").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    // Get the key of the last check-in entry
//                    String checkInKey = snapshot.getKey();
//                    if (checkInKey != null) {
//                        // Update the check-out time for the last check-in entry
//                        String date = DateFormat.format("yyyy-MM-dd", System.currentTimeMillis()).toString();
//                        String time = DateFormat.format("HH:mm:ss", System.currentTimeMillis()).toString();
//                        attendanceRef.child(checkInKey).child("checkOutDate").setValue(date);
//                        attendanceRef.child(checkInKey).child("checkOutTime").setValue(time)
//                                .addOnSuccessListener(aVoid -> {
//                                    // Check-out time updated successfully
//                                    Log.d(TAG, "performCheckOut: Check-out marked successfully");
//                                    Toast.makeText(UserMenu.this, "Check-out marked successfully.", Toast.LENGTH_SHORT).show();
//                                })
//                                .addOnFailureListener(e -> {
//                                    // Error occurred while updating check-out time
//                                    Log.e(TAG, "performCheckOut: Error marking check-out", e);
//                                    Toast.makeText(UserMenu.this, "Failed to mark check-out. Please try again.", Toast.LENGTH_SHORT).show();
//                                });
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e(TAG, "performCheckOut: Error retrieving last check-in entry", databaseError.toException());
//                Toast.makeText(UserMenu.this, "Failed to mark check-out. Please try again.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }