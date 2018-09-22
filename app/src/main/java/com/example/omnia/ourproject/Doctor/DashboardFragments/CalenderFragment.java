package com.example.omnia.ourproject.Doctor.DashboardFragments;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;




import com.example.omnia.ourproject.Doctor.Adapters.PatientDateAdapter;
import com.example.omnia.ourproject.Doctor.Adapters.SpinnerAdapter;
import com.example.omnia.ourproject.Doctor.Classes.DoctorClass;
import com.example.omnia.ourproject.Doctor.Classes.PatientDate;
import com.example.omnia.ourproject.Patient.Classes.PatientClass;
import com.example.omnia.ourproject.R;
import com.example.omnia.ourproject.SharedClasses.SharedParameters;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import static android.content.ContentValues.TAG;

/**
 * Created by 3ZT on 03-Dec-17.
 */

@SuppressLint("ValidFragment")
public class CalenderFragment extends Fragment {
    private TextView Month,from,to;
    private FloatingActionButton fab ;
    private Dialog dialog,dialog2;
    private DoctorClass aClass;
    private long DayOfDate;
    private PatientClass patientClass;
    private List<PatientDate> patientDateArrayList = new ArrayList<>();
    private ListView listView;
    private PatientDateAdapter patientDateAdapter;
    private SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
    private SimpleDateFormat Year_date = new SimpleDateFormat("yyyy");
    private Spinner materialSpinner ;
    private List<Event> eventList=new ArrayList<>();
    private View v;
    private CompactCalendarView calendarView;



    @SuppressLint("ValidFragment")
    @RequiresApi(api = Build.VERSION_CODES.M)

    public CalenderFragment (DoctorClass aClass)
    {
        this.aClass =aClass;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {


        v=inflater.inflate(R.layout.fragment_calender,container,false);

        Init();

        setHasOptionsMenu(true);




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

        fab.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                ShowDialog();
            }

        });

        getGalasatat(Calendar.getInstance().getTime().getTime());


        return v;
    }


    private void Init()
    {
        calendarView=v.findViewById(R.id.compactcalendar_view);
        Month=v.findViewById(R.id.d);
        Month.setText(month_date.format(Calendar.getInstance().getTime())+" " +Calendar.getInstance().get(Calendar.YEAR));
        fab= v.findViewById(R.id.F);
        listView = v.findViewById(R.id.dates);
    }




    private  void  returnDate(final long day)
    {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query = reference.child("Doctor Agenda").child(aClass.DoctorID).orderByChild("fromHour");
            query.addValueEventListener(new ValueEventListener() {
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
                    }

                    Collections.sort(patientDateArrayList, new Comparator() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            PatientDate p1 = (PatientDate) o1;
                            PatientDate p2 = (PatientDate) o2;
                            return p1.getFromHour().compareTo(p2.getFromHour());
                        }
                    });
                    patientDateAdapter = new PatientDateAdapter(getActivity(), patientDateArrayList);
                   listView.setAdapter(patientDateAdapter);
                    calendarView.addEvents(eventList);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

    }



    private long ConvertToMS(String o){

        long Total=0;
        String [] ar=o.split(":");
        long num = Long.parseLong(ar[0]);
        long num2 = Long.parseLong(ar[1]);

        Total=(num*60*60*1000)+(num2*60*1000);

        return Total;
    }



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


    private void SetDoctorTime(String DoctorId, long DateOfDay, String GalsaID,String PatientId,String PatientName,
                               String Category,long FromHour,long ToHour)
    {

        DatabaseReference mDatabase;
        FirebaseDatabase firebasedatabase;

        mDatabase = FirebaseDatabase.getInstance().getReference();


        firebasedatabase= FirebaseDatabase.getInstance();
        mDatabase = firebasedatabase.getReferenceFromUrl
                ("https://jalsa-e9ec7.firebaseio.com/Doctor Agenda/"+ DoctorId+"/"+DateOfDay+"" );

        DatabaseReference id=mDatabase.push();
        id.setValue(new PatientDate(aClass.DoctorName,DoctorId,DateOfDay,GalsaID,PatientId,PatientName,Category,FromHour,ToHour));






        mDatabase = firebasedatabase.getReferenceFromUrl
                ("https://jalsa-e9ec7.firebaseio.com/Patient Agenda/"+ PatientId+"/"+DateOfDay+"" );

        DatabaseReference idd=mDatabase.push();
        idd.setValue(new PatientDate(aClass.DoctorName,DoctorId,DateOfDay,GalsaID,PatientId,PatientName,Category,FromHour,ToHour));


        SharedParameters.SendNotification("New Galsa",
                "Doctor Set new Galsa",PatientId);

        dialog.dismiss();

    }




    private void ShowDialogSe(final boolean b)
    {
        dialog2=new Dialog(getActivity());
        dialog2.setContentView(R.layout.view_timepicker);
        final TimePicker timePicker=dialog2.findViewById(R.id.timedialog);
        Button buttonSet=dialog2.findViewById(R.id.buttonSet);

        buttonSet.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                String result = String.format("%1$02d:%2$02d",timePicker.getHour() , timePicker.getMinute());
                if (b)
                {
                    from.setText(result);
                }
                else {
                    to.setText(result);
                }
                dialog2.dismiss();
            }
        });
        dialog2.show();
    }

    private void ShowDialog()
    {
        dialog=new Dialog(getActivity());
        dialog.setContentView(R.layout.veiw_add_date);
        dialog.setTitle("Add Time ...");




        TextView date=dialog.findViewById(R.id.date);
        from = dialog.findViewById(R.id.text);
        to = dialog.findViewById(R.id.text2);
        Button btn = dialog.findViewById(R.id.btn);
        materialSpinner=dialog.findViewById(R.id.spinner);
        Button buttonOpen=dialog.findViewById(R.id.buttonOpen);
        Button buttonOpen2=dialog.findViewById(R.id.buttonOpen2);
        materialSpinner.setAdapter(new SpinnerAdapter(getActivity(), SharedParameters.patientClasses));





        materialSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                patientClass= (PatientClass) adapterView.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (from.getText().toString().isEmpty()||to.getText().toString().isEmpty())
                {
                    Toast.makeText(getActivity(), "Complete Your data", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    isValid(DayOfDate,ConvertToMS(from.getText()+""),ConvertToMS(to.getText()+""));
                }
            }
        });


        buttonOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogSe(true);
            }
        });
        buttonOpen2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogSe(false);
            }
        });

        dialog.show();
    }


    private void isValid(long dayOfDate, final long start, final long end)
    {
        final boolean[] b = {true};
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Doctor Agenda").child(aClass.DoctorID).child(dayOfDate+"");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        long oldStart=Long.parseLong(issue.child("fromHour").getValue().toString());
                        long oldEnd=Long.parseLong(issue.child("toHour").getValue().toString());
                        if((end<=start)||(start>oldStart&&start<oldEnd)||(start<oldEnd&&end>oldStart))
                        {
                            b[0] =false;
                            break;
                        }
                    }
                }
                else {
                    if(end<=start)
                    {
                        b[0] =false;
                    }
                }
                if(b[0])
                {
                    SetDoctorTime(aClass.DoctorID,DayOfDate,"", patientClass.PatientID,patientClass.PatientName,"",
                            ConvertToMS(from.getText()+""),ConvertToMS(to.getText()+""));
                }
                else {
                    Toast.makeText(getActivity(), "Time Invalid", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}
