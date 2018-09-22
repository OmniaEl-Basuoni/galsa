package com.example.omnia.ourproject.SharedClasses;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omnia.ourproject.Patient.Activities.PatientGroupDetailsActivity;
import com.example.omnia.ourproject.Patient.Classes.PatientClass;
import com.example.omnia.ourproject.R;

import java.util.Date;
import java.util.List;

/**
 * Created by 3ZT on 10/23/2017.
 */

public class TherapyAdapter extends RecyclerView.Adapter<TherapyAdapter.MyViewHolder> {

    PatientClass aClass;
    Context context;
    List<GroupClass> TherapyClassList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title,time,date;
        public CardView CardviewTherapy;

        public MyViewHolder(View view) {
            super(view);
            title =  view.findViewById(R.id.TV_Name);
            date =  view.findViewById(R.id.TV_DDate);
            time=view.findViewById(R.id.TV_Date);
            CardviewTherapy=view.findViewById(R.id.CardviewTherapy);
        }
    }
    public TherapyAdapter(Context mContext, List<GroupClass> therapyClassList,PatientClass patientClass) {
        this.context = mContext;
        this.TherapyClassList = therapyClassList;
        this.aClass=patientClass;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_groups, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final GroupClass album = TherapyClassList.get(position);

        try {


            holder.title.setText(album.getGroupName());
            holder.date.setText(album.getGroupDateDay().substring(0,3));
            holder.time.setText(album.getGroupDateTime());

            holder.CardviewTherapy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, PatientGroupDetailsActivity.class);
                    intent.putExtra("GroupID",album.getGroupID()+"");
                    intent.putExtra("PatientClass",aClass);
                    context.startActivity(intent);
                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public int getItemCount() {
        return TherapyClassList.size();
    }


}
