package com.example.application_team3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;

public class Calendar_Overview extends AppCompatActivity {
    ViewNavigator navigator = new ViewNavigator(this);
    private static final String TAG = "calendar_overview";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_overview);
        Intent get_elderlyUserName = getIntent();
        String elderly_username = get_elderlyUserName.getStringExtra("elderlyUserName");
        CalendarView mCalendarview;
        mCalendarview = (CalendarView) findViewById(R.id.calendarView);
        mCalendarview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                String date = year +"-" + (month + 1) + "-" + day;
                Log.d(TAG,"OnSelectedDayChange: date: " + date);


                Intent intent = new Intent(Calendar_Overview.this, Meal_register.class); //Elderly_Scheduler
                intent.putExtra("date", date);
                //intent.putExtra("elderlyUserName", "rem12");
                intent.putExtra("elderlyName", "rem");
                intent.putExtra("elderlyUserName", elderly_username);
                startActivity(intent);

            }
        });

    }
}