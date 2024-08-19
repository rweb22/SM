package com.tripleseven.android;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

public class PushNotificationService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        String name = "HEADS_UP_NOTIFICATION";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // channel for notifications

            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            if (notificationManager != null) {
                List<NotificationChannel> channelList = notificationManager.getNotificationChannels();

                for (int i = 0; channelList != null && i < channelList.size(); i++)
                    notificationManager.deleteNotificationChannel(channelList.get(i).getId());
            }


            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel("HEADS_UP_NOTIFICATION", name, importance);

            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
            Intent intent= new Intent(this, Notifications.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(intent);
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            String title = remoteMessage.getNotification().getTitle();
            String text = remoteMessage.getNotification().getBody();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, name)
                    .setSmallIcon(R.drawable.logo)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setContentIntent(pendingIntent);


            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(0, builder.build());


    }
}
