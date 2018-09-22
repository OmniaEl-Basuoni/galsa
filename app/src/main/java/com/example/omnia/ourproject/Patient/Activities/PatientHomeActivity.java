package com.example.omnia.ourproject.Patient.Activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omnia.ourproject.Alarm.MyAlarm;
import com.example.omnia.ourproject.Alarm.PatientAlarm;
import com.example.omnia.ourproject.Doctor.Adapters.SendRequestAdapter;
import com.example.omnia.ourproject.Doctor.Classes.DoctorClass;
import com.example.omnia.ourproject.Doctor.Adapters.DoctorHomeAdapter;
import com.example.omnia.ourproject.Doctor.Classes.AgendaHomeClass;
import com.example.omnia.ourproject.Doctor.Adapters.DoctorInfoAdapter;
import com.example.omnia.ourproject.Notifications.NotificationServices.Common;
import com.example.omnia.ourproject.Patient.Classes.PatientClass;
import com.example.omnia.ourproject.Patient.SearchPackage.TrySearchActivity;
import com.example.omnia.ourproject.R;
import com.example.omnia.ourproject.SharedActivity.FeedbackActivity;
import com.example.omnia.ourproject.SharedActivity.StartActivity;
import com.example.omnia.ourproject.SharedClasses.GroupClass;
import com.example.omnia.ourproject.SharedClasses.SharedParameters;
import com.example.omnia.ourproject.SharedClasses.TherapyAdapter;
import com.example.omnia.ourproject.VideoChat.BaseActivity;
import com.example.omnia.ourproject.VideoChat.SinchService;
import com.github.pavlospt.roundedletterview.RoundedLetterView;
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
import com.sinch.android.rtc.SinchError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class PatientHomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener
        ,SinchService.StartFailedListener {

    private long DayOfDate = 0;

    private String myDoctorsIds;
    private TextView seeMore,textView_doctor;
    private List<DoctorClass> smallDoctorClassList=new ArrayList<>();

    private TextView CalenderMonth,CalenderDay,CalenderDayName;

    private DoctorHomeAdapter DoctorHomeAdapter;
    private List<AgendaHomeClass> doctorClassList2 = new ArrayList<>();
    private RecyclerView recyclerViewCalender;

    //region Therapy
    private   RecyclerView TherapyRecyclerView;
    private TherapyAdapter therapyAdapter;
    private List<GroupClass> therapyClassList=new ArrayList<>();
    //endregion

    private RecyclerView recyclerViewDoctor;
    private DoctorInfoAdapter doctorInfoAdapter;
    private SendRequestAdapter sendRequestAdapter;
    private List<DoctorClass> doctorClassList = new ArrayList<>();
    private PatientClass aClass;

    private RoundedLetterView roundedLetterView;
    private TextView NameView;
    //region SlideMenuDeclaration
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private View header;
    private TextView NameSlide,EmailSlide;
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_patient_home);



        Init();

        CheckPermission();

        //region DoctorProfileMenu
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayShowCustomEnabled(true);

            final LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflator.inflate(R.layout.menu_personal_icon_patient, null);
            roundedLetterView=v.findViewById(R.id.rlv_name_view);
            NameView=v.findViewById(R.id.nameView);
            actionBar.setCustomView(v);

            roundedLetterView.setOnClickListener(new View.OnClickListener() {
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


        PatientPersonalData();

        setToken();

        CalenderDate();



        getToday();

        CheckHaveDoctor();
        ReturnTherapy();




        //region SlideMenu
        mDrawerLayout=findViewById(R.id.drawer);
        mNavigationView = findViewById(R.id.fr);
        header=mNavigationView.getHeaderView(0);
        NameSlide=header.findViewById(R.id.Name_Slide);
        EmailSlide=header.findViewById(R.id.Email_Slide);
        NameSlide.setText(aClass.PatientName);
        EmailSlide.setText(aClass.PatientEmail);
        //region SlideMenu
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.group:

                        Intent GroupIntent=new Intent(PatientHomeActivity.this,PatientJoinTherapyActivity.class);
                        GroupIntent.putExtra("PatientClass",aClass);
                        startActivity(GroupIntent);
                        break;
                    case R.id.doctor:
                        Intent SearchIntent=new Intent(PatientHomeActivity.this,TrySearchActivity.class);
                        SearchIntent.putExtra("PatientClass",aClass);
                        startActivity(SearchIntent);
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                    case R.id.calender:
                        Intent CalenderIntent=new Intent(PatientHomeActivity.this,PatientCalenderActivity.class);
                        CalenderIntent.putExtra("PatientClass",aClass);
                        startActivity(CalenderIntent);
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        break;

                    case R.id.feedback:
                        startActivity(new Intent(PatientHomeActivity.this, FeedbackActivity.class));
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        break;


                    case R.id.help:
                        startActivity(new Intent(PatientHomeActivity.this, PatientHelpActivity.class));
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        break;


                    case R.id.logout:
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        Logout();
                        break;
                }
                return true;
            }
        });
        //endregion
        //endregion




        seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PatientHomeActivity.this,PatientViewYourDoctorActivity.class);
                intent.putExtra("ClassPatient",aClass);
                intent.putExtra("myDoctorsIds",myDoctorsIds);
                startActivity(intent);
            }
        });


        PatientAlarm.UpdateAlarm(this,aClass.PatientID);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
    }

    public boolean onCreateOptionsMenu (Menu menu)
    {
        getMenuInflater().inflate(R.menu.patienthomemenu, menu);
        return true;
    }

    public boolean onOptionsItemSelected (MenuItem item)
    {
        switch (item.getItemId()){
            case R.id.msg:
                Intent PatientMessageIntent=new Intent(PatientHomeActivity.this,
                        PatientMessageActivity.class);
                PatientMessageIntent.putExtra("PatientClass",aClass);
                startActivity(PatientMessageIntent);
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

        recyclerViewDoctor = (RecyclerView) findViewById(R.id.recycler_view);

        TherapyRecyclerView=findViewById(R.id.RV_Therapy);



        recyclerViewCalender=(RecyclerView) findViewById(R.id.RV_Calender);

        CalenderMonth=findViewById(R.id.TV_Month);
        CalenderDay=findViewById(R.id.TV_Day);
        CalenderDayName=findViewById(R.id.TV_DName);


        seeMore=findViewById(R.id.tv_seeMore);
        textView_doctor=findViewById(R.id.textView_doctor);



    }



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
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        return true;
    }


    private void FillDoctorAgenda(final long day)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Patient Agenda").child(aClass.PatientID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    doctorClassList2.clear();
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        for (DataSnapshot dataSnapshot1 : issue.getChildren()) {
                            if (issue.getKey().toString().equals(day + "")) {

                                    doctorClassList2.add(new AgendaHomeClass(dataSnapshot1.child("fromHour").getValue().toString()
                                            , dataSnapshot1.child("doctorName").getValue() + "",
                                            dataSnapshot1.child("toHour").getValue().toString()
                                    ));
                            }
                        }
                    }

                }
                else {
                    Toast.makeText(PatientHomeActivity.this, "Not places in this Category", Toast.LENGTH_LONG).show();
                }
                Collections.sort(doctorClassList2, new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        AgendaHomeClass p1 = (AgendaHomeClass) o1;
                        AgendaHomeClass p2 = (AgendaHomeClass) o2;
                        return p1.Date.compareToIgnoreCase(p2.Date);
                    }
                });
                DoctorHomeAdapter = new DoctorHomeAdapter(PatientHomeActivity.this, doctorClassList2);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(PatientHomeActivity.this, 1);
                recyclerViewCalender.setLayoutManager(mLayoutManager);
                recyclerViewCalender.addItemDecoration(new PatientHomeActivity.GridSpacingItemDecoration(1, dpToPx(0), true));
                recyclerViewCalender.setItemAnimator(new DefaultItemAnimator());
                recyclerViewCalender.setAdapter(DoctorHomeAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void CalenderDate()
    {
        Calendar cal=Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        SimpleDateFormat day_date = new SimpleDateFormat("EEEE");
        CalenderDayName.setText(day_date.format(cal.getTime()));
        CalenderMonth.setText(month_date.format(cal.getTime()));
        CalenderDay.setText(cal.getTime().getDate()+"");
        /*String month_name = month_date.format(cal.getTime());
        String day_name = month_date.format(cal.getTime());
        Month.setText(month_name);*/
    }




    private void ReturnTherapy() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Group Support").orderByChild("Group ID");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    try {
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                            therapyClassList.add(new GroupClass(
                                    issue.child("Group ID").getValue()+"",
                                    issue.getKey()+"",
                                    issue.child("Group Description").getValue()+"",
                                    issue.child("Group Day").getValue()+"",
                                    issue.child("Group Time").getValue()+"",
                                    Integer.parseInt(issue.child("Group Capicity").getValue()+"")
                            ));
                        }
                    }
                    catch (Exception Ex) {
                        Toast.makeText(PatientHomeActivity.this, Ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //Toast.makeText(PatientJoinTherapyActivity.this, "Not places in this Category", Toast.LENGTH_LONG).show();
                }
                therapyAdapter = new TherapyAdapter(PatientHomeActivity.this, therapyClassList,aClass);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(PatientHomeActivity.this, 1);
                TherapyRecyclerView.setLayoutManager(mLayoutManager);
                TherapyRecyclerView.addItemDecoration(new PatientHomeActivity.GridSpacingItemDecoration(1, dpToPx(4), true));
                TherapyRecyclerView.setItemAnimator(new DefaultItemAnimator());
                TherapyRecyclerView.setAdapter(therapyAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }





    //region DoctorList
    private void CheckHaveDoctor() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Doctor_Patient");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    myDoctorsIds="";
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        for (DataSnapshot dataSnapshot1 : issue.getChildren()) {
                            if (dataSnapshot1.child("Patient ID").getValue().equals(SharedParameters.PatientUid)) {
                                myDoctorsIds += issue.getKey();
                            }
                        }
                    }
                } else {
                    Toast.makeText(PatientHomeActivity.this, "Not places in this Category", Toast.LENGTH_LONG).show();
                }
                if (myDoctorsIds.isEmpty()) {
                    ReturnDefaultDoctor();
                } else {
                    ReturnMyDoctor(myDoctorsIds);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    private void ReturnMyDoctor(final String ids) {
        textView_doctor.setText(R.string.my_doctor);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Doctor").orderByChild("DoctorRate");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    doctorClassList.clear();
                    try {
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                            if(ids.contains(issue.getKey())) {
                                doctorClassList.add(new DoctorClass(issue.child("PersonalPhoto").getValue() + ""
                                        , issue.child("DoctorName").getValue() + ""
                                        , Double.parseDouble(issue.child("DoctorRate").getValue() + "")
                                        , getCategory(issue.child("Category").getValue()+"")
                                        , issue.getKey()
                                        ,issue.child("Doctor PriceHalf").getValue()+""
                                        ,issue.child("Doctor PriceHour").getValue()+""
                                ));
                            }
                        }
                    }
                    catch (Exception Ex)
                    {
                        Toast.makeText(PatientHomeActivity.this, Ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(PatientHomeActivity.this, "Not places in this Category", Toast.LENGTH_LONG).show();
                }
                Collections.reverse(doctorClassList);
                if(doctorClassList.size()>3)
                {
                    seeMore.setVisibility(View.VISIBLE);
                    smallDoctorClassList.add(doctorClassList.get(0));
                    smallDoctorClassList.add(doctorClassList.get(1));
                    smallDoctorClassList.add(doctorClassList.get(2));
                    doctorInfoAdapter = new DoctorInfoAdapter(PatientHomeActivity.this, smallDoctorClassList,aClass);
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(PatientHomeActivity.this, 3);
                    recyclerViewDoctor.setLayoutManager(mLayoutManager);
                    recyclerViewDoctor.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(1), true));
                    recyclerViewDoctor.setItemAnimator(new DefaultItemAnimator());
                    recyclerViewDoctor.setAdapter(doctorInfoAdapter);
                }
                else {
                    seeMore.setVisibility(View.INVISIBLE);
                    doctorInfoAdapter = new DoctorInfoAdapter(PatientHomeActivity.this, doctorClassList,aClass);
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(PatientHomeActivity.this, 3);
                    recyclerViewDoctor.setLayoutManager(mLayoutManager);
                    recyclerViewDoctor.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(3), true));
                    recyclerViewDoctor.setItemAnimator(new DefaultItemAnimator());
                    recyclerViewDoctor.setAdapter(doctorInfoAdapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    private void ReturnDefaultDoctor() {
        textView_doctor.setText(R.string.rated_doctor);
        seeMore.setVisibility(View.INVISIBLE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Doctor").orderByChild("DoctorRate").limitToLast(3);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    doctorClassList.clear();
                    try {
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                            doctorClassList.add(new DoctorClass(issue.child("PersonalPhoto").getValue()+""
                                    ,issue.child("DoctorName").getValue()+""
                                    ,Double.parseDouble(issue.child("DoctorRate").getValue()+"")
                                    ,getCategory(issue.child("Category").getValue()+"")
                                    ,issue.getKey()
                                    ,issue.child("Doctor PriceHalf").getValue()+""
                                    ,issue.child("Doctor PriceHour").getValue()+""
                            ));
                        }
                    }
                    catch (Exception Ex)
                    {
                        Toast.makeText(PatientHomeActivity.this, Ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(PatientHomeActivity.this, "Not places in this Category", Toast.LENGTH_LONG).show();
                }
                Collections.reverse(doctorClassList);
                sendRequestAdapter = new SendRequestAdapter(PatientHomeActivity.this, doctorClassList,aClass);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(PatientHomeActivity.this, 3);
                recyclerViewDoctor.setLayoutManager(mLayoutManager);
                recyclerViewDoctor.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(3), true));
                recyclerViewDoctor.setItemAnimator(new DefaultItemAnimator());
                recyclerViewDoctor.setAdapter(sendRequestAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    //endregion


    private void PatientPersonalData()
    {

        Bundle extras = getIntent().getExtras();
        if ((!extras.isEmpty())) {
            aClass= (PatientClass) extras.getSerializable("PatientClass");
            /*ImageLoader.getInstance().displayImage(aClass.DoctorPhotoUrl, PersonalPhoto, new ImageLoadingListener() {
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
            });*/

            /*NameSlide.setText(aClass.DoctorName);
            EmailSlide.setText(aClass.DoctorEmail);
            DoctorRateSlide.setRating((float) aClass.DoctorRate);*/

                roundedLetterView.setTitleText(aClass.PatientName.toUpperCase().charAt(0)+"");
                NameView.setText(aClass.PatientName);


        }
        else {
            Toast.makeText(this, "Here", Toast.LENGTH_SHORT).show();
        }

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
        openPlaceCallActivity();
    }


    private void loginClicked() {
        String userName = aClass.PatientID;
        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
            return;
        }

        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(userName);
        }
    }

    private void openPlaceCallActivity() {
        /*Intent mainActivity = new Intent(this, DoctorChatActivity.class);
        startActivity(mainActivity);*/
    }
    //endregion


    //region RecycleView
    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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




    private void setToken()
    {
        Common.currentToken= FirebaseInstanceId.getInstance().getToken();
        FirebaseDatabase.getInstance().getReference().child("Tokens").child(aClass.PatientID).setValue(Common.currentToken);
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
