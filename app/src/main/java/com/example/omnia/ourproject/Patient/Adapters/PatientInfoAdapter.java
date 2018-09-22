package com.example.omnia.ourproject.Patient.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omnia.ourproject.Doctor.Activities.DoctorChatActivity;
import com.example.omnia.ourproject.Doctor.Activities.DoctorMessageActivity;
import com.example.omnia.ourproject.Doctor.Adapters.DoctorMessagesAdapter;
import com.example.omnia.ourproject.Doctor.Classes.DoctorClass;
import com.example.omnia.ourproject.Patient.Activities.PatientGeneralInfo;
import com.example.omnia.ourproject.Patient.Classes.PatientClass;
import com.example.omnia.ourproject.R;
import com.example.omnia.ourproject.SharedClasses.DoctorPatientMessagesClass;
import com.github.pavlospt.roundedletterview.RoundedLetterView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 3ZT on 10/23/2017.
 */

public class PatientInfoAdapter extends RecyclerView.Adapter<PatientInfoAdapter.MyViewHolder> {

    private DoctorPatientMessagesClass doctorPatientMessagesClass;
    private Context mContext;
    private List<PatientClass> PatientClassList;
    private DatabaseReference reference;
    private DoctorClass doctorClass;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public RoundedLetterView roundedLetterView;
        public TextView textViewName;
        public CardView cardView;
        public CircleImageView imageView;


        public MyViewHolder(View view) {
            super(view);
            roundedLetterView = view.findViewById(R.id.rlv_name_view);
            textViewName = view.findViewById(R.id.TV_PatientName);
            cardView = view.findViewById(R.id.CV_patientview);
            imageView = view.findViewById(R.id.IV_Chat);

        }
    }

    public PatientInfoAdapter(Context mContext, List<PatientClass> patientClassList, DoctorClass doctorClass) {
        this.mContext = mContext;
        PatientClassList = patientClassList;
        this.doctorClass = doctorClass;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_patient, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final PatientClass album = PatientClassList.get(position);
        holder.textViewName.setText(album.PatientName);
        holder.roundedLetterView.setTitleText(album.PatientName.toUpperCase().charAt(0) + "");

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PatientGeneralInfo.class);
                intent.putExtra("Patient ID", album.PatientID);
                intent.putExtra("Doctor ID", doctorClass.DoctorID);
                intent.putExtra("Tag", true);
                mContext.startActivity(intent);
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //OpenMessages(album);
                checkGalasat(album);
            }
        });
    }

    @Override
    public int getItemCount() {
        return PatientClassList.size();
    }



    private void OpenMessages(final PatientClass aClass)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("MessagesBetweenDoctorPatient")
                .orderByChild("lastMessageTime");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                            if (issue.child("doctorUid").getValue().equals(doctorClass.DoctorID)&&
                                    issue.child("patientUid").getValue().equals(aClass.PatientID)  )
                            {
                               doctorPatientMessagesClass=new DoctorPatientMessagesClass(
                                        issue.child("doctorUid").getValue()+"",
                                        issue.child("doctorName").getValue()+"",
                                        issue.child("doctorPhoto").getValue()+"",
                                        issue.child("patientUid").getValue()+"",
                                        issue.child("patientName").getValue()+"",
                                        issue.getKey()+"",
                                        issue.child("lastMessageText").getValue()+"",
                                        (long)issue.child("lastMessageTime").getValue()
                                );
                               break;
                            }
                        }
                }


                Intent DoctorChatIntent=new Intent(mContext
                        ,DoctorChatActivity.class);
                DoctorChatIntent.putExtra("ChatClass", doctorPatientMessagesClass);
                DoctorChatIntent.putExtra("toResult",true);
                ((Activity) mContext).startActivityForResult(DoctorChatIntent,10);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void checkGalasat(final PatientClass aClass)
    {
        final boolean[] found = new boolean[1];
        //region getToday
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
        //endregion
        Query query=FirebaseDatabase.getInstance().getReference().child("Doctor Agenda").child(doctorClass.DoctorID)
                .child(DayOfDate+"").orderByChild("fromHour");


        final long finalTime = DayOfDate;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        long TotalStart = finalTime + Long.parseLong(issue.child("fromHour").getValue() + "");
                        long TotalEnd = finalTime + Long.parseLong(issue.child("toHour").getValue() + "");
                        long TotalNow=Calendar.getInstance().getTime().getTime();
                        if (TotalStart-TotalNow<=(10*60*1000)&&TotalNow<=TotalEnd
                                &&aClass.PatientID.equals(issue.child("patientID").getValue()))
                        {
                            found[0] =true;
                            break;
                        }
                        else {
                            found[0]=false;
                        }
                    }
                }

                if (found[0])
                {
                    OpenMessages(aClass);
                }
                else
                {
                    Toast.makeText(mContext, "There is No Session Now", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }








}
