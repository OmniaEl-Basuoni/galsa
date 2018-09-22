package com.example.omnia.ourproject.SharedClasses;

/**
 * Created by Omnia on 2/2/2018.
 */

public class ChatClass {
    public   String PatientName;
    public   String PatientID;
    public   Long MsgLastTime;

    public ChatClass(String patientname,String patientid,Long msgLastTime)
    {
        this.PatientName=patientname;
        this.PatientID=patientid;
        this.MsgLastTime=msgLastTime;
    }

    public String getPatientName() {
        return PatientName;
    }

    public void setPatientName(String patientName) {
        PatientName = patientName;
    }

    public String getPatientID() {
        return PatientID;
    }

    public void setPatientID(String patientID) {
        PatientID = patientID;
    }

    public Long getMsgLastTime() {
        return MsgLastTime;
    }

    public void setMsgLastTime(Long msgLastTime) {
        MsgLastTime = msgLastTime;
    }





}
