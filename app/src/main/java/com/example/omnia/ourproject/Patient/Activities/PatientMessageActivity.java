package com.example.omnia.ourproject.Patient.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.omnia.ourproject.Doctor.Activities.DoctorChatActivity;
import com.example.omnia.ourproject.Doctor.Activities.DoctorMessageActivity;
import com.example.omnia.ourproject.Doctor.Adapters.DoctorMessagesAdapter;
import com.example.omnia.ourproject.Doctor.Classes.DoctorClass;
import com.example.omnia.ourproject.Patient.Adapters.PatientMessagesAdapter;
import com.example.omnia.ourproject.Patient.Classes.PatientClass;
import com.example.omnia.ourproject.R;
import com.example.omnia.ourproject.SharedClasses.DoctorPatientMessagesClass;
import com.example.omnia.ourproject.SharedClasses.SharedParameters;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PatientMessageActivity extends AppCompatActivity {

    private PatientClass aClass;
    private List<DoctorPatientMessagesClass> doctorPatientMessagesClassList =new ArrayList<>();
    private PatientMessagesAdapter patientMessagesAdapter;
    private ListView doctorMessagelistView;
    private DoctorPatientMessagesClass MessageClassObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_patient_message);
        Init();


        DoctorPersonalData();










        try {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query = reference.child("MessagesBetweenDoctorPatient")
                    .orderByChild("lastMessageTime");
            query.keepSynced(true);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    doctorPatientMessagesClassList.clear();
                    if (dataSnapshot.exists()) {
                        try {
                            for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                if (issue.child("patientUid").getValue().equals(aClass.PatientID))
                                {
                                    doctorPatientMessagesClassList.add(new DoctorPatientMessagesClass(
                                            issue.child("doctorUid").getValue()+"",
                                            issue.child("doctorName").getValue()+"",
                                            issue.child("doctorPhoto").getValue()+"",
                                            issue.child("patientUid").getValue()+"",
                                            issue.child("patientName").getValue()+"",
                                            issue.getKey()+"",
                                            issue.child("lastMessageText").getValue()+"",
                                            (long)issue.child("lastMessageTime").getValue())
                                    );
                                }
                            }
                        }
                        catch (Exception Ex)
                        {
                            Toast.makeText(PatientMessageActivity.this, Ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(PatientMessageActivity.this, "Not places in this Category", Toast.LENGTH_LONG).show();
                    }


                    Collections.sort(doctorPatientMessagesClassList, new Comparator<DoctorPatientMessagesClass>(){

                        @Override
                        public int compare(DoctorPatientMessagesClass doctorPatientMessagesClass, DoctorPatientMessagesClass t1) {

                            return (doctorPatientMessagesClass.getLastMessageTime() > t1.getLastMessageTime())
                                    ? -1: (doctorPatientMessagesClass.getLastMessageTime() > t1.getLastMessageTime())
                                    ? 1:0 ;
                        }
                    });

                    patientMessagesAdapter=new PatientMessagesAdapter(PatientMessageActivity.this, doctorPatientMessagesClassList);

                    doctorMessagelistView.setAdapter(patientMessagesAdapter);



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }catch (Exception e)
        {
            Log.d("Error",e.getMessage());
        }




        doctorMessagelistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageClassObject= (DoctorPatientMessagesClass) parent.getItemAtPosition(position);
                Intent PatientChatIntent=new Intent(PatientMessageActivity.this
                        ,PatientChatActivity.class);
                PatientChatIntent.putExtra("ChatClass", MessageClassObject);

                startActivity(PatientChatIntent);
            }
        });


    }

    private void Init()
    {
        //region ImageLoader
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);
        //endregion
        doctorMessagelistView=findViewById(R.id.LV_PatientMessage);
    }


    private void DoctorPersonalData()
    {

        Bundle extras = getIntent().getExtras();
        if ((!extras.isEmpty())) {
            aClass= (PatientClass) extras.getSerializable("PatientClass");
        }
        else {
            Toast.makeText(this, "Here", Toast.LENGTH_SHORT).show();
        }

    }
}
