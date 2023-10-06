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

public class Notification {

    private static final String CHANNEL_ID = "channel1";
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @SuppressLint("ScheduleExactAlarm")
    public void setAlarm(Context context) {

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(context, 1,intent, PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        //calendar.set(Calendar.HOUR, 10);
        //calendar.set(Calendar.MINUTE, 37);
        calendar.set(Calendar.SECOND, 3);
        //calendar.set(Calendar.MILLISECOND, 0);

        long triggerTimeInMillis = calendar.getTimeInMillis();

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTimeInMillis, pendingIntent);

        Toast.makeText(context, "Alarm set successfully", Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm(Context context) {

        Intent intent = new Intent(context, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context,1,intent,PendingIntent.FLAG_IMMUTABLE);

        if(alarmManager == null) {
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        alarmManager.cancel(pendingIntent);

        Toast.makeText(context, "Alarm canceled", Toast.LENGTH_SHORT).show();
    }
    public void createNotificationChannel(Context context) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "channel_name_1";
            String description = "Channel 1 description";

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel =
                    new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);
            notificationChannel.enableVibration(true);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}