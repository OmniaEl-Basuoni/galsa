package com.example.omnia.ourproject.SharedClasses;

import com.example.omnia.ourproject.SharedActivity.FeedbackActivity;

import java.util.Date;

/**
 * Created by 3ZT on 10/23/2017.
 */

public class FeedbackClass  {
    private String FeedbackText;

    public FeedbackClass(String feedbackText) {
        FeedbackText = feedbackText;
        FeedbackTime=new Date().getTime();
    }

    private long FeedbackTime;

    public FeedbackClass(String feedbackText, long feedbackTime) {
        FeedbackText = feedbackText;
        FeedbackTime = feedbackTime;
    }



    public String getFeedbackText() {
        return FeedbackText;
    }

    public void setFeedbackText(String feedbackText) {
        FeedbackText = feedbackText;
    }

    public long getFeedbackTime() {
        return FeedbackTime;
    }

    public void setFeedbackTime(long feedbackTime) {
        FeedbackTime = feedbackTime;
    }



}
