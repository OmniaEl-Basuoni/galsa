package com.example.omnia.ourproject.Doctor.Classes;

/**
 * Created by Menna Emad on 05/04/2018.
 */

public class PatientDate {

    private String DoctorName;
    private String doctorID;
    private long dateOfDay;
    private String galsaID;
    private String patientID;
    private String patientName;
    private String category;
    private Long fromHour;
    private Long toHour;


    public PatientDate(String doctorName, String doctorID, long dateOfDay, String galsaID,
                       String patientID, String patientName, String category, Long fromHour, Long toHour) {
        this.DoctorName = doctorName;
        this.doctorID = doctorID;
        this.dateOfDay = dateOfDay;
        this.galsaID = galsaID;
        this.patientID = patientID;
        this.patientName = patientName;
        this.category = category;
        this.fromHour = fromHour;
        this.toHour = toHour;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String doctorName) {
        DoctorName = doctorName;
    }


    public String getDoctorID() {
        return doctorID;
    }

    public long getDateOfDay() {
        return dateOfDay;
    }

    public String getGalsaID() {
        return galsaID;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getCategory() {
        return category;
    }

    public Long getFromHour() {
        return fromHour;
    }

    public Long getToHour() {
        return toHour;
    }


    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public void setDateOfDay(long dateOfDay) {
        this.dateOfDay = dateOfDay;
    }

    public void setGalsaID(String galsaID) {
        this.galsaID = galsaID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setFromHour(Long fromHour) {
        this.fromHour = fromHour;
    }

    public void setToHour(Long toHour) {
        this.toHour = toHour;
    }
}
