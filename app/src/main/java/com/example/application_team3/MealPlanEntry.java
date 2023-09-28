package com.example.application_team3;

import java.util.HashMap;

public class MealPlanEntry {
    private String date;
    private HashMap<String, String> meals = new HashMap<>();
    private MealPlanEntry() {}
    public MealPlanEntry(String date, String breakfastNote, String lunchNote, String smallMealNote, String dinnerNote) {
        this.date = date;
        meals.put("breakfast", breakfastNote);
        meals.put("lunch", lunchNote);
        meals.put("small-meal", smallMealNote);
        meals.put("dinner", dinnerNote);
    }

    public String getDate(){
        return date;
    }
    public void setDate(String date){
        this.date = date;
    }
    public HashMap<String, String> getMeals(){
        return meals;
    }
    public void setMeals(HashMap<String, String> meals){
        this.meals = meals;
    }

    public void addMeal(String meal, String mealNote){
        meals.put(meal, mealNote);
    }


}
