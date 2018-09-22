package com.example.omnia.ourproject.Doctor.EditFragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.omnia.ourproject.AutoCompleteCountry.Adapter.CountryAdapter;
import com.example.omnia.ourproject.AutoCompleteCountry.Class.CountriesClass;
import com.example.omnia.ourproject.AutoCompleteCountry.Class.Country;
import com.example.omnia.ourproject.AutoCompleteCountry.Remote.ApiUtlis;
import com.example.omnia.ourproject.AutoCompleteCountry.Remote.UserService;
import com.example.omnia.ourproject.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@SuppressLint("ValidFragment")
public class GeneralInfoFragment extends Fragment {

    private CountryAdapter countryAdapter;
    private ArrayList<Country> countryList=new ArrayList<>();
    private AutoCompleteTextView completeTextView;
    private UserService userService;

    private String UID;
    private View view;
    private String name,country,phone;
    private String category;
    private EditText editTextName,editTextPhone,editTextCategory;
    private TagContainerLayout containerLayout;
    private Button buttonAdd,buttonFinish;

    ArrayList<String> categorylist=new ArrayList<>();
    @SuppressLint("ValidFragment")
    public GeneralInfoFragment(String name, String category, String country, String email, String phone,String UID ) {
        this.name=name;
        this.category=category;
        this.country=country;
        this.phone=phone;
        this.UID=UID;
        String [] a=category.replace("[","").replace("]","").split(",");
        for (String s:a)
        {
            categorylist.add(s);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_general_info, container, false);
        InitComponent();
        SetData();
        AutoCountry();
        containerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
            }

            @Override
            public void onTagLongClick(int position, String text) {
                categorylist.remove(position);
                containerLayout.setTags(categorylist);
            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });

        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveChanges();
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categorylist.add(editTextCategory.getText()+"");
                containerLayout.setTags(categorylist);
            }
        });
        return view;
    }

    private void SaveChanges() {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        reference.child("Doctor").child(UID).child("DoctorName").setValue(editTextName.getText()+"");
        reference.child("Doctor").child(UID).child("Doctor Country").setValue(completeTextView.getText()+"");
        reference.child("Doctor").child(UID).child("Category").setValue(categorylist);
        getActivity().finish();
    }

    private void SetData() {
        editTextName.setText(name);
        editTextPhone.setText(phone);
        completeTextView.setText(country);
        containerLayout.setTags(categorylist);
    }

    private void InitComponent() {

        completeTextView=view.findViewById(R.id.ET_Country);
        userService= ApiUtlis.getUserService();


        editTextName=view.findViewById(R.id.ET_name);
        editTextPhone=view.findViewById(R.id.ET_phone);
        editTextCategory=view.findViewById(R.id.ET_Category);

        containerLayout=view.findViewById(R.id.category_container);

        buttonAdd=view.findViewById(R.id.BT_add);
        buttonFinish=view.findViewById(R.id.BT_finish);
    }

    private void AutoCountry()
    {
        Call<CountriesClass> call=userService.Auto();
        call.enqueue(new Callback<CountriesClass>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<CountriesClass> call, Response<CountriesClass> response) {
                if(response.isSuccessful())
                {
                    countryList=response.body().getCountries();
                    countryAdapter=new CountryAdapter(getContext(),R.layout.view_spinner,countryList);
                    completeTextView.setAdapter(countryAdapter);
                    completeTextView.setThreshold(1);
                }
                else {
                    Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CountriesClass> call, Throwable t) {

            }
        });
    }

}
