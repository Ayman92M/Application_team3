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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Notification {

    private static final String CHANNEL_ID = "channel1";
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
            // Use BOOT_COMPLETED action for pre-Oreo devices
            intent.setAction(Intent.ACTION_BOOT_COMPLETED);
        }

        intent.putExtra("mealType", mealType);
        intent.putExtra("mealDate", mealDate);
        intent.putExtra("elderlyUserName", pid);
        intent.putExtra("elderlyName", elderly_name);
        intent.putExtra("triggerTimeInMillis", triggerTimeInMillis);
        intent.putExtra("timeUpToMillis", timeUpToMillis);




        id = getMealId(mealType,mealDate);
        pendingIntent = PendingIntent.getBroadcast(context, id,intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTimeInMillis, pendingIntent);
        //Toast.makeText(context, "setAlarm for: " + mealType + " -> ID: " + id , Toast.LENGTH_SHORT).show();
        System.out.println("-------> setAlarm for: " + mealType + " -> ID: " + id);
    }


    public void runFunction(Context context, String elderlyUserName, String elderlyName, TextView textview) {

        ViewNavigator navigator = new ViewNavigator(context);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setAction("RUN_FUNCTION_ACTION");
        } else {
            // Use BOOT_COMPLETED action for pre-Oreo devices
            intent.setAction(Intent.ACTION_BOOT_COMPLETED);
        }

        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Set up a repeating alarm that starts after 1 hour and repeats every hour
        long repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
        long triggerTime = System.currentTimeMillis() + repeatInterval;

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerTime, repeatInterval, pendingIntent);

        // Use the same action for creating the notification channel
        createNotificationChannel(context, "RUN_FUNCTION_ACTION");

        if (elderlyUserName != null && elderlyName != null) {
            intent.putExtra("elderlyUserName", elderlyUserName);
            intent.putExtra("elderlyName", elderlyName);

            // Consider whether you need a new instance of ViewNavigator for each call
            System.out.println("---- runFunction ----");
            navigator.updateNotificationCaregiver(elderlyUserName, elderlyName, textview);
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

    public void cancelAlarm(Context context, String mealType, String mealDate) {

        id = getMealId(mealType, mealDate);

        Intent intent = new Intent(context, AlarmReceiver.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setAction("unique_action_string");
        } else {
            intent.setAction(Intent.ACTION_BOOT_COMPLETED);
        }

        pendingIntent = PendingIntent.getBroadcast(context, id, intent,PendingIntent.FLAG_IMMUTABLE);

        if(alarmManager == null) {
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        alarmManager.cancel(pendingIntent);

        Toast.makeText(context, "Alarm canceled for: " + "mealId: "+ id, Toast.LENGTH_SHORT).show();
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
}