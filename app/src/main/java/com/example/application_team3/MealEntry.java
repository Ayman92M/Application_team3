package com.example.application_team3;

public class MealEntry {
    private String date;
    private String mealType;

    private String time;
    private String note;
    private boolean hasEaten;
    private String elderlyNotificationID, caregiverNotificationID;

    private MealEntry() {}
    public MealEntry(String date, String mealType, String time, String note, boolean hasEaten) {
        this.date = date;
        this.mealType = mealType;
        this.time = time;
        this.note = note;
        this.hasEaten = hasEaten;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isHasEaten() {
        return hasEaten;
    }

    public void setHasEaten(boolean hasEaten) {
        this.hasEaten = hasEaten;
    }

    public String getElderlyNotificationID() {
        return elderlyNotificationID;
    }

    public void setElderlyNotificationID(String elderlyNotificationID) {
        this.elderlyNotificationID = elderlyNotificationID;
    }

    public String getCaregiverNotificationID() {
        return caregiverNotificationID;
    }

    public void setCaregiverNotificationID(String caregiverNotificationID) {
        this.caregiverNotificationID = caregiverNotificationID;
    }
}