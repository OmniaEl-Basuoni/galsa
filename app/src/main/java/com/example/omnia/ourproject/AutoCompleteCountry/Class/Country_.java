package com.example.omnia.ourproject.AutoCompleteCountry.Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Country_ {

    @SerializedName("country_id")
    @Expose
    private String countryId;
    @SerializedName("country_name")
    @Expose
    private String countryName;

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

}