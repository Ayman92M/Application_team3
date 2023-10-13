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

    private static Database database = new Database();
    private static ViewBuilder view = new ViewBuilder();
    private String activeDate;

    public Controller() {
        view.setControl(this);
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

    public void setDb(Database db) {
        database = db;
    }

    public ViewBuilder getView() {
        return view;
    }

    public void setView(ViewBuilder view_) {
        view = view_;
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
                        goToActivity(context, Caregiver_dash.class);
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

    public Task<List<String>> showMealList(Context context, ListView listView, int layoutResourceId, boolean elderlyView){
        List<String> mealStrings = new ArrayList<>();
        TaskCompletionSource<List<String>> mealStringsTaskSource = new TaskCompletionSource<>();
        Task<List<String>> mealStringsTask = mealStringsTaskSource.getTask();
        // Hämta en lista av ElderlyEntry-objekt som tillhör en caregiver (som har caregiverUserName som username)
        Task<DataSnapshot> mealPlanTask = database.fetchMealPlanDate(elderlyUser.getPid(), activeDate);
        // Hämta resultatet
        Tasks.whenAll(mealPlanTask).addOnCompleteListener(task -> {
            DataSnapshot mealsData = mealPlanTask.getResult();
            mealStrings.clear();
            if(mealsData != null && mealsData.hasChildren()){
                for(DataSnapshot mealData : mealsData.getChildren()) {
                    MealEntry meal = mealData.getValue(MealEntry.class);
                    String mealString = meal.getMealType() +", " + meal.getTime()+", "
                            + meal.getNote() +", "+ meal.isHasEaten() + ", "+  activeDate;
                    mealStrings.add(mealString);
                    //notification.setAlarm(context, meal.getMealType(), convertStringToMillis(date + " " + meal.getTime()));
                }
            }
            sortMealByTime(mealStrings);

            view.setupMealListView(context, mealStrings, listView, layoutResourceId);
            mealStringsTaskSource.setResult(mealStrings);
        });
        return mealStringsTask;
    }

    private void sortMealByTime(List<String> mealStrings) {
        Collections.sort(mealStrings, (meal1, meal2) -> {
            // Antag att tiden är i formatet "HH:mm"
            String[] parts1 = meal1.split(", ")[1].split(":");
            String[] parts2 = meal2.split(", ")[1].split(":");

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
                //System.out.println("convert: "+ date);
                //System.out.println("convert: "+ date.getTime());
                return date.getTime();
            } catch (ParseException ignored) {
                // Ignorera ParseException för detta format, fortsätt med nästa
            }
        }

        // Ingen matchande format hittades
        return -1;
    }

    public String addMinutesToTime(String timeStr, int minutesToAdd) {
        // Define an array of possible patterns
        String[] patterns = {"HH:mm", "H:mm", "HH,M", "H,M", "HH:m", "H:m"};

        // Attempt to parse the input time using each pattern
        LocalTime originalTime = null;
        for (String pattern : patterns) {
            try {
                DateTimeFormatter formatter = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    formatter = DateTimeFormatter.ofPattern(pattern);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    originalTime = LocalTime.parse(timeStr, formatter);
                }
                break; // Break if parsing is successful
            } catch (Exception e) {
                // Continue to the next pattern if parsing fails
            }
        }

        if (originalTime == null) {
            throw new IllegalArgumentException("Unsupported time format: " + timeStr);
        }

        // Add minutes to the original time
        LocalTime newTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            newTime = originalTime.plusMinutes(minutesToAdd);
        }

        // Format the result back to the original time format
        String resultTimeStr = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            resultTimeStr = newTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        }

        return resultTimeStr;
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
