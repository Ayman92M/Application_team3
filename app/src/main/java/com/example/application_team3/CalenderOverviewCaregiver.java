package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalenderOverviewCaregiver extends AppCompatActivity {
    ListView listView;
    String date;

    Controller control;
    Database db;
    ViewBuilder vb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_overview_caregiver);

        Intent get_info = getIntent();
        control = (Controller) get_info.getSerializableExtra("controller");
        db = control.getDatabase();
        vb = control.getViewBuilder();

        Button back_btn = findViewById(R.id.button_back);
        back_btn.setOnClickListener(view -> finish());

        Button addMeal_btn = findViewById(R.id.button_AddMeal);
        addMeal_btn.setOnClickListener(view -> control.goToActivity(CalenderOverviewCaregiver.this, MealRegister.class));

        CalendarView mCalendarview = findViewById(R.id.CalendarView_calender_ID);
        mCalendarview.setOnDateChangeListener((calendarView, year, month, day) -> {
            date = String.format("%04d-%02d-%02d", year, month + 1, day);
            control.setActiveDate(date);
            listView = findViewById(R.id.listView_caregiver_scheduler);
            getTodayList();
        });
        getTodayList();
    }

    private void getTodayList(){
        if(date == null){
            Calendar day_calendar = Calendar.getInstance();
            int year = day_calendar.get(Calendar.YEAR);
            int month = day_calendar.get(Calendar.MONTH);
            int day = day_calendar.get(Calendar.DAY_OF_MONTH);
            date = String.format("%04d-%02d-%02d", year, month + 1, day);
            control.setActiveDate(date);
        }
        listView = findViewById(R.id.listView_caregiver_scheduler);
        mealListView();
    }

    private void mealListView(){
        List<String> mealStrings = new ArrayList<>();

        Task<DataSnapshot> mealPlanTask = db.fetchMealPlanDate(control.getElderlyUser().getUsername(), control.getActiveDate());

        Tasks.whenAll(mealPlanTask).addOnCompleteListener(task -> {
            DataSnapshot mealsData = mealPlanTask.getResult();
            List<MealEntry> mealList = new ArrayList<>();
            if(mealsData != null && mealsData.hasChildren()){
                for(DataSnapshot mealData : mealsData.getChildren()) {
                    MealEntry meal = mealData.getValue(MealEntry.class);
                    mealList.add(meal);
                }
            }

            control.sortMealByTime(mealList);
            for(MealEntry meal : mealList)
            {
                String meal_type = getMeal(meal.getMealType());
                mealStrings.add(meal_type + ", " + meal.getTime() + ", " + meal.isHasEaten() + ", " + meal.getDate());
            }
            vb.buildListView(true,
                    mealStrings, this, listView,
                    R.layout.activity_list_item_caregiverscheduler, R.id.meal, R.id.time
            );
            listView.setOnItemClickListener((parent, view, position, id) -> mealInfo_caregiver(view, mealList.get(position)));
        });
    }

    public String getMeal(String meal){
        String meal_type = meal;
        if(meal_type.equals("Breakfast")) {
            return meal_type = getString(R.string.Breakfast);
        }
        else if(meal_type.equals("Lunch")) {
            return meal_type = getString(R.string.Lunch);
        }
        else if(meal_type.equals("Dinner")) {
            return meal_type = getString(R.string.Dinner);
        }
        else if(meal_type.equals("Snack")) {
            return meal_type = getString(R.string.Snack);
        }
        return meal_type;
    }



    private void mealInfo_caregiver(View view, MealEntry meal) {
        Context context = view.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_meal_info_caregiver, null);
        final PopupWindow popupWindow = control.getViewBuilder().buildPopup(popupView);

        TextView meal_type = popupView.findViewById(R.id.meal_info);
        meal_type.setText("     " + meal.getMealType());
        TextView note = popupView.findViewById(R.id.textView7);
        note.setText(" " + meal.getNote());

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        Button deleteMeal_btn = popupView.findViewById(R.id.deleteMeal);

        deleteMeal_btn.setEnabled(!control.isTimeUp(meal.getDate(), meal.getTime(), -720));

        deleteMeal_btn.setOnClickListener(view1 -> {
            db.deleteMeal(control.getElderlyUser().getUsername(), meal.getDate(), meal.getMealType());
            recreate();
        });
    }
}