package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MealRegister extends AppCompatActivity {

    String[] mealtype = {"Breakfast", "Lunch", "Dinner", "Snack"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_register);

        autoCompleteTextView = findViewById(R.id.auto_complete_text);
        adapterItems = new ArrayAdapter<String>(this, R.layout.activity_list_item_meal_register, mealtype);
        autoCompleteTextView.setAdapter(adapterItems);


        TimePicker picker=(TimePicker)findViewById(R.id.timepicker1);
        picker.setIs24HourView(true);


        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(MealRegister.this,"Meal type: "+item, Toast.LENGTH_SHORT).show();
                //db.registerMeal()
            }
        });

    }
}