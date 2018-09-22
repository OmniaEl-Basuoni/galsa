package com.example.omnia.ourproject.Patient.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omnia.ourproject.Doctor.Activities.DoctorChatActivity;
import com.example.omnia.ourproject.Doctor.Adapters.DoctorChatAdapter;
import com.example.omnia.ourproject.Doctor.Classes.Record;
import com.example.omnia.ourproject.Patient.Adapters.GroupChatAdapter;
import com.example.omnia.ourproject.Patient.Classes.PatientClass;
import com.example.omnia.ourproject.Patient.Classes.RecordGroup;
import com.example.omnia.ourproject.R;
import com.example.omnia.ourproject.SharedClasses.ChatMessage;
import com.example.omnia.ourproject.SharedClasses.DoctorPatientMessagesClass;
import com.example.omnia.ourproject.SharedClasses.GroupClass;
import com.example.omnia.ourproject.SharedClasses.TherapyAdapter;
import com.github.pavlospt.roundedletterview.RoundedLetterView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class PatientJoinTherapyActivity extends AppCompatActivity {


    private RoundedLetterView roundedLetterView;
    private TextView textView_groupName;
    private View rootView;
    private EmojIconActions emojIcon;
    private ImageView emojiImageView,OpenCamera,OpenGallery;
    private Button button;
    private DatabaseReference mDatabase;
    private FirebaseDatabase firebasedatabase;
    private GroupChatAdapter adapter;
    private List<ChatMessage> chatMessageList = new ArrayList<>();
    private CircleImageView fab;
    private EmojiconEditText input;
    private ListView listView;








    private ProgressDialog progressDialog;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private final int Camera_Request=71;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri filePath;


    private android.app.AlertDialog alertDialog;






    private RecordGroup record;



    private RelativeLayout relativeLayoutWait,relativeLayoutMessage;
    private TextView textView_WaitText;
    private PatientClass aClass;
    private String groupMembers,groupID,groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_join_therapy);

        InitComponent();
        PatientPersonalData();
        checkHaveGroupToday();

        //region PatientProfileMenu
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.menu_group, null);
        roundedLetterView=v.findViewById(R.id.photoView);
        textView_groupName=v.findViewById(R.id.nameView);

        roundedLetterView.setTitleText(aClass.PatientID.substring(0,3));

        actionBar.setCustomView(v);
        //endregion

        //region Emoji
        rootView = findViewById(R.id.root_view);
        emojiImageView =  findViewById(R.id.emoji_btn);
        emojIcon = new EmojIconActions(this, rootView, input, emojiImageView);
        emojIcon.ShowEmojIcon();
        emojIcon.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.smiley);
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("W", "Keyboard opened!");
            }

            @Override
            public void onKeyboardClose() {
                Log.e("W", "Keyboard closed");
            }
        });
        //endregion

        //region SendMessage
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!input.getText().toString().isEmpty())
                {
                    chatMessageList.add(new ChatMessage(input.getText()+"",aClass.PatientID
                            ,"Text","NotSend"));
                    adapter=new GroupChatAdapter(PatientJoinTherapyActivity.this,chatMessageList,aClass.PatientID);
                    listView.setAdapter(adapter);

                    SendMessage(true,input.getText().toString(),"Text","NotSend");
                }

            }
        });
        //endregion

        //region Photo
        //Photo

        OpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseImageCamera();
            }
        });

        OpenGallery.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View view) {
                ChooseImageGallary();
            }
        });
        //endregion

        //region Recorder
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                {

                    alertDialog= new SpotsDialog.Builder().setContext(PatientJoinTherapyActivity.this).build();
                    alertDialog.setMessage("Listen");
                    record.startRecording();
                    alertDialog.show();
                }
                else if (motionEvent.getAction()==MotionEvent.ACTION_UP)
                {
                    alertDialog.dismiss();
                    record.stopRecording();
                }
                return false;
            }
        });
        //endregion
    }



    private void ShowMessages()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("MessagesGroup")
                .child(groupID).child(dateLong(Calendar.getInstance().getTime().getTime())+"");
        query.keepSynced(true);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatMessageList.clear();
                if (dataSnapshot.exists()) {
                    try {
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {

                            chatMessageList.add(new ChatMessage(
                                    issue.child("messageText").getValue() + "",
                                    issue.child("messageOwner").getValue() + "",
                                    issue.child("type").getValue()+"",
                                    issue.child("messageState").getValue()+""));
                        }
                    } catch (Exception Ex) {
                        Toast.makeText(PatientJoinTherapyActivity.this, Ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PatientJoinTherapyActivity.this, "Not places in this Category", Toast.LENGTH_LONG).show();
                }


                adapter = new GroupChatAdapter(PatientJoinTherapyActivity.this, chatMessageList,aClass.PatientID);

                listView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void SendMessage(final boolean b, final String Message, String Type, String State)
    {
        firebasedatabase= FirebaseDatabase.getInstance();
        mDatabase = firebasedatabase.getReferenceFromUrl
                ("https://jalsa-e9ec7.firebaseio.com/MessagesGroup/"+groupID+"/"+dateLong(Calendar.getInstance().getTime().getTime())+"");
        final DatabaseReference id=mDatabase.push();
        id.setValue(new ChatMessage(
                Message,aClass.PatientID,Type,State))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            SetMessageSend(id.getKey());
                        }
                    }
                });

        listView.setSelection(listView.getAdapter().getCount());
        input.setText("");
    }

    private void SetMessageSend(String Id)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("MessagesGroup").child(groupID).child(dateLong(Calendar.getInstance().getTime().getTime())+"")
                .child(Id).child("messageState").setValue("Send");

    }


    //region PhotoUpload
    //Photo

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void ChooseImageGallary()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select picture"),Camera_Request);
    }
    private void ChooseImageCamera()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Camera_Request&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null)
        {
            filePath=data.getData();
            try {
                UploadImage();
            }
            catch (Exception e)
            {
                Toast.makeText(this, "Error"+"\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }


        else {
            onCaptureImageResult(data);
        }

    }



    private void onCaptureImageResult(Intent data)
    {
        try {


            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");

            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
//        data.setData(ImageUri);
                filePath = data.getData();
                filePath = getImageUri(this, thumbnail);

                UploadImage();
                //circleImageView.setImageBitmap(thumbnail);
                /*circleImageView.setImageBitmap(thumbnail);*/
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (Exception r) {
                Log.e("Error",r.getMessage());
                Toast.makeText(this, ""+r.getMessage(), Toast.LENGTH_SHORT).show();

            }

        } catch (Exception c) {
            Log.e("Error",c.getMessage());
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void UploadImage()
    {
        final String[] Url = new String[1];
        if (filePath!=null)
        {
            ShowPhoto(filePath);
            final StorageReference ref=storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Url[0] =taskSnapshot.getDownloadUrl().toString();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PatientJoinTherapyActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                            /*progressDialog.dismiss();*/
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful())
                            {
                                SendMessage(false,Url[0],"Photo","Send");
                                //UploadImageID();
                            }
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double process=(100.0*(taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount()));
                            progressDialog.setMessage("Uploading "+(int)(process/2)+" %");
                        }
                    });
        }
        else {
            Toast.makeText(this, "Null", Toast.LENGTH_SHORT).show();
        }
    }

    private void ShowPhoto(Uri filePath)
    {
        chatMessageList.add(new ChatMessage(filePath+"",aClass.PatientID
                ,"Photo","NotSend"));
        adapter=new GroupChatAdapter(PatientJoinTherapyActivity.this,chatMessageList,aClass.PatientID);
        listView.setAdapter(adapter);
    }
    //endregion




















    private void InitComponent() {
        relativeLayoutWait=findViewById(R.id.wait);
        relativeLayoutMessage=findViewById(R.id.message);
        textView_WaitText=findViewById(R.id.textWait);





        //region ImageLoader
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);
        //endregion

        mDatabase = FirebaseDatabase.getInstance().getReference();
        input = findViewById(R.id.Input);
        fab = findViewById(R.id.fab);
        listView = findViewById(R.id.list_of_message);
        OpenCamera=findViewById(R.id.camera_btn);
        OpenGallery=findViewById(R.id.gallery_btn);
        button=findViewById(R.id.mic);





        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");

        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

    }


    private void checkHaveGroupToday() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Group Support");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.child("Last Group").getValue().equals(dateLong(Calendar.getInstance().getTime().getTime())))
                        {
                            groupMembers=snapshot.child("Group Members").child(dateLong(Calendar.getInstance()
                                            .getTime().getTime())+"").getValue()+"";

                            if (groupMembers.contains(aClass.PatientID))
                            {
                                groupName=snapshot.getKey();
                                groupID=snapshot.child("Group ID").getValue()+"";
                                textView_groupName.setText(groupName);
                                isValidTime();
                            }
                            else {
                                textView_WaitText.setText("You Dont Registr in this today Group");
                                relativeLayoutWait.setVisibility(View.VISIBLE);
                                relativeLayoutMessage.setVisibility(View.GONE);
                            }


                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void isValidTime() {
        if(Calendar.getInstance().getTime().getHours()>=4&&Calendar.getInstance().getTime().getHours()<20)
        {
            relativeLayoutMessage.setVisibility(View.VISIBLE);
            relativeLayoutWait.setVisibility(View.GONE);
            ShowMessages();
            record=new RecordGroup(this,groupID,aClass.PatientID,dateLong(Calendar.getInstance().getTime().getTime()));
        }
        else {
            textView_WaitText.setText("The Group Start in 7 and end in 8");
            relativeLayoutWait.setVisibility(View.VISIBLE);
            relativeLayoutMessage.setVisibility(View.GONE);
        }
    }


    private void PatientPersonalData() {

        Bundle extras = getIntent().getExtras();
        if ((!extras.isEmpty())) {
            aClass = (PatientClass) extras.getSerializable("PatientClass");


        }
    }


    private long dateLong(long dateLong) {
        long DayOfDate = 0;
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
        String dateText = df2.format(dateLong);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(dateText);

            DayOfDate = date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return DayOfDate;

    }


}
