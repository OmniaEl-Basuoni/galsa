package com.example.omnia.ourproject.Patient.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.omnia.ourproject.R;
import com.example.omnia.ourproject.SharedClasses.DoctorPatientMessagesClass;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 3ZT on 03-Dec-17.
 */

public class PatientMessagesAdapter extends BaseAdapter {
    Context context;
    List<DoctorPatientMessagesClass> doctorPatientMessagesClassList;


    public PatientMessagesAdapter(Context context, List<DoctorPatientMessagesClass> doctorPatientMessagesClassList)
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
        CircleImageView photoView;
        TextView DoctorName;
        TextView LastMessageText;
        TextView LastMessageTime;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder=null;

        LayoutInflater mIFlater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View row=mIFlater.inflate(R.layout.view_messages_inpatient,viewGroup,false);

        holder=new ViewHolder();

        holder.DoctorName=row.findViewById(R.id.TV_Name);
        holder.LastMessageText= row.findViewById(R.id.TV_MessageText);
        holder.LastMessageTime= row.findViewById(R.id.TV_MessageTime);
        holder.photoView=row.findViewById(R.id.photoview);



        holder.DoctorName.setText(doctorPatientMessagesClassList.get(i).getDoctorName());
        holder.LastMessageText.setText(doctorPatientMessagesClassList.get(i).getLastMessageText());
        holder.LastMessageTime.setText(
                DateFormat.format
                        ("HH:mm",doctorPatientMessagesClassList.get(i).getLastMessageTime()));


        ImageLoader.getInstance().displayImage(doctorPatientMessagesClassList.get(i).getDoctorPhoto(),
                holder.photoView, new ImageLoadingListener() {
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


        return row;
    }
}
