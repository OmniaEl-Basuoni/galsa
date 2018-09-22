package com.example.omnia.ourproject.Doctor.DashboardFragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.omnia.ourproject.Doctor.Classes.DoctorClass;
import com.example.omnia.ourproject.Patient.Classes.PatientClass;
import com.example.omnia.ourproject.Patient.Adapters.PatientInfoAdapter;
import com.example.omnia.ourproject.R;
import com.example.omnia.ourproject.SharedClasses.GridSpacingItemDecoration;
import com.example.omnia.ourproject.SharedClasses.SharedParameters;
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
public class PatientsFragment extends Fragment {
    DoctorClass aClass;
    View view;
    RecyclerView recyclerView;
    List<PatientClass> patientClassesList=new ArrayList<>();

    @SuppressLint("ValidFragment")
    public PatientsFragment(DoctorClass aClass) {
        this.aClass = aClass;
    }

    DatabaseReference reference;
    PatientInfoAdapter patientInfoAdapter;
    private GridSpacingItemDecoration gridSpacingItemDecoration;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_patients,container,false);
        //((AppCompatActivity) getActivity()).getSupportActionBar().show();



        Init();

        //setHasOptionsMenu(true);

        FillPatients();



        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem item = menu.findItem(R.id.menuSearch);

        final SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                //SearchText(query,SearchTagActivity.this);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {


                return false;
            }
        });

    }



    private void Init()
    {
        recyclerView=view.findViewById(R.id.recycler_view_Patient);
        reference = FirebaseDatabase.getInstance().getReference();
    }
    private void FillPatients()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Doctor_Patient").child(SharedParameters.DoctorUid).orderByChild("Patient Name");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    try {
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {

                            patientClassesList.add(new PatientClass
                                    (issue.child("Patient Name").getValue()+"", issue.child("Patient ID").getValue()+""));
                        }
                    }
                    catch (Exception Ex)
                    {
                        Toast.makeText(getActivity(), Ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getActivity(), "Not places in this Category", Toast.LENGTH_LONG).show();
                }

                patientInfoAdapter = new PatientInfoAdapter(getActivity(), patientClassesList,aClass);
                RecyclerView.LayoutManager mLayoutManager3 = new GridLayoutManager(getActivity(), 1);
                recyclerView.setLayoutManager(mLayoutManager3);
                gridSpacingItemDecoration=new GridSpacingItemDecoration (1,5,true,getActivity());
                recyclerView.addItemDecoration(gridSpacingItemDecoration);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(patientInfoAdapter);


                SharedParameters.patientClasses=patientClassesList;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
