package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CargiverElderlyPageActivity extends AppCompatActivity {
    Intent get_info;
    ViewNavigator navigator = new ViewNavigator(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargiver_elderly_page);

        get_info = getIntent();
        String elderly_name = get_info.getStringExtra("elderlyName");
        String elderly_username = get_info.getStringExtra("elderlyUserName");
        String caregiver_username = get_info.getStringExtra("caregiverUserName");

        ( (TextView) findViewById(R.id.elderly_name)).setText("          Elderly " + elderly_name);


        Button removeElderly = findViewById(R.id.deleteElderly);
        navigator.remove(removeElderly, caregiver_username, elderly_username);

        Button meal_reg = findViewById(R.id.mealPlanner);
        meal_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CargiverElderlyPageActivity.this, Calendar_Overview.class);
                get_info = getIntent();
                String elderly_username = get_info.getStringExtra("elderlyUserName");
                intent.putExtra("elderlyUserName", elderly_username);
                navigator.notis(elderly_username);
                startActivity(intent);
            }
        });

    }
}