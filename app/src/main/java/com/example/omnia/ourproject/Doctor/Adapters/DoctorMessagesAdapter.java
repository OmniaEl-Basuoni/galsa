package com.example.omnia.ourproject.Doctor.Adapters;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.omnia.ourproject.SharedClasses.DoctorPatientMessagesClass;
import com.example.omnia.ourproject.R;
import com.github.pavlospt.roundedletterview.RoundedLetterView;

import java.util.List;

/**
 * Created by 3ZT on 03-Dec-17.
 */

public class DoctorMessagesAdapter extends BaseAdapter {
    Context context;
    List<DoctorPatientMessagesClass> doctorPatientMessagesClassList;


    public DoctorMessagesAdapter(Context context, List<DoctorPatientMessagesClass> doctorPatientMessagesClassList)
    {
        this.context=context;
        this.doctorPatientMessagesClassList = doctorPatientMessagesClassList;
    }

    @Override
    public int getCount() {
        return doctorPatientMessagesClassList.size();
    }

    @Override
    public Object getItem(int i) {
       return doctorPatientMessagesClassList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return doctorPatientMessagesClassList.indexOf(getItem(i));
    }

    class ViewHolder
    {
        RoundedLetterView roundedLetterView;
        TextView PatientName;
        TextView LastMessageText;
        TextView LastMessageTime;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder=null;

        LayoutInflater mIFlater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View row=mIFlater.inflate(R.layout.view_doctor_message,viewGroup,false);

        holder=new ViewHolder();

        holder.PatientName=row.findViewById(R.id.TV_Name);
        holder.LastMessageText= row.findViewById(R.id.TV_MessageText);
        holder.LastMessageTime= row.findViewById(R.id.TV_MessageTime);
        holder.roundedLetterView=row.findViewById(R.id.rlv_name_view);



        holder.PatientName.setText(doctorPatientMessagesClassList.get(i).getPatientName());
        holder.LastMessageText.setText(doctorPatientMessagesClassList.get(i).getLastMessageText());
        holder.LastMessageTime.setText(DateFormat.format
                ("HH:mm",doctorPatientMessagesClassList.get(i).getLastMessageTime()));
        holder.roundedLetterView.setTitleText(doctorPatientMessagesClassList
                .get(i).getPatientName().toUpperCase().charAt(0)+"");



        return row;
    }
}
