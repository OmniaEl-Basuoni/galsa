package com.example.omnia.ourproject.SharedActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.omnia.ourproject.R;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class AppIntoActivity extends AppIntro2 {

    int i=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        addSlide(AppIntroFragment.newInstance("", "", R.drawable.ic_logo2
                                                     , getResources().getColor(R.color.color_light)));
        addSlide(AppIntroFragment.newInstance("", "", R.drawable.ic_logo
                                                      , getResources().getColor(R.color.colorPrimary)));



        showStatusBar(false);



        showSkipButton(true);
        //skipButton.setBackgroundColor(Color.GREEN);

        setProgressButtonEnabled(true);


        //setImageSkipButton(getResources().getDrawable(R.drawable.ic_cam));


        setZoomAnimation();
    }

    // Instead of fragments, you can also use our default slide
    // Just set a title, description, background and image. AppIntro will do the rest.


    @Override
    public void onSkipPressed() {
        /*Intent intent=new Intent(AppIntoActivity.this,StartActivity.class);
        startActivity(intent);
        */




        SetPreviousStarted();

        startActivity(new Intent(this,StartActivity.class));
        finish();
    }

    private void SetPreviousStarted()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean("State", false);
        if(!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean("State", Boolean.TRUE);
            edit.commit();
        }
    }





}

