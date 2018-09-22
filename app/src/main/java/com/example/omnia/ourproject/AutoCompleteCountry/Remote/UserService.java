package com.example.omnia.ourproject.AutoCompleteCountry.Remote;

import com.example.omnia.ourproject.AutoCompleteCountry.Class.CountriesClass;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserService {


    @GET("country/json")
    Call<CountriesClass> Auto();


}
