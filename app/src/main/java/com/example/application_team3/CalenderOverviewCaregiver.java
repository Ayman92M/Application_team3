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
import com.google.android.material.bottomappbar.BottomAppBar;

import java.util.Calendar;
import java.util.List;

public class CalenderOverviewCaregiver extends AppCompatActivity {
    ListView listView;
    String date;

    Controller control;
    BottomAppBar bottomAppBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_overview_caregiver);

        Intent get_info = getIntent();
        control = (Controller) get_info.getSerializableExtra("controller");

        bottomAppBar = findViewById(R.id.bottomAppBar);
        bottomAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId()==R.id.bottomNav_back){
                control.goToActivity(CalenderOverviewCaregiver.this, CaregiverElderlyPageActivity.class);
            }
            return false;
        });


        Button addMeal_btn = findViewById(R.id.button_AddMeal);
        addMeal_btn.setOnClickListener(view -> control.goToActivity(CalenderOverviewCaregiver.this, Meal_register.class));

        CalendarView mCalendarview = findViewById(R.id.CalendarView_calender_ID);
        mCalendarview.setOnDateChangeListener((calendarView, year, month, day) -> {
            date = year +"-" + (month + 1) + "-" + day;
            control.setActiveDate(date);
            listView = findViewById(R.id.listView_caregiver_scheduler);
            getTodayList();
            //navigator.showMealList(listView, R.layout.activity_list_item_caregiverscheduler, false,elderly_username, elderly_name, date);

        });
        getTodayList();
    }

    private void getTodayList(){
        if(date == null){
            Calendar day_calendar = Calendar.getInstance();
            int year = day_calendar.get(Calendar.YEAR);
            int month = day_calendar.get(Calendar.MONTH);
            int day = day_calendar.get(Calendar.DAY_OF_MONTH);
            date = year + "-" + (month+1) + "-" + day;
            control.setActiveDate(date);
        }
            listView = findViewById(R.id.listView_caregiver_scheduler);
            Task<List<MealEntry>> mealListTask = control.showMealList(CalenderOverviewCaregiver.this, listView, R.layout.activity_list_item_caregiverscheduler);

            Tasks.whenAll(mealListTask).addOnCompleteListener(task -> {
                List<MealEntry> mealList = mealListTask.getResult();
                elderlyMealListActionListener(mealList, listView);
            });
    }

    public void elderlyMealListActionListener(List<MealEntry> mealList, ListView listView){

        listView.setOnItemClickListener((parent, view, position, id) -> {
            System.out.println(mealList.get(position).getTime());
            mealInfo_caregiver(view, mealList.get(position));

        });

    }
    private void mealInfo_caregiver(View view, MealEntry meal) {
        System.out.println(meal.getDate());
        Context context = view.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_meal_info_caregiver, null);
        System.out.println("0");
        final PopupWindow popupWindow = control.getView().buildPopup(popupView);

        System.out.println("1");
        TextView meal_type = popupView.findViewById(R.id.meal_info);
        meal_type.setText("     " + meal.getMealType());
        System.out.println("2");
        TextView note = popupView.findViewById(R.id.textView7);
        note.setText(" " + meal.getNote());

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        System.out.println("3");
        Button deleteMeal_btn = popupView.findViewById(R.id.deleteMeal);
        String current_time = control.getCurrentTime();
        long current_time_ToMillis = control.convertStringToMillis(current_time);
        final int addTime = 135000;
        long missTime = current_time_ToMillis + addTime;

        if (missTime <= current_time_ToMillis) {
            deleteMeal_btn.setEnabled(false);
        }

        deleteMeal_btn.setOnClickListener(view1 -> {
            control.getDb().deleteMeal(control.getElderlyUser().getPid(), meal.getDate(), meal.getMealType());
            control.goToActivity(CalenderOverviewCaregiver.this, CalenderOverviewCaregiver.class);
        });
    }
}