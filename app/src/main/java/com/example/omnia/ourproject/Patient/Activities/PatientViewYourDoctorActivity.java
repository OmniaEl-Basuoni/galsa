package com.example.omnia.ourproject.Patient.Activities;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.widget.Toast;

import com.example.omnia.ourproject.Doctor.Adapters.DoctorInfoAdapter;
import com.example.omnia.ourproject.Doctor.Adapters.SeeMoreAdapter;
import com.example.omnia.ourproject.Doctor.Classes.DoctorClass;
import com.example.omnia.ourproject.Patient.Classes.PatientClass;
import com.example.omnia.ourproject.R;
import com.example.omnia.ourproject.SharedClasses.GridSpacingItemDecoration;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PatientViewYourDoctorActivity extends AppCompatActivity {
    private String myDoctorsIds;
    private PatientClass aClass;
    private RecyclerView recyclerViewDoctor;
    private SeeMoreAdapter doctorInfoAdapter;
    private List<DoctorClass> doctorClassList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_your_doctor);
        InitComponent();
        getData();
        ReturnDefultDoctor(myDoctorsIds);
    }

    private void getData() {
        Bundle bundle=getIntent().getExtras();
        if (!bundle.isEmpty())
        {
            aClass= (PatientClass) bundle.getSerializable("ClassPatient");
            myDoctorsIds=bundle.getString("myDoctorsIds");
        }
    }

    private void InitComponent() {
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

        recyclerViewDoctor = (RecyclerView) findViewById(R.id.recycler_view);
    }

    private void ReturnDefultDoctor(final String ids) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Doctor").orderByChild("DoctorRate");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    try {
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                            if(ids.contains(issue.getKey())) {
                                doctorClassList.add(new DoctorClass(issue.child("PersonalPhoto").getValue() + ""
                                        , issue.child("DoctorName").getValue() + ""
                                        , Double.parseDouble(issue.child("DoctorRate").getValue() + "")
                                        , getCategory(issue.child("Category").getValue()+"")
                                        , issue.getKey()
                                ));
                            }
                        }
                    }
                    catch (Exception Ex)
                    {
                        Toast.makeText(PatientViewYourDoctorActivity.this, Ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(PatientViewYourDoctorActivity.this, "Not places in this Category", Toast.LENGTH_LONG).show();
                }
                Collections.reverse(doctorClassList);

                    doctorInfoAdapter = new SeeMoreAdapter(PatientViewYourDoctorActivity.this, doctorClassList,aClass);
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(PatientViewYourDoctorActivity.this, 1);
                    recyclerViewDoctor.setLayoutManager(mLayoutManager);
                    recyclerViewDoctor.addItemDecoration(new GridSpacingItemDecoration(1, 3, true,PatientViewYourDoctorActivity.this));
                    recyclerViewDoctor.setItemAnimator(new DefaultItemAnimator());
                    recyclerViewDoctor.setAdapter(doctorInfoAdapter);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    private List<String> getCategory(String s)
    {
        List<String> stringList=new ArrayList<>();
        String [] ar=s.replace("[","").replace("]","").split(",");
        for(String cat:ar)
        {
            stringList.add(cat);
        }
        return stringList;
    }
}
