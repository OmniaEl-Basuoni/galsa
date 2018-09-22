package com.example.omnia.ourproject.SharedClasses;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.omnia.ourproject.R;

import java.util.List;

/**
 * Created by 3ZT on 03-Dec-17.
 */

public class TestAdapter extends BaseAdapter {
    Context context;
    List<TestsClass> testsClassList;
    public TestAdapter(Context context, List<TestsClass>testsClassList)
    {
        this.context=context;
        this.testsClassList=testsClassList;
    }

    @Override
    public int getCount() {
        return testsClassList.size();
    }

    @Override
    public Object getItem(int i) {
       return testsClassList.get(i).TestName;
    }

    @Override
    public long getItemId(int i) {
        return testsClassList.indexOf(getItem(i));
    }

    private class ViewHolder
    {
        RelativeLayout relativeLayoutCat,relativeLayouttest;
        TextView text_cat;
        TextView text_name;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder = null;

        LayoutInflater mIFlater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View row = mIFlater.inflate(R.layout.view_tests, viewGroup, false);

        holder = new ViewHolder();

        holder.text_name = row.findViewById(R.id.TV_TestName);
        holder.text_cat = row.findViewById(R.id.Cat);
        holder.relativeLayoutCat = row.findViewById(R.id.RL_Category);
        holder.relativeLayouttest = row.findViewById(R.id.RL_Test);

        // PublicPlaces publicPlaces=placesList.get(position);

        if (testsClassList.get(i).IsCategory) {
            holder.relativeLayouttest.setVisibility(View.GONE);
            holder.relativeLayoutCat.setVisibility(View.VISIBLE);
        } else{
            holder.relativeLayouttest.setVisibility(View.VISIBLE);
            holder.relativeLayoutCat.setVisibility(View.GONE);
        }

        holder.text_name.setText(testsClassList.get(i).TestName);
        holder.text_cat.setText(testsClassList.get(i).CategoryName);

        return row;
    }
}
