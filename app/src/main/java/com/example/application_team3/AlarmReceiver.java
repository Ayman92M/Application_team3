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

    private static final String textTitle = "Breakfast Time";
    private static final String textContent = "Eat your meal!";

    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "channel1";

    @Override
    public void onReceive(Context context, Intent intent) {

        showNotification(context, intent, textTitle, textContent);

        }

    public void showNotification(Context context, Intent intent, String textTitle, String textContent) {

        Intent i = new Intent(context, MainActivity.class);
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