package com.example.omnia.ourproject.AutoCompleteCountry;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.omnia.ourproject.AutoCompleteCountry.Adapter.CountryAdapter;
import com.example.omnia.ourproject.AutoCompleteCountry.Class.CountriesClass;
import com.example.omnia.ourproject.AutoCompleteCountry.Class.Country;
import com.example.omnia.ourproject.R;
import com.example.omnia.ourproject.AutoCompleteCountry.Remote.ApiUtlis;
import com.example.omnia.ourproject.AutoCompleteCountry.Remote.UserService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AutoCountryActivity extends AppCompatActivity {
    CountryAdapter countryAdapter;
    ArrayList<Country> countryList=new ArrayList<>();
    AutoCompleteTextView completeTextView;
    private UserService userService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try_main);
        InitComponent();

        Call<CountriesClass> call=userService.Auto();
        call.enqueue(new Callback<CountriesClass>() {
            @Override
            public void onResponse(Call<CountriesClass> call, Response<CountriesClass> response) {
                if(response.isSuccessful())
                {
                   countryList=response.body().getCountries();
                   countryAdapter=new CountryAdapter(AutoCountryActivity.this,R.layout.view_spinner,countryList);
                   completeTextView.setAdapter(countryAdapter);
                   completeTextView.setThreshold(1);
                }
                else {
                    Toast.makeText(AutoCountryActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CountriesClass> call, Throwable t) {

            }
        });


    }

    private void InitComponent() {
        completeTextView=findViewById(R.id.auto);
        userService= ApiUtlis.getUserService();
    }
}
