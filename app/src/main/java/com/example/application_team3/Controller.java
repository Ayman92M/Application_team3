package com.example.application_team3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Controller implements Serializable {

    private static CaregiverEntry caregiverUser;
    private static ElderlyEntry elderlyUser;

    private static final Database database = new Database();
    private static final ViewBuilder viewBuilder = new ViewBuilder();
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

    public Database getDatabase() {
        return database;
    }


    public ViewBuilder getViewBuilder() {
        return viewBuilder;
    }


    public void caregiverLogIn(String username, String password, Context context){
        UserAccountControl userControl = new UserAccountControl();
        if (!userControl.isValidUsername(username) || !userControl.isValidPassword(password))
            viewBuilder.notis("invalid user name or password", context);
        else{
            Task<Boolean> checkLogin = database.checkLoginCaregiver(username, password);
            Task<DataSnapshot> caregiverTask = database.fetchCaregiver(username);
            Tasks.whenAll(checkLogin, caregiverTask).addOnCompleteListener(task-> {
                if(checkLogin.getResult()){

                    CaregiverEntry caregiver = caregiverTask.getResult().getValue(CaregiverEntry.class);
                    if (caregiver != null) {
                        setCaregiverUser(caregiver);
                        goToActivity(context, CaregiverDash.class);
                    }

                }
                else{
                    viewBuilder.notis("False", context);
                }
            });
        }
    }

    public void logInElderly(String username, String password, Context context){
        UserAccountControl userControl = new UserAccountControl();
        if (!userControl.isValidUsername(username) || !userControl.isValidPin(password))
            viewBuilder.notis("invalid user name or Pin", context);

        Task<Boolean> loginCheck = database.checkLoginElderly(username, password);
        Task<DataSnapshot> elderlyTask = database.fetchElderly(username);
        Tasks.whenAll(loginCheck, elderlyTask).addOnCompleteListener(task -> {
            if(loginCheck.getResult()) {
                ElderlyEntry elderly = elderlyTask.getResult().getValue(ElderlyEntry.class);
                if(elderly != null){
                    setElderlyUser(elderly);
                    goToActivity(context, ElderlyScheduler.class);
                }
            }
            else
                viewBuilder.notis("False", context);

        });

    }

    public void logout(Activity activity) {
        // Clear saved credentials
        SharedPreferences preferences = activity.getPreferences(Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("rememberMe");
        editor.remove("username");
        editor.remove("password");
        editor.apply();

        goToActivity(activity, MainActivity.class);
    }

    public void sortMealByTime(List<MealEntry> mealList) {
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
