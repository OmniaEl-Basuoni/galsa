package com.example.omnia.ourproject.Patient.Classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.omnia.ourproject.SharedClasses.ChatMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * Created by zt on 06/03/18.
 */

public class RecordGroup {

    private Long dateLong;
    String MessageId,User;
    Context context;
    private DatabaseReference mDatabase;
    private MediaRecorder mRecorder;
    private String mFileName=null;
    private StorageReference mStorage;
    private ProgressDialog progressDialog;
    private String s;
    public RecordGroup(Context context, String messageId, String user,long dateLong)
    {
        this.dateLong=dateLong;
        this.User=user;
        this.MessageId=messageId;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        this.context=context;
        progressDialog=new ProgressDialog(context);
        mStorage= FirebaseStorage.getInstance().getReference();
        mFileName= Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName+="/recordingaudio.3gp";
    }


    public void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("Error", "prepare() failed");
        }

        mRecorder.start();
    }

    public void stopRecording() {
        try {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;

            UploadAudio(User);
        }
        catch (Exception e)
        {
            Toast.makeText(context, "Press & hold to send record", Toast.LENGTH_SHORT).show();
        }
    }


    public void UploadAudio(final String User) {

        progressDialog.setMessage("Uploading......");
        progressDialog.show();

        StorageReference filePath =mStorage.child("Audio/"+ UUID.randomUUID().toString());
        Uri uri=Uri.fromFile(new File(mFileName));

        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                s=taskSnapshot.getDownloadUrl()+"";

                FirebaseDatabase firebasedatabase= FirebaseDatabase.getInstance();
                mDatabase = firebasedatabase.getReferenceFromUrl
                        ("https://jalsa-e9ec7.firebaseio.com/MessagesGroup/"+MessageId+"/"+dateLong+"");
                final DatabaseReference id=mDatabase.push();
                id.setValue(new ChatMessage(
                        s,User+"","Voice","Send"))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                }
                            }
                        });

                progressDialog.dismiss();
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful())
                {

                }
            }
        });
    }

}
