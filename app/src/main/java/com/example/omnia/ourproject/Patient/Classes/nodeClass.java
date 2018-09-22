package com.example.omnia.ourproject.Patient.Classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Omnia on 5/13/2018.
 */

public class nodeClass implements Serializable,Parcelable {
    public String key;
    public String valye;

    protected nodeClass(Parcel in) {
        key = in.readString();
        valye = in.readString();
    }

    public static final Creator<nodeClass> CREATOR = new Creator<nodeClass>() {
        @Override
        public nodeClass createFromParcel(Parcel in) {
            return new nodeClass(in);
        }

        @Override
        public nodeClass[] newArray(int size) {
            return new nodeClass[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValye() {
        return valye;
    }

    public void setValye(String valye) {
        this.valye = valye;
    }

    public nodeClass(String key, String valye) {
        this.key = key;
        this.valye = valye;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(key);
        parcel.writeString(valye);
    }
}
