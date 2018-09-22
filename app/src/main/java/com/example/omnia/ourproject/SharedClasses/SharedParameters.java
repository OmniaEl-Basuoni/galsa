package com.example.omnia.ourproject.SharedClasses;

import android.util.Log;
import android.widget.Toast;

import com.example.omnia.ourproject.Notifications.Model.MyResponse;
import com.example.omnia.ourproject.Notifications.Model.Notification;
import com.example.omnia.ourproject.Notifications.Model.Sender;
import com.example.omnia.ourproject.Notifications.NotificationServices.Common;
import com.example.omnia.ourproject.Notifications.Retrofit.APIServices;
import com.example.omnia.ourproject.Notifications.TryActivity;
import com.example.omnia.ourproject.Patient.Classes.PatientClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 3ZT on 10/30/2017.
 */

public class SharedParameters {
    public static List<String> CategoriesList=new ArrayList<>();
    public static enum Role { Doctor, Patient}
    public static enum Gender { Male, Female}
    public static String DoctorUid;
    public static String PatientUid;
    public static String MessageUid;
    public static boolean WithAPI;
    public static String PatientName;
    public static String DoctorName;
    public static String CallId;
    public static List<PatientClass> patientClasses=new ArrayList<>();


    public static String AlarmUpdateID;
    public static boolean AlarmFlag;


    public static void SendNotification(final String Title, final String Body, String ID)
    {
        Query query = FirebaseDatabase.getInstance().getReference().child("Tokens").child(ID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    SetNotification(
                            Title,
                            Body,
                            dataSnapshot.getValue()+"");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private static void SetNotification(String Title, String Body, final String To)
    {
        APIServices apiServices= Common.getFCMClient();
        Notification notification=new Notification(Body,Title);
        Sender sender=new Sender(notification,To);
        apiServices.sendNotivication(sender)
                .enqueue(new Callback<MyResponse>() {
                    @Override
                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                        if(response.isSuccessful())
                        {
                            Log.d("AAAA", response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<MyResponse> call, Throwable t) {
                        Log.d("AAAA",t.getMessage());
                    }
                });

    }
}
