package com.example.omnia.ourproject.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.omnia.ourproject.SharedClasses.SharedParameters;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PatientAlarm {

    public long aLong;

    private static void setAlarm(Context context, long time) {

        //getting the alarm manager
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(context, MyAlarm.class);
        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);

        //setting the repeating alarm that will be fired every day
        time=time-(5*60*1000);
        am.setRepeating(AlarmManager.RTC, time, AlarmManager.INTERVAL_DAY, pi);


    }

    public static void UpdateAlarm(final Context context, final String PatientIID) {

        long time=5;

        //region Time
        SharedParameters.AlarmFlag=true;
        SharedParameters.AlarmUpdateID =PatientIID;
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
        String dateText = df2.format(Calendar.getInstance().getTime());

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(dateText);

            time=date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        //endregion
        Query query = FirebaseDatabase.getInstance().getReference()
                .child("Patient Agenda").child(PatientIID).child(""+time).orderByChild("fromHour");

        final long finalTime = time;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        long TotalStart = finalTime + Long.parseLong(issue.child("fromHour").getValue() + "");
                        if (TotalStart-(5*60*1000) >= Calendar.getInstance().getTime().getTime()) {
                            setAlarm(context,TotalStart);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }





}
