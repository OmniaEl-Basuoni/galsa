package com.example.omnia.ourproject.Patient.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omnia.ourproject.Doctor.Classes.PatientDate;
import com.example.omnia.ourproject.Patient.Classes.PatientClass;
import com.example.omnia.ourproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PatientGroupDetailsActivity extends AppCompatActivity {

    private PatientClass aClass;
    private DatabaseReference reference ;
    private String GroupID,LastGroup;
    private TextView textView_GroupName,textView_GroupDescription,textView_GroupDay
            ,textView_GroupTime,textView_GroupLimit,textView_GroupCount;
    Button buttonJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);
        InitComponent();
        getGroupData();

        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckExist();
            }
        });

    }

    private void getGroupData() {
        Query query = reference.child("Group Support");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for (DataSnapshot snapshot:dataSnapshot.getChildren())
                    {
                        if(GroupID.equals(snapshot.child("Group ID").getValue()+""))
                        {
                            LastGroup=snapshot.child("Last Group").getValue()+"";
                            textView_GroupCount.setText(snapshot.child("Group Members").child(LastGroup).getChildrenCount()+"");
                            textView_GroupName.setText(snapshot.getKey());
                            textView_GroupDescription.setText(snapshot.child("Group Description").getValue()+"");
                            textView_GroupDay.setText(snapshot.child("Group Day").getValue()+"");
                            textView_GroupTime.setText(snapshot.child("Group Time").getValue()+"");
                            textView_GroupLimit.setText(snapshot.child("Group Capicity").getValue()+"");
                            break;
                        }
                    }
                }
                else {
                    Toast.makeText(PatientGroupDetailsActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void InitComponent() {
        getIntentData();
        reference= FirebaseDatabase.getInstance().getReference();
        textView_GroupName=findViewById(R.id.textView_groupName);
        textView_GroupDescription=findViewById(R.id.textView_groupDescription);
        textView_GroupDay=findViewById(R.id.textView_groupDay);
        textView_GroupTime=findViewById(R.id.textView_groupTime);
        textView_GroupLimit=findViewById(R.id.textView_groupCapicity);
        textView_GroupCount=findViewById(R.id.textView_groupMember);
        buttonJoin=findViewById(R.id.button_Join);
    }

    private void getIntentData() {
        Bundle extras=getIntent().getExtras();
        if ((!extras.isEmpty())) {
            GroupID = extras.getString("GroupID");
            aClass= (PatientClass) extras.getSerializable("PatientClass");
        }
        else {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
    }

    private void Join(boolean valid)
    {
        if(CheckLater())
        {
            if (valid)
            {
                reference.child("Group Support").child(textView_GroupName.getText()+"")
                        .child("Group Members").child(LastGroup).child(Integer.parseInt(textView_GroupCount.getText()+"")+"").setValue(aClass.PatientID) ;


                FirebaseDatabase firebasedatabase;

                firebasedatabase= FirebaseDatabase.getInstance();


                reference = firebasedatabase.getReferenceFromUrl
                        ("https://jalsa-e9ec7.firebaseio.com/Patient Agenda/"+ aClass.PatientID+"/"+LastGroup+"" );

                DatabaseReference idd=reference.push();
                idd.setValue(new PatientDate(textView_GroupName.getText()+"","",Long.parseLong(LastGroup),"",
                        aClass.PatientID,aClass.PatientName,"",ConvertToMS("7"),ConvertToMS("8")));



            }
            else {
                Toast.makeText(this, "Exist", Toast.LENGTH_SHORT).show();
            }
        }

    }
    private void CheckExist()
    {
        final boolean[] isValid = new boolean[1];
        Query query=reference.child("Group Support").child(textView_GroupName.getText()+"")
                .child("Group Members").child(LastGroup);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    for(DataSnapshot issue:dataSnapshot.getChildren())
                    {
                        if (aClass.PatientID.equals(issue.getValue()))
                        {
                            Log.d("AAAA","ttttttttt");
                            isValid[0] =false;
                            break;
                        }
                        else {
                            isValid[0]=true;
                        }

                    }
                }
                else {
                    isValid[0]=true;
                }
                Log.d("AAAA",isValid[0]+"");
                Join(isValid[0]);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean CheckLater()
    {
        if (DateOfDay()>Long.parseLong(LastGroup))
        {
            Toast.makeText(this, "You are late .... Wait next Group", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            return true;
    }

    private long ConvertToMS(String o){

        long num = Long.parseLong(o);

        num*=60*60*1000;

        return num;
    }
    private long DateOfDay()
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

        return DayOfDate;
    }
}