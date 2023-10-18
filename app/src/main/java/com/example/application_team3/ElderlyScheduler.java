package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import android.widget.ListView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ElderlyScheduler extends AppCompatActivity {
    ListView listView;

    Controller control;

    TextView chosenDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elderly_scheduler);

        chosenDate = findViewById(R.id.day_and_date);
        Intent get_info = getIntent();
        control = (Controller) get_info.getSerializableExtra("controller");


        Calendar day_calendar = Calendar.getInstance();
        int year = day_calendar.get(Calendar.YEAR);
        int month = day_calendar.get(Calendar.MONTH);
        int day = day_calendar.get(Calendar.DAY_OF_MONTH);

        if(control.getActiveDate() == null){
            control.setActiveDate(year + "-" + (month + 1) + "-" + day);
        }
        chosenDate.setText(control.getActiveDate());
        listView = findViewById(R.id.listView_elderly_scheduler);

        mealListView();

    }

    private void mealListView(){
        List<String> mealStrings = new ArrayList<>();

        Task<DataSnapshot> mealPlanTask = control.getDatabase().fetchMealPlanDate(control.getElderlyUser().getPid(), control.getActiveDate());

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
                mealStrings.add(meal.getMealType() + ", " + meal.getTime());
            }
            control.getViewBuilder().buildListView(
                    mealStrings, this, listView,
                    R.layout.activity_list_item_caregiverscheduler, R.id.time, R.id.meal
            );
            listView.setOnItemClickListener((parent, view, position, id) -> mealInfo_elderly(view, mealList.get(position)));
        });
    }

    private void mealInfo_elderly(View view, MealEntry meal){
        Context context = view.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_meal_info_elderly, null);

        final PopupWindow popupWindow = control.getViewBuilder().buildPopup(popupView);

        // Set a click listener for the close button in the popup


        TextView meal_type = popupView.findViewById(R.id.meal_info);
        meal_type.setText("     " + meal.getMealType());

        TextView note = popupView.findViewById(R.id.textView7);
        note.setText(" " + meal.getTime());

        // Show the popup at the center of the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


        Button bt_sant = popupView.findViewById(R.id.bt_sant);
        Button bt_falsk = popupView.findViewById(R.id.bt_falsk);
        bt_sant.setOnClickListener(view1 -> {
            ////////////////
            control.getDatabase().hasEatenMeal(control.getElderlyUser().getPid(), meal.getDate(), meal.getMealType());

        });

        bt_falsk.setOnClickListener(view1 -> {
            ///////////////////

        });

    }
}