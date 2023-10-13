package com.example.application_team3;


import static android.provider.Settings.System.getString;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class AlarmReceiver extends BroadcastReceiver {

    private static final String textTitle = "Breakfast Time";
    private static final String textContent = "Eat your meal!";
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "channel1";

    private static final String NOTIFICATION_ACTION_YES = "com.app.action.YES";
    private static final String NOTIFICATION_ACTION_NO = "com.app.action.NO";
    private static final String NOTIFICATION_ACTION_SHOW = "com.app.action.SHOW";
    private static final String NOTIFICATION_ACTION_CLICK = "com.app.action.CLICK";

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        Bundle extras = new Bundle();
        extras = intent.getExtras();

        System.out.println(action);

        switch (action) {
            case NOTIFICATION_ACTION_NO:
                System.out.println("No");
                // If click extra action button no
                break;
            case NOTIFICATION_ACTION_YES:
                System.out.println("Yes");

                if(extras != null)
                    System.out.println(extras.getString("mealType"));
                else
                    System.out.println("Exras is null");

                // If click extra action button yes
                break;
            case NOTIFICATION_ACTION_CLICK:
                // If click the notification itself
                break;
            case NOTIFICATION_ACTION_SHOW:
                showNotification(context, intent, textTitle, textContent);
                break;
            default:
                // Idk how this happened
                break;
        }
    }

    public void showNotification(Context context, Intent intent, String textTitle, String textContent) {

        String ACTION_YES = "Yes";
        String ACTION_NO = "No";

        String action = intent.getAction();
        Bundle extras = intent.getExtras();

        Intent YesIntent = new Intent(context, AlarmReceiver.class);
        YesIntent.setAction(NOTIFICATION_ACTION_YES);
        YesIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingYesIntent =
                PendingIntent.getBroadcast(context, 3, YesIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent NoIntent = new Intent(context, AlarmReceiver.class);
        NoIntent.setAction(NOTIFICATION_ACTION_NO);
        NoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingNoIntent =
                PendingIntent.getBroadcast(context, 2, NoIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent i = new Intent(context, MainActivity.class);
        i.setAction(NOTIFICATION_ACTION_CLICK);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 1, i, PendingIntent.FLAG_IMMUTABLE);

        if(extras != null) {
            YesIntent.putExtras(extras);
            NoIntent.putExtras(extras);
            i.putExtras(extras);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.notification_icon)
        .setContentTitle(textTitle)
        .setContentText(textContent)
        .setDefaults(NotificationCompat.DEFAULT_ALL) // must requires VIBRATE permission
        .setPriority(NotificationCompat.PRIORITY_HIGH) //must give priority to High, Max which will considered as heads-up notification
        .setContentIntent(pendingIntent)
        .addAction(R.drawable.ic_extra_action_button_yes, ACTION_YES, pendingYesIntent)
        .addAction(R.drawable.ic_extra_action_button_no, ACTION_NO, pendingNoIntent)
        .setAutoCancel(true);

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