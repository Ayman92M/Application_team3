package com.example.application_team3;


import static android.provider.Settings.System.getString;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class AlarmReceiver extends BroadcastReceiver {

    private static final String textContent = "Eat your meal!";
    private static final String RUN_FUNCTION_ACTION = "RUN_FUNCTION_ACTION";
    private static final String MEAL_ACTION = "MEAL_ACTION";


    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "channel1";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null && intent.getAction().equals(MEAL_ACTION)){
            ViewNavigator navigator = new ViewNavigator(context);

            String meal_type = intent.getStringExtra("mealType");
            String meal_date = intent.getStringExtra("mealDate");
            String elderlyUserName = intent.getStringExtra("elderlyUserName");
            String elderlyName = intent.getStringExtra("elderlyName");
            long triggerTimeInMillis = intent.getLongExtra("triggerTimeInMillis",0L);
            long timeUpToMillis = intent.getLongExtra("timeUpToMillis",0L);

            if (elderlyName != null){
                showNotification(context, intent, elderlyName + " has miss a meal!", elderlyUserName, meal_type);
                //navigator.updateNotificationCaregiver(elderlyUserName, elderlyName);

            }

            else{
                showNotification(context, intent, meal_type,  elderlyUserName, textContent);
                navigator.crateReminder(elderlyUserName, meal_type, meal_date, triggerTimeInMillis, timeUpToMillis);
                navigator.updateNotification(elderlyUserName);
            }

        }

        if (intent.getAction() != null && intent.getAction().equals(RUN_FUNCTION_ACTION)) {
            // Handle the repeating alarm action (runFunction)

            ViewNavigator navigator = new ViewNavigator(context);

            String elderlyUserName = intent.getStringExtra("elderlyUserName");
            String elderlyName = intent.getStringExtra("elderlyName");
            System.out.println("VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV");
            System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            System.out.println("YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY");
            System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");

            //Notification notification = new Notification();
            //notification.runFunction(context, elderlyUserName, elderlyName, null);

            navigator.updateNotificationCaregiver(elderlyUserName, elderlyName, null);

        }


        }


    public void showNotification(Context context, Intent intent, String textTitle, String elderlyUserName, String textContent) {

        String ACTION_YES = "Yes";
        String ACTION_NO = "No";

        Intent YesIntent = new Intent(context, MainActivity.class);
        YesIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingYesIntent =
                PendingIntent.getActivity(context, 3, YesIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent NoIntent = new Intent(context, MainActivity.class);
        NoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingNoIntent =
                PendingIntent.getActivity(context, 2, NoIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent i = new Intent(context, Elderly_Scheduler.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 1, i, PendingIntent.FLAG_IMMUTABLE);



        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.notification_icon)
        .setContentTitle(textTitle)
        .setContentText(textContent)
        .setContentIntent(pendingIntent)
        .setDefaults(NotificationCompat.DEFAULT_ALL) // must requires VIBRATE permission
        .setPriority(NotificationCompat.PRIORITY_HIGH) //must give priority to High, Max which will considered as heads-up notification
        .setAutoCancel(true)
        .addAction(R.drawable.ic_extra_action_button_yes, ACTION_YES,
                pendingYesIntent)
        .addAction(R.drawable.ic_extra_action_button_no, ACTION_NO,
                pendingNoIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
        }


}