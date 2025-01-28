//package com.example.gymbuddychat;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
////import android.text.format.DateFormat;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.zxing.integration.android.IntentIntegrator;
//import com.google.zxing.integration.android.IntentResult;
//
//import java.util.HashMap;
//import java.text.DateFormat;
//
//public class ScanFragment extends Fragment {
//
//    private static final String TAG = "ScanFragment";
//    private String StoredQR="abcd"; // Initialize this with your stored QR code
//
//    public ScanFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_scan, container, false);
//        initViews(view);
//        return view;
//    }
//
//    private void initViews(View view) {
//        view.findViewById(R.id.ScanQR).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                scanCode();
//            }
//        });
//    }
//
//    private void scanCode() {
//        IntentIntegrator integrator = new IntentIntegrator(getActivity());
//        integrator.setOrientationLocked(false);
//        integrator.setPrompt("Scan a QR Code");
//        integrator.initiateScan();
//    }
//
//
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);
//        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (intentResult != null) {
//            String contents = intentResult.getContents();
//            Log.d(TAG, "onActivityResult: scanned contents=" + contents);
//            if (contents != null) {
//                // Compare scanned QR with stored QR
//                if (contents.equals(StoredQR)) {
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
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Attendance Marked");
//        builder.setMessage("Mark Your Attendance");
//
//        // Set the positive button (Check In)
//        builder.setPositiveButton("Check In", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                Toast.makeText(getActivity(), "Attendance Marked for Check In.", Toast.LENGTH_SHORT).show();
//                // Add your check-in logic here
//                markAttendance("Check In");
//            }
//        });
//
//        // Set the negative button (Check Out)
//        builder.setNegativeButton("Check Out", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                Toast.makeText(getActivity(), "Attendance Marked for Check Out.", Toast.LENGTH_SHORT).show();
//                // Add your check-out logic here
//                markAttendance("Check Out");
//            }
//        });
//
//        builder.show();
//    }
//
//    // Method to show dialog for failed attendance
//    private void showFailureDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
//    private void markAttendance(String attendanceType) {
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (currentUser != null) {
//            String userID = currentUser.getUid();
//            DatabaseReference attendanceRef = FirebaseDatabase.getInstance().getReference("attendance").child(userID);
//
//            // Check if the user is already checked in
//            attendanceRef.orderByChild("checkOutDate").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    boolean isAlreadyCheckedIn = false;
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        AttendanceEntry lastEntry = snapshot.getValue(AttendanceEntry.class);
//                        if (lastEntry != null && lastEntry.getCheckOutDate() == null) {
//                            // User is already checked in
//                            isAlreadyCheckedIn = true;
//                            break;
//                        }
//                    }
//
//                    if (isAlreadyCheckedIn) {
//                        // User is already checked in, prevent check-in
//                        Toast.makeText(getActivity(), "You are already checked in. Please check out first.", Toast.LENGTH_SHORT).show();
//                    } else {
//                        // User is not checked in, proceed with check-in or check-out logic
//                        if (attendanceType.equals("Check In")) {
//                            // Perform check-in
//                            performCheckIn(attendanceRef, userID);
//                        } else if (attendanceType.equals("Check Out")) {
//                            // Perform check-out
//                            String date = DateFormat.getDateInstance().format(System.currentTimeMillis());
//                            String time = DateFormat.getTimeInstance().format(System.currentTimeMillis());
//                            performCheckOut(attendanceRef, date, time);
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    Log.e(TAG, "Error checking attendance status", databaseError.toException());
//                    Toast.makeText(getActivity(), "Failed to check attendance status. Please try again.", Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else {
//            Log.e(TAG, "Current user is null");
//            Toast.makeText(getActivity(), "User not logged in. Please log in again.", Toast.LENGTH_SHORT).show();
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
//                            String date = DateFormat.getDateInstance().format(System.currentTimeMillis());
//                            String time = DateFormat.getTimeInstance().format(System.currentTimeMillis());
//                            HashMap<String, Object> attendanceData = new HashMap<>();
//                            attendanceData.put("name", userName);
//                            attendanceData.put("checkInDate", date);
//                            attendanceData.put("checkInTime", time);
//
//                            // Push the data to Firebase under a unique key
//                            attendanceRef.push().setValue(attendanceData)
//                                    .addOnSuccessListener(aVoid -> {
//                                        // Attendance marked successfully
//                                        Log.d(TAG, "Check-in marked successfully");
//                                        Toast.makeText(getActivity(), "Check-in marked successfully.", Toast.LENGTH_SHORT).show();
//                                    })
//                                    .addOnFailureListener(e -> {
//                                        // Error occurred while marking attendance
//                                        Log.e(TAG, "Error marking check-in", e);
//                                        Toast.makeText(getActivity(), "Failed to mark check-in. Please try again.", Toast.LENGTH_SHORT).show();
//                                    });
//                        } else {
//                            Log.e(TAG, "User name is null or empty");
//                            Toast.makeText(getActivity(), "Failed to retrieve user name. Please try again.", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Log.e(TAG, "User document does not exist");
//                        Toast.makeText(getActivity(), "User document not found. Please try again.", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    Log.e(TAG, "Error fetching user data", e);
//                    Toast.makeText(getActivity(), "Failed to fetch user data. Please try again.", Toast.LENGTH_SHORT).show();
//                });
//    }
//
//    // Function to perform check-out
//    private void performCheckOut(DatabaseReference attendanceRef, String date, String time) {
//        // Retrieve the last check-in entry from Firebase
//        attendanceRef.orderByChild("checkInDate").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    // Get the key of the last check-in entry
//                    String checkInKey = snapshot.getKey();
//                    if (checkInKey != null) {
//                        // Update the check-out time for the last check-in entry
//                        attendanceRef.child(checkInKey).child("checkOutDate").setValue(date);
//                        attendanceRef.child(checkInKey).child("checkOutTime").setValue(time)
//                                .addOnSuccessListener(aVoid -> {
//                                    // Check-out time updated successfully
//                                    Log.d(TAG, "performCheckOut: Check-out marked successfully");
//                                    Toast.makeText(getActivity(), "Check-out marked successfully.", Toast.LENGTH_SHORT).show();
//                                })
//                                .addOnFailureListener(e -> {
//                                    // Error occurred while updating check-out time
//                                    Log.e(TAG, "performCheckOut: Error marking check-out", e);
//                                    Toast.makeText(getActivity(), "Failed to mark check-out. Please try again.", Toast.LENGTH_SHORT).show();
//                                });
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e(TAG, "performCheckOut: Error retrieving last check-in entry", databaseError.toException());
//                Toast.makeText(getActivity(), "Failed to mark check-out. Please try again.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}
