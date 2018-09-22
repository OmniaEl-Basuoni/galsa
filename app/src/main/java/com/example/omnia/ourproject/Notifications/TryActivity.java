package com.example.omnia.ourproject.Notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.omnia.ourproject.Alarm.MyAlarm;
import com.example.omnia.ourproject.Notifications.Model.MyResponse;
import com.example.omnia.ourproject.Notifications.Model.Notification;
import com.example.omnia.ourproject.Notifications.Model.Sender;
import com.example.omnia.ourproject.Notifications.NotificationServices.Common;
import com.example.omnia.ourproject.Notifications.Retrofit.APIServices;
import com.example.omnia.ourproject.R;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TryActivity extends AppCompatActivity {


    EditText title,content;
    Button button,Put,Read;

    APIServices  apiServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try);

        Init();

        Common.currentToken= FirebaseInstanceId.getInstance().getToken();

        apiServices=Common.getFCMClient();

        Log.d("Error",Common.currentToken);



        Log.d("Time", Calendar.getInstance().getTime().getTime()+"");


        setAlarm(Calendar.getInstance().getTime().getTime()+10000);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Notification notification=new Notification("Send Now Yala","Try Now");
                Sender sender=new Sender(notification,
                        "fiZwabJeIrs:APA91bGI1byXnnlhK8WwaF-9zcV3WvJoJigIuLhi3OVE8Q_hvWg4mFwnd07Ywq2ZtYgc24yr66_3TNMEE3OJX1erInODa34qwJ5GH5RNbRhpsG-yHjyIbcxvAAaR0WQfYpWh279l-SbjnbwlBT12U7j9VrYzCNDjIQ"
                );
                apiServices.sendNotivication(sender)
                        .enqueue(new Callback<MyResponse>() {
                            @Override
                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                if(response.isSuccessful())
                                {
                                    Toast.makeText(TryActivity.this, "Su", Toast.LENGTH_SHORT).show();
                                }
                                else
                                    Toast.makeText(TryActivity.this, "F", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<MyResponse> call, Throwable t) {

                            }
                        });
            }
        });



    }
    private void Init()
    {
        title=findViewById(R.id.edit);
        content=findViewById(R.id.editcon);

        button=findViewById(R.id.send);
        Put=findViewById(R.id.Put);
        Read=findViewById(R.id.Read);
    }

    private void setAlarm(long time) {
        //getting the alarm manager
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(this, MyAlarm.class);

        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);

        //setting the repeating alarm that will be fired every day
        am.setRepeating(AlarmManager.RTC, time, AlarmManager.INTERVAL_DAY, pi);
        Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show();
    }
}
