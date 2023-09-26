package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Elderly_home_test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elderly_home_test);

        Intent get_user_name = getIntent();
        String message = get_user_name.getStringExtra("key");

        ( (TextView) findViewById(R.id.textView2)).setText("Welcome " + message);
    }
}