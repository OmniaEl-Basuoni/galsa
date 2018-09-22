package com.example.omnia.ourproject.Doctor.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.example.omnia.ourproject.Doctor.Adapters.DoctorMessagesAdapter;
import com.example.omnia.ourproject.Doctor.Classes.DoctorClass;
import com.example.omnia.ourproject.Patient.Activities.PatientChatActivity;
import com.example.omnia.ourproject.Patient.Activities.PatientMessageActivity;
import com.example.omnia.ourproject.Patient.Adapters.PatientMessagesAdapter;
import com.example.omnia.ourproject.SharedClasses.ChatMessage;
import com.example.omnia.ourproject.SharedClasses.DoctorPatientMessagesClass;
import com.example.omnia.ourproject.R;
import com.example.omnia.ourproject.SharedClasses.SharedParameters;
import com.example.omnia.ourproject.VideoChat.BaseActivity;
import com.example.omnia.ourproject.VideoChat.SinchService;
import com.firebase.ui.database.FirebaseListAdapter;
import com.github.pavlospt.roundedletterview.RoundedLetterView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sinch.android.rtc.SinchError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DoctorMessageActivity extends BaseActivity {

    private DoctorPatientMessagesClass MessageClassObject;
    private DoctorClass aClass;
    private List<DoctorPatientMessagesClass> doctorPatientMessagesClassList =new ArrayList<>();
    private DoctorMessagesAdapter doctorMessagesAdapter;
    private ListView doctorMessagelistView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_doctor_message);
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
                                if (issue.child("doctorUid").getValue().equals(aClass.DoctorID))
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
                            Toast.makeText(DoctorMessageActivity.this, Ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(DoctorMessageActivity.this, "Not places in this Category", Toast.LENGTH_LONG).show();
                    }


                    Collections.sort(doctorPatientMessagesClassList, new Comparator<DoctorPatientMessagesClass>(){

                        @Override
                        public int compare(DoctorPatientMessagesClass doctorPatientMessagesClass, DoctorPatientMessagesClass t1) {

                            return (doctorPatientMessagesClass.getLastMessageTime() > t1.getLastMessageTime())
                                    ? -1: (doctorPatientMessagesClass.getLastMessageTime() > t1.getLastMessageTime())
                                    ? 1:0 ;
                        }
                    });

                    doctorMessagesAdapter=new DoctorMessagesAdapter(DoctorMessageActivity.this,
                            doctorPatientMessagesClassList);
                    doctorMessagelistView.setAdapter(doctorMessagesAdapter);
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
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                MessageClassObject= (DoctorPatientMessagesClass) adapterView.getItemAtPosition(i);
                Intent DoctorChatIntent=new Intent(DoctorMessageActivity.this
                        ,DoctorChatActivity.class);
                DoctorChatIntent.putExtra("ChatClass", MessageClassObject);
                DoctorChatIntent.putExtra("toResult",false);
                startActivity(DoctorChatIntent);
            }
        });


    }
    private void Init()
    {
        doctorMessagelistView=findViewById(R.id.LV_DoctorMessage);
    }


    private void DoctorPersonalData()
    {

        Bundle extras = getIntent().getExtras();
        if ((!extras.isEmpty())) {
            aClass= (DoctorClass) extras.getSerializable("DoctorClass");
        }
        else {
            Toast.makeText(this, "Here", Toast.LENGTH_SHORT).show();
        }

    }



}
