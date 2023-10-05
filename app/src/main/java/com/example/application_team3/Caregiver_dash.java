package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class Caregiver_dash extends AppCompatActivity {
    private ListView listView;
    ViewNavigator navigator = new ViewNavigator(this);
    String _caregiverName, _caregiverUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiverdash);

        //getIntent() används för att hämta data som har skickats till den här aktiviteten via intent.
        Intent get_info = getIntent();
        _caregiverName = get_info.getStringExtra("caregiverName");
        _caregiverUserName = get_info.getStringExtra("caregiverUserName");
        System.out.println("caregiverName Dashboard: " + _caregiverName);
        System.out.println("caregiverUserName Dashboard: " + _caregiverUserName);

        ((TextView) findViewById(R.id.textView_Welcome)).setText("Welcome " + _caregiverName);

        listView = findViewById(R.id.listView);
        navigator.showList(listView, _caregiverUserName, _caregiverName);

        TextView newElderly = findViewById(R.id.TextView_newElderly);
        navigator.goToNextActivity(newElderly, Signup_elderly.class);

        /*navigator.goToNextActivity(newElderly, Signup_elderly.class,
                "caregiverUserName",_caregiverUserName ,
                "caregiverName", _caregiverName);*/

        //
        TextView addElderly = findViewById(R.id.TextView_addElderly);
        addElderly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigator.notis("need a layout or a list");
            }
        });

        //navigator.addElderlyToCaregiver(addElderly, _caregiverUserName, _caregiverName);

    }



}