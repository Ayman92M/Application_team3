package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputEditText;

import java.lang.reflect.Array;

public class MealRegister extends AppCompatActivity {
    Spinner mealPicker;
    Controller control;
    Database db;
    ViewBuilder vb;
    ArrayAdapter<CharSequence> adapterItems;
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
            vb = control.getViewBuilder();
        }
        Button back_btn = findViewById(R.id.button_back);
        back_btn.setOnClickListener(view -> finish());

        mealPicker = findViewById(R.id.mealspinner);
        adapterItems = ArrayAdapter.createFromResource(this, R.array.meal_type_array, android.R.layout.simple_spinner_dropdown_item);
        adapterItems.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mealPicker.setAdapter(adapterItems);

        TimePicker picker = findViewById(R.id.timepicker1);
        picker.setIs24HourView(true);

        mealPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                item = mealPicker.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Button create_meal = findViewById(R.id.Button_createMeal);
        create_meal.setOnClickListener(view -> {
            String date = control.getActiveDate();
            String elderly_username1 = control.getElderlyUser().getUsername();
            TextInputEditText _note = findViewById(R.id.TextInputEditText_EnterMeal);
            note = _note.getText().toString();

            int hour = picker.getHour();    int minute = picker.getMinute();
            String hourString = String.format("%02d", hour);
            String minuteString = String.format("%02d", minute);
            String formattedTime = hourString + ":" + minuteString;

            //Toast.makeText(Meal_register.this, elderly_username , Toast.LENGTH_SHORT).show();
            String meal_type = null;
            if(item.equals("Frukost") ) {
                meal_type = "Breakfast";
            }
            else if(item.equals("Lunch")) {
                meal_type = "Lunch";
            }
            else if(item.equals("Middag")) {
                meal_type = "Dinner";
            }
            else if(item.equals("Mellanmål")) {
                meal_type = "Snack";
            }
            else meal_type = item;

            db.registerMeal(elderly_username1, date, meal_type, formattedTime, note);
            vb.notis("Registered meal", view.getContext());
        });

    }
}