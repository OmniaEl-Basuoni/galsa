package com.example.omnia.ourproject.Patient.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omnia.ourproject.Doctor.Classes.PatientDate;
import com.example.omnia.ourproject.Patient.Activities.PatientHelpActivity;
import com.example.omnia.ourproject.Patient.Classes.HospitalInfo;
import com.example.omnia.ourproject.R;

import java.util.List;

/**
 * Created by Omnia on 7/6/2018.
 */

public class HelpAdapter extends BaseAdapter {

    public Context context;
    public List<HospitalInfo> hospitalInfoList ;

    public HelpAdapter(PatientHelpActivity patientHelpActivity, List<HospitalInfo> hospitalInfoList) {
        this.context = patientHelpActivity;
        this.hospitalInfoList = hospitalInfoList;
    }

    @Override
    public int getCount() {
        return hospitalInfoList.size();
    }

    @Override
    public Object getItem(int i) {
        return hospitalInfoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return hospitalInfoList.indexOf(getItem(i));
    }


    class ViewHolder
    {
        TextView HospitalName;
        TextView HospitalAddress;
        TextView HospitalPhone;
    }



    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        HelpAdapter.ViewHolder holder=null;

        LayoutInflater mIFlater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View row=mIFlater.inflate(R.layout.help_view,viewGroup,false);

        holder=new ViewHolder();

            holder.HospitalName=row.findViewById(R.id.hospitalName);
            holder.HospitalAddress= row.findViewById(R.id.hospitalAddress);
            holder.HospitalPhone= row.findViewById(R.id.hospitalPhone);


            holder.HospitalName.setText(hospitalInfoList.get(i).getHospitalName());
            holder.HospitalAddress.setText(hospitalInfoList.get(i).getHospitalAddress());

            holder.HospitalPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", hospitalInfoList.get(i).getHospitalPhone(), null)));
                }
            });


        return row;    }
}
