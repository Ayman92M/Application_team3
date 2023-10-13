package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Elderly_Scheduler extends AppCompatActivity {
    ListView listView;
    ViewNavigator navigator = new ViewNavigator(this);

    TextView chosenDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elderly_scheduler);

        chosenDate = findViewById(R.id.day_and_date);
        Intent incomingIntent = getIntent();
        String date = incomingIntent.getStringExtra("date");


        Calendar day_calendar = Calendar.getInstance();
        int year = day_calendar.get(Calendar.YEAR);
        int month = day_calendar.get(Calendar.MONTH);
        int day = day_calendar.get(Calendar.DAY_OF_MONTH);

        // Adding 1 to month to get the human-readable month representation
        Intent get_info = getIntent();
        String elderly_username = get_info.getStringExtra("elderlyUserName");
        String elderly_name = get_info.getStringExtra("elderlyName");

        if(date == null){
            date = year + "-" + (month + 1) + "-" + day;
            System.out.println("date == null");
            //db.listenForMealPlan(elderly_username);
        }

        Log.d("incoming intent", "chosen date" + date);
        chosenDate.setText(date);



        System.out.println(date);
        listView = findViewById(R.id.listView_elderly_scheduler);

        navigator.showMealList(listView, R.layout.activity_list_item_elderlyscheduler, true, elderly_username, elderly_name, date);

    }
}