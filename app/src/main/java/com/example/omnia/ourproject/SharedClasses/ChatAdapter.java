package com.example.omnia.ourproject.SharedClasses;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.omnia.ourproject.R;
import com.github.pavlospt.roundedletterview.RoundedLetterView;

import java.util.List;

/**
 * Created by Omnia on 2/2/2018.
 */

public class ChatAdapter extends BaseAdapter {

    Context context;
    List<ChatClass> chatClassList;

    public ChatAdapter(Context context, List<ChatClass> chatClassList)
    {
        this.context=context;
        this.chatClassList=chatClassList;
    }



    @Override
    public int getCount() {
        return chatClassList.size();
    }

    @Override
    public Object getItem(int i) {
        return chatClassList.get(i).PatientID;
    }

    @Override
    public long getItemId(int i) {
        return chatClassList.indexOf(getItem(i));   }

    private class ViewHolder
    {
        RoundedLetterView roundedLetterView;
        TextView PatientName;
        TextView MsgLastTime;

    }



    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        ViewHolder holder=null;
        LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View v=layoutInflater.inflate(R.layout.view_messages_indoctor,viewGroup,false);

        holder=new ViewHolder();
        holder.PatientName=(TextView) v.findViewById(R.id.TV_PatientName);
        holder.MsgLastTime=(TextView) v.findViewById(R.id.TV_Msglasttime);
        holder.roundedLetterView=(RoundedLetterView) v.findViewById(R.id.rlv_name_view);

        holder.PatientName.setText(chatClassList.get(i).PatientName);
        holder.MsgLastTime.setText(DateFormat.format("HH:MM",chatClassList.get(i).getMsgLastTime()));
        holder.roundedLetterView.setTitleText(chatClassList.get(i).PatientName.toUpperCase().charAt(0)+"");


        return v;

    }
}
