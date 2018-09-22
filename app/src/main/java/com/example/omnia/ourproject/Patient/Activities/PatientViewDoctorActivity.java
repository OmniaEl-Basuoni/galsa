package com.example.omnia.ourproject.Patient.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omnia.ourproject.Doctor.Activities.DoctorRequestsActivity;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientViewDoctorActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseDatabase firebasedatabase;
    private Button requests;
    private String DoctorId, PatientId, PatientName;


    private CircleImageView PersonalPhoto;
    private RatingBar Rate;

    ;
    private String doctorName, doctorCategory, doctorCountry, doctorMail,
            doctorPhone, doctorUniversity, doctorCertification, doctorPrice1, doctorPrice2,
            doctorRate, doctorPhoto, doctorGender, doctorAge;


    private TextView textViewName, textViewCategory, textViewCountry, textViewMail,
            textViewPhone, textViewUniversity, textViewCertification, textViewPrice1, textViewPrice2, textViewGender, textViewAge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_patient_view_doctor);

        Init();
        GetDataIntent();
        ReadData();

        Check();
        //region MakeRequest
        requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MakeRequest();
            }
        });
        //endregion

    }

    private void Init() {
        textViewGender = findViewById(R.id.gender);
        textViewAge = findViewById(R.id.age);
        textViewPhone = findViewById(R.id.phone);
        textViewUniversity = findViewById(R.id.university);
        textViewCertification = findViewById(R.id.certification);
        textViewPrice1 = findViewById(R.id.price1);
        textViewPrice2 = findViewById(R.id.price2);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        requests = findViewById(R.id.BT_requests);
        PersonalPhoto = findViewById(R.id.Photo);
        Rate = findViewById(R.id.RB_rate);
        textViewName = findViewById(R.id.name);
        textViewCategory = findViewById(R.id.category);
        textViewCountry = findViewById(R.id.country);
        textViewMail = findViewById(R.id.mail);
    }

    private void MakeRequest() {
        firebasedatabase = FirebaseDatabase.getInstance();
        mDatabase = firebasedatabase.getReferenceFromUrl
                ("https://jalsa-e9ec7.firebaseio.com/Requests/" + DoctorId + "");

        DatabaseReference id = mDatabase.push();
        id.child("Patient ID").setValue(PatientId);
        id.child("Patient Name").setValue(PatientName);
        id.child("Request Time").setValue(new Date().getTime());

        SharedParameters.SendNotification("New Request",
                "Request From "+PatientName,DoctorId);

    }

    private void GetDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (!bundle.isEmpty()) {
            PatientName = bundle.getString("Patient Name");
            PatientId = bundle.getString("Patient ID");
            DoctorId = bundle.getString("Doctor ID");
        }
    }

    private void Check() {
        final boolean[] b = {true};
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Requests").child(DoctorId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        if (issue.child("Patient ID").getValue().equals(PatientId)) {
                            b[0] = false;
                            break;
                        }
                    }
                }
                if (b[0]) {
                    CheckComplete();
                } else {
                    requests.setText("Response");
                    requests.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void ReadData() {

        Query query = FirebaseDatabase.getInstance().getReference().child("Doctor").child(DoctorId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    doctorGender = dataSnapshot.child("DoctorGender").getValue() + "";
                    doctorAge = dataSnapshot.child("DoctorBirth").getValue() + "";
                    doctorPhoto = dataSnapshot.child("PersonalPhoto").getValue() + "";
                    doctorRate = dataSnapshot.child("DoctorRate").getValue() + "";
                    doctorName = dataSnapshot.child("DoctorName").getValue() + "";
                    doctorMail = dataSnapshot.child("DoctorEmail").getValue() + "";
                    doctorPhone = dataSnapshot.child("DoctorPhone").getValue() + "";
                    doctorCountry = dataSnapshot.child("Doctor Country").getValue() + "";
                    doctorUniversity = dataSnapshot.child("Doctor University").getValue() + "";
                    doctorPrice1 = "" + dataSnapshot.child("Doctor PriceHalf").getValue();
                    doctorPrice2 = "" + dataSnapshot.child("Doctor PriceHour").getValue();
                    doctorCategory = "" + dataSnapshot.child("Category").getValue();
                    doctorCertification = "" + dataSnapshot.child("Doctor Certification").getValue();
                }
                fillData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fillData() {
        Rate.setRating(Float.parseFloat(doctorRate));
        textViewName.setText(doctorName);
        textViewCategory.setText(doctorCategory.replace("[","").replace("]",""));
        textViewCountry.setText(doctorCountry);
        textViewMail.setText(doctorMail);
        textViewPhone.setText(doctorPhone);
        textViewUniversity.setText(doctorUniversity);
        textViewCertification.setText(doctorCertification.replace("[","").replace("]",""));
        textViewPrice1.setText("Price for 30m : "+doctorPrice1);
        textViewPrice2.setText("Price for 60m : "+doctorPrice2);
        textViewGender.setText(doctorGender);
        textViewAge.setText(doctorAge);
    }

    private void CheckComplete()
    {
        final boolean[] b = {true};
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Doctor_Patient").child(DoctorId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        if (issue.child("Patient ID").getValue().equals(PatientId)) {
                            b[0] = false;
                            break;
                        }
                    }
                }
                if (b[0]) {
                    requests.setText("Requests");
                    requests.setEnabled(true);
                } else {
                    requests.setText("You are Related");
                    requests.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
