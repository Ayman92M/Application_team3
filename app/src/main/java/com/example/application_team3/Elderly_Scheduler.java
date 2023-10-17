package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Elderly_Scheduler extends AppCompatActivity {
    ListView listView;
    String mealString;
    List<String> mealStrings =  new ArrayList<>();
    Database db = new Database();
    ViewNavigator navigator = new ViewNavigator(this);
    String date;
    String elderly_username, elderly_name;

    TextView chosenDate;
    private static final int COUNTDOWN_TIME = 10000; // 10 sekunder
    private CountDownTimer countDownTimer;
    private Button sosButton;
    private Button cancelButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elderly_scheduler);

        chosenDate = findViewById(R.id.day_and_date);
        Intent incomingIntent = getIntent();
        date = incomingIntent.getStringExtra("date");


        Calendar day_calendar = Calendar.getInstance();
        int year = day_calendar.get(Calendar.YEAR);
        int month = day_calendar.get(Calendar.MONTH);
        int day = day_calendar.get(Calendar.DAY_OF_MONTH);

        Intent get_info = getIntent();
        elderly_username = get_info.getStringExtra("elderlyUserName");
        elderly_name = get_info.getStringExtra("elderlyName");

        if(date == null){
            date = String.format("%04d-%02d-%02d", year, month + 1, day);
        }

        Log.d("incoming intent", "chosen date" + date);
        chosenDate.setText(date);

        listView = findViewById(R.id.listView_elderly_scheduler);

        if (elderly_username == null){
            elderly_username = navigator.getUsernameFromPreferences();
            //navigator.notis("elderly_username null :" + elderly_username);
        }
        else{
            navigator.saveInputToPreferencesElderlySchedular(elderly_username);
        }


        navigator.showMealList(listView, R.layout.activity_list_item_elderlyscheduler, true, elderly_username, elderly_name, date);


        Button bt_next = findViewById(R.id.Button_next);
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = getADay(date, 1);
                chosenDate.setText(date);
                navigator.showMealList(listView, R.layout.activity_list_item_elderlyscheduler, true, elderly_username, elderly_name, date);
            }
        });

        Button bt_prev = findViewById(R.id.Button_previous);
        bt_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = getADay(date, -1);
                chosenDate.setText(date);
                navigator.showMealList(listView, R.layout.activity_list_item_elderlyscheduler, true, elderly_username, elderly_name, date);
            }
        });

        sosButton = findViewById(R.id.sosButton);

        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCountdown();
            }
        });

        cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelCountdown();
            }
        });

    }

    public String getADay(String currentDate, int x) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            // Parse the input date string
            Date date = sdf.parse(currentDate);

            // Create a Calendar instance and set it to the parsed date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            // Increment the day by 1
            calendar.add(Calendar.DAY_OF_MONTH, x);

            // Format the updated date into "yyyy:MM:dd" format
            return sdf.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle parsing exception if needed
            return null;
        }
    }

    private void startCountdown() {
        sosButton.setEnabled(false);
        sosButton.setVisibility(View.INVISIBLE);
        cancelButton.setVisibility(View.VISIBLE);

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(COUNTDOWN_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                cancelButton.setText("Cancel on\n " +String.valueOf(secondsRemaining));
            }

            @Override
            public void onFinish() {
                sosButton.setEnabled(true);
                sosButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.INVISIBLE);
                sosButton.setText("SOS");
                ringSOS();
            }
        }.start();
    }

    private void cancelCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            sosButton.setEnabled(true);
            sosButton.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.INVISIBLE);
            sosButton.setText("SOS");
        }
    }

    private void ringSOS() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:112"));

        try {
            startActivity(callIntent);
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(this, "Call error", Toast.LENGTH_SHORT).show();
        }
    }

}