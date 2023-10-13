package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Controller control = new Controller();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_language);

        Button _elderly = findViewById(R.id.button);
        _elderly.setOnClickListener(view -> control.goToActivity(MainActivity.this, Login_elderly.class));

        Button _caregiver = findViewById(R.id.button2);

        _caregiver.setOnClickListener(view -> control.goToActivity(MainActivity.this, Log_in.class));



        Button _test = findViewById(R.id.testButton);
        //Delete Elderly_home_test.class and write the name of your class
        //className.class
        //navigator.goToNextActivity(_caregiver, className.class);
        // --navigator.goToNextActivity(_test, CargiverElderlyPageActivity.class);
        //you can Merge into Master only if it works.
        _test.setOnClickListener(view -> {

        });

    }

}