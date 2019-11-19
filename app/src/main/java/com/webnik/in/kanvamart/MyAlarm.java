package com.webnik.in.kanvamart;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class MyAlarm extends BroadcastReceiver {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences.Editor editor;
    String title,time;
    DbHelper dbHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE);
        editor=context.getSharedPreferences(MyPREFERENCES, context.MODE_PRIVATE).edit();
        title = prefs.getString("title", "abc");
        time = prefs.getString("time", "xyz");
        dbHelper=new DbHelper(context);
        dbHelper.getTodos();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);

        Intent mintent = new Intent(context, MainActivity.class);
        editor.putBoolean("notification", true);
        editor.apply();
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, mintent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.checked_small)
                .setContentTitle(title)
                .setContentText(time)
                .setContentIntent(pIntent);

        notificationManager.notify(1, notificationBuilder.build());
    }
}
