package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Elderly_Scheduler extends AppCompatActivity {

    TextView chosenDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elderly_scheduler);

        chosenDate = findViewById(R.id.textView_date);
        Intent incomingIntent = getIntent();
        String date = incomingIntent.getStringExtra("date");
        Log.d("incoming intent", "chosen date" + date);
        chosenDate.setText(date);


    }
}