package com.example.omnia.ourproject.Doctor.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omnia.ourproject.Doctor.Classes.AgendaHomeClass;
import com.example.omnia.ourproject.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Omnia on 12/3/2017.
 */

public class DoctorHomeAdapter extends RecyclerView.Adapter<DoctorHomeAdapter.MyViewHolder> {

    private SimpleDateFormat format = new SimpleDateFormat("HH:mm");
    private Context mContext;
    private List<AgendaHomeClass> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewstartTime,textViewPatientname,textViewendTime;
        public CardView cardView;


        public MyViewHolder(View view) {
            super(view);
            textViewstartTime =  view.findViewById(R.id.TV_startTime);
            textViewPatientname =  view.findViewById(R.id.TV_PatientName);
            textViewendTime=view.findViewById(R.id.TV_endTime);
            cardView=view.findViewById(R.id.CV_);

        }
    }


    public DoctorHomeAdapter(Context mContext, List<AgendaHomeClass> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_agenda, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        String s="";
        AgendaHomeClass album = albumList.get(position);
        try{

            holder.textViewstartTime.setText("From  : " + format.format((Double.parseDouble(album.Date)-7200000)));
              holder.textViewendTime.setText("To    : " + format.format((Double.parseDouble(album.Time)-7200000)));
            holder.textViewPatientname.setText(album.PatientName);
        }
        catch (Exception e)
        {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if(album.PatientName.isEmpty())
        {
            holder.textViewstartTime.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            holder.cardView.setVisibility(View.GONE);
        }




    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }


}
