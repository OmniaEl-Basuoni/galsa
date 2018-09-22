package com.example.omnia.ourproject.SharedClasses;

import android.text.format.DateFormat;
import android.widget.ArrayAdapter;

import com.example.omnia.ourproject.Patient.Classes.nodeClass;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Omnia on 5/9/2018.
 */

public class JalsatClass {
    String ID;
    String  Notes;
    Long DateLong;

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getMedicines() {
        return Medicines;
    }

    public void setMedicines(String medicines) {
        Medicines = medicines;
    }

    String Medicines;

    public JalsatClass(String ID, String notes, Long date, String medicines) {
        this.ID = ID;
        Notes = notes;
        DateLong = date;
        Medicines = medicines;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }



    public String getDate() {

       return DateFormat.format("MM/dd/yyyy", new Date(DateLong))+"";

    }

    public void setDate(Long date) {
        DateLong = date;
    }


}
