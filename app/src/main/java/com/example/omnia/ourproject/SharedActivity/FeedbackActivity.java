package com.example.omnia.ourproject.SharedActivity;

import android.app.Dialog;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.omnia.ourproject.R;
import com.example.omnia.ourproject.SharedClasses.FeedbackClass;
import com.example.omnia.ourproject.SharedClasses.FeedbackAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FeedbackActivity extends AppCompatActivity {

    private Dialog dialog;
    private FeedbackAdapter feedbackAdapter;
    private List<FeedbackClass> feedbackClassList = new ArrayList<FeedbackClass>();
    private ListView listViewFeedback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_shared_feedback);
        init();
        FillFeedbackList();

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addfeedbackmenu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.addfeedback:
                ShowDialog();
                return true;
        }
        return true;
    }


    private void ShowDialog()
    {
        dialog=new Dialog(FeedbackActivity.this);
        dialog.setContentView(R.layout.view_addfeedback);
        dialog.setTitle("Add Feedback ...");

        final EditText editText=dialog.findViewById(R.id.ET_Feedback);
        Button buttonSave=dialog.findViewById(R.id.btn_finish);
        Button buttonCancel=dialog.findViewById(R.id.btn_cancel);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFeedback(editText.getText()+"");
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FeedbackActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.show();
    }



    public void init()
    {
        listViewFeedback=(ListView) findViewById(R.id.feedback_lis_view);
    }



    //region Feedback
    private void FillFeedbackList()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Feedback").orderByChild("feedbackTime");
        query.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    feedbackClassList.clear();
                    try {
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                            feedbackClassList.add(new FeedbackClass
                                    (issue.child("feedbackText").getValue()+"",
                                            Long.parseLong(issue.child("feedbackTime").getValue()+"")));
                        }
                    }
                    catch (Exception Ex)
                    {
                        Toast.makeText(FeedbackActivity.this, Ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(FeedbackActivity.this, "Not places in this Category", Toast.LENGTH_LONG).show();
                }

                Collections.reverse(feedbackClassList);

                feedbackAdapter=new FeedbackAdapter(FeedbackActivity.this, feedbackClassList);
                listViewFeedback.setAdapter(feedbackAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    private void AddFeedback(String Text) {
            DatabaseReference mDatabase;
            FirebaseDatabase firebasedatabase = FirebaseDatabase.getInstance();
            mDatabase = firebasedatabase.getReferenceFromUrl
                    ("https://jalsa-e9ec7.firebaseio.com/Feedback/");
            final DatabaseReference id = mDatabase.push();
            id.setValue(new FeedbackClass(Text)).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        dialog.dismiss();
                    }
                }
            });
    }
}



