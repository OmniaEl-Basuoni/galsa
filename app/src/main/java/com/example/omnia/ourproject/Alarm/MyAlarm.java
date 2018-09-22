package com.example.omnia.ourproject.Alarm;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.omnia.ourproject.SharedClasses.SharedParameters;

/**
 * Created by Belal on 8/29/2017.
 */

//class extending the Broadcast Receiver
public class MyAlarm extends BroadcastReceiver {

    //the method will be fired when the alarm is triggerred
    @Override
    public void onReceive(Context context, Intent intent) {

        if(SharedParameters.AlarmFlag)
        {
            PatientAlarm.UpdateAlarm(context,SharedParameters.AlarmUpdateID);
        }
        else {
             DoctorAlarm.UpdateAlarm(context,SharedParameters.AlarmUpdateID);
        }

        Intent serviceIntent = new Intent(context,RingtonePlayingService.class);
        serviceIntent.putExtra("ID",SharedParameters.AlarmUpdateID);
        context.startService(serviceIntent);
    }

}