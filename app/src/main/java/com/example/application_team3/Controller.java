package com.example.application_team3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Controller implements Serializable {
    private static CaregiverEntry caregiverUser;
    private static ElderlyEntry elderlyUser;
    private static final Database database = new Database();
    private static final ViewBuilder viewBuilder = new ViewBuilder();
    private static final Notification notification = new Notification();
    private String activeDate;

    public Controller() {
        viewBuilder.setController(this);
        notification.setController(this);
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

    public Notification getNotification(){ return notification; }

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
                    updateNotificationElderly(elderly.getUsername(), context);
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
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Date date = dateFormat.parse(dateString);
            if (date != null) {
                return date.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the ParseException if needed
        }

        // Return a default value or handle the case where the conversion fails
        return -1;
    }

    public String getCurrentTime(){
        Calendar calendar = Calendar.getInstance();
        // Format the date and time into "yyyy-MM-dd HH:mm" format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    public boolean isTimeUp(String date, String time, int minutesToAdd){
        String current_time = getCurrentTime();
        long current_time_ToMillis = convertStringToMillis(current_time);

        String date_timeMeal = date + " " + time;
        String date_timeUp = addMinutesToDateString(date_timeMeal, minutesToAdd);
        long date_timeUpToMillis = convertStringToMillis(date_timeUp);

        return date_timeUpToMillis <= current_time_ToMillis;
    }

    public String getToday() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    public void getADay(String currentDate, int x) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            // Parse the input date string
            Date date = sdf.parse(currentDate);

            // Create a Calendar instance and set it to the parsed date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            // Increment the day by 1
            calendar.add(Calendar.DAY_OF_MONTH, x);
            setActiveDate(sdf.format(calendar.getTime()));
            // Format the updated date into "yyyy:MM:dd" format
            //return sdf.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle parsing exception if needed
            //return null;
        }
    }

    public void updateNotificationElderly(String elderly_username, Context context){

        Task<DataSnapshot> mealPlanTask = database.fetchMealPlanDate(elderly_username, getToday());
        Tasks.whenAll(mealPlanTask).addOnCompleteListener(task -> {
            DataSnapshot mealsData = mealPlanTask.getResult();

            if(mealsData != null && mealsData.hasChildren()){

                for(DataSnapshot mealData : mealsData.getChildren()) {

                    MealEntry meal = mealData.getValue(MealEntry.class);
                    long current_time_ToMillis = convertStringToMillis(getCurrentTime());
                    long dateToMillis = convertStringToMillis(meal.getDate()+ " " + meal.getTime());

                    if (meal.getElderlyNotificationID() == null && dateToMillis >= current_time_ToMillis){
                        notification.createNotificationChannel(context, meal.getMealType());
                        System.out.println("updateNotification ---> setAlarm for: " + meal.getMealType() + " -> at: " + meal.getDate()+ " " + meal.getTime());
                        notification.setAlarm(context, meal.getMealType(), elderly_username, null, meal.getDate(), dateToMillis);
                        meal.setElderlyNotificationID(meal.getDate()+meal.getTime());
                        database.updateMeal(elderly_username, meal);
                    }
                }
            }
        });
    }

    public void copyMealElderly(String elderly_username){

        Task<DataSnapshot> mealPlanTask = database.fetchMealPlanDate(elderly_username, getToday());
        Tasks.whenAll(mealPlanTask).addOnCompleteListener(task -> {
            DataSnapshot mealsData = mealPlanTask.getResult();

            if(mealsData != null && mealsData.hasChildren()){

                for(DataSnapshot mealData : mealsData.getChildren()) {

                    MealEntry meal = mealData.getValue(MealEntry.class);
                    String mealNextDate = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        mealNextDate = getNextWeekDate(meal.getDate());
                    }
                    if (mealNextDate != null)
                        if(mealData.child(mealNextDate).exists()){
                            if(!mealData.child(mealNextDate).child(meal.getMealType()).exists()){
                                database.registerMeal(elderly_username, mealNextDate, meal.getMealType(),meal.getTime(), meal.getNote());
                            }
                        }else {
                            database.registerMeal(elderly_username, mealNextDate, meal.getMealType(),meal.getTime(), meal.getNote());
                        }


                }
            }

        });

    }

    public void updateNotificationCaregiver(String elderly_username, String elderly_name, TextView textView, Context context){

        String current_time = getCurrentTime();
        long current_time_ToMillis = convertStringToMillis(current_time);

        Task<List<MealEntry>> mealListTask = database.MealPlanList(elderly_username);
        Tasks.whenAll(mealListTask).addOnCompleteListener(task ->
        { List<MealEntry> mealList = mealListTask.getResult();

            if(mealList != null ){
                for (MealEntry meal : mealList){
                    long dateToMillisMiss = getTimeUp(meal.getDate()+ " "+ meal.getTime(), 135);

                    if(!meal.isHasEaten() && dateToMillisMiss <= current_time_ToMillis && meal.getCaregiverNotificationID() == null){
                        if (textView != null )  textView.setError("miss");
                        notification.createNotificationChannel(context, meal.getMealType());
                        notification.setAlarm(context, meal.getMealType(), elderly_username, elderly_name, meal.getDate(), current_time_ToMillis);
                        System.out.println("SET Notification for Caregiver: " + meal.getMealType() + " -> at: " + meal.getDate()+ " " + meal.getTime());
                        meal.setCaregiverNotificationID(meal.getDate()+meal.getTime());
                        database.updateMeal(elderly_username, meal);

                    }
                }
            }


        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getNextWeekDate(String inputDate) {
        // Skapa en DateTimeFormatter för att konvertera strängen till LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(inputDate, formatter);

        LocalDate nextWeekDate = date.plusWeeks(1);

        return nextWeekDate.format(formatter);
    }
    private long getTimeUp(String date, int minutesToAdd){
        String timeUp = addMinutesToDateString(date, minutesToAdd);
        return convertStringToMillis(timeUp);
    }

    public String addMinutesToDateString(String inputDate, int minutesToAdd) {
        // Define the date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            // Parse the input date string
            Date date = dateFormat.parse(inputDate);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, minutesToAdd);
            //System.out.println(inputDate +" + " + minutesToAdd + " --> " + dateFormat.format(calendar.getTime()) );
            return dateFormat.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace(); // Handle the exception according to your needs
            return null;
        }
    }

}
