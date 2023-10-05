package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

public class Elderly_Scheduler extends AppCompatActivity {
    ListView listView;
    ViewNavigator navigator = new ViewNavigator(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elderly_scheduler);

        Intent get_info = getIntent();
        String elderly_username = get_info.getStringExtra("elderlyUserName");
        String elderly_name = get_info.getStringExtra("elderlyName");
        listView = findViewById(R.id.listView_elderly_scheduler);
        navigator.showList2(listView, elderly_username, elderly_name, "2023-10-5");

    }


}