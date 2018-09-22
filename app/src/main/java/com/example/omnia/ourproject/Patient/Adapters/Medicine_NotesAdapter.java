package com.example.omnia.ourproject.Patient.Adapters;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.omnia.ourproject.Doctor.Adapters.DoctorDayAdapter;
import com.example.omnia.ourproject.Doctor.Classes.Dates;
import com.example.omnia.ourproject.Patient.Classes.nodeClass;
import com.example.omnia.ourproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Omnia on 5/12/2018.
 */

public class Medicine_NotesAdapter extends BaseAdapter {
    Context context;
    private List<String> item=new ArrayList<>();
    public Medicine_NotesAdapter(Context context, List<String> item) {
        this.context = context;
        this.item=item;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int i) {
        return item.get(i);
    }

    @Override
    public long getItemId(int i) {
        return item.indexOf(getItem(i));
    }


    class ViewHolder
    {
        TextView Item;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Medicine_NotesAdapter.ViewHolder holder=null;

        LayoutInflater mIFlater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View row=mIFlater.inflate(R.layout.medicine_notes_item,viewGroup,false);

        holder=new ViewHolder();

        holder.Item=row.findViewById(R.id.item);


        holder.Item.setText(item.get(i));


        return row;
    }
}
