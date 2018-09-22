package com.example.omnia.ourproject.SharedClasses;

import android.text.format.DateFormat;

import java.util.Date;

/**
 * Created by 3ZT on 04-Dec-17.
 */

public class ComplainsClass {
    private String ComplainID;

    public ComplainsClass(String complainID, String complainsText, long complainsDate, String complainsState, String patientID) {
        ComplainID = complainID;
        ComplainsText = complainsText;
        ComplainsDate = complainsDate;
        ComplainsState = complainsState;
        PatientID = patientID;
    }

    public String getComplainID() {
        return ComplainID;
    }

    public void setComplainID(String complainID) {
        ComplainID = complainID;
    }

    public String getComplainsText() {
        return ComplainsText;
    }

    public void setComplainsText(String complainsText) {
        ComplainsText = complainsText;
    }

    public String getComplainsDate() {
        String dateString = DateFormat.format("dd/MM/yyyy", new Date(ComplainsDate)).toString();
        return dateString;
    }

    public void setComplainsDate(long complainsDate) {
        ComplainsDate = complainsDate;
    }

    public String getComplainsState() {
        return ComplainsState;
    }

    public void setComplainsState(String complainsState) {
        ComplainsState = complainsState;
    }

    public String getPatientID() {
        return PatientID;
    }

    public void setPatientID(String patientID) {
        PatientID = patientID;
    }

    private String ComplainsText;
    private long ComplainsDate;
    private String ComplainsState;
    private String PatientID;






}
