package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Database db = new Database();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_language);

        Button _elderly = findViewById(R.id.button);
        _elderly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent page1 = new Intent(MainActivity.this, Login_elderly.class);
                startActivity(page1);
            }
        });

        Button _caregiver = findViewById(R.id.button2);
        _caregiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setContentView(R.layout.activity_log_in);
                Intent page1 = new Intent(MainActivity.this, Log_in.class);
                startActivity(page1);
            }
        });


    }
}