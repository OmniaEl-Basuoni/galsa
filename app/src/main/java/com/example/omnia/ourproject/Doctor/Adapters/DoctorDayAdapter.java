package com.example.omnia.ourproject.Doctor.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.omnia.ourproject.Doctor.Classes.Dates;
import com.example.omnia.ourproject.R;
import java.util.List;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by Menna Emad on 27/03/2018.
 */

public class DoctorDayAdapter extends BaseAdapter {


        Context context;
        List<Dates> DoctorDatesList;
        private LinearLayout.LayoutParams params;

        public DoctorDayAdapter(Context context,
                                 List<Dates> DoctorDatesList)
        {
            this.context=context;
            this.DoctorDatesList = DoctorDatesList;
            params = new LinearLayout.
                    LayoutParams(WRAP_CONTENT,WRAP_CONTENT);
        }

        @Override
        public int getCount() {
            return DoctorDatesList.size();
        }

        @Override
        public Object getItem(int i) {
            return DoctorDatesList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return DoctorDatesList.indexOf(getItem(i));
        }

        class ViewHolder
        {
            TextView Name,Time;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            ViewHolder holder=null;

            LayoutInflater mIFlater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            View row=mIFlater.inflate(R.layout.view_date,viewGroup,false);

           holder=new ViewHolder();

            holder.Name=row.findViewById(R.id.name);
            holder.Time=row.findViewById(R.id.time);

            holder.Name.setText(DoctorDatesList.get(i).getName());
            holder.Name.setText(DoctorDatesList.get(i).getTime());

            return row;
        }
    }

