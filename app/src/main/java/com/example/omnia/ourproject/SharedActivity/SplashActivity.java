package com.example.omnia.ourproject.SharedActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.omnia.ourproject.Doctor.Activities.DoctorHomeActivity;
import com.example.omnia.ourproject.Doctor.Classes.DoctorClass;
import com.example.omnia.ourproject.Patient.Activities.PatientHomeActivity;
import com.example.omnia.ourproject.Patient.Classes.PatientClass;
import com.example.omnia.ourproject.R;
import com.example.omnia.ourproject.SharedClasses.SharedParameters;
import com.example.omnia.ourproject.VideoChat.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends BaseActivity {
    private SharedPreferences prefs;
    private DoctorClass Doctor;
    private PatientClass Patient;
    private Query query;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_shared_splash);
        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        user=FirebaseAuth.getInstance().getCurrentUser();


        v();

        IsPreviousStarted();


    }



    //region SharedPreference
    private void IsPreviousStarted()
    {
        boolean previouslyStarted = prefs.getBoolean("State", false);
        if(!previouslyStarted) {

            new CountDownTimer(5000, 1000) {

                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    Intent mainActivity = new Intent(SplashActivity.this, AppIntoActivity.class);
                    startActivity(mainActivity);
                    finish();
                }
            }.start();
        }
        else {
            IsLogined();
        }
    }

    private void IsLogined()
    {
        Gson gson = new Gson();
        String json = prefs.getString("Object", "");
        String Role=prefs.getString("Role","");
        if (!(user==null))
        {
            if(Role.equals("Doctor"))
            {
                //region Doctor
                Doctor = gson.fromJson(json, DoctorClass.class);
                SharedParameters.DoctorUid =user.getUid();
                SharedParameters.DoctorName=Doctor.DoctorName;
                Intent intent=new Intent(SplashActivity.this, DoctorHomeActivity.class);
                intent.putExtra("DoctorClass",Doctor);
                startActivity(intent);
                finish();
                //endregion
            }
            else if (Role.equals("Client"))
            {
                //region Patient
                Patient = gson.fromJson(json, PatientClass.class);
                SharedParameters.PatientUid =user.getUid();
                SharedParameters.PatientName=Patient.PatientName;
                Intent intent=new Intent(SplashActivity.this, PatientHomeActivity.class);
                intent.putExtra("PatientClass", Patient);
                startActivity(intent);
                finish();
                //endregion
            }
            else {
                new CountDownTimer(3000, 1000) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        Intent mainActivity = new Intent(SplashActivity.this, StartActivity.class);
                        startActivity(mainActivity);
                        finish();
                    }
                }.start();
            }
        }
        else {
            Intent mainActivity = new Intent(this, StartActivity.class);
            startActivity(mainActivity);
            finish();
        }
    }
    //endregion


    private void v()
    {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e) {

        }
        catch (NoSuchAlgorithmException e) {

        }
    }

}
