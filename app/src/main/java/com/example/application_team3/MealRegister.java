package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class MealRegister extends AppCompatActivity {
    String[] mealtype = {"Breakfast", "Lunch", "Dinner", "Snack"};
    AutoCompleteTextView autoCompleteTextView;
    Controller control;
    Database db;
    ViewBuilder viewBuilder;
    ArrayAdapter<String> adapterItems;

    String item, note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_register);


        Intent get_info = getIntent();
        control = (Controller) get_info.getSerializableExtra("controller");
        if(control != null)
        {
            db = control.getDatabase();
            viewBuilder = control.getViewBuilder();
        }

        autoCompleteTextView = findViewById(R.id.auto_complete_text);
        adapterItems = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, mealtype);
        autoCompleteTextView.setAdapter(adapterItems);

        TimePicker picker = findViewById(R.id.timepicker1);
        picker.setIs24HourView(true);

        autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {
            item = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(MealRegister.this, "Meal type: " + item, Toast.LENGTH_SHORT).show();

        });




        Button create_meal = findViewById(R.id.Button_createMeal);
        create_meal.setOnClickListener(view -> {
            String date = control.getActiveDate();
            String elderly_username1 = control.getElderlyUser().getPid();
            TextInputEditText _note = findViewById(R.id.TextInputEditText_EnterMeal);
            note = _note.getText().toString();

            int hour = picker.getHour();    int minute = picker.getMinute();
            String hourString = String.format("%02d", hour);
            String minuteString = String.format("%02d", minute);
            String formattedTime = hourString + ":" + minuteString;

            //Toast.makeText(Meal_register.this, elderly_username , Toast.LENGTH_SHORT).show();
            db.registerMeal(elderly_username1, date, item, formattedTime, note);
            viewBuilder.notis("Registered meal", view.getContext());

        });

    }
}