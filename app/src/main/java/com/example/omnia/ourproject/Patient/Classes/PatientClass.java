package com.example.omnia.ourproject.Patient.Classes;

import java.io.Serializable;

/**
 * Created by 3ZT on 09-Nov-17.
 */
public class PatientClass implements Serializable {
    public String PatientID;
    public String PatientField;
    public String PatientName;
    public String PatientEmail;
    public String PatientPassword;
    public String PatientPhone;
    public String PatientNationalID;
    public String PatientGender;
    public String PatientBirth;

    public PatientClass(String PatientName,String PatientEmail,String PatientPassword
                        ,String PatientPhone,String PatientNationalID,String PatientGender,String PatientBirth)
    {
        this.PatientName=PatientName;
        this.PatientEmail=PatientEmail;
        this.PatientPassword=PatientPassword;
        this.PatientPhone=PatientPhone;
        this.PatientNationalID=PatientNationalID;
        this.PatientGender=PatientGender;
        this.PatientBirth=PatientBirth;
    }

    public PatientClass(String PatientId ,String PatientName,String PatientEmail,String PatientPassword
            ,String PatientPhone,String PatientNationalID,String PatientGender,String PatientBirth)
    {
        this.PatientID=PatientId;
        this.PatientName=PatientName;
        this.PatientEmail=PatientEmail;
        this.PatientPassword=PatientPassword;
        this.PatientPhone=PatientPhone;
        this.PatientNationalID=PatientNationalID;
        this.PatientGender=PatientGender;
        this.PatientBirth=PatientBirth;
    }

    public PatientClass(String PatientName,String PatientUid)
    {
        this.PatientName=PatientName;
        this.PatientID=PatientUid;
    }
}
