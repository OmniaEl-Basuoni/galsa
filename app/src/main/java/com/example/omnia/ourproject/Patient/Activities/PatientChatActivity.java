package com.example.omnia.ourproject.Patient.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omnia.ourproject.Doctor.Classes.Record;
import com.example.omnia.ourproject.Patient.Adapters.PatientChatAdapter;
import com.example.omnia.ourproject.R;
import com.example.omnia.ourproject.SharedClasses.ChatMessage;
import com.example.omnia.ourproject.SharedClasses.DoctorPatientMessagesClass;
import com.example.omnia.ourproject.VideoChat.BaseActivity;
import com.example.omnia.ourproject.VideoChat.CallScreenActivity;
import com.example.omnia.ourproject.VideoChat.SinchService;
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
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;


public class PatientChatActivity extends BaseActivity implements SinchService.StartFailedListener {
    private View rootView;
    private EmojIconActions emojIcon;
    private TextView DoctorName;
    private CircleImageView circleImageView;
    private DoctorPatientMessagesClass aClass;
    private DatabaseReference mDatabase;
    private FirebaseDatabase firebasedatabase;
    private PatientChatAdapter adapter;
    private List<ChatMessage> chatMessageList = new ArrayList<>();
    private CircleImageView fab;
    private EmojiconEditText input;
    private ListView listView;
    private ImageView emojiImageView,OpenCamera,OpenGallery;
    private Button button;



    private ProgressDialog progressDialog;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private final int Camera_Request=71;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri filePath;


    private android.app.AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_chat);

        //region PatientProfileMenu
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.menu_chat_icon_patient, null);
        circleImageView=v.findViewById(R.id.photoView);
        DoctorName=v.findViewById(R.id.nameView);
        actionBar.setCustomView(v);
        //endregion

        Init();

        final Record record=new Record(this,aClass.getMessageId(),"P");

        //region SendMessage
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!input.getText().toString().isEmpty()) {
                    chatMessageList.add(new ChatMessage(input.getText() + "", "P"
                            , "Text", "NotSend"));
                    adapter = new PatientChatAdapter(PatientChatActivity.this, chatMessageList);
                    listView.setAdapter(adapter);

                    SendMessage(true, input.getText().toString(), "Text", "NotSend");
                }
            }
        });
        //endregion

        ShowMessages();


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
                    alertDialog= new SpotsDialog.Builder().setContext(PatientChatActivity.this).build();
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


        //region StartService
        new CountDownTimer(500,250)
        {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                loginClicked();
            }
        } .start();
        //endregion
    }
    //region Menu
    public boolean onCreateOptionsMenu (Menu menu)
    {
        getMenuInflater().inflate(R.menu.doctorchatmenu, menu);
        return true;
    }

    public boolean onOptionsItemSelected (MenuItem item)
    {
        switch (item.getItemId()){
            case R.id.call:
                Toast.makeText(this, "Call", Toast.LENGTH_SHORT).show();
                return true;
            case  R.id.videocall:
                Toast.makeText(this, "Video", Toast.LENGTH_SHORT).show();
                callButtonClicked();
                return true;
            case R.id.about:


                return true;
            default:
                Toast.makeText(this, "Back", Toast.LENGTH_SHORT).show();
                return true;
        }
    }
    //endregion

    private void Init() {

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



        MessageData();
    }






    private void MessageData()
    {

        Bundle extras = getIntent().getExtras();
        if ((!extras.isEmpty())) {
            aClass= (DoctorPatientMessagesClass) extras.getSerializable("ChatClass");
            ImageLoader.getInstance().displayImage(aClass.getDoctorPhoto(), circleImageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                }
            });

            DoctorName.setText(aClass.getDoctorName());

        }
        else {
            Toast.makeText(this, "Here", Toast.LENGTH_SHORT).show();
        }

    }











    private void SendMessage(final boolean b, final String Message, String Type, String State)
    {
        firebasedatabase= FirebaseDatabase.getInstance();
        mDatabase = firebasedatabase.getReferenceFromUrl
                ("https://jalsa-e9ec7.firebaseio.com/Messages/"+aClass.getMessageId()+"");
        final DatabaseReference id=mDatabase.push();
        id.setValue(new ChatMessage(
                Message,"P",Type,State))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            SetMessageSend(id.getKey());
                            UpdateLastMessage(Message,b);
                        }
                    }
                });

        listView.setSelection(listView.getAdapter().getCount());
        input.setText("");
    }

    private void SetMessageSend(String Id)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Messages").child(aClass.getMessageId())
                .child(Id).child("messageState").setValue("Send");

    }

    private void UpdateLastMessage(String Text,boolean b)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(b)
        {
            mDatabase.child("MessagesBetweenDoctorPatient").child(aClass.getMessageId())
                    .child("lastMessageText").setValue(Text);
        }
        else {
            mDatabase.child("MessagesBetweenDoctorPatient").child(aClass.getMessageId())
                    .child("lastMessageText").setValue("Photo");
        }
        mDatabase.child("MessagesBetweenDoctorPatient").child(aClass.getMessageId())
                .child("lastMessageTime").setValue(new Date().getTime());
    }

    private void ShowMessages()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Messages")
                .child(aClass.getMessageId());
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
                        Toast.makeText(PatientChatActivity.this, Ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PatientChatActivity.this, "Not places in this Category", Toast.LENGTH_LONG).show();
                }


                adapter = new PatientChatAdapter(PatientChatActivity.this, chatMessageList);

                listView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                            Toast.makeText(PatientChatActivity.this, "Fail", Toast.LENGTH_SHORT).show();
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
        chatMessageList.add(new ChatMessage(filePath+"","P"
                ,"Photo","NotSend"));
        adapter=new PatientChatAdapter(PatientChatActivity.this,chatMessageList);
        listView.setAdapter(adapter);
    }
    //endregion


    //region VideoChat
    @Override
    public void onDestroy() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
        }
        super.onDestroy();
    }

    private void callButtonClicked() {
        String userName = aClass.getDoctorUid();
        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a user to call", Toast.LENGTH_LONG).show();
            return;
        }

        Call call = getSinchServiceInterface().callUserVideo(userName);
        String callId = call.getCallId();

        Intent callScreen = new Intent(this, CallScreenActivity.class);
        callScreen.putExtra(SinchService.CALL_ID, callId);
        callScreen.putExtra("boolRole",false);
        callScreen.putExtra("DoctorName",aClass.getDoctorName());
        callScreen.putExtra("DoctorPhoto",aClass.getDoctorPhoto());
        startActivity(callScreen);
    }
    //endregion


    //region VideoChatServiceRegister
    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStarted() {
        openPlaceCallActivity();
    }


    private void loginClicked() {
        String userName = aClass.getPatientUid();
        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
            return;
        }

        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(userName);
        }
    }

    private void openPlaceCallActivity() {
        /*Intent mainActivity = new Intent(this, DoctorChatActivity.class);
        startActivity(mainActivity);*/
    }
    //endregion


}
