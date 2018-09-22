package com.example.omnia.ourproject.Patient.Activities;

import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omnia.ourproject.Patient.Adapters.JalsatAdapter;
import com.example.omnia.ourproject.Patient.Classes.PatientClass;
import com.example.omnia.ourproject.Patient.Classes.nodeClass;
import com.example.omnia.ourproject.SharedClasses.JalsatClass;
import com.example.omnia.ourproject.R;
import com.example.omnia.ourproject.SharedClasses.GridSpacingItemDecoration;
import com.github.pavlospt.roundedletterview.RoundedLetterView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.omnia.ourproject.R.drawable.ic_save;

public class PatientGeneralInfo extends AppCompatActivity {

    private CircleImageView imageView;
private String PatientId;
    private PatientClass aClass;
    private String doctorId;
    private ArrayList <nodeClass> Patientnotes=new ArrayList<>();
private ArrayList <nodeClass> medicine=new ArrayList<>();
    private JalsatAdapter jalsatAdapter;
    private List<JalsatClass> jalsatClassList = new ArrayList<>();
    private RecyclerView recyclerViewJalsat;
    private RoundedLetterView roundedLetterView;
    private TextView textViewname,textViewage;
    private EditText editTextstaus;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_patient_general_info);
        Init();
        jk();
        FillJalsa();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextstaus.isEnabled())
                {
                    SaveData(editTextstaus.getText()+"");
                    fab.setImageResource(R.drawable.ic_edit);
                    editTextstaus.setEnabled(false);
                }
                else{
                    fab.setImageResource(R.drawable.ic_save);
                    editTextstaus.setEnabled(true);
                }
            }
        });
    }

    private void SaveData(String s) {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Patient").child(PatientId).child("PatientStatus").setValue(s);
    }

    private void jk()
    {
        Bundle bundle=getIntent().getExtras();
        if(!(bundle.isEmpty()))
        {
            PatientId=bundle.getString("Patient ID");
            doctorId=bundle.getString("Doctor ID");
            if(bundle.getBoolean("Tag"))
            {
                PatientPersonalData();
            }
            else {
                DoctorPersonalData();
            }
        }
    }

    private void Init() {
        imageView=findViewById(R.id.imageView);
        recyclerViewJalsat = (RecyclerView) findViewById(R.id.RV_galsat);
        roundedLetterView = findViewById(R.id.rlv_name_view);
        textViewname=(TextView)findViewById(R.id.TV_PatientName) ;
        textViewage=(TextView)findViewById(R.id.TV_PatientAge) ;
        editTextstaus=(EditText)findViewById(R.id.et_PatientStatus) ;
        fab=(FloatingActionButton)findViewById(R.id.edit1);
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
    }


    private void FillJalsa()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Jalsat").child(doctorId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if (PatientId.equals(dataSnapshot1.getKey())) {
                            for (DataSnapshot issue:dataSnapshot1.getChildren())
                            {

                                jalsatClassList.add(new JalsatClass(
                                        issue.getKey(),
                                        ""+issue.child("Notes").getValue(),
                                       Long.parseLong(""+issue.child("Date").getValue()),
                                        ""+issue.child("Medicine").getValue()));
                            }
                        }
                    }

                }

                jalsatAdapter = new JalsatAdapter(PatientGeneralInfo.this, jalsatClassList);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(PatientGeneralInfo.this, 3);
                recyclerViewJalsat.setLayoutManager(mLayoutManager);
                GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(3, 10, true, PatientGeneralInfo.this);
                recyclerViewJalsat.addItemDecoration(gridSpacingItemDecoration);
                recyclerViewJalsat.setItemAnimator(new DefaultItemAnimator());
                recyclerViewJalsat.setAdapter(jalsatAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void PatientPersonalData()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query=reference.child("Patient").child(PatientId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    textViewname.setText( dataSnapshot.child("PatientName").getValue().toString());
                    textViewage.setText( dataSnapshot.child("PatientBirth").getValue().toString());
                    editTextstaus.setText(dataSnapshot.child("PatientStatus").getValue()+"");
                    roundedLetterView.setTitleText(String.valueOf(textViewname.getText().toString().charAt(0)).toUpperCase());
                }else
                {
                    Toast.makeText(PatientGeneralInfo.this, "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        imageView.setVisibility(View.GONE);
    }

    private void DoctorPersonalData()
    {
        final String[] s = new String[1];
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query=reference.child("Doctor").child(doctorId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    textViewname.setText( dataSnapshot.child("DoctorName").getValue().toString());
                    s[0] =dataSnapshot.child("PersonalPhoto").getValue().toString();
                }
                ImageLoader.getInstance().displayImage(s[0], imageView, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        roundedLetterView.setVisibility(View.GONE);
        editTextstaus.setVisibility(View.GONE);
        textViewage.setVisibility(View.GONE);
        fab.setVisibility(View.GONE);

    }

}
