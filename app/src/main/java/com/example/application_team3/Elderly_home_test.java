package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Elderly_home_test extends AppCompatActivity {
    ViewNavigator navigator = new ViewNavigator(this);
    Intent get_info;
    String caregiverName, caregiverUserName, elderlyName, elderlyUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elderly_home_test);

        get_info = getIntent();
        String _caregiver_username = get_info.getStringExtra("elderlyName");

        ( (TextView) findViewById(R.id.textView2)).setText("Welcome " + _caregiver_username);
        skrivUt();
    }

    public void skrivUt(){
        Intent get_info = getIntent();
        String _caregiverName = get_info.getStringExtra("caregiverName");
        String _caregiverUserName = get_info.getStringExtra("caregiverUserName");
        String _elderlyName = get_info.getStringExtra("elderlyName");
        String _elderlyUserName = get_info.getStringExtra("elderlyUserName");

        System.out.println("----caregiverName : " + _caregiverName);
        System.out.println("----caregiver_username: "+ _caregiverUserName);
        System.out.println("----elderly_name: "+ _elderlyName);
        System.out.println("----elderly_username: "+ _elderlyUserName);

    }
}