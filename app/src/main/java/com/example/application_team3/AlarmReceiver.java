package com.example.application_team3;




import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;


import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class AlarmReceiver extends BroadcastReceiver {

    
    private static final String RUN_FUNCTION_ACTION_CAREGIVER = "RUN_FUNCTION_ACTION_CAREGIVER";
    private static final String RUN_FUNCTION_ACTION_ELDERLY = "RUN_FUNCTION_ACTION_ELDERLY";
    private static final String MEAL_ACTION = "MEAL_ACTION";
    private static final String CHANNEL_ID = "channel1";
    private Controller control;


    @Override
    public void onReceive(Context context, Intent intent) {

        String ELDERLY_CONTENT = context.getString(R.string.eat_your_meal);
        String CAREGIVER_CONTENT = context.getString(R.string.has_miss_a_meal);

        System.out.println("AlarmReceiver ..... .....");
        control = (Controller) intent.getSerializableExtra("controller");

        if (intent.getAction() != null && intent.getAction().equals(MEAL_ACTION)){

            String meal_type = intent.getStringExtra("mealType");
            String elderlyUserName = intent.getStringExtra("elderlyUserName");
            String elderlyName = intent.getStringExtra("elderlyName");

            meal_type = getMeal(meal_type, context);
            if (elderlyName != null)
                showNotification(context,  elderlyName.toUpperCase() + CAREGIVER_CONTENT, null, meal_type);
            else
                showNotification(context,  meal_type,  elderlyUserName, ELDERLY_CONTENT);

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


    public void showNotification(Context context, String textTitle, String elderly, String textContent) {

        NotificationCompat.Builder builder;
        Intent resultIntent = (elderly != null) ?
                new Intent(context, ElderlyScheduler.class):
                new Intent(context, CaregiverDash.class);

        resultIntent.putExtra("controller", control);


        // Create the TaskStackBuilder and add the intent, which inflates the back
        // stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack.
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setContentIntent(resultPendingIntent);
        builder.setSmallIcon(R.drawable.notification_icon);
        builder.setContentTitle(textTitle);
        builder.setContentText(textContent);
        builder.setAutoCancel(true);

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
        notificationManager.notify(getNotificationId(), builder.build());

    }

    public int getNotificationId() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmssSSS", Locale.getDefault());
        return Integer.parseInt(sdf.format(calendar.getTime()));
    }

    public String getMeal(String meal, Context context){
        String meal_type = meal;
        if(meal_type.equals("Breakfast")) {
            return meal_type = context.getString(R.string.Breakfast);
        }
        else if(meal_type.equals("Lunch")) {
            return meal_type = context.getString(R.string.Lunch);
        }
        else if(meal_type.equals("Dinner")) {
            return meal_type = context.getString(R.string.Dinner);
        }
        else if(meal_type.equals("Snack")) {
            return meal_type = context.getString(R.string.Snack);
        }
        return meal_type;
    }




}
