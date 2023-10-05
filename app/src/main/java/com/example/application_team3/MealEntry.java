package com.example.application_team3;

public class MealEntry {
    private String mealType;
    private String time;
    private String note;
    private boolean hasEaten;
    private MealEntry() {}
    public MealEntry(String mealType, String time, String note, boolean hasEaten) {
        this.mealType = mealType;
        this.time = time;
        this.note = note;
        this.hasEaten = hasEaten;
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
}
