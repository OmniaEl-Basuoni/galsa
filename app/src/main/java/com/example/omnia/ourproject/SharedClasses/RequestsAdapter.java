package com.example.omnia.ourproject.SharedClasses;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omnia.ourproject.Doctor.Classes.DoctorClass;
import com.example.omnia.ourproject.R;
import com.example.omnia.ourproject.Doctor.Activities.DoctorRequestsActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static com.facebook.internal.FacebookDialogFragment.TAG;
import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.round;

/**
 * Created by Omnia on 12/3/2017.
 */

public class RequestsAdapter extends BaseAdapter {

    DoctorClass aClass;
    int InternalPostion;
    Context context;
    List<RequestsClass> requestslist;
    String DoctorName;

    public RequestsAdapter(Context context, List<RequestsClass> requestslist, DoctorClass doctorClass) {
        this.context = context;
        this.requestslist = requestslist;
        this.aClass = doctorClass;
    }

    @Override
    public int getCount() {
        return requestslist.size();
    }

    @Override
    public Object getItem(int position) {
        return requestslist.get(position).PatientName;
    }

    @Override
    public long getItemId(int position) {
        return requestslist.indexOf(getItem(position));
    }


    private class ViewHolder {
        Button Confirm, Delete;
        TextView PatientName;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        try {

            ViewHolder viewHolder;

            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.view_requests, parent, false);
                viewHolder.PatientName = convertView.findViewById(R.id.TV_PatientName);
                viewHolder.Confirm = convertView.findViewById(R.id.confirm_request);
                viewHolder.Delete = convertView.findViewById(R.id.delete_request);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            viewHolder.PatientName.setText(requestslist.get(position).PatientName);

            viewHolder.Confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InternalPostion = position;
                    CreateMessage(requestslist.get(InternalPostion).PatientId,
                            requestslist.get(InternalPostion).PatientName, aClass);

                    CreateMessageRoot();

                    ConfirmRequest(requestslist.get(InternalPostion).PatientId
                            , requestslist.get(InternalPostion).PatientName);
                    new DoctorRequestsActivity().requestsClassList.remove(InternalPostion);
                    new DoctorRequestsActivity().listViewRequests.invalidateViews();
                }
            });


            try {
                viewHolder.Delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InternalPostion = position;
                        DeleteRequest(requestslist.get(InternalPostion).PatientId, true);
                        new DoctorRequestsActivity().requestsClassList.remove(InternalPostion);
                        new DoctorRequestsActivity().listViewRequests.invalidateViews();
                    }
                });
            } catch (Exception t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }


        } catch (Exception r) {
            Toast.makeText(context, r.getMessage(), Toast.LENGTH_LONG).show();
        }
        return convertView;
    }


    private void DeleteRequest(String PatientUid, final boolean Delete) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("Requests").child(SharedParameters.DoctorUid)
                .orderByChild("Patient ID").equalTo(PatientUid);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });

        applesQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (Delete)
                    Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void ConfirmRequest(final String PatientUid, final String PatientName) {
        DatabaseReference mDatabase;
        FirebaseDatabase firebasedatabase;
        firebasedatabase = FirebaseDatabase.getInstance();
        mDatabase = firebasedatabase.getReferenceFromUrl
                ("https://jalsa-e9ec7.firebaseio.com/Doctor_Patient/" + SharedParameters.DoctorUid + "");
        DatabaseReference id = mDatabase.push();
        id.child("Patient ID").setValue(PatientUid);
        id.child("Patient Name").setValue(PatientName);

        id.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DeleteRequest(PatientUid, false);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        SharedParameters.SendNotification("Request","Doctor Accept Request",PatientUid);
    }

    private void CreateMessage(final String PatientUid, final String PatientName, final DoctorClass doctorClass) {


        DatabaseReference mDatabase;
        FirebaseDatabase firebasedatabase = FirebaseDatabase.getInstance();
        mDatabase = firebasedatabase.getReferenceFromUrl
                ("https://jalsa-e9ec7.firebaseio.com/MessagesBetweenDoctorPatient/");
        DatabaseReference id = mDatabase.push();

        id.setValue(new DoctorPatientMessagesClass(
                id.getKey()+"",
                doctorClass.DoctorID + "",
                doctorClass.DoctorName + "",
                doctorClass.DoctorPhotoUrl + "",
                PatientUid + "",
                PatientName + "",
                "أهلا كيف يمكني ان اساعدك"
        ));

        SharedParameters.MessageUid=id.getKey();
    }

    private void CreateMessageRoot()
    {
        DatabaseReference mDatabase;
        FirebaseDatabase firebasedatabase = FirebaseDatabase.getInstance();
        mDatabase = firebasedatabase.getReferenceFromUrl
                ("https://jalsa-e9ec7.firebaseio.com/Messages/" + SharedParameters.MessageUid + "");
        DatabaseReference id = mDatabase.push();
        id.setValue(new ChatMessage(
                "أهلا كيف يمكني ان اساعدك", "D","Text","Send"));
    }

    //public void


}
