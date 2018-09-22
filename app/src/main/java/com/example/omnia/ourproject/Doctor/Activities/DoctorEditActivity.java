package com.example.omnia.ourproject.Doctor.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.omnia.ourproject.Doctor.EditFragments.EducationInfoFragment;
import com.example.omnia.ourproject.Doctor.EditFragments.GeneralInfoFragment;
import com.example.omnia.ourproject.R;

public class DoctorEditActivity extends AppCompatActivity {


    private Fragment fragment;
    private FragmentManager fragmentManager;
    private String Key, name, phone,country, category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_edit);


        getExtra();
    }


    private void getExtra (){
        fragmentManager = getFragmentManager();
        Bundle bundle =getIntent().getExtras();
        if(!(bundle.isEmpty()))
        {
            Key=bundle.getString("key");
            if(Key.equals("A"))
            {
                fragment=new GeneralInfoFragment(bundle.getString("name"),bundle.getString("category")
                ,bundle.getString("country"),bundle.getString("mail"),bundle.getString("phone"),bundle.getString("Uid"));
                fragmentManager.beginTransaction().replace(R.id.content, fragment)
                        .commit();
            }
            else if(Key.equals("B"))
            {
                fragment=new EducationInfoFragment(bundle.getString("Uid")
                        ,bundle.getString("university")
                        ,bundle.getString("certification"));
                fragmentManager.beginTransaction().replace(R.id.content, fragment)
                        .commit();
            }
            else {
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
