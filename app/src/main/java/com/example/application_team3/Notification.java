package com.example.application_team3;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Notification {

    private static final String CHANNEL_ID = "channel1";
    private static final String RUN_FUNCTION_ACTION_CAREGIVER = "RUN_FUNCTION_ACTION_CAREGIVER";
    private static final String RUN_FUNCTION_ACTION_ELDERLY = "RUN_FUNCTION_ACTION_ELDERLY";
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    int id;

    @SuppressLint("ScheduleExactAlarm")
    public void setAlarm(Context context, String mealType, String pid, String elderly_name, String mealDate, long triggerTimeInMillis, long timeUpToMillis) {

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setAction("MEAL_ACTION");
        } else {
            intent.setAction(Intent.ACTION_BOOT_COMPLETED);
        }

        intent.putExtra("mealType", mealType);
        intent.putExtra("mealDate", mealDate);
        intent.putExtra("elderlyUserName", pid);
        intent.putExtra("elderlyName", elderly_name);

        id = getMealId(mealType,mealDate);
        pendingIntent = PendingIntent.getBroadcast(context, id,intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTimeInMillis, pendingIntent);

        //System.out.println("-------> setAlarm for: " + mealType + " -> ID: " + id);

        if(elderly_name == null)
            for (int i = 1; i < 4; i++) {
                int id = getReminderId(mealType, i);
                intent.putExtra("reminderTime", id);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_IMMUTABLE);

                String reminderTime = addMinutesToDateString(getCurrentTime(), 1*i);
                long reminderTimeToMillis = convertStringToMillis(reminderTime);

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTimeToMillis, pendingIntent);
                System.out.println("-------> SET Reminder "+ id + " for : " + mealType);
            }
    }
    public void runFunctionCaregiver(Context context, String elderlyUserName, String elderlyName) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setAction("RUN_FUNCTION_ACTION_CAREGIVER");
        } else {
            intent.setAction(Intent.ACTION_BOOT_COMPLETED);
        }

        if (elderlyUserName != null)    intent.putExtra("elderlyUserName", elderlyUserName);
        if (elderlyName != null)        intent.putExtra("elderlyName", elderlyName);


        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        long repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES/15;
        long triggerTime = System.currentTimeMillis() + repeatInterval;
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerTime, repeatInterval, pendingIntent);

        createNotificationChannel(context, "RUN_FUNCTION_ACTION_CAREGIVER");
        System.out.println("RUN_FUNCTION_ACTION_CAREGIVER");
    }
    public void runFunctionElderly(Context context, String elderlyUserName, String elderlyName) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setAction(RUN_FUNCTION_ACTION_ELDERLY);
        } else {
            intent.setAction(Intent.ACTION_BOOT_COMPLETED);
        }

        if (elderlyUserName != null)    intent.putExtra("elderlyUserName", elderlyUserName);
        if (elderlyName != null)        intent.putExtra("elderlyName", elderlyName);

        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        long repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES/15;
        long triggerTime = System.currentTimeMillis() + repeatInterval;
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerTime, repeatInterval, pendingIntent);

        createNotificationChannel(context, "RUN_FUNCTION_ACTION_ELDERLY");
        System.out.println("RUN_FUNCTION_ACTION_ELDERLY");
    }
    public void cancelAlarm(Context context, String mealType, String mealDate) {

        Intent intent = new Intent(context, AlarmReceiver.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setAction("MEAL_ACTION");
        } else {
            intent.setAction(Intent.ACTION_BOOT_COMPLETED);
        }

        id = getMealId(mealType, mealDate);
        pendingIntent = PendingIntent.getBroadcast(context, id, intent,PendingIntent.FLAG_IMMUTABLE);
        if(alarmManager == null) {
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        alarmManager.cancel(pendingIntent);

        Toast.makeText(context, "Alarm canceled for: " + "mealId: "+ id, Toast.LENGTH_SHORT).show();

        for (int i = 1; i < 4; i++) {
            id = getReminderId(mealType, i);
            pendingIntent = PendingIntent.getBroadcast(context, id, intent,PendingIntent.FLAG_IMMUTABLE);

            if(alarmManager == null) {
                alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            }
            alarmManager.cancel(pendingIntent);

            Toast.makeText(context, "Alarm canceled for: " + "mealId: "+ id, Toast.LENGTH_SHORT).show();
        }


    }
    public void createNotificationChannel(Context context, String mealType) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel =
                    new NotificationChannel(CHANNEL_ID, "1", importance);
            notificationChannel.setDescription(mealType + " description");
            notificationChannel.enableVibration(true);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public void runCopyMeal(Context context, String elderlyUserName){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setAction("RUN_FUNCTION_COPY_MEAL");
        } else {
            intent.setAction(Intent.ACTION_BOOT_COMPLETED);
        }

        intent.putExtra("elderlyUserName", elderlyUserName);
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        long repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES/15;
        long triggerTime = System.currentTimeMillis() + repeatInterval;
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerTime, repeatInterval, pendingIntent);

        System.out.println("RUN_FUNCTION_COPY_MEAL");
    }
    public int getReminderId(String mealType, int ReminderNum) {
        String mealInfo;
        try {

            if ("Breakfast".equals(mealType)) {
                mealInfo = "1"+ReminderNum;
            } else if ("Lunch".equals(mealType)) {
                mealInfo = "2"+ReminderNum;
            } else if ("Dinner".equals(mealType)) {
                mealInfo = "3"+ReminderNum;
            } else if ("Snack".equals(mealType)) {
                mealInfo = "4"+ReminderNum;
            } else {
                throw new IllegalArgumentException("Ogiltig måltidstyp: " + mealType);
            }

            mealInfo = mealInfo.replace("-", "");
            return Integer.valueOf(mealInfo);
        } catch (NumberFormatException e) {
            // Handle the case where ReminderNum is not a valid integer
            e.printStackTrace(); // or log the error
            return -1; // or some default value
        }
    }
    public int getMealId(String mealType, String date) {
        Integer id;
        String mealInfo;
        if("Breakfast".equals(mealType)){
            mealInfo = "1"+date;
            mealInfo = mealInfo.replace("-", "");
            return id = Integer.valueOf(mealInfo);
        }
        else if ("Lunch".equals(mealType)) {
            mealInfo = "2"+date;
            mealInfo = mealInfo.replace("-", "");
            return id = Integer.valueOf(mealInfo);
        } else if ("Dinner".equals(mealType)) {
            mealInfo = "3"+date;
            mealInfo = mealInfo.replace("-", "");
            return id = Integer.valueOf(mealInfo);
        } else if ("Snack".equals(mealType)) {
            mealInfo = "1"+date;
            mealInfo = mealInfo.replace("-", "");
            return id = Integer.valueOf(mealInfo);
        } else {
            throw new IllegalArgumentException("Ogiltig måltidstyp: " + mealType);
        }
    }


    private boolean isTimeUp(String[] itemParts, int minutesToAdd){
        String current_time = getCurrentTime();
        long current_time_ToMillis = convertStringToMillis(current_time);

        String date_timeMeal = itemParts[4] + " " + itemParts[1];
        String date_timeUp = addMinutesToDateString(date_timeMeal, minutesToAdd);
        long date_timeUpToMillis = convertStringToMillis(date_timeUp);

        return date_timeUpToMillis <= current_time_ToMillis;
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
    public String getCurrentTime(){
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Months are 0-based, so we add 1
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Format the date and time into "yyyy-MM-dd HH:mm" format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }
}