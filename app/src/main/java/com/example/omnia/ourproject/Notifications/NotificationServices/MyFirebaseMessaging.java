package com.example.omnia.ourproject.Notifications.NotificationServices;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.omnia.ourproject.Doctor.Activities.DoctorHomeActivity;
import com.example.omnia.ourproject.R;
import com.example.omnia.ourproject.Notifications.TryActivity;
import com.example.omnia.ourproject.SharedActivity.SplashActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by zt on 15/03/18.
 */

public class MyFirebaseMessaging extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        showNotification(remoteMessage.getNotification());
    }

    private void showNotification(RemoteMessage.Notification notificationr) {

       /* NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Intent intent = new Intent(getApplicationContext(),DoctorHomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(notificationr.getTitle());
        builder.setContentText(notificationr.getBody());
        builder.setSubText("Tap to view the website.");
        builder.setFullScreenIntent(pendingIntent,true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Will display the notification in the notification bar
        notificationManager.notify(1, builder.build());*/

        // Create pending intent, mention the Activity which needs to be
        //triggered when user clicks on notification(StopScript.class in this case)

        Intent intent=new Intent(this,SplashActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);



        // Sets an ID for the notification
        int mNotificationId = 001;

        // Build Notification , setOngoing keeps the notification always in status bar
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        mBuilder.setContentTitle(notificationr.getTitle());
        mBuilder.setContentText(notificationr.getBody());
        mBuilder.setSubText("Tap to view the App");
       // mBuilder.setFullScreenIntent(contentIntent,false);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setBadgeIconType(R.mipmap.ic_launcher);
        mBuilder.setAutoCancel(true);





        mBuilder.setContentIntent(contentIntent);


        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);



        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());


    }



}
