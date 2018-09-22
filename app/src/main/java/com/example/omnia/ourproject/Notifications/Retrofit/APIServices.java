package com.example.omnia.ourproject.Notifications.Retrofit;

import com.example.omnia.ourproject.Notifications.Model.MyResponse;
import com.example.omnia.ourproject.Notifications.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by zt on 15/03/18.
 */

public interface APIServices {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAD4cNfck:APA91bEqWgUJHy8MCpktKOtrXmHekn23OKn6A1SiXLdLDzN8wIAd996LhESzj8O2wwJgTmMqtXNNsUZHO6DZ_s0Wtd0TnB8C8WuD9Ds3uheDO59rb_qgR268j-HUXpa-61hundKucDgF"
    })
    @POST("fcm/send")

    Call<MyResponse> sendNotivication (@Body Sender body);

}
