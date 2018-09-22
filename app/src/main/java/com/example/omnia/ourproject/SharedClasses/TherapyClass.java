package com.example.omnia.ourproject.SharedClasses;

/**
 * Created by zt on 20/02/18.
 */

public class TherapyClass {
    public String TherapyName;
    public String TherapyDescription;
    public int TherapyLimit;
    public long TherapyTime;
    public long TherapyDate;


    public TherapyClass(String therapyDescription,int therapyLimit,long therapyTime)
    {
        TherapyDescription=therapyDescription;
        TherapyLimit=therapyLimit;
        TherapyTime=therapyTime;
    }

    public TherapyClass(String therapyName, String therapyDescription,
                        int therapyLimit,long therapyTime,long therapyDate)
    {
        TherapyName=therapyName;
        TherapyDescription=therapyDescription;
        TherapyLimit=therapyLimit;
        TherapyTime=therapyTime;
        TherapyDate=therapyDate;
    }
}
