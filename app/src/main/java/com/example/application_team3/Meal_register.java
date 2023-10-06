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

import com.google.android.material.textfield.TextInputEditText;

public class Meal_register extends AppCompatActivity {
    String[] mealtype = {"Breakfast", "Lunch", "Dinner", "Snack"};
    AutoCompleteTextView autoCompleteTextView;
    Database db = new Database();
    ArrayAdapter<String> adapterItems;
    Intent get_info;
    String item, note;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_register);

        autoCompleteTextView = findViewById(R.id.auto_complete_text);
        adapterItems = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, mealtype);
        autoCompleteTextView.setAdapter(adapterItems);

        TimePicker picker = findViewById(R.id.timepicker1);
        picker.setIs24HourView(true);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(Meal_register.this, "Meal type: " + item, Toast.LENGTH_SHORT).show();

            }
        });




        Button create_meal = findViewById(R.id.Button_createMeal);
        create_meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent incomingIntent = getIntent();
                String date = incomingIntent.getStringExtra("date");
                Intent get_info = getIntent();
                String elderly_username = get_info.getStringExtra("elderlyUserName");
                String elderly_name = get_info.getStringExtra("elderlyName");
                TextInputEditText _note = findViewById(R.id.TextInputEditText_EnterMeal);
                note = _note.getText().toString();

                get_info = getIntent();
                //String elderly_username = get_info.getStringExtra("elderlyUserName");
                int hour = picker.getHour();    int minute = picker.getMinute();
                String hourString = hour + ":" +minute ;

                //Toast.makeText(Meal_register.this, elderly_username , Toast.LENGTH_SHORT).show();
                db.registerMeal(elderly_username, date, item, hourString, note);

            }
        });

    }
}