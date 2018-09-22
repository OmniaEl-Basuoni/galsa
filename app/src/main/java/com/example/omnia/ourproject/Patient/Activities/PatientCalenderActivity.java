package com.example.omnia.ourproject.Patient.Activities;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omnia.ourproject.Doctor.Classes.PatientDate;
import com.example.omnia.ourproject.Patient.Adapters.DoctorDateAdapter;
import com.example.omnia.ourproject.Patient.Classes.PatientClass;
import com.example.omnia.ourproject.R;
import com.example.omnia.ourproject.SharedClasses.GridSpacingItemDecoration;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class PatientCalenderActivity extends AppCompatActivity {

    TextView Month;
    Dialog dialog;
    private LinearLayout.LayoutParams params;

    PatientClass aClass;

    private long DayOfDate;

    PatientClass patientClass;
    private List<Event> eventList=new ArrayList<>();
    private List<PatientDate> patientDateArrayList = new ArrayList<>();
    ListView listView;
    DoctorDateAdapter doctorDateAdapter;



    private GridSpacingItemDecoration gridSpacingItemDecoration;

    SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
    SimpleDateFormat Year_date = new SimpleDateFormat("yyyy");
    Spinner materialSpinner ;


    CompactCalendarView calendarView;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_calender);

        Init();





        calendarView.setUseThreeLetterAbbreviation(true);

        calendarView.displayOtherMonthDays(true);



        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                getGalasatat(dateClicked.getTime());
            }


            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Log.d(TAG, "Month was scrolled to: " + firstDayOfNewMonth);
                Month.setText(month_date.format(firstDayOfNewMonth.getTime())+" "
                        +Year_date.format(firstDayOfNewMonth.getTime()));
            }
        });




        DoctorPersonalData();

        getGalasatat(Calendar.getInstance().getTime().getTime());
    }

    private void Init()
    {
        params = new LinearLayout.LayoutParams(MATCH_PARENT,WRAP_CONTENT);
        calendarView=findViewById(R.id.compactcalendar_view);
        Month=findViewById(R.id.d);
        Month.setText(month_date.format(Calendar.getInstance().getTime())+" " +Calendar.getInstance().get(Calendar.YEAR));
        listView = findViewById(R.id.dates);
    }




    private  void  returnDate(final long day){


        patientDateArrayList.clear();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Patient Agenda").child(aClass.PatientID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    patientDateArrayList.clear();
                    eventList.clear();
                    calendarView.removeAllEvents();
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        for (DataSnapshot dataSnapshot1: issue.getChildren()){
                            if (issue.getKey().toString().equals(day+""))
                            {
                                    patientDateArrayList.add(new PatientDate(
                                            ""+dataSnapshot1.child("doctorName").getValue(),
                                            dataSnapshot1.child("doctorID").getValue()+""
                                            ,Long.parseLong(dataSnapshot1.child("dateOfDay").getValue()+"")
                                            ,dataSnapshot1.child("galsaID").getValue()+""
                                            ,dataSnapshot1.child("patientID").getValue()+""
                                            ,dataSnapshot1.child("patientName").getValue()+""
                                            ,dataSnapshot1.child("category").getValue()+""
                                            ,Long.parseLong(dataSnapshot1.child("fromHour").getValue()+"")
                                            ,Long.parseLong(dataSnapshot1.child("toHour").getValue()+"")));

                            }
                            eventList.add(new Event(Color.GREEN,Long.parseLong(dataSnapshot1.child("dateOfDay").getValue()+"")));
                        }
                    }
                } else {
                    Toast.makeText(PatientCalenderActivity.this, "Not places in this Category", Toast.LENGTH_LONG).show();
                }
                doctorDateAdapter = new DoctorDateAdapter(PatientCalenderActivity.this, patientDateArrayList);
                listView.setAdapter(doctorDateAdapter);
                calendarView.addEvents(eventList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.M)


    private void getGalasatat(long dateLong)
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

        returnDate(DayOfDate);

    }

    private void DoctorPersonalData()
    {

        Bundle extras = getIntent().getExtras();
        if ((!extras.isEmpty())) {
            aClass= (PatientClass) extras.getSerializable("PatientClass");
        }
        else {
            Toast.makeText(this, "Here", Toast.LENGTH_SHORT).show();
        }

    }
}
