package com.example.omnia.ourproject.SharedClasses;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zt on 26/02/18.
 */

public class DoctorPatientMessagesClass implements Serializable {

    private String DoctorUid;
    private String DoctorName;

    public String getDoctorPhoto() {
        return DoctorPhoto;
    }

    public void setDoctorPhoto(String doctorPhoto) {
        DoctorPhoto = doctorPhoto;
    }

    public DoctorPatientMessagesClass(String doctorUid, String doctorName, String doctorPhoto,
                                      String patientUid, String patientName,
                                      String messageId, String lastMessageText, long lastMessageTime) {
        DoctorUid = doctorUid;
        DoctorName = doctorName;
        DoctorPhoto = doctorPhoto;
        PatientUid = patientUid;
        PatientName = patientName;
        MessageId = messageId;
        LastMessageText = lastMessageText;
        LastMessageTime = lastMessageTime;
    }

    public DoctorPatientMessagesClass(String messageId, String doctorUid, String doctorName, String doctorPhoto,
                                      String patientUid, String patientName,
                                      String lastMessageText) {
        MessageId=messageId;
        DoctorUid = doctorUid;
        DoctorName = doctorName;
        DoctorPhoto = doctorPhoto;
        PatientUid = patientUid;
        PatientName = patientName;
        LastMessageText = lastMessageText;
        LastMessageTime = new Date().getTime();
    }

    public DoctorPatientMessagesClass() {
    }

    private String DoctorPhoto;
    private String PatientUid;
    private String PatientName;
    private String MessageId;
    private String LastMessageText;
    private long LastMessageTime;


    public String getDoctorUid() {
        return DoctorUid;
    }

    public void setDoctorUid(String doctorUid) {
        DoctorUid = doctorUid;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String doctorName) {
        DoctorName = doctorName;
    }
    public String getLastMessageText() {
        return LastMessageText;
    }

    public void setLastMessageText(String lastMessageText) {
        LastMessageText = lastMessageText;
    }

    public long getLastMessageTime() {
        return LastMessageTime;
    }

    public void setLastMessageTime(long lastMessageTime) {
        LastMessageTime = lastMessageTime;
    }

    public String getPatientUid() {
        return PatientUid;
    }

    public void setPatientUid(String patientUid) {
        PatientUid = patientUid;
    }

    public String getPatientName() {
        return PatientName;
    }

    public void setPatientName(String patientName) {
        PatientName = patientName;
    }

    public String getMessageId() {
        return MessageId;
    }

    public void setMessageId(String messageId) {
        MessageId = messageId;
    }
}
