package com.example.omnia.ourproject.Doctor.Classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 3ZT on 10/23/2017.
 */

public class DoctorClass implements Serializable {
    public String DoctorIdPhoto;
    public String DoctorID;
    public String DoctorName;
    public String DoctorEmail;
    public String DoctorPassword;
    public String DoctorPhone;
    public String DoctorNationalID;
    public String DoctorGender;
    public String DoctorBirth;
    public String DoctorPhotoUrl;
    public double DoctorRate;
    public double DoctorHalfPrice;
    public double DoctorHourPrice;
    public List<String> Categories;



    public DoctorClass(String url, String DoctorName, double DoctorRate, List<String>categories)
    {
        this.DoctorPhotoUrl =url;
        this.DoctorName = DoctorName;
        this.DoctorRate = DoctorRate;
        this.Categories=categories;
    }


    public DoctorClass(String url, String DoctorName, double DoctorRate, List<String>categories,String doctorID)
    {
        this.DoctorPhotoUrl =url;
        this.DoctorName = DoctorName;
        this.DoctorRate = DoctorRate;
        this.Categories=categories;
        this.DoctorID=doctorID;
    }

    public DoctorClass(String DoctorBirth,String DoctorEmail
            ,String DoctorGender,String DoctorName,String DoctorNationalID,String DoctorPhone,
                       String doctorPhotoUrl,String doctorIdPhoto,double doctorRate)
    {
        this.DoctorBirth=DoctorBirth;
        this.DoctorEmail=DoctorEmail;
        this.DoctorGender=DoctorGender;
        this.DoctorName=DoctorName;
        this.DoctorNationalID=DoctorNationalID;
        this.DoctorPhone=DoctorPhone;
        this.DoctorPhotoUrl=doctorPhotoUrl;
        this.DoctorIdPhoto=doctorIdPhoto;
        this.DoctorRate=doctorRate;
    }

    public DoctorClass(String DoctorId,String DoctorBirth,String DoctorEmail
            ,String DoctorGender,String DoctorName,String DoctorNationalID,String DoctorPhone,
                       String doctorPhotoUrl,String doctorIdPhoto,double doctorRate)
    {
        this.DoctorID=DoctorId;
        this.DoctorBirth=DoctorBirth;
        this.DoctorEmail=DoctorEmail;
        this.DoctorGender=DoctorGender;
        this.DoctorName=DoctorName;
        this.DoctorNationalID=DoctorNationalID;
        this.DoctorPhone=DoctorPhone;
        this.DoctorPhotoUrl=doctorPhotoUrl;
        this.DoctorIdPhoto=doctorIdPhoto;
        this.DoctorRate=doctorRate;
    }

    public DoctorClass(String DoctorName,String DoctorEmail,String DoctorPassword
            ,String DoctorPhone,String DoctorNationalID,String DoctorGender,String DoctorBirth)
    {
        this.DoctorName=DoctorName;
        this.DoctorEmail=DoctorEmail;
        this.DoctorPassword=DoctorPassword;
        this.DoctorPhone=DoctorPhone;
        this.DoctorNationalID=DoctorNationalID;
        this.DoctorGender=DoctorGender;
        this.DoctorBirth=DoctorBirth;
        this.DoctorRate=0;
    }

    public DoctorClass(String DoctorUid,String DoctorName,String DoctorPhoto,double DoctorRate
            ,List<String> categories,String DoctorGender,double Half,double Hour)
    {
        this.DoctorID=DoctorUid;
        this.DoctorName=DoctorName;
        this.DoctorPhotoUrl=DoctorPhoto;
        this.DoctorRate=DoctorRate;
        this.Categories=categories;
        this.DoctorGender=DoctorGender;
        this.DoctorHalfPrice=Half;
        this.DoctorHourPrice=Hour;

    }


    //PatientHome
    public DoctorClass(String url, String DoctorName, double DoctorRate, List<String>categories,String doctorID,String Half,String Hour)
    {
        this.DoctorPhotoUrl =url;
        this.DoctorName = DoctorName;
        this.DoctorRate = DoctorRate;
        this.Categories=categories;
        this.DoctorID=doctorID;
        this.DoctorHalfPrice=Double.parseDouble(Half);
        this.DoctorHourPrice=Double.parseDouble(Hour);
    }
}
