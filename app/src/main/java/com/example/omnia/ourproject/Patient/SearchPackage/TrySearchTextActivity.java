package com.example.omnia.ourproject.Patient.SearchPackage;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.omnia.ourproject.R;

import java.util.Collections;
import java.util.List;

public class TrySearchTextActivity extends AppCompatActivity {
    SearchView searchView;
    sqlHelper db;
    ArrayAdapter<String> stringArrayAdapter;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_try_search_text);
        InitComponent();
        //region DoctorProfileMenu
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.menu_search_view, null);
        searchView=v.findViewById(R.id.search);
        actionBar.setCustomView(v);

        searchView.setActivated(true);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                db.insert(query);
                Intent intent = new Intent();
                intent.putExtra("AAAA", query);
                setResult(RESULT_OK, intent);
                finish();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(TrySearchTextActivity.this,  parent.getItemAtPosition(position)+"", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("AAAA", parent.getItemAtPosition(position)+"");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        //endregion
        ShowRecent();

    }
        private void InitComponent() {
            lv=(ListView) findViewById(R.id.LV_Search);
            db=new sqlHelper(this);
        }

        private void ShowRecent()
        {
            List<String> modellist = db.select();
            Collections.reverse(modellist);
            stringArrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,modellist);
            lv.setAdapter(stringArrayAdapter);
        }

}
