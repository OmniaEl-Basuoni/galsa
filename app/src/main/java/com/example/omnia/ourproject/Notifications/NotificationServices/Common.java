package com.example.omnia.ourproject.Notifications.NotificationServices;

import com.example.omnia.ourproject.Notifications.Retrofit.APIServices;
import com.example.omnia.ourproject.Notifications.Retrofit.RetrofitClient;

/**
 * Created by zt on 15/03/18.
 */

public class Common {
    public static String currentToken="";
    public static String baseUrl="https://fcm.googleapis.com/";

    public static APIServices getFCMClient()
    {
        return RetrofitClient.getClient(baseUrl).create(APIServices.class);
    }
}
