package com.example.omnia.ourproject.Doctor.Activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.example.omnia.ourproject.Alarm.DoctorAlarm;
import com.example.omnia.ourproject.Alarm.MyAlarm;
import com.example.omnia.ourproject.Doctor.Classes.DoctorClass;
import com.example.omnia.ourproject.Doctor.Adapters.DoctorHomeAdapter;
import com.example.omnia.ourproject.Doctor.Classes.AgendaHomeClass;
import com.example.omnia.ourproject.Notifications.NotificationServices.Common;
import com.example.omnia.ourproject.R;
import com.example.omnia.ourproject.SharedActivity.StartActivity;
import com.example.omnia.ourproject.SharedClasses.GridSpacingItemDecoration;
import com.example.omnia.ourproject.SharedClasses.SharedParameters;
import com.example.omnia.ourproject.VideoChat.BaseActivity;
import com.example.omnia.ourproject.VideoChat.SinchService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sinch.android.rtc.SinchError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class DoctorHomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener
        ,SinchService.StartFailedListener {


    private GridSpacingItemDecoration gridSpacingItemDecoration;
    private DoctorClass aClass;
    private ImageView PersonalPhoto;
    private DrawerLayout mDrawerLayout;
    private TextView NameSlide,EmailSlide,Month,Day,DayName,DoctorName;
    private RatingBar DoctorRateSlide;
    private NavigationView mNavigationView;
    private com.example.omnia.ourproject.Doctor.Adapters.DoctorHomeAdapter DoctorHomeAdapter;
    private List<AgendaHomeClass> doctorClassList = new ArrayList<>();
    private  RecyclerView recyclerViewCalender;
    private View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_doctor_home);

        Init();

        CheckPermission();

        Date();

        //region SlideButtonClick
        mNavigationView.setNavigationItemSelectedListener
                (new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        Intent ProfileIntent=new Intent(DoctorHomeActivity.this,DoctorProfileActivity.class);
                        ProfileIntent.putExtra("DoctorClass",aClass);
                        startActivity(ProfileIntent);
                        break;
                    case R.id.dashboard:
                        Intent DashIntent=new Intent(DoctorHomeActivity.this,DoctorDashboardActivity.class);
                        DashIntent.putExtra("DoctorClass",aClass);
                        startActivity(DashIntent);
                        break;
                    case R.id.logout:
                        Logout();
                        break;
                }
                return true;
            }
        });
        //endregion

        //region DoctorProfileMenu
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.menu_personal_icon, null);
        PersonalPhoto=v.findViewById(R.id.imageView4);
        DoctorName=v.findViewById(R.id.nameView);
        actionBar.setCustomView(v);

        PersonalPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT); //CLOSE Nav Drawer!
                }else{
                    mDrawerLayout.openDrawer(GravityCompat.START,true); //OPEN Nav Drawer!
                }
            }
        });
        //endregion


        DoctorPersonalData();

        setToken();

        getToday();

        CheckCompeleteData();


        //region StartService
        new CountDownTimer(500,250)
        {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                loginClicked();
            }
        } .start();
        //endregion

        DoctorAlarm.UpdateAlarm(this, aClass.DoctorID);
    }

    private void CheckCompeleteData() {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query = reference.child("Doctor").child(aClass.DoctorID);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()&&dataSnapshot.getChildrenCount()<18) {
                        ShowSnackBar();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
    }


    public boolean onCreateOptionsMenu (Menu menu)
    {
        getMenuInflater().inflate(R.menu.dochomemenu, menu);
        return true;
    }

    public boolean onOptionsItemSelected (MenuItem item)
    {
        switch (item.getItemId()){
            case R.id.msg:
                Intent DoctorMessageIntent=new Intent(DoctorHomeActivity.this,DoctorMessageActivity.class);
                DoctorMessageIntent.putExtra("DoctorClass",aClass);
                startActivity(DoctorMessageIntent);
                return true;
            case  R.id.request:
                Intent RequestIntent=new Intent(DoctorHomeActivity.this,DoctorRequestsActivity.class);
                RequestIntent.putExtra("DoctorClass",aClass);
                startActivity(RequestIntent);
                return true;
            case R.id.notification:
                Toast.makeText(this, "Notification", Toast.LENGTH_SHORT).show();
                return true;
        }
        return true;
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

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mNavigationView = (NavigationView) findViewById(R.id.fr);


        header=mNavigationView.getHeaderView(0);

        recyclerViewCalender=(RecyclerView) findViewById(R.id.RV_Calender);

        NameSlide=header.findViewById(R.id.Name_Slide);
        EmailSlide=header.findViewById(R.id.Email_Slide);
        DoctorRateSlide=header.findViewById(R.id.RB_rate);

        Month=findViewById(R.id.TV_Month);
        Day=findViewById(R.id.TV_Day);
        DayName=findViewById(R.id.TV_DName);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        return true;
    }


    //region Try
   /* @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("someVarA", "Ezzat");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
         S= savedInstanceState.getString("someVarA");
        Toast.makeText(this, S, Toast.LENGTH_SHORT).show();
    }
*/


    //endregion

    @Override
    public void onBackPressed()
    {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }
        else {
            super.onBackPressed();
        }
    }

    private void DoctorPersonalData()
    {

        Bundle extras = getIntent().getExtras();
         if ((!extras.isEmpty())) {
            aClass= (DoctorClass) extras.getSerializable("DoctorClass");
            ImageLoader.getInstance().displayImage(aClass.DoctorPhotoUrl, PersonalPhoto, new ImageLoadingListener() {
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

            NameSlide.setText(aClass.DoctorName);
            EmailSlide.setText(aClass.DoctorEmail);
            DoctorRateSlide.setRating((float) aClass.DoctorRate);
            DoctorName.setText(aClass.DoctorName);
        }
        else {
            Toast.makeText(this, "Here", Toast.LENGTH_SHORT).show();
        }

    }


    private void FillDoctorAgenda(final long day)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Doctor Agenda").child(aClass.DoctorID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    doctorClassList.clear();
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        for (DataSnapshot dataSnapshot1 : issue.getChildren()) {

                            if (issue.getKey().toString().equals(day + "")) {

                                try {
                                    doctorClassList.add(new AgendaHomeClass(dataSnapshot1.child("fromHour").getValue().toString()
                                            , dataSnapshot1.child("patientName").getValue() + "",
                                            dataSnapshot1.child("toHour").getValue().toString()));

                                } catch (Exception e) {
                                    Toast.makeText(DoctorHomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d("AAAA", e.getMessage());
                                }
                            }
                        }
                    }

                }

                else {
                    Toast.makeText(DoctorHomeActivity.this, "Not Data In Agenda", Toast.LENGTH_LONG).show();
                }
                Collections.sort(doctorClassList, new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        AgendaHomeClass p1 = (AgendaHomeClass) o1;
                        AgendaHomeClass p2 = (AgendaHomeClass) o2;
                        return p1.Date.compareToIgnoreCase(p2.Date);
                    }
                });
                DoctorHomeAdapter = new DoctorHomeAdapter(DoctorHomeActivity.this, doctorClassList);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(DoctorHomeActivity.this, 1);
                recyclerViewCalender.setLayoutManager(mLayoutManager);
                gridSpacingItemDecoration=new GridSpacingItemDecoration (1,0,true,DoctorHomeActivity.this);
                recyclerViewCalender.addItemDecoration(gridSpacingItemDecoration);
                recyclerViewCalender.setItemAnimator(new DefaultItemAnimator());
                recyclerViewCalender.setAdapter(DoctorHomeAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    private void Date()
    {
        Calendar cal=Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        SimpleDateFormat day_date = new SimpleDateFormat("EEEE");
        DayName.setText(day_date.format(cal.getTime()));
        Month.setText(month_date.format(cal.getTime()));
        Day.setText(cal.getTime().getDate()+"");
    }



    //region VideoChatServicesRegister
    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStarted() {
    }


    private void loginClicked() {
        String userName = aClass.DoctorID;
        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
            return;
        }

        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(userName);
        }
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


    private void getToday()
    {
        long DayOfDate = 0;
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
        String dateText = df2.format(Calendar.getInstance().getTime());

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(dateText);

            DayOfDate = date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        FillDoctorAgenda(DayOfDate);
    }

    private void ShowSnackBar()
    {
        LinearLayout coordinatorLayout=findViewById(R.id.layout);
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "Complete Your Profile Data ,Please ..! \uD83D\uDE01 ", TSnackbar.LENGTH_INDEFINITE);

        snackbar.setActionTextColor(Color.WHITE);
        snackbar.setAction("Go", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(DoctorHomeActivity.this, DoctorProfileActivity.class);
               intent.putExtra("DoctorClass",aClass);
               startActivity(intent);
            }
        });
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.parseColor("#000000"));
        snackbar.show();
    }

    private void setToken()
    {
        Common.currentToken= FirebaseInstanceId.getInstance().getToken();
        FirebaseDatabase.getInstance().getReference().child("Tokens").child(aClass.DoctorID).setValue(Common.currentToken);
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
