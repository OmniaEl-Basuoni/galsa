package com.example.omnia.ourproject.Doctor.DashboardFragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omnia.ourproject.Doctor.Classes.DoctorClass;
import com.example.omnia.ourproject.R;
import com.example.omnia.ourproject.SharedClasses.ComplainsAdapter;
import com.example.omnia.ourproject.SharedClasses.ComplainsClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 3ZT on 03-Dec-17.
 */

@SuppressLint("ValidFragment")
public class ComplainsFragment extends Fragment {
    private Dialog dialog;
    DoctorClass aClass;
    DatabaseReference reference;
    private View view;
    ListView listView;
    List<ComplainsClass>complainsClassList=new ArrayList<>();
    ComplainsAdapter complainsAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_complains,container,false);
        Init();
        FillComplains();

        return view;
    }

    @SuppressLint("ValidFragment")
    public ComplainsFragment(DoctorClass aClass) {
        this.aClass = aClass;
    }

    private void Init()
    {
        reference = FirebaseDatabase.getInstance().getReference();
        listView= (ListView) view.findViewById(R.id.listView_Complains);
    }
    private void FillComplains()
    {

        Query query = reference.child("Complains").child(aClass.DoctorID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    complainsClassList.clear();
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                            complainsClassList.add(new ComplainsClass(
                                             issue.getKey()+""
                                            ,issue.child("Complain Text").getValue()+""
                                            , Long.parseLong(issue.child("Complain Date").getValue()+"")
                                            ,issue.child("Complain State").getValue()+""
                                            ,issue.child("Patient ID").getValue()+""
                            ));
                        }
                }
                else {
                    Toast.makeText(getActivity(), "Not places in this Category", Toast.LENGTH_LONG).show();
                }
                complainsAdapter=new ComplainsAdapter(getActivity(),complainsClassList);
                listView.setAdapter(complainsAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ComplainsClass complainsClass= (ComplainsClass) adapterView.getAdapter().getItem(i);
                ShowDialog(complainsClass);
            }
        });
    }


    private void ShowDialog(ComplainsClass complainsClass){

        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.view_show_compains);

        final TextView textView=dialog.findViewById(R.id.text);
        textView.setText(complainsClass.getComplainsText());
        FirebaseDatabase.getInstance().getReference().child("Complains")
                .child(aClass.DoctorID).child(complainsClass.getComplainID()).child("Complain State")
                .setValue(0);
        dialog.show();

    }


}
