package com.example.application_team3;


import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;



import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class Notification implements Serializable {


    private static final String CHANNEL_ID = "channel1";
    private static Controller control;
    int id;

    public void setController(Controller _control){ control = _control; }

    public void setAlarm(Context context, String mealType, String pid, String elderly_name, String mealDate, long triggerTimeInMillis) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setAction("MEAL_ACTION");
        } else {
            intent.setAction(Intent.ACTION_BOOT_COMPLETED);
        }

        intent.putExtra("controller", control);
        intent.putExtra("mealType", mealType);
        intent.putExtra("mealDate", mealDate);
        intent.putExtra("elderlyUserName", pid);
        intent.putExtra("elderlyName", elderly_name);


        id = getMealId(mealType,mealDate);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id,intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTimeInMillis, pendingIntent);

        if(elderly_name == null)
            for (int i = 1; i < 4; i++) {
                int id = getReminderId(mealType, i);

                pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_IMMUTABLE);
                long reminderTimeToMillis = convertStringToMillis(addMinutesToDateString(getCurrentTime(), 1*i));
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTimeToMillis, pendingIntent);
                System.out.println("-------> SET Reminder "+ i + " for : " + mealType + " after "+ 1*i + " min");
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

        intent.putExtra("controller", control);
        if (elderlyUserName != null)    intent.putExtra("elderlyUserName", elderlyUserName);
        if (elderlyName != null)        intent.putExtra("elderlyName", elderlyName);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
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
            intent.setAction("RUN_FUNCTION_ACTION_ELDERLY");
        } else {
            intent.setAction(Intent.ACTION_BOOT_COMPLETED);
        }

        intent.putExtra("controller", control);
        if (elderlyUserName != null)    intent.putExtra("elderlyUserName", elderlyUserName);
        if (elderlyName != null)        intent.putExtra("elderlyName", elderlyName);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
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
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent,PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        System.out.println("Alarm canceled for: " + "mealId: "+ id);

        for (int i = 1; i < 4; i++) {
            id = getReminderId(mealType, i);
            pendingIntent = PendingIntent.getBroadcast(context, id, intent,PendingIntent.FLAG_IMMUTABLE);
            alarmManager.cancel(pendingIntent);
            System.out.println("Alarm canceled for: " + "mealId: ");
        }

    }
    public void createNotificationChannel(Context context, String mealType) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel =
                    new NotificationChannel(CHANNEL_ID, mealType, importance);
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

        intent.putExtra("controller", control);
        intent.putExtra("elderlyUserName", elderlyUserName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
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
            return Integer.parseInt(mealInfo);
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

        return -1;
    }

    public String addMinutesToDateString(String inputDate, int minutesToAdd) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            // Parse the input date string
            Date date = dateFormat.parse(inputDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, minutesToAdd);

            return dateFormat.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace(); // Handle the exception according to your needs
            return null;
        }
    }
    public String getCurrentTime(){

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }
}
