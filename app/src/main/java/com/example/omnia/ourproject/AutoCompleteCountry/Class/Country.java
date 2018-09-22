package com.example.omnia.ourproject.AutoCompleteCountry.Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Country {

    @SerializedName("country")
    @Expose
    private Country_ country;

    public Country_ getCountry() {
        return country;
    }

    public void setCountry(Country_ country) {
        this.country = country;
    }

}
