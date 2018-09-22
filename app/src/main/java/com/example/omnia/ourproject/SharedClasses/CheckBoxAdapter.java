package com.example.omnia.ourproject.SharedClasses;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omnia.ourproject.Patient.Classes.PatientClass;
import com.example.omnia.ourproject.R;

import java.util.List;

public class CheckBoxAdapter extends RecyclerView.Adapter<CheckBoxAdapter.MyViewHolder> {
    Context context;
    List<String> ClassList;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public CheckBox checkBox;

        public MyViewHolder(View view) {
            super(view);
           checkBox=view.findViewById(R.id.checkbox);
        }
    }

    public CheckBoxAdapter(Context mContext,List<String> classList)
    {
        this.context=mContext;
        this.ClassList=classList;

    }
    @Override
    public CheckBoxAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CheckBoxAdapter.MyViewHolder holder, final int position) {

        final String album = ClassList.get(position);
        holder.checkBox.setText(album);
        holder.checkBox.setChecked(true);

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked)
                {
                    SharedParameters.CategoriesList.remove(holder.checkBox.getText()+"");
                }
                else
                {
                    SharedParameters.CategoriesList.add(holder.checkBox.getText()+"");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
return ClassList.size();    }
}
