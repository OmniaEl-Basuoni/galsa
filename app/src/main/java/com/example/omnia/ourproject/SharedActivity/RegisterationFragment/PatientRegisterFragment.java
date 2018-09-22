package com.example.omnia.ourproject.SharedActivity.RegisterationFragment;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omnia.ourproject.AutoCompleteCountry.Adapter.CountryAdapter;
import com.example.omnia.ourproject.AutoCompleteCountry.Class.CountriesClass;
import com.example.omnia.ourproject.AutoCompleteCountry.Class.Country;
import com.example.omnia.ourproject.AutoCompleteCountry.Remote.ApiUtlis;
import com.example.omnia.ourproject.AutoCompleteCountry.Remote.UserService;
import com.example.omnia.ourproject.Patient.Classes.PatientClass;
import com.example.omnia.ourproject.R;
import com.example.omnia.ourproject.SharedActivity.StartActivity;
import com.example.omnia.ourproject.SharedClasses.SharedParameters;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@SuppressLint("ValidFragment")
public class PatientRegisterFragment extends Fragment 
        implements DatePickerDialog.OnDateSetListener {

    private CountryAdapter countryAdapter;
    private ArrayList<Country> countryList=new ArrayList<>();
    private AutoCompleteTextView completeTextView;
    private UserService userService;

    private PatientClass Patient,patientClass;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private RadioButton radioButtonMale,radioButtonFemale;
    private String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private CountryCodePicker ccp;
    private EditText Username, Password, Retry_Password, Email, NationalID, editTextCarrierNumber;
    private TextView textViewBirthdate,textViewShowBirth,textViewPhotoPath,textViewCVPath;
    private TextInputLayout textInputLayoutUsername, textInputLayoutPassword,
            textInputLayoutRetryPassword, textInputLayoutEmail, textInputLayoutNatiobalID;
    private Button buttonRegister;
    private boolean WithApi;
    private String Uid,GenderFlag,stringUsername,stringEmail;
    private View view;
    

    @SuppressLint("ValidFragment")
    public PatientRegisterFragment(boolean withApi) {
        this.WithApi=withApi;
    }
    @SuppressLint("ValidFragment")
    public PatientRegisterFragment(boolean withApi, String Username, String Email,String Uid)
    {
        this.WithApi=withApi;
        this.stringUsername=Username;
        this.stringEmail=Email;
        this.Uid=Uid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_patient_register, container, false);
        InitComponent();
        AutoCountry();

        //region SetDataFromAPI
        if(WithApi)
        {
            Username.setText(stringUsername);
            Email.setText(stringEmail);
            Password.setText("**********");
            Retry_Password.setText("**********");
        }
        //endregion

        //region RadioButton
        radioButtonMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    GenderFlag= SharedParameters.Gender.Male+"";
                }
                else {
                    GenderFlag=SharedParameters.Gender.Female+"";
                }
            }
        });

        radioButtonFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked)
                {
                    GenderFlag=SharedParameters.Gender.Male+"";
                }
                else {
                    GenderFlag=SharedParameters.Gender.Female+"";
                }
            }
        });
        //endregion

        
        //region Birthday
        textViewBirthdate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar now=Calendar.getInstance();
                DatePickerDialog datePickerDialog= DatePickerDialog.newInstance(PatientRegisterFragment.this
                        ,now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setTitle("DatePicker");
                datePickerDialog.show(getFragmentManager(),"DatePicker");
            }
        });
        //endregion


        //Button Register
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidUser() && ValidPassword() && ValidTryPassword() && ValidEmail() && ValidID() && ValidGender() && ValidBirthdate() && ValidPhone()) {
                    if (WithApi) {
                        PatientRegisterWithAPI();
                    } else {
                        PatientRegister();
                    }
                }
            }         
        });
        
        return view;
    }

    private void InitComponent() {


        completeTextView=view.findViewById(R.id.ET_Country);
        userService= ApiUtlis.getUserService();

        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("Uploading...");
        
        radioButtonMale=(RadioButton)view.findViewById(R.id.RB_Male);
        radioButtonFemale=(RadioButton)view.findViewById(R.id.RB_Female);

        Username = (EditText) view.findViewById(R.id.ET_Username);
        Password = (EditText) view.findViewById(R.id.ET_Password);
        Retry_Password = (EditText) view.findViewById(R.id.ET_Password1);
        Email = (EditText) view.findViewById(R.id.ET_Email);
        NationalID = (EditText) view.findViewById(R.id.ET_ID);
        textViewBirthdate = (TextView) view.findViewById(R.id.TV_Birthdate);
        textViewShowBirth=(TextView)view.findViewById(R.id.TV_ShowBirthdate);


        textInputLayoutUsername = (TextInputLayout) view.findViewById(R.id.TIL_Username);
        textInputLayoutPassword = (TextInputLayout) view.findViewById(R.id.TIL_Password);
        textInputLayoutRetryPassword = (TextInputLayout) view.findViewById(R.id.TIL_Password1);
        textInputLayoutEmail = (TextInputLayout) view.findViewById(R.id.TIL_Email);
        textInputLayoutNatiobalID = (TextInputLayout) view.findViewById(R.id.TIL_ID);

        buttonRegister = (Button) view.findViewById(R.id.BT_next);


        ccp = (CountryCodePicker) view.findViewById(R.id.ccp);
        editTextCarrierNumber = (EditText) view.findViewById(R.id.editText_carrierNumber);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        ccp.registerCarrierNumberEditText(editTextCarrierNumber);

        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date= String.format("%d/%d/%d",dayOfMonth,monthOfYear+1,year);
        textViewShowBirth.setText(date);
    }

    private boolean ValidEmail()
    {
        boolean isValid=true;
        if(Email.getText().toString().isEmpty())
        {
            textInputLayoutEmail.setError("This field is required");
            isValid=false;
        }
        else if ((!Email.getText().toString().trim().matches(emailPattern))&&isValid)
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
        if(Password.getText().toString().length()<5)
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
    private boolean ValidTryPassword()
    {
        boolean isValid;
        if (Retry_Password.getText().toString().length()==0)
        {
            textInputLayoutRetryPassword.setError("This field is required");
            isValid=false;
        }
        else if(!(Password.getText().toString().equals(Retry_Password.getText().toString())))
        {
            textInputLayoutRetryPassword.setError("Don't Match");
            isValid=false;
        }

        else {
            textInputLayoutRetryPassword.setErrorEnabled(false);
            isValid=true;
        }
        return isValid;
    }
    private boolean ValidUser()
    {
        boolean isValid;
        if(Username.getText().toString().length()<5)
        {
            textInputLayoutUsername.setError("This field must more than 5 ");
            isValid=false;
        }

        else {
            textInputLayoutUsername.setErrorEnabled(false);
            isValid=true;
        }
        return isValid;
    }
    private boolean ValidID()
    {
        boolean isValid;
        if(NationalID.getText().toString().length()<1)
        {
            textInputLayoutNatiobalID.setError("This field is required ");
            isValid=false;
        }

        else {
            textInputLayoutNatiobalID.setErrorEnabled(false);
            isValid=true;
        }
        return isValid;
    }
    private boolean ValidBirthdate()
    {
        boolean isValid;
        if(textViewShowBirth.getText().toString().isEmpty())
        {
            isValid=false;
        }
        else {
            isValid=true;
        }
        return isValid;
    }
    private boolean ValidGender()
    {
        boolean isValid;
        if(GenderFlag.isEmpty())
        {
            isValid=false;
        }
        else {
            isValid=true;
        }
        return isValid;
    }
    private boolean ValidPhone()
    {
        boolean isValid;
        if(!ccp.isValidFullNumber())
        {
            isValid=false;
            Toast.makeText(getActivity(), "Wrong Number", Toast.LENGTH_SHORT).show();
        }
        else {
            isValid=true;
        }
        return isValid;
    }

    //Auth

    private void Verification(final FirebaseUser user)
    {
        mAuth = FirebaseAuth.getInstance();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "Email sent.");
                            //Toast.makeText(getActivity(), user.getUid(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }    
    private void PatientRegister()
    {
        progressDialog.show();
        CreatePatient();
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(Patient.PatientEmail, Patient.PatientPassword)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("G", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Verification(user);
                            mDatabase.child("Patient").child(user.getUid()).setValue(Patient);
                            patientClass=new PatientClass(
                                    Uid,Patient.PatientName,Patient.PatientEmail,Patient.PatientPassword,
                                    Patient.PatientPhone,Patient.PatientNationalID,Patient.PatientGender,
                                    Patient.PatientBirth);
                            SharedParameters.PatientUid =Uid;
                            SharedParameters.PatientName=Patient.PatientName;
                            Intent intent=new Intent(getActivity()
                                    , StartActivity.class);
                            intent.putExtra("PatientClass",patientClass);
                            progressDialog.dismiss();
                            startActivity(intent);
                            getActivity().finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("G", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            /*updateUI(null);*/
                        }

                        // ...
                    }
                });
    }

    private void PatientRegisterWithAPI()
    {
        progressDialog.show();
        CreatePatient();
        mAuth = FirebaseAuth.getInstance();
        mDatabase.child("Patient").child(Uid).setValue(Patient)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            patientClass=new PatientClass(
                                    Uid,Patient.PatientName,Patient.PatientEmail,Patient.PatientPassword,
                                    Patient.PatientPhone,Patient.PatientNationalID,Patient.PatientGender,
                                    Patient.PatientBirth);
                            SharedParameters.PatientUid =Uid;
                            SharedParameters.PatientName=Patient.PatientName;
                            Intent intent=new Intent(getActivity()
                                    ,StartActivity.class);
                            intent.putExtra("PatientClass",patientClass);
                            progressDialog.dismiss();
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }
                });
    }

    private void CreatePatient()
    {
        String PatientName=Username.getText()+"";
        String PatientEmail=Email.getText()+"";
        String PatientPassword=Password.getText()+"";
        String PatientPhone=ccp.getFullNumber().toString();
        String PatientNationalID=NationalID.getText()+"";
        String PatientGender=GenderFlag;
        String PatientBirth=textViewShowBirth.getText().toString();
        Patient=new PatientClass(PatientName,PatientEmail,PatientPassword,PatientPhone,PatientNationalID,PatientGender,PatientBirth);
    }

    private void AutoCountry()
    {
        Call<CountriesClass> call=userService.Auto();
        call.enqueue(new Callback<CountriesClass>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<CountriesClass> call, Response<CountriesClass> response) {
                if(response.isSuccessful())
                {
                    countryList=response.body().getCountries();
                    countryAdapter=new CountryAdapter(getContext(),R.layout.view_spinner,countryList);
                    completeTextView.setAdapter(countryAdapter);
                    completeTextView.setThreshold(1);
                }
                else {
                    Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CountriesClass> call, Throwable t) {

            }
        });
    }
}
