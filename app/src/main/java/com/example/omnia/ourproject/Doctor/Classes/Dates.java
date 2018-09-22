package com.example.omnia.ourproject.Doctor.Classes;

/**
 * Created by Menna Emad on 27/03/2018.
 */

public class Dates {

    public String Name;
    public String Time;

    public Dates(String name, String time) {
        Name = name;
        Time = time;
    }

    public String getName() {
        return Name;
    }

    public String getTime() {
        return Time;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setTime(String time) {
        Time = time;
    }
}
