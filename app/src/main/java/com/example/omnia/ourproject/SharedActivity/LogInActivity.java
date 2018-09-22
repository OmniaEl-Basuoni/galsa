package com.example.omnia.ourproject.SharedActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.omnia.ourproject.Doctor.Activities.DoctorHomeActivity;
import com.example.omnia.ourproject.Doctor.Classes.DoctorClass;
import com.example.omnia.ourproject.Patient.Activities.PatientHomeActivity;
import com.example.omnia.ourproject.Patient.Classes.PatientClass;
import com.example.omnia.ourproject.R;
import com.example.omnia.ourproject.SharedClasses.SharedParameters;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class LogInActivity extends AppCompatActivity {
    private DoctorClass Doctor;
    private PatientClass Patient;
    private String TAG="eyH";
    DatabaseReference reference;
    private FirebaseAuth mAuth;
    private EditText editTextEmail, editTextPassword;
    private RelativeLayout relativeLayoutDoctor, relativeLayoutPatient;
    private TextInputLayout textInputLayoutEmail, textInputLayoutPassword;
    private Button buttonLogin;
    private String Flag = "Patient";
    private boolean Found ;
    private String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private ProgressDialog dialog;
    boolean Search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_shared_log_in);

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.setMessage("Please wait...");


        Init();


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ValidEmail()&&ValidPassword())
               SignIn();
            }
        });

    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,StartActivity.class));
        finish();
    }

    private void Init()
    {
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        buttonLogin = (Button) findViewById(R.id.BT_login);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.TIL_Email);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.TIL_Password);
        editTextEmail = (EditText) findViewById(R.id.ET_Email);
        editTextPassword = (EditText) findViewById(R.id.ET_Password);
    }


    private boolean ValidEmail()
    {
        boolean isValid=true;
        if(editTextEmail.getText().toString().isEmpty())
        {
            textInputLayoutEmail.setError("This field is required");
            isValid=false;
        }
        else if ((!editTextEmail.getText().toString().trim().matches(emailPattern))&&isValid)
        {
            textInputLayoutEmail.setError("This email address is invalid");
            isValid=false;
        }
        else {
            textInputLayoutEmail.setErrorEnabled(false);
            isValid=true;
        }
        return isValid;
    }
    private boolean ValidPassword()
    {
        boolean isValid;
        if(editTextPassword.getText().toString().length()<5)
        {
            textInputLayoutPassword.setError("This field must more than 5 ");
            isValid=false;
        }

        else {
            textInputLayoutPassword.setErrorEnabled(false);
            isValid=true;
        }
        return isValid;
    }


    private void SignIn()
    {
        dialog.show();
        mAuth.signInWithEmailAndPassword(editTextEmail.getText()+"", editTextPassword.getText()+"")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user.isEmailVerified())
                            {
                                Search=true;
                                Search("Patient",user);
                            }
                            else {
                                dialog.dismiss();
                                Toast.makeText(LogInActivity.this, "Pls Valid Email", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            dialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LogInActivity.this, "Authentication failed."+"\n"+task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
    private void Search(final String Category, final FirebaseUser user)
    {
        final boolean[] Fla = {true};
        final Query[] query = {reference.child(Category)};
        query[0].addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot info : dataSnapshot.getChildren()) {
                        if (info.getKey().equals(user.getUid())) {
                            if(Category.equals("Doctor"))
                            {
                                SharedParameters.DoctorName=info.child("DoctorName").getValue()+"";
                                Doctor=new DoctorClass(
                                        ""+info.getKey(),
                                        ""+info.child("DoctorBirth").getValue(),
                                        ""+info.child("DoctorEmail").getValue(),
                                        ""+info.child("DoctorGender").getValue(),
                                        ""+info.child("DoctorName").getValue(),
                                        ""+info.child("DoctorNationalID").getValue(),
                                        ""+info.child("DoctorPhone").getValue(),
                                        ""+info.child("PersonalPhoto").getValue(),
                                        ""+info.child("IDPhoto").getValue(),
                                        Double.parseDouble(""+info.child("DoctorRate").getValue())
                                        );
                            }
                            else {
                                SharedParameters.PatientName=info.child("PatientName").getValue()+"";
                                Patient=new PatientClass(
                                        ""+info.getKey(),
                                        ""+info.child("PatientName").getValue(),
                                        ""+info.child("PatientEmail").getValue(),
                                        "",
                                        ""+info.child("PatientPhone").getValue(),
                                        ""+info.child("PatientNationalID").getValue(),
                                        ""+info.child("PatientGender").getValue(),
                                        ""+info.child("PatientBirth").getValue()
                                );
                            }
                            Fla[0] = false;
                            break;
                        }
                    }
                }
                if (!Fla[0]) {
                    if(Search)
                    {
                        //region SharedPreferences
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        SharedPreferences.Editor edit = prefs.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(Patient);
                        edit.putString("Object",json);
                        edit.putString("Role", "Client");
                        edit.commit();
                        //endregion

                        SharedParameters.PatientUid =user.getUid();
                        dialog.dismiss();
                        Intent intent=new Intent(LogInActivity.this, PatientHomeActivity.class);
                        intent.putExtra("PatientClass", Patient);
                        startActivity(intent);
                        finish();
                    }
                    else {

                        //region SharedPreferences
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        SharedPreferences.Editor edit = prefs.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(Doctor);
                        edit.putString("Object",json);
                        edit.putString("Role", "Doctor");
                        edit.commit();
                        //endregion

                        SharedParameters.DoctorUid =user.getUid();
                        Intent intent=new Intent(LogInActivity.this, DoctorHomeActivity.class);
                        intent.putExtra("DoctorClass",Doctor);
                        dialog.dismiss();
                        startActivity(intent);
                        finish();
                    }

                } else {
                    if(Search)
                    {
                        Search("Doctor",user);
                        Search=false;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
