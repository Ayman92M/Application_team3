package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Calendar_overview extends AppCompatActivity {
    ViewNavigator navigator = new ViewNavigator(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_overview);
    }
}