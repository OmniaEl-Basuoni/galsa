package com.example.omnia.ourproject.Doctor.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.example.omnia.ourproject.Doctor.Classes.DoctorClass;
import com.example.omnia.ourproject.R;
import com.example.omnia.ourproject.SharedClasses.RequestsAdapter;
import com.example.omnia.ourproject.SharedClasses.RequestsClass;
import com.example.omnia.ourproject.SharedClasses.SharedParameters;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DoctorRequestsActivity extends AppCompatActivity {

    private DoctorClass aClass;
    public static ListView listViewRequests;
    private RequestsAdapter requestsAdapter;
    public static List<RequestsClass> requestsClassList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_doctor_requests);

        Init();
        SetData();
        FillRequestsList();

    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void Init()
    {
        listViewRequests=(ListView) findViewById(R.id.LV_Requests);
        requestsClassList.clear();
    }

    public void FillRequestsList() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Requests").child(SharedParameters.DoctorUid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        requestsClassList.add(new RequestsClass(issue.child("Patient ID").getValue() + ""
                                , issue.child("Patient Name").getValue() + ""
                        ));
                    }
                } else {
                    Toast.makeText(DoctorRequestsActivity.this, "Not places in this Category", Toast.LENGTH_LONG).show();
                }
                requestsAdapter = new RequestsAdapter(DoctorRequestsActivity.this, requestsClassList, aClass);
                listViewRequests.setAdapter(requestsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

        private void SetData() {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            aClass = (DoctorClass) extras.getSerializable("DoctorClass");
            if (aClass != null) {


            } else {
            }
        }
    }





}
