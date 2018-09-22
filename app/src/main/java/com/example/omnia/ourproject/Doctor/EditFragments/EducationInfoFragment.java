package com.example.omnia.ourproject.Doctor.EditFragments;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.constraint.solver.widgets.ConstraintTableLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.omnia.ourproject.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class EducationInfoFragment extends Fragment {

    private View view;
    private String UID,University,certification;
    private ArrayList<String> Certification=new ArrayList<>();

    private EditText editTextUniversity,editTextCertification;
    private TagContainerLayout tagContainerLayout;
    private Button buttonAdd,buttonFinish;

    public EducationInfoFragment(String Uid,String University,String certification) {
        this.UID=Uid;
        this.University=University;
        this.certification=certification;
        String []a=certification.replace("[","").replace("]","").split(",");
        for (String s:a)
        {
            Certification.add(s);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_education_info, container, false);
        InitComponent();
        SetData();
        tagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
            }

            @Override
            public void onTagLongClick(int position, String text) {
                Certification.remove(position);
                tagContainerLayout.setTags(Certification);
            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Certification.add(editTextCertification.getText()+"");
                tagContainerLayout.setTags(Certification);
                editTextCertification.setText("");
            }
        });
        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveChange();
            }
        });
        return view;
    }

    private void SaveChange() {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        reference.child("Doctor").child(UID).child("Doctor University").setValue(editTextUniversity.getText()+"");
        reference.child("Doctor").child(UID).child("Doctor Certification").setValue(Certification);
        getActivity().finish();
    }

    private void SetData() {
        editTextUniversity.setText(University);
        tagContainerLayout.setTags(Certification);
    }

    private void InitComponent() {
        editTextUniversity=view.findViewById(R.id.ET_university);
        editTextCertification=view.findViewById(R.id.ET_Certification);
        tagContainerLayout=view.findViewById(R.id.category_container);
        buttonAdd=view.findViewById(R.id.BT_add);
        buttonFinish=view.findViewById(R.id.BT_finish);
    }


}
