package com.example.omnia.ourproject.Patient.Classes;

import java.io.Serializable;

/**
 * Created by Omnia on 7/6/2018.
 */

public class HospitalInfo implements Serializable {
    private String HospitalName;
    private String HospitalPhone;
    private String HospitalAddress;

    public HospitalInfo(String hospitalName, String hospitalPhone, String hospitalAddress) {
        HospitalName = hospitalName;
        HospitalPhone = hospitalPhone;
        HospitalAddress = hospitalAddress;
    }

    public String getHospitalName() {
        return HospitalName;
    }

    public void setHospitalName(String hospitalName) {
        HospitalName = hospitalName;
    }

    public String getHospitalPhone() {
        return HospitalPhone;
    }

    public void setHospitalPhone(String hospitalPhone) {
        HospitalPhone = hospitalPhone;
    }

    public String getHospitalAddress() {
        return HospitalAddress;
    }

    public void setHospitalAddress(String hospitalAddress) {
        HospitalAddress = hospitalAddress;
    }
}
