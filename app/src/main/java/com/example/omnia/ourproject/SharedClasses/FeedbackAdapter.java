package com.example.omnia.ourproject.SharedClasses;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.omnia.ourproject.R;

import java.util.List;
import java.util.Random;

/**
 * Created by Omnia on 2/24/2018.
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class FeedbackAdapter extends BaseAdapter {
    Context context;
    List<FeedbackClass> feedbackClassList;

    int[] colors;

    public FeedbackAdapter(Context context, List<FeedbackClass> feedbackClasses) {
        this.context = context;
        this.feedbackClassList = feedbackClasses;
         colors = new int[] {
                context.getResources().getColor(R.color.colorFeed),
                context.getResources().getColor(R.color.colorFeed1),
                context.getResources().getColor(R.color.colorFeed2),
                context.getResources().getColor(R.color.colorFeed3),
                context.getResources().getColor(R.color.colorFeed4),
                context.getResources().getColor(R.color.colorFeed5),
                context.getResources().getColor(R.color.colorFeed6),
                context.getResources().getColor(R.color.colorFeed7)
        };
    }

    @Override
    public int getCount() {
        return feedbackClassList.size();
    }

    @Override
    public Object getItem(int i) {
        return feedbackClassList.get(i).getFeedbackText();

    }

    @Override
    public long getItemId(int i) {
        return feedbackClassList.indexOf(getItem(i));
    }

    private class ViewHolder {
        TextView feedback_text;
        TextView date;
        RelativeLayout rl_Feedback;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;

        LayoutInflater mIFlater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View row = mIFlater.inflate(R.layout.view_feedback, viewGroup, false);

        holder = new FeedbackAdapter.ViewHolder();

        holder.feedback_text = row.findViewById(R.id.TV_FeedbackText);
        holder.date = row.findViewById(R.id.TV_Date);
        holder.rl_Feedback=row.findViewById(R.id.Rl_Feedback);

        // PublicPlaces publicPlaces=placesList.get(position);

        holder.feedback_text.setText(feedbackClassList.get(i).getFeedbackText());
        holder.date.setText( DateFormat.format("HH:mm",feedbackClassList.get(i).getFeedbackTime()));

        Random random=new Random();
        holder.rl_Feedback.setBackgroundColor(colors[random.nextInt(7)]);
        return row;
    }
}


