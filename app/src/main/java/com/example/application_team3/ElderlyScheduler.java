package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import android.widget.ListView;
import android.widget.Toast;

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

    private static final int COUNTDOWN_TIME = 10000; // 10 sekunder
    private CountDownTimer countDownTimer;

    private Button sosButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elderly_scheduler);

        chosenDate = findViewById(R.id.day_and_date);
        Intent get_info = getIntent();
        control = (Controller) get_info.getSerializableExtra("controller");
        if(control == null){
            System.out.println("NuLL CONTROLLER");
        }

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

        Button bt_next = findViewById(R.id.Button_next);
        bt_next.setOnClickListener(view -> {
            control.getADay(control.getActiveDate(), 1);
            chosenDate.setText(control.getActiveDate());
            mealListView();
        });


        Button bt_prev = findViewById(R.id.Button_previous);
        bt_prev.setOnClickListener(view -> {
            control.getADay(control.getActiveDate(), -1);
            chosenDate.setText(control.getActiveDate());
            mealListView();
        });


        sosButton = findViewById(R.id.sosButton);


        sosButton.setOnClickListener(view -> startCountdown());


        cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(view -> cancelCountdown());


    }

    private void mealListView(){
        Notification notification = control.getNotification();
        notification.runFunctionElderly(this, control.getElderlyUser().getPid(), null);
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
                mealStrings.add(meal.getMealType() + ", " + meal.getTime() + ", " + meal.isHasEaten() + ", " + meal.getDate());
            }
            control.getViewBuilder().buildListView(true,
                    mealStrings, this, listView,
                    R.layout.activity_list_item_elderlyscheduler, R.id.meal, R.id.time
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
            control.getNotification().cancelAlarm(view1.getContext(), meal.getMealType(), meal.getDate());
            mealListView();
        });

        bt_falsk.setOnClickListener(view1 -> {
            ///////////////////

        });

    }

    private void startCountdown() {
        sosButton.setEnabled(false);
        sosButton.setVisibility(View.INVISIBLE);
        cancelButton.setVisibility(View.VISIBLE);

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(COUNTDOWN_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                cancelButton.setText("Cancel on\n " +String.valueOf(secondsRemaining));
            }

            @Override
            public void onFinish() {
                sosButton.setEnabled(true);
                sosButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.INVISIBLE);
                sosButton.setText("SOS");
                ringSOS();
            }
        }.start();
    }

    private void cancelCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            sosButton.setEnabled(true);
            sosButton.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.INVISIBLE);
            sosButton.setText("SOS");
        }
    }

    private void ringSOS() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:112"));

        try {
            startActivity(callIntent);
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(this, "Call error", Toast.LENGTH_SHORT).show();
        }
    }
}