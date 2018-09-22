package com.example.omnia.ourproject.SharedClasses;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.omnia.ourproject.R;

import java.util.List;

/**
 * Created by 3ZT on 03-Dec-17.
 */

public class ComplainsAdapter extends BaseAdapter {
    Context context;
    List<ComplainsClass> complainsClassList;
    public ComplainsAdapter(Context context, List<ComplainsClass>complainsClassList)
    {
        this.context=context;
        this.complainsClassList=complainsClassList;
    }

    @Override
    public int getCount() {
        return complainsClassList.size();
    }

    @Override
    public Object getItem(int i) {
       return complainsClassList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return complainsClassList.indexOf(getItem(i));
    }

    class ViewHolder
    {
        TextView text_name;
        TextView text_date;
        ImageView imageView_new;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder=null;

        LayoutInflater mIFlater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View row=mIFlater.inflate(R.layout.view_compains,viewGroup,false);

        holder=new ViewHolder();

        holder.imageView_new=row.findViewById(R.id.IV_New);
        holder.text_name= row.findViewById(R.id.TV_TestText);
        holder.text_date= row.findViewById(R.id.TV_TestDate);


        // PublicPlaces publicPlaces=placesList.get(position);

        holder.text_name.setText(complainsClassList.get(i).getComplainsText());
        holder.text_date.setText(complainsClassList.get(i).getComplainsDate());


        if(complainsClassList.get(i).getComplainsState().equals("0"))
        {
            holder.imageView_new.setVisibility(View.INVISIBLE);
            holder.text_name.setTextSize(12);
            holder.text_name.setTypeface(null, Typeface.NORMAL);
        }
        else if (complainsClassList.get(i).getComplainsState().equals("1"))
        {
            holder.imageView_new.setVisibility(View.VISIBLE);
            holder.text_name.setTextSize(16);
            holder.text_name.setTypeface(null, Typeface.BOLD);
        }
        return row;
    }
}
