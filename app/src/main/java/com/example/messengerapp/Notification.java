package com.example.messengerapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Notification extends Application {
    public static final String CHANNEL_1_ID = "message";
    public static final String CHANNEL_2_ID = "online";
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID, "New message",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is new message channel");

            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID, "Online status",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel2.setDescription("Online status channel");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel1);

        }
    }
}
