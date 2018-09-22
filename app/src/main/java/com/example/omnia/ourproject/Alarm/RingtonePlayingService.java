package com.example.omnia.ourproject.Alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.example.omnia.ourproject.Notifications.NotificationServices.Common;
import com.example.omnia.ourproject.R;
import com.example.omnia.ourproject.SharedClasses.SharedParameters;

import java.util.Random;

public class RingtonePlayingService extends Service {
    

    MediaPlayer mMediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("MyActivity", "In the Richard service");
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

        mMediaPlayer = MediaPlayer.create(this, R.raw.phone_loud1);
        mMediaPlayer.start();

        String s=intent.getExtras().getString("ID");
        Log.d("AAAAA",s);
        SharedParameters.SendNotification("Galsa Time","New Galsa Begain after 5 mintues",s);

        return START_NOT_STICKY;

    }


    @Override
    public void onDestroy() {
        Log.e("JSLog", "on destroy called");
        super.onDestroy();

       
    }







}
