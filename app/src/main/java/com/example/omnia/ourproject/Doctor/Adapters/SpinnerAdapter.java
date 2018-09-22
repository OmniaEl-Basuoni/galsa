package com.example.omnia.ourproject.Doctor.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.omnia.ourproject.Patient.Classes.PatientClass;
import com.example.omnia.ourproject.R;
import java.util.List;

/**
 * Created by Menna Emad on 05/04/2018.
 */

public class SpinnerAdapter extends BaseAdapter {

    Context context;
    List<PatientClass> patientClassList;

    public SpinnerAdapter(Context context, List<PatientClass> patientClassList) {
        this.context = context;
        this.patientClassList = patientClassList;
    }

    @Override
    public int getCount() {
        return patientClassList.size();
    }

    @Override
    public Object getItem(int i) {
        return patientClassList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return patientClassList.indexOf(patientClassList.get(i));
    }

    class ViewHolder
    {
        TextView PatientName;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder=null;

        LayoutInflater mIFlater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View row=mIFlater.inflate(R.layout.view_spinner,viewGroup,false);

        holder=new SpinnerAdapter.ViewHolder();

        holder.PatientName=row.findViewById(R.id.spinnerView);



        holder.PatientName.setText(patientClassList.get(i).PatientName);
        return row;

    }
}
