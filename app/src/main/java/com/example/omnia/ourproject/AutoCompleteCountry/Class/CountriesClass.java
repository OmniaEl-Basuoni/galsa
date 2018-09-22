package com.example.omnia.ourproject.AutoCompleteCountry.Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CountriesClass {
    @SerializedName("countries")
    @Expose
    private ArrayList<Country> countries = null;

    public ArrayList<Country> getCountries() {
        return countries;
    }

    public void setCountries(ArrayList<Country> countries) {
        this.countries = countries;
    }

}