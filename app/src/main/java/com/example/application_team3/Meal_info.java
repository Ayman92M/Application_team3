package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class Meal_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_info);

        Intent get_info = getIntent();
        System.out.println("mealType: " + get_info.getStringExtra("mealType"));
        System.out.println("time: " + get_info.getStringExtra("mealTime"));
        System.out.println("note: " + get_info.getStringExtra("mealNote"));
        System.out.println("hasEaten: " + get_info.getStringExtra("hasEaten"));
        System.out.println("elderly_name: " + get_info.getStringExtra("elderlyName"));
        System.out.println("username: " + get_info.getStringExtra("elderlyUserName"));


    }
}