package com.example.omnia.ourproject.Patient.SearchPackage;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.omnia.ourproject.Doctor.Adapters.DoctorInfoAdapter;
import com.example.omnia.ourproject.Doctor.Adapters.SearchRequestAdapter;
import com.example.omnia.ourproject.Doctor.Adapters.SendRequestAdapter;
import com.example.omnia.ourproject.Doctor.Classes.DoctorClass;
import com.example.omnia.ourproject.Patient.Classes.PatientClass;
import com.example.omnia.ourproject.R;
import com.example.omnia.ourproject.SharedClasses.CheckBoxAdapter;
import com.example.omnia.ourproject.SharedClasses.GridSpacingItemDecoration;
import com.example.omnia.ourproject.SharedClasses.SharedParameters;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TrySearchActivity extends AppCompatActivity {
    boolean UseFilter=false;

    private SearchView searchView;
    private PatientClass aClass;

    private List<String> CountryList=new ArrayList<>();
    private List<String> CategoryList=new ArrayList<>();


    SearchRequestAdapter doctorInfoAdapter;
    CheckBoxAdapter checkBoxAdapter;
    List<DoctorClass> doctorClassList=new ArrayList<>();
    List<DoctorClass>doctorClassListModified=new ArrayList<>();

    RecyclerView rv,country,category;
    Spinner spinner;
    CheckBox BoxMale,BoxFemale;

    private FloatingActionButton floatingActionButton;
    private BottomSheetBehavior bottomSheetBehavior;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_try_search);

        GetData();

        InitComponent();

        //region DoctorProfileMenu
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

        final LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.menu_search_view, null);
        searchView = v.findViewById(R.id.search);
        actionBar.setCustomView(v);
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    startActivityForResult(new Intent(TrySearchActivity.this, TrySearchTextActivity.class), 1);
                }
            }
        });



        //endregion

        DefaultSearch();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState==BottomSheetBehavior.STATE_HIDDEN)
                {
                    FilterSearc(doctorClassList);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(UseFilter)
                    Filter(position,doctorClassListModified);
                else
                    Filter(position,doctorClassList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });







    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            Search(data.getExtras().getString("AAAA"));
            searchView.setQuery(data.getExtras().getString("AAAA"),false);
        }
    }

    private void InitComponent() {

        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout));
        floatingActionButton=findViewById(R.id.fab3);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        //region DoctorRecycleView
        rv= findViewById(R.id.recyclerview_search);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(TrySearchActivity.this, 1);
        rv.setLayoutManager(mLayoutManager);
        rv.addItemDecoration(new GridSpacingItemDecoration(1,5,true,TrySearchActivity.this));
        rv.setItemAnimator(new DefaultItemAnimator());
        //endregion
        //region CountryRecycleView
        country= findViewById(R.id.rv_country);
        RecyclerView.LayoutManager LayoutManager = new GridLayoutManager(TrySearchActivity.this, 2);
        country.setLayoutManager(LayoutManager);
        country.addItemDecoration(new GridSpacingItemDecoration(2,5,true,TrySearchActivity.this));
        country.setItemAnimator(new DefaultItemAnimator());
        //endregion
        //region CategoryRecycleView
        category= findViewById(R.id.rv_category);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(TrySearchActivity.this, 2);
        category.setLayoutManager(layoutManager);
        category.addItemDecoration(new GridSpacingItemDecoration(2,5,true,TrySearchActivity.this));
        category.setItemAnimator(new DefaultItemAnimator());
        //endregion
        BoxMale=findViewById(R.id.RB_Male);
        BoxFemale=findViewById(R.id.RB_Female);
        //region Spinner
        spinner =  findViewById(R.id.spinner);

        List<String> Filterlist=new ArrayList<>();
        Filterlist.add("Rate");
        Filterlist.add("Price");
        Filterlist.add("A->Z");
        Filterlist.add("Z->A");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this
                , android.R.layout.simple_spinner_item, Filterlist);

        spinner.setAdapter(dataAdapter);
        //endregion

    }

    @Override
    public void onBackPressed() {
        if(bottomSheetBehavior.getState()!=BottomSheetBehavior.STATE_HIDDEN)
        {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
        else
        super.onBackPressed();
    }

    private void Filter (int i, List<DoctorClass>doctorClassList)
    {

        try {
            switch (i)
            {
                case 0:

                    //region RateOlder
                    Collections.sort(doctorClassList, new Comparator<DoctorClass>(){

                        @Override
                        public int compare(DoctorClass doctorPatientMessagesClass, DoctorClass t1) {

                            return (doctorPatientMessagesClass.DoctorRate < t1.DoctorRate)
                                    ? -1: (doctorPatientMessagesClass.DoctorRate > t1.DoctorRate)
                                    ? 1:0 ;
                        }
                    });
                    Collections.reverse(doctorClassList);
                    //endregion
                    break;


                case 1:

                    //region Price
                    Collections.sort(doctorClassList, new Comparator<DoctorClass>(){

                        @Override
                        public int compare(DoctorClass doctorPatientMessagesClass, DoctorClass t1) {

                            return (Integer.parseInt(doctorPatientMessagesClass.DoctorName)
                                    > Integer.parseInt(t1.DoctorName))
                                    ? -1: (Integer.parseInt(doctorPatientMessagesClass.DoctorName) > (Integer.parseInt(t1.DoctorName)))
                                    ? 1:0 ;
                        }
                    });
                    //endregion
                    break;


                case 2:

                    //region DoctorName
                    Collections.sort(doctorClassList, new Comparator<DoctorClass>(){

                        @Override
                        public int compare(DoctorClass doctorPatientMessagesClass, DoctorClass t1) {

                            int compare = doctorPatientMessagesClass.DoctorName.compareTo(t1.DoctorName);

                            return (compare<0)
                                    ? -1
                                    : (compare>0)
                                    ? 1
                                    :0 ;
                        }
                    });
                    //endregion
                    break;



                case 3:

                    //region DoctorName
                    Collections.sort(doctorClassList, new Comparator<DoctorClass>(){

                        @Override
                        public int compare(DoctorClass doctorPatientMessagesClass, DoctorClass t1) {

                            int compare = doctorPatientMessagesClass.DoctorName.compareTo(t1.DoctorName);

                            return (compare>0)
                                    ? -1
                                    : (compare>0)
                                    ? 1
                                    :0 ;
                        }
                    });
                    //endregion
                    break;
            }
            doctorInfoAdapter = new SearchRequestAdapter(this, doctorClassList,aClass);
            rv.setAdapter(doctorInfoAdapter);
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void GetData()
    {
        Bundle bundle=getIntent().getExtras();
        if(!bundle.isEmpty())
        {
            aClass= (PatientClass) bundle.getSerializable("PatientClass");
        }
    }


    private void Search(final String nameDoc) {
        UseFilter=false;
        CategoryList.clear();
        CountryList.clear();
        doctorClassList.clear();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Doctor").orderByChild("DoctorRate");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    try {
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {

                            if (issue.getChildrenCount()>=14)
                            {
                                if ((issue.child("DoctorName").getValue()+"").toLowerCase().contains(nameDoc.toLowerCase()))
                                {
                                    if (!(CountryList.contains(issue.child("Doctor Country").getValue()+"")))
                                    {
                                        CountryList.add(issue.child("Doctor Country").getValue()+"");
                                    }
                                    doctorClassList.add(new DoctorClass(
                                            issue.getKey()+"",
                                            issue.child("DoctorName").getValue() + "",
                                            issue.child("PersonalPhoto").getValue() + ""
                                            , Double.parseDouble(issue.child("DoctorRate").getValue() + "")
                                            , ConvertToList(issue.child("Category").getChildren()),
                                            issue.child("DoctorGender").getValue()+""
                                            , Double.parseDouble(issue.child("Doctor PriceHalf").getValue() + "")
                                            , Double.parseDouble(issue.child("Doctor PriceHour").getValue() + "")
                                    ));

                                }
                            }
                        }
                    } catch (Exception Ex) {
                        Toast.makeText(TrySearchActivity.this, Ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(TrySearchActivity.this, "Not places in this Category", Toast.LENGTH_LONG).show();
                }



                doctorInfoAdapter = new SearchRequestAdapter(TrySearchActivity.this, doctorClassList,aClass);
                rv.setAdapter(doctorInfoAdapter);


                checkBoxAdapter = new CheckBoxAdapter(TrySearchActivity.this, CountryList);
                country.setAdapter(checkBoxAdapter);

                checkBoxAdapter = new CheckBoxAdapter(TrySearchActivity.this, CategoryList);
                category.setAdapter(checkBoxAdapter);

                SharedParameters.CategoriesList=CategoryList;


                Filter(0,doctorClassList);



            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    private void DefaultSearch()
    {
        UseFilter=false;
        CategoryList.clear();
        CountryList.clear();
        doctorClassList.clear();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Doctor").orderByChild("DoctorRate");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    try {
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {

                            if (issue.getChildrenCount()>=14)
                            {
                                if (!(CountryList.contains(issue.child("Doctor Country").getValue()+"")))
                                {
                                    CountryList.add(issue.child("Doctor Country").getValue()+"");
                                }
                                doctorClassList.add(new DoctorClass(
                                        issue.getKey()+"",
                                        issue.child("DoctorName").getValue() + "",
                                        issue.child("PersonalPhoto").getValue() + ""
                                        , Double.parseDouble(issue.child("DoctorRate").getValue() + "")
                                        , ConvertToList(issue.child("Category").getChildren()),
                                        issue.child("DoctorGender").getValue()+""
                                        , Double.parseDouble(issue.child("Doctor PriceHalf").getValue() + "")
                                        , Double.parseDouble(issue.child("Doctor PriceHour").getValue() + "")
                                ));
                            }

                        }
                    } catch (Exception Ex) {
                        Toast.makeText(TrySearchActivity.this, Ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(TrySearchActivity.this, "Not places in this Category", Toast.LENGTH_LONG).show();
                }


                doctorInfoAdapter = new SearchRequestAdapter(TrySearchActivity.this, doctorClassList,aClass);
                rv.setAdapter(doctorInfoAdapter);


                checkBoxAdapter = new CheckBoxAdapter(TrySearchActivity.this, CountryList);
                country.setAdapter(checkBoxAdapter);



                checkBoxAdapter = new CheckBoxAdapter(TrySearchActivity.this, CategoryList);
                category.setAdapter(checkBoxAdapter);

                SharedParameters.CategoriesList=CategoryList;


                Filter(0,doctorClassList);


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void FilterSearc(List<DoctorClass> doctorClassList)
    {
        doctorClassListModified.clear();
        if((!BoxFemale.isChecked())||(!BoxMale.isChecked()))
        {
            UseFilter=true;
            if(BoxMale.isChecked())
            {
                for (DoctorClass doctorClass:doctorClassList)
                {
                    if (doctorClass.DoctorGender.equals("Male"))
                    {
                        for(String item:doctorClass.Categories)
                        {
                            if (SharedParameters.CategoriesList.contains(item)&&(!doctorClassListModified.contains(doctorClass)))
                            {
                                doctorClassListModified.add(doctorClass);
                            }
                        }
                    }
                }
            }
            else if(BoxFemale.isChecked())
            {
                for (DoctorClass doctorClass:doctorClassList)
                {
                    if (doctorClass.DoctorGender.equals("Female"))
                    {
                        for(String item:doctorClass.Categories)
                        {
                            if (SharedParameters.CategoriesList.contains(item)&&(!doctorClassListModified.contains(doctorClass)))
                            {
                                doctorClassListModified.add(doctorClass);
                            }
                        }
                    }
                }
            }
        }
        else
        {
            Log.d("AAAA",SharedParameters.CategoriesList.toString());
                for (DoctorClass doctorClass:doctorClassList)
                {
                    for(String item:doctorClass.Categories)
                    {
                        if (SharedParameters.CategoriesList.contains(item)&&(!doctorClassListModified.contains(doctorClass)))
                        {
                            doctorClassListModified.add(doctorClass);
                        }
                        else {
                            UseFilter=true;
                        }
                    }
                }

        }



        doctorInfoAdapter = new SearchRequestAdapter(TrySearchActivity.this, doctorClassListModified,aClass);
        rv.setAdapter(doctorInfoAdapter);



    }




    private List<String> ConvertToList(Iterable<DataSnapshot> Cat)
    {
        List<String> list=new ArrayList<>();
        for (DataSnapshot c:Cat)
        {
            list.add(c.getValue()+"");
            if(!(CategoryList.contains(c.getValue()+"")))
                CategoryList.add(c.getValue()+"");
        }
        return list;
    }
}
