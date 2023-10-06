package com.example.application_team3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;

public class testCalendar extends AppCompatActivity {
    ViewNavigator navigator = new ViewNavigator(this);
    private static final String TAG = "calendar_overview";
    private CalendarView mCalendarview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_calendar);
        mCalendarview = (CalendarView) findViewById(R.id.calendarView);
        mCalendarview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                String date = day +"/" + (month + 1) + "/" + year;
                Log.d(TAG,"OnSelectedDayChange: date: " + date);


                Intent intent = new Intent(testCalendar.this, Elderly_Scheduler.class);
                intent.putExtra("date", date);
                startActivity(intent);


                /* In the next page (dayPlanner):
                Intent incomingIntent = getIntent();
                String date = incomingIntent.getStringExtra("date");

                Lägg in date i textView med xxx.setText(date);
                 */
            }
        });

    }
}