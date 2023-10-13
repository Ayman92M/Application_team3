package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.textfield.TextInputEditText;

public class Meal_register extends AppCompatActivity {
    String[] mealtype = {"Breakfast", "Lunch", "Dinner", "Snack"};
    AutoCompleteTextView autoCompleteTextView;
    ViewNavigator navigator = new ViewNavigator(this);
    Database db = new Database();
    ArrayAdapter<String> adapterItems;
    Intent get_info;
    String item, note;
    BottomAppBar bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_register);

        bottomAppBar = findViewById(R.id.bottomAppBar);

        get_info = getIntent();
        String elderly_name = get_info.getStringExtra("elderlyName");
        String elderly_username = get_info.getStringExtra("elderlyUserName");
        String caregiver_username = get_info.getStringExtra("caregiverUserName");
        String caregiver_name = get_info.getStringExtra("caregiverName");

        bottomAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId()==R.id.bottomNav_back){
                Intent intent = new Intent(Meal_register.this, CalenderOverviewCaregiver.class);
                intent.putExtra("elderlyName", elderly_name);
                intent.putExtra("elderlyUserName", elderly_username);
                intent.putExtra("caregiverName", caregiver_name);
                intent.putExtra("caregiverUserName", caregiver_username);
                startActivity(intent);
            }
            return false;
        });

        autoCompleteTextView = findViewById(R.id.auto_complete_text);
        adapterItems = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, mealtype);
        autoCompleteTextView.setAdapter(adapterItems);

        TimePicker picker = findViewById(R.id.timepicker1);
        picker.setIs24HourView(true);

        autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {
            item = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(Meal_register.this, "Meal type: " + item, Toast.LENGTH_SHORT).show();

        });




        Button create_meal = findViewById(R.id.Button_createMeal);
        create_meal.setOnClickListener(view -> {
            Intent incomingIntent = getIntent();
            String date = incomingIntent.getStringExtra("date");
            Intent get_info = getIntent();
            String elderly_username1 = get_info.getStringExtra("elderlyUserName");
            String elderly_name1 = get_info.getStringExtra("elderlyName");
            TextInputEditText _note = findViewById(R.id.TextInputEditText_EnterMeal);
            note = _note.getText().toString();

            int hour = picker.getHour();    int minute = picker.getMinute();
            String hourString = hour + ":" +minute ;

            //Toast.makeText(Meal_register.this, elderly_username , Toast.LENGTH_SHORT).show();
            db.registerMeal(elderly_username1, date, item, hourString, note);
            navigator.notis("Done");

        });

    }
}