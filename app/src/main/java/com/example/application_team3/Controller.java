package com.example.application_team3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.ListView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Controller implements Serializable {

    private static CaregiverEntry caregiverUser;
    private static ElderlyEntry elderlyUser;

    private static final Database database = new Database();
    private static final ViewBuilder view = new ViewBuilder();
    private String activeDate;

    public Controller() {
    }

    public void goToActivity(Context context, Class<?> targetActivity){
        Intent intent = new Intent(context, targetActivity);
        intent.putExtra("controller", this);
        context.startActivity(intent);
    }

    public void setCaregiverUser(CaregiverEntry caregiver){
        caregiverUser = caregiver;
    }
    public CaregiverEntry getCaregiverUser(){
        return caregiverUser;
    }


    public void setElderlyUser(ElderlyEntry elderly){
        elderlyUser = elderly;
    }
    public ElderlyEntry getElderlyUser(){
        return elderlyUser;
    }

    public String getActiveDate() {
        return activeDate;
    }

    public void setActiveDate(String activeDate) {
        this.activeDate = activeDate;
    }

    public Database getDb() {
        return database;
    }


    public ViewBuilder getView() {
        return view;
    }


    public void caregiverLogIn(String _user_name, String _pass, Context context){
        UserAccountControl userControl = new UserAccountControl();
        if (!userControl.isValidUsername(_user_name) || !userControl.isValidPassword(_pass))
            view.notis("invalid user name or password", context);
        else{
            Task<Boolean> checkLogin = database.checkLoginCaregiver(_user_name, _pass);
            Task<DataSnapshot> caregiverTask = database.fetchCaregiver(_user_name);
            Tasks.whenAll(checkLogin, caregiverTask).addOnCompleteListener(task-> {
                if(checkLogin.getResult()){

                    CaregiverEntry caregiver = caregiverTask.getResult().getValue(CaregiverEntry.class);
                    if (caregiver != null) {
                        setCaregiverUser(caregiver);
                        goToActivity(context, Caregiver_Dash.class);
                    }

                }
                else{
                    view.notis("False", context);
                }
            });
        }
    }

    public void logout(Activity activity, Class<?> targetActivity) {
        // Clear saved credentials
        SharedPreferences preferences = activity.getPreferences(Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("rememberMe");
        editor.remove("username");
        editor.remove("password");
        editor.apply();

        goToActivity(activity, targetActivity);
    }

    public Task<List<MealEntry>> showMealList(Context context, ListView listView, int layoutResourceId){
        TaskCompletionSource<List<MealEntry>> mealStringsTaskSource = new TaskCompletionSource<>();
        Task<List<MealEntry>> mealStringsTask = mealStringsTaskSource.getTask();
        Task<DataSnapshot> mealPlanTask = database.fetchMealPlanDate(elderlyUser.getPid(), activeDate);

        Tasks.whenAll(mealPlanTask).addOnCompleteListener(task -> {
            DataSnapshot mealsData = mealPlanTask.getResult();
            List<MealEntry> mealList = new ArrayList<>();
            if(mealsData != null && mealsData.hasChildren()){
                for(DataSnapshot mealData : mealsData.getChildren()) {
                    MealEntry meal = mealData.getValue(MealEntry.class);
                    mealList.add(meal);
                }
            }

            sortMealByTime(mealList);

            view.setupMealListView(context, mealList, listView, layoutResourceId);
            mealStringsTaskSource.setResult(mealList);
        });
        return mealStringsTask;
    }

    private void sortMealByTime(List<MealEntry> mealList) {
        mealList.sort((meal1, meal2) -> {
            // Antag att tiden är i formatet "HH:mm"
            String[] parts1 = meal1.getTime().split(":");
            String[] parts2 = meal2.getTime().split(":");

            int hour1 = Integer.parseInt(parts1[0]);
            int minute1 = Integer.parseInt(parts1[1]);

            int hour2 = Integer.parseInt(parts2[0]);
            int minute2 = Integer.parseInt(parts2[1]);

            if (hour1 == hour2) {
                return Integer.compare(minute1, minute2);
            } else {
                return Integer.compare(hour1, hour2);
            }
        });
    }

    public long convertStringToMillis(String dateString) {
        List<String> possibleFormats = new ArrayList<>();
        possibleFormats.add("yyyy-MM-dd HH:mm");
        possibleFormats.add("yyyy-MM-d HH:mm");
        possibleFormats.add("yyyy-M-dd HH:mm");
        possibleFormats.add("yyyy-MM-d H:mm");

        for (String format : possibleFormats) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(format);
                Date date = dateFormat.parse(dateString);
                return date.getTime();
            } catch (ParseException ignored) {
                // Ignorera ParseException för detta format, fortsätt med nästa
            }
        }

        // Ingen matchande format hittades
        return -1;
    }

    public String getCurrentTime(){
        Calendar day_calendar = Calendar.getInstance();
        int year = day_calendar.get(Calendar.YEAR);
        int month = day_calendar.get(Calendar.MONTH);
        int day = day_calendar.get(Calendar.DAY_OF_MONTH);
        int hour = day_calendar.get(Calendar.HOUR_OF_DAY);
        int min = day_calendar.get(Calendar.MINUTE);

        return year + "-" + (month + 1) + "-" + day + " " + hour + ":" + min;
    }

}
