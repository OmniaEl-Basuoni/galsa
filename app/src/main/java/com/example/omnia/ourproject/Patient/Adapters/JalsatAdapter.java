package com.example.omnia.ourproject.Patient.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.omnia.ourproject.Patient.Activities.PatientGeneralInfo;
import com.example.omnia.ourproject.Patient.Activities.PatientInfoActivity;
import com.example.omnia.ourproject.SharedClasses.JalsatClass;
import com.example.omnia.ourproject.R;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Omnia on 5/9/2018.
 */

public class JalsatAdapter  extends Adapter<JalsatAdapter.MyViewHolder> {

    private Context mContext;
    private List<JalsatClass> jalsatClassList;
    private DatabaseReference reference;


    public JalsatAdapter(Context mContext, List<JalsatClass> jalsatClassList) {
        this.mContext = mContext;
        this.jalsatClassList = jalsatClassList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewDate;
        public CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.TV_Date);
            cardView = (CardView) itemView.findViewById(R.id.CV_Galsa);

        }
    }

    @Override
    public JalsatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.galsat_view, parent, false);

        JalsatAdapter.MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(JalsatAdapter.MyViewHolder holder, int position) {
        final JalsatClass album = jalsatClassList.get(position);
        holder.textViewDate.setText(album.getDate() + "");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PatientInfoActivity.class);
                intent.putExtra("Patient Notes",  album.getNotes());
                intent.putExtra("Patient Medicine", album.getMedicines());
                intent.putExtra("Date", album.getDate());
                mContext.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return jalsatClassList.size();
    }

}
