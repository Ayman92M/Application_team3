package com.example.application_team3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CalenderOverviewCaregiver extends AppCompatActivity {
    ListView listView;
    String date;
    ViewNavigator navigator = new ViewNavigator(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_overview_caregiver);

        Intent get_elderlyUserName = getIntent();
        String elderly_username = get_elderlyUserName.getStringExtra("elderlyUserName");
        String elderly_name = get_elderlyUserName.getStringExtra("elderlyName");

        date = getTodayList(elderly_username, elderly_name);
        Button bt_addMeal = findViewById(R.id.button_AddMeal);
        addMeal(bt_addMeal, elderly_username, elderly_name);


        CalendarView mCalendarview = (CalendarView) findViewById(R.id.CalendarView_calender_ID);
        calenderActionListener(mCalendarview, elderly_username, elderly_name);
    }

    private void calenderActionListener(CalendarView mCalendarview, String elderly_username, String elderly_name){
        mCalendarview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                date = year +"-" + (month + 1) + "-" + day;

                listView = findViewById(R.id.listView_caregiver_scheduler);
                navigator.showMealList(listView, R.layout.activity_list_item_caregiverscheduler, elderly_username, elderly_name, date);

                Button bt_addMeal = findViewById(R.id.button_AddMeal);
                addMeal(bt_addMeal, elderly_username, elderly_name);

            }
        });
    }

    private void addMeal(Button bt, String elderly_username, String elderly_name){
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalenderOverviewCaregiver.this, Meal_register.class); //Elderly_Scheduler
                intent.putExtra("date", date);
                intent.putExtra("elderlyName", elderly_name);
                intent.putExtra("elderlyUserName", elderly_username);
                navigator.notis("selected date: " + date);
                startActivity(intent);
            }
        });

    }
    private String getTodayList(String elderly_username, String elderly_name){
        Calendar day_calendar = Calendar.getInstance();
        int year = day_calendar.get(Calendar.YEAR);
        int month = day_calendar.get(Calendar.MONTH);
        int day = day_calendar.get(Calendar.DAY_OF_MONTH);

        if(date == null){
            listView = findViewById(R.id.listView_caregiver_scheduler);
            date = year + "-" + (month + 1) + "-" + day;
            System.out.println(date);
            navigator.showMealList(listView, R.layout.activity_list_item_caregiverscheduler, elderly_username, elderly_name, date);

            //db.listenForMealPlan(elderly_username);
        }
        return date;
    }




}