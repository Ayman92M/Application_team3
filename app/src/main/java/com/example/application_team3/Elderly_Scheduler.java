package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Elderly_Scheduler extends AppCompatActivity {
    ListView listView;
    String mealString;
    List<String> mealStrings =  new ArrayList<>();
    Database db = new Database();
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

        Intent get_info = getIntent();
        String elderly_username = get_info.getStringExtra("elderlyUserName");
        String elderly_name = get_info.getStringExtra("elderlyName");

        if(date == null){
            date = String.format("%04d:%02d:%02d", year, month + 1, day);
            System.out.println("date == null");
            //db.listenForMealPlan(elderly_username);
        }

        Log.d("incoming intent", "chosen date" + date);
        chosenDate.setText(date);


        System.out.println(date);
        listView = findViewById(R.id.listView_elderly_scheduler);

        if (elderly_username == null){
            elderly_username = navigator.getUsernameFromPreferences(Elderly_Scheduler.this);
            navigator.notis("elderly_username null :" + elderly_username);

        }
        else{
            navigator.saveInputToPreferencesElderlySchedular(elderly_username);
        }


        navigator.showMealList(listView, R.layout.activity_list_item_elderlyscheduler, true, elderly_username, elderly_name, date);

    }
}