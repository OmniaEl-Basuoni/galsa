package com.example.omnia.ourproject.Doctor.Activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omnia.ourproject.Doctor.DashboardFragments.CalenderFragment;
import com.example.omnia.ourproject.Doctor.DashboardFragments.ComplainsFragment;
import com.example.omnia.ourproject.Doctor.DashboardFragments.PatientsFragment;
import com.example.omnia.ourproject.Doctor.Classes.DoctorClass;
import com.example.omnia.ourproject.R;
import com.example.omnia.ourproject.SharedActivity.StartActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DoctorDashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FragmentManager fragmentManager ;
    private Fragment fragment;
    private BottomBar mBottomBar;
    private DoctorClass aClass;
    private ImageView PersonalPhoto,SearchImage;


    private long DayOfDate;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private View header,v;
    private TextView NameSlide,EmailSlide;
    private RatingBar DoctorRateSlide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_doctor_dashboard);

        Init();

        //region DoctorProfileMenu
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(v);

        PersonalPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START, true);
            }
        });

        SearchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DoctorDashboardActivity.this, "Search", Toast.LENGTH_SHORT).show();
            }
        });

        //endregion

        SetData();

        //region BottomBar
        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTabSelected(int tabId) {
                switch (tabId)
                {
                    case R.id.home:
                        SearchImage.setVisibility(View.VISIBLE);
                        fragment=new PatientsFragment(aClass);
                        break;
                    case R.id.calender:
                        SearchImage.setVisibility(View.GONE);
                        fragment=new CalenderFragment(aClass);
                        break;
                    case R.id.complains:
                        SearchImage.setVisibility(View.GONE);
                        fragment=new ComplainsFragment(aClass);
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.content, fragment)
                        .commit();
            }
        });
        //endregion

        //region SlideButtonClick
        mNavigationView.setNavigationItemSelectedListener
                (new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.profile:
                                Intent ProfileIntent=new Intent(DoctorDashboardActivity.this,DoctorProfileActivity.class);
                                ProfileIntent.putExtra("DoctorClass",aClass);
                                startActivity(ProfileIntent);
                                break;
                            case R.id.dashboard:
                                break;
                            case R.id.logout:
                                Logout();
                                break;
                        }
                        return true;
                    }
                });
        //endregion



    }



    @Override
    public void onBackPressed() {
         finish();

    }


    private void Init()
    {
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

        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflator.inflate(R.layout.menu_doctor_dashboard_icon, null);
        PersonalPhoto=v.findViewById(R.id.imageView4);
        SearchImage=v.findViewById(R.id.imageView3);

        mBottomBar=findViewById(R.id.bottombar);
        fragmentManager = getFragmentManager();

        mDrawerLayout =  findViewById(R.id.drawer);
        mNavigationView = findViewById(R.id.fr);
        header=mNavigationView.getHeaderView(0);
        NameSlide=header.findViewById(R.id.Name_Slide);
        EmailSlide=header.findViewById(R.id.Email_Slide);
        DoctorRateSlide=header.findViewById(R.id.RB_rate);
    }


    private void SetData() {

        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            aClass = (DoctorClass) extras.getSerializable("DoctorClass");
            if(aClass!=null)
            {
                NameSlide.setText(aClass.DoctorName);
                EmailSlide.setText(aClass.DoctorEmail);
                DoctorRateSlide.setRating((float) aClass.DoctorRate);
            }
            else {
            }
        }
    }


    //region Menu
    public boolean onCreateOptionsMenu (Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_doctor_dashboard, menu);
        return true;
    }

    public boolean onOptionsItemSelected (MenuItem item)
    {
        switch (item.getItemId()){
            /*case R.id.msg:
                Toast.makeText(this, "messages", Toast.LENGTH_SHORT).show();
                return true;
            case  R.id.request:
                Toast.makeText(this, "Notification", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.notification:
                Toast.makeText(this, "Notification", Toast.LENGTH_SHORT).show();
                return true;*/
        }
        return true;
    }

    //endregion

    private void Logout() {
        //region SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor edit = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson("");
        edit.putString("Object",json);
        edit.putString("Role", "Client");
        edit.commit();
        //endregion
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, StartActivity.class));
        finish();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode== Activity.RESULT_OK)
        {
            Bundle bundle=data.getExtras();
            if (!bundle.isEmpty())
            {
                String PatientID=bundle.getString("PatientID");
                String DoctorID=bundle.getString("DoctorID");
                String Notes=bundle.getString("Notes");
                ArrayList<String>Medicines=bundle.getStringArrayList("Medicines");
                AddJalasat(PatientID,DoctorID,Notes,Medicines);
            }
        }
    }

    private void AddJalasat(String patientID, String doctorID, String notes, ArrayList<String> medicines) {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference()
                .child("Jalsat").child(doctorID).child(patientID).child(""+getGalasatat(Calendar.getInstance().getTime().getTime()));
        reference.child("Date").setValue(getGalasatat(Calendar.getInstance().getTime().getTime()));
        reference.child("Medicine").setValue(medicines);
        reference.child("Notes").setValue(notes);
    }

    private long getGalasatat(long dateLong)
    {
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
        String dateText = df2.format(dateLong);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(dateText);

            DayOfDate = date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return DayOfDate;
    }

}
