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
        addMeal_btn.setOnClickListener((View.OnClickListener) view -> control.goToActivity(CalenderOverviewCaregiver.this, Meal_register.class));

        CalendarView mCalendarview = (CalendarView) findViewById(R.id.CalendarView_calender_ID);
        mCalendarview.setOnDateChangeListener((calendarView, year, month, day) -> {
            date = year +"-" + (month + 1) + "-" + day;
            control.setActiveDate(date);
            listView = findViewById(R.id.listView_caregiver_scheduler);
            getTodayList();
            //navigator.showMealList(listView, R.layout.activity_list_item_caregiverscheduler, false,elderly_username, elderly_name, date);

        });
    }

    private void getTodayList(){
        Calendar day_calendar = Calendar.getInstance();
        int year = day_calendar.get(Calendar.YEAR);
        int month = day_calendar.get(Calendar.MONTH);
        int day = day_calendar.get(Calendar.DAY_OF_MONTH);

        if(date == null){
            listView = findViewById(R.id.listView_caregiver_scheduler);
            date = year + "-" + (month + 1) + "-" + day;
            System.out.println(date);
            Task<List<String>> mealStringsTask = control.showMealList(CalenderOverviewCaregiver.this, listView, R.layout.activity_list_item_caregiverscheduler, false);

            Tasks.whenAll(mealStringsTask).addOnCompleteListener(task -> {
                List<String> mealStrings = mealStringsTask.getResult();
                elderlyMealListActionListener(mealStrings, listView);
            });
            //db.listenForMealPlan(elderly_username);
        }
    }

    public void elderlyMealListActionListener(List<String> mealStrings, ListView listView){
        String[] mealArray = mealStrings.toArray(new String[0]);

        // Sätter en klickhändelse för ListView
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Hämtar det valda elementet från listan baserat på positionen
            String selectedItem = mealArray[position];
            // Delar upp det valda elementet i delar med hjälp av ", " som separator.
            String[] nameParts = selectedItem.split(", ");


                mealInfo_caregiver(view, nameParts);
            /*
            goToNextActivity(Meal_info.class, "----"
                          + nameParts[0]+ " -- " + nameParts[1] +" -- " + nameParts[2] +" -- " + nameParts[3] ,
                "elderlyName",elderlyName, "elderlyUserName", elderlyUserName,
              "mealType", nameParts[0], "mealTime", nameParts[1], "mealNote", nameParts[2], "hasEaten", nameParts[3]);


             */

        });

    }
    private void mealInfo_caregiver(View view, String[] nameParts) {
        Context context = view.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_meal_info_caregiver, null);

        final PopupWindow popupWindow = control.getView().buildPopup(popupView);

        // Set a click listener for the close button in the popup
        Button closePopupBtn = popupView.findViewById(R.id.closePopupBtn);
        closePopupBtn.setOnClickListener(v -> {
            // Dismiss the popup
            popupWindow.dismiss();
        });

        TextView meal_type = popupView.findViewById(R.id.meal_info);
        meal_type.setText("     " + nameParts[0]);

        TextView note = popupView.findViewById(R.id.textView7);
        note.setText(" " + nameParts[2]);


        // Show the popup at the center of the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


        Button deleteMeal_btn = popupView.findViewById(R.id.deleteMeal);
        String current_time = control.getCurrentTime();
        long current_time_ToMillis = control.convertStringToMillis(current_time);

        String missTime = control.addMinutesToTime(nameParts[1], 135);
        long dateToMillisMiss = control.convertStringToMillis(nameParts[4] + " " + missTime);

        if (dateToMillisMiss <= current_time_ToMillis) {
            deleteMeal_btn.setEnabled(false);
        }

        deleteMeal_btn.setOnClickListener(view1 -> {
            control.getDb().deleteMeal(control.getElderlyUser().getPid(), nameParts[1], nameParts[0]);
            this.recreate();
        });
    }




}