package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    ViewNavigator navigator = new ViewNavigator(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_language);

        Button _elderly = findViewById(R.id.button);
        navigator.goToNextActivity(_elderly, Login_elderly.class);

        Button _caregiver = findViewById(R.id.button2);
        navigator.goToNextActivity(_caregiver, Log_in.class);

    }
}