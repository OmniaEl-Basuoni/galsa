package com.example.omnia.ourproject.SharedActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.omnia.ourproject.Doctor.Activities.DoctorHomeActivity;
import com.example.omnia.ourproject.Doctor.Classes.DoctorClass;
import com.example.omnia.ourproject.Patient.Activities.PatientHomeActivity;
import com.example.omnia.ourproject.Patient.Classes.PatientClass;
import com.example.omnia.ourproject.R;
import com.example.omnia.ourproject.SharedActivity.RegisterationFragment.RegistrationActivity;
import com.example.omnia.ourproject.SharedClasses.SharedParameters;
import com.facebook.AccessToken;
import com.facebook.BuildConfig;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.Arrays;

public class StartActivity extends AppCompatActivity {
    private ImageView Google,Facebook;
    private Button buttonLogin, buttonRegister;
    private static final int RC_SIGN_IN=9001;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private GoogleSignInOptions gso;
    CallbackManager mCallbackManager;
    LoginButton loginButton;
    String name;
    DatabaseReference reference;
    boolean Search;
    private ProgressDialog dialog;
    private DoctorClass Doctor;
    private PatientClass Patient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.setMessage("Please wait...");

        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id));
        //initialize Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }
        setContentView(R.layout.activity_shared_start);

        Init();


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, LogInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedParameters.WithAPI=true;
                Intent intent = new Intent(StartActivity.this, RegistrationActivity.class);
                intent.putExtra("withAPI",false);
                startActivity(intent);
                finish();
            }
        });


        Google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    signIn();
                }
                catch (Exception e)
                {
                    Toast.makeText(StartActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });




        mCallbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


        mAuthListener = new FirebaseAuth.AuthStateListener(){


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user!=null){
                    name = user.getDisplayName();
                }else {
                    Toast.makeText(StartActivity.this,"something went wrong",Toast.LENGTH_LONG).show();
                }


            }
        };


        Facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
                LoginManager.getInstance().logInWithReadPermissions(StartActivity.this
                        , Arrays.asList("public_profile", "user_friends"));
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = result.getSignInAccount();
                    firebaseAuthWithGoogle(account);
                } else {
                    // Google Sign In failed, update UI appropriately
                    // ...
                }
            } else {

                mCallbackManager.onActivityResult(requestCode, resultCode, data);
            }

            // Pass the activity result back to the Facebook SDK
        }
        catch (Exception e)
        {

        }
    }


    private void Init() {
        try {


            buttonLogin = (Button) findViewById(R.id.BT_login);
            buttonRegister = (Button) findViewById(R.id.BT_register);
            Google = (ImageView) findViewById(R.id.IV_Google);
            mAuth = FirebaseAuth.getInstance();
            ConfigureGoogleSign();

            //Facebook
            Facebook=(ImageView)findViewById(R.id.IV_face);

            loginButton = (LoginButton) findViewById(R.id.login_button);

            reference = FirebaseDatabase.getInstance().getReference();
        }
        catch (Exception e)
        {

        }
    }


    private void ConfigureGoogleSign()
    {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient=new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(StartActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());
        //AuthCredential credential = FacebookAuthProvider.getCredential(acct.getIdToken());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        final boolean[] Found = new boolean[1];


       /* mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Found[0] =false;
                            Log.d("TAG", "signInWithCredential:success");
                            Check(mAuth.getCurrentUser());
                        }
                        *//*else {
                           *//**//* // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(StartActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();*//**//*
                        }

                        // ...*//*
                    }
                });
*/
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "signInWithCredential:success");
                                Check(mAuth.getCurrentUser());
                            } else {
                                Toast.makeText(StartActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

    }




    //Facebook"

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("", "handleFacebookAccessToken:" + token);
        final boolean[] Found = new boolean[1];
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Found[0] =false;
                            Check(mAuth.getCurrentUser());
                        }
                        else {
                            Toast.makeText(StartActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        /*if(Found[0])
        {
            mAuth.getCurrentUser().linkWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Check(mAuth.getCurrentUser());
                    }
                }
            });
        }*/
    }



    private void Check(FirebaseUser user)
    {
        dialog.show();
        Search=true;
        Search("Patient",user);
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
                        SharedParameters.PatientName=user.getDisplayName();
                        Intent intent=new Intent(StartActivity.this, PatientHomeActivity.class);
                        intent.putExtra("PatientClass", Patient);
                        startActivity(intent);
                        dialog.dismiss();
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
                        SharedParameters.DoctorName=user.getDisplayName();
                        Intent intent=new Intent(StartActivity.this, DoctorHomeActivity.class);
                        intent.putExtra("DoctorClass",Doctor);
                        startActivity(intent);
                        dialog.dismiss();
                        finish();
                    }
                    
                } else {
                    if(Search)
                    {
                    Search("Doctor",user);
                    Search=false;
                     }
                     else {
                        Intent intent=new Intent(StartActivity.this,RegistrationActivity.class);
                        intent.putExtra("withAPI",true);
                        intent.putExtra("Uid",user.getUid());
                        intent.putExtra("Email",user.getEmail());
                        intent.putExtra("Password","*********");
                        intent.putExtra("Name",user.getDisplayName());
                        //intent.putExtra("Photo",user.getPhotoUrl());
                        SharedParameters.WithAPI=false;
                        dialog.dismiss();
                        startActivity(intent);
                        finish();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}