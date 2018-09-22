package com.example.omnia.ourproject.Notifications.NotificationServices;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by zt on 15/03/18.
 */

public class MyFirebaseServices extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshedToken= FirebaseInstanceId.getInstance().getToken();
        Common.currentToken=refreshedToken;

        Log.d("Token",Common.currentToken);

    }



}
