package com.example.application_team3;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Notification {

    //private static final String CHANNEL_ID = "channel1";
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    Integer id;

    @SuppressLint("ScheduleExactAlarm")
    public void setAlarm(Context context, String mealType, String pid, long triggerTimeInMillis) {

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setAction("unique_action_string");
        } else {
            // Use BOOT_COMPLETED action for pre-Oreo devices
            intent.setAction(Intent.ACTION_BOOT_COMPLETED);
        }

        intent.putExtra("mealType", mealType);
        intent.putExtra("elderlyUserName", pid);

        id = getMealId(triggerTimeInMillis);


        pendingIntent = PendingIntent.getBroadcast(context, id,intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTimeInMillis, pendingIntent);
        Toast.makeText(context, "setAlarm for: " + mealType + " -> ID: " + id + " at: " + triggerTimeInMillis, Toast.LENGTH_SHORT).show();
        System.out.println("setAlarm for: " + mealType + " -> ID: " + id + " at: " + triggerTimeInMillis);
    }

    public int getMealId(long triggerTimeInMillis) {

        if (triggerTimeInMillis >= Integer.MIN_VALUE && triggerTimeInMillis <= Integer.MAX_VALUE) {
            int id = (int) (triggerTimeInMillis / 1000);
            return id;
        }
        else{
            int id = (int) triggerTimeInMillis;
            return id;
        }

    }
    /*
    public int getMealId(String mealType) {
        Integer id;
        if("Breakfast".equals(mealType))
            return id = 1;
        else if ("Lunch".equals(mealType)) {
            return id = 2;
        } else if ("Dinner".equals(mealType)) {
            return id = 3;
        } else if ("Snack".equals(mealType)) {
            return id = 4;
        } else {
            throw new IllegalArgumentException("Ogiltig måltidstyp: " + mealType);
        }
    }
     */



    public void cancelAlarm(Context context, String mealType) {

        //id = getMealId(mealType);
        Intent intent = new Intent(context, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, id, intent,PendingIntent.FLAG_IMMUTABLE);

        if(alarmManager == null) {
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        alarmManager.cancel(pendingIntent);

        Toast.makeText(context, "Alarm canceled", Toast.LENGTH_SHORT).show();
    }
    public void createNotificationChannel(Context context, String mealType) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel =
                    new NotificationChannel(mealType, mealType, importance);
            notificationChannel.setDescription(mealType + " description");
            notificationChannel.enableVibration(true);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}