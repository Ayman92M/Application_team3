package com.example.application_team3;




import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;


import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;




public class AlarmReceiver extends BroadcastReceiver {


    private static final String ELDERLY_CONTENT = "Eat your meal!";
    private static final String CAREGIVER_CONTENT = " has miss a meal!";
    private static final String RUN_FUNCTION_ACTION_CAREGIVER = "RUN_FUNCTION_ACTION_CAREGIVER";
    private static final String RUN_FUNCTION_ACTION_ELDERLY = "RUN_FUNCTION_ACTION_ELDERLY";
    private static final String MEAL_ACTION = "MEAL_ACTION";
    private Controller control;



    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "channel1";


    @Override
    public void onReceive(Context context, Intent intent) {
        control = (Controller) intent.getSerializableExtra("controller");


        if (intent.getAction() != null && intent.getAction().equals(MEAL_ACTION)){


            String meal_type = intent.getStringExtra("mealType");
            String meal_date = intent.getStringExtra("mealDate");
            String elderlyUserName = intent.getStringExtra("elderlyUserName");
            String elderlyName = intent.getStringExtra("elderlyName");


            if (elderlyName != null)
                showNotification(context, intent, elderlyName.toUpperCase() + CAREGIVER_CONTENT, null, meal_type);
            else
                showNotification(context, intent, meal_type,  elderlyUserName, ELDERLY_CONTENT);


        }


        if (intent.getAction() != null && intent.getAction().equals(RUN_FUNCTION_ACTION_CAREGIVER)) {
            String elderlyUserName = intent.getStringExtra("elderlyUserName");
            String elderlyName = intent.getStringExtra("elderlyName");
            System.out.println("AlarmReceiver_ACTION_CAREGIVER --> updateNotificationCaregiver for: elderlyName " + elderlyName);
            control.updateNotificationCaregiver(elderlyUserName, elderlyName, null, context);


        }


        if (intent.getAction() != null && intent.getAction().equals(RUN_FUNCTION_ACTION_ELDERLY)) {


            String elderlyUserName = intent.getStringExtra("elderlyUserName");
            System.out.println("AlarmReceiver_ACTION_ELDERLY --> updateNotification for: " + elderlyUserName);
            control.updateNotificationElderly(elderlyUserName, context);


        }


        if (intent.getAction() != null && intent.getAction().equals("RUN_FUNCTION_COPY_MEAL")) {
            String elderlyUserName = intent.getStringExtra("elderlyUserName");
            control.copyMealElderly(elderlyUserName);
            System.out.println("AlarmReceiver_ACTION_ELDERLY --> copy meal : ");


        }


    }




    public void showNotification(Context context, Intent intent, String textTitle, String elderly, String textContent) {


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


        NotificationCompat.Builder builder;
        if(elderly != null){
            Intent i = new Intent(context, ElderlyScheduler.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(context, 1, i, PendingIntent.FLAG_IMMUTABLE);


            builder = new NotificationCompat.Builder(context, CHANNEL_ID)
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
        }
        else {
            Intent i = new Intent(context, CaregiverDash.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(context, 1, i, PendingIntent.FLAG_IMMUTABLE);


            builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle(textTitle)
                    .setContentText(textContent)
                    .setContentIntent(pendingIntent)
                    .setDefaults(NotificationCompat.DEFAULT_ALL) // must requires VIBRATE permission
                    .setPriority(NotificationCompat.PRIORITY_HIGH) //must give priority to High, Max which will considered as heads-up notification
                    .setAutoCancel(true)
            ;
        }




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
