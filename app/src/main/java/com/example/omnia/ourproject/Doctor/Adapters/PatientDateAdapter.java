package com.example.omnia.ourproject.Doctor.Adapters;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omnia.ourproject.Doctor.Classes.PatientDate;
import com.example.omnia.ourproject.R;
import com.github.pavlospt.roundedletterview.RoundedLetterView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Menna Emad on 28/04/2018.
 */

public class PatientDateAdapter extends BaseAdapter {


    private SimpleDateFormat format = new SimpleDateFormat("HH:mm");
    public Context context;
    public List<PatientDate> patientDateList ;


    public PatientDateAdapter(Context context,List<PatientDate> patientDate)
    {
        this.context=context;
        this.patientDateList = patientDate;
    }

    @Override
    public int getCount() {
        return patientDateList.size();
    }

    @Override
    public Object getItem(int i) {
        return patientDateList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return patientDateList.indexOf(getItem(i));
    }


    class ViewHolder
    {
        TextView PatientName;
        TextView From;
        TextView To;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        ViewHolder holder=null;

        LayoutInflater mIFlater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View row=mIFlater.inflate(R.layout.view_patient_date,viewGroup,false);

        holder=new ViewHolder();

       try
       {
           holder.PatientName=row.findViewById(R.id.name);
           holder.From= row.findViewById(R.id.from);
           holder.To= row.findViewById(R.id.to);


           holder.PatientName.setText(patientDateList.get(i).getPatientName());
           holder.From.setText("From : " + format.format((patientDateList.get(i).getFromHour())-7200000));
           holder.To.setText(  "To   : " + format.format((patientDateList.get(i).getToHour())-7200000));
       }
       catch (Exception e)
       {
           Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
       }

        return row;

    }



}
