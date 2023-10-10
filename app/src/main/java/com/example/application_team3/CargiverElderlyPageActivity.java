package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomappbar.BottomAppBar;

public class CargiverElderlyPageActivity extends AppCompatActivity {
    Intent get_info;
    ViewNavigator navigator = new ViewNavigator(this);

    BottomAppBar bottomAppBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargiver_elderly_page);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId()==R.id.bottomNav_back){
                    Intent intent = new Intent(CargiverElderlyPageActivity.this, Caregiver_dash.class);
                    startActivity(intent);
                }
                return false;
            }
        });

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
                Intent intent = new Intent(CargiverElderlyPageActivity.this, CalenderOverviewCaregiver.class);
                get_info = getIntent();
                String elderly_username = get_info.getStringExtra("elderlyUserName");
                String elderly_name = get_info.getStringExtra("elderlyName");
                intent.putExtra("elderlyUserName", elderly_username);
                intent.putExtra("elderlyName", elderly_name);
                //navigator.notis(elderly_username);
                startActivity(intent);
            }
        });

    }
}