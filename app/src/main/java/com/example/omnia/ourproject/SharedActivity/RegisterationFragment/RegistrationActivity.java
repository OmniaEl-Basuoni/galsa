package com.example.omnia.ourproject.SharedActivity.RegisterationFragment;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.omnia.ourproject.Doctor.DashboardFragments.PatientsFragment;
import com.example.omnia.ourproject.R;
import com.example.omnia.ourproject.SharedActivity.StartActivity;

public class RegistrationActivity extends AppCompatActivity {
    private FragmentManager fragmentManager ;
    private Fragment fragment;
    private String Uid,Username,Email;
    private FrameLayout frameLayout;
    private LinearLayout linearLayout;
    private RelativeLayout RL_Doctor,RL_patient;
    private boolean WithAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);
        InitComponent();

        CheckPermission();

        RL_Doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                if (WithAPI)
                {
                    fragment=new DoctorRegesterFragment(WithAPI,Username,Email,Uid);
                }
                else {
                    fragment=new DoctorRegesterFragment(WithAPI);
                }
                fragmentManager.beginTransaction().replace(R.id.content, fragment)
                        .commit();
            }
        });

        RL_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                if (WithAPI)
                {
                    fragment=new PatientRegisterFragment(WithAPI,Username,Email,Uid);
                }
                else {
                    fragment=new PatientRegisterFragment(WithAPI);
                }
                fragmentManager.beginTransaction().replace(R.id.content, fragment)
                        .commit();
            }
        });

    }
    private void InitComponent()
    {
        getIntentData();
        fragmentManager = getFragmentManager();
        frameLayout=findViewById(R.id.content);
        linearLayout=findViewById(R.id.LL_Role);
        RL_Doctor=findViewById(R.id.RL_Doctor);
        RL_patient=findViewById(R.id.RL_patient);
    }

    @Override
    public void onBackPressed() {
        if(linearLayout.getVisibility()==View.GONE)
        {
            linearLayout.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
        }
        else {
            startActivity(new Intent(this,StartActivity.class));
            finish();
        }
    }

    private void getIntentData()
    {
        Bundle extBundle=getIntent().getExtras();
        if(!(extBundle.isEmpty()))
        {
            WithAPI=extBundle.getBoolean("withAPI");
            if(WithAPI)
            {
                Uid = extBundle.getString("Uid");
                Username=extBundle.getString("Name");
                Email=extBundle.getString("Email");
            }
            else {
                Uid = extBundle.getString("Uid");
            }
        }
    }


    //region CheckPermissionRegion
    private void CheckPermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.INTERNET,
                    Manifest.permission.READ_CALENDAR,
                    Manifest.permission.WRITE_CALENDAR,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.MODIFY_AUDIO_SETTINGS,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_NETWORK_STATE}, 0);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {

                    /*// permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "No", Toast.LENGTH_SHORT).show();*/

                }
                return;
            }
        }
    }
    //endregion

}
