package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ViewNavigator navigator = new ViewNavigator(this);
    private Notification noti = new Notification();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_language);

        Button _elderly = findViewById(R.id.button);
        navigator.goToNextActivity(_elderly, Login_elderly.class);

        Button _caregiver = findViewById(R.id.button2);
        navigator.goToNextActivity(_caregiver, Log_in.class);


        noti.createNotificationChannel(MainActivity.this);
        Button _test = findViewById(R.id.testButton);
        //Delete Elderly_home_test.class and write the name of your class
        //className.class
        //navigator.goToNextActivity(_caregiver, className.class);
        // --navigator.goToNextActivity(_test, CargiverElderlyPageActivity.class);
        //you can Merge into Master only if it works.
        _test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noti.setAlarm(MainActivity.this, "05:01");
                convertStringToMillis("2023-10-6 05:01");
            }
        });

    }

    public long convertStringToMillis(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-d HH:mm");
        try {
            Date date = dateFormat.parse(dateString);
            System.out.println("conert: "+date.getTime());
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }
}