package com.example.gymbuddychat.model;
public class ExerciseRequest {
    private String exerciseId;
    private String exerciseType;
    private String time;
    private String date;
    private String senderUserId;
    private String senderUsername;
    private String receiverUserId;
    private String receiverUsername;
    private boolean accepted;
    private String acceptedByUserId;
    private String acceptedByUsername;

    public ExerciseRequest() {
        // Default constructor required for Firebase
    }

    public ExerciseRequest(String exerciseId, String exerciseType, String time, String date, String senderUserId, String senderUsername, String receiverUserId, String receiverUsername) {
        this.exerciseId = exerciseId;
        this.exerciseType = exerciseType;
        this.time = time;
        this.date = date;
        this.senderUserId = senderUserId;
        this.senderUsername = senderUsername;
        this.receiverUserId = receiverUserId;
        this.receiverUsername = receiverUsername;
        this.accepted = false; // Initially set to false
    }

    public ExerciseRequest(String exerciseId, String exerciseType, String time, String date, String senderUserId, String senderUsername) {
        this.exerciseId = exerciseId;
        this.exerciseType = exerciseType;
        this.time = time;
        this.date = date;
        this.senderUserId = senderUserId;
        this.senderUsername = senderUsername;
        this.accepted = false; // Initially set to false
    }

    public ExerciseRequest(String exerciseId, String exerciseType, String time, String date,
                           String senderUserId, String senderUsername,
                           String receiverUserId, String receiverUsername,
                           boolean accepted, String acceptedByUserId, String acceptedByUsername) {
        this.exerciseId = exerciseId;
        this.exerciseType = exerciseType;
        this.time = time;
        this.date = date;
        this.senderUserId = senderUserId;
        this.senderUsername = senderUsername;
        this.receiverUserId = receiverUserId;
        this.receiverUsername = receiverUsername;
        this.accepted = accepted;
        this.acceptedByUserId = acceptedByUserId;
        this.acceptedByUsername = acceptedByUsername;
    }

    public String getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(String exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getReceiverUserId() {
        return receiverUserId;
    }

    public void setReceiverUserId(String receiverUserId) {
        this.receiverUserId = receiverUserId;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getAcceptedByUserId() {
        return acceptedByUserId;
    }

    public void setAcceptedByUserId(String acceptedByUserId) {
        this.acceptedByUserId = acceptedByUserId;
    }

    public String getAcceptedByUsername() {
        return acceptedByUsername;
    }

    public void setAcceptedByUsername(String acceptedByUsername) {
        this.acceptedByUsername = acceptedByUsername;
    }
}
