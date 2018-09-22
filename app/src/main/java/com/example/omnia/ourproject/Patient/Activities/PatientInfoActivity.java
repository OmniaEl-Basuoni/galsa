package com.example.omnia.ourproject.Patient.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omnia.ourproject.Patient.Adapters.Medicine_NotesAdapter;
import com.example.omnia.ourproject.Patient.Classes.nodeClass;
import com.example.omnia.ourproject.R;

import java.util.ArrayList;
import java.util.List;

public class PatientInfoActivity extends AppCompatActivity {
    private String Patientnotes,medicine;
    private String date;
   private Medicine_NotesAdapter medicine_notesAdapter;
    private ListView listViewMedicine;
    private TextView listViewNotes,galsaDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_patient_info);
        jk();

        listViewNotes=findViewById(R.id.LV_Notes);
        listViewMedicine=(ListView)findViewById(R.id.LV_medicine);
        listViewNotes.setText(Patientnotes);
        medicine_notesAdapter=new Medicine_NotesAdapter(this,
                convertNodeClass(medicine.replace("[","").replace("]","")));
        listViewMedicine.setAdapter(medicine_notesAdapter);
        galsaDate=findViewById(R.id.TV_GalsaName);
        galsaDate.setText(date);
    }


    private void jk()
    {
        Bundle bundle=getIntent().getExtras();
        if(!(bundle.isEmpty()))
        {

            Patientnotes=bundle.getString("Patient Notes");
            medicine=bundle.getString("Patient Medicine");
            date=bundle.getString("Date");
        }
        else {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
    }

    private List<String> convertNodeClass(String medicine)
    {
        List<String> nodeClassList=new ArrayList<>();
        String []a=medicine.split(",");
        for (String s:a)
        {
            nodeClassList.add(s);
        }
        return nodeClassList;
    }
}
