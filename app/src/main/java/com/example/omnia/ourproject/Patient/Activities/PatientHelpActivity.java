package com.example.omnia.ourproject.Patient.Activities;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

;
import com.example.omnia.ourproject.Doctor.Classes.PatientDate;
import com.example.omnia.ourproject.Patient.Adapters.DoctorDateAdapter;
import com.example.omnia.ourproject.Patient.Adapters.HelpAdapter;
import com.example.omnia.ourproject.Patient.Classes.HospitalInfo;
import com.example.omnia.ourproject.R;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;


public class PatientHelpActivity extends AppCompatActivity {

    private RelativeLayout Country,Egypt,Saudia,Emirates,Amman,America;
    ListView listView;
    HelpAdapter helpAdapter;
    private List<HospitalInfo> hospitalInfoList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_help);

        Init();

        //region RelativeLayout
        Egypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnInfo("Egypt");
            }
        });
        Saudia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnInfo("Saudi Arabia");
            }
        });
        Emirates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnInfo("United Arab Emirates");
            }
        });
        America.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Amman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnInfo("Amman");
            }
        });
        //endregion

    }


    public void Init() {
        Egypt=findViewById(R.id.relativeLayoutEgypt);
        Saudia=findViewById(R.id.relativeLayoutSaudi);
        Emirates=findViewById(R.id.relativeLayoutEmerates);
        Amman=findViewById(R.id.relativeLayoutAmman);
        America=findViewById(R.id.relativeLayoutUs);
        Country=findViewById(R.id.Country);
        listView=findViewById(R.id.list);
    }

    private  void  returnInfo(String s){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Help").child(s);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                                hospitalInfoList.add(new HospitalInfo(
                                        ""+dataSnapshot1.child("Hospital Name").getValue(),
                                        dataSnapshot1.child("Phone").getValue()+"",
                                        dataSnapshot1.child("Address").getValue()+""
                                       ));

                    }
                }
                if (hospitalInfoList.size()>0)
                {
                    Country.setVisibility(View.GONE);
                    helpAdapter = new HelpAdapter(PatientHelpActivity.this,hospitalInfoList);
                    listView.setAdapter(helpAdapter);
                }
                else {
                    Toast.makeText(PatientHelpActivity.this, "No Places", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


    @Override
    public void onBackPressed() {
        if (Country.getVisibility()==View.GONE)
        {
            Country.setVisibility(View.VISIBLE);
        }
        else
        {
            super.onBackPressed();
        }
    }
}




