package com.example.omnia.ourproject.SharedClasses;

import android.widget.RelativeLayout;

/**
 * Created by Omnia on 12/3/2017.
 */

public class RequestsClass {
  public   String DoctorName;
  public   String PatientName;
  public   String Date;
  public String State;
  public String PatientId;


  public RequestsClass(String doctorName,String patientName,String date,String state)
  {
      this.DoctorName=doctorName;
      this.PatientName=patientName;
              this.Date=date;
              this.State=state;

  }

  public RequestsClass(String patientId,String patientName)
  {
   this.PatientId=patientId;
   this.PatientName=patientName;
  }





}
