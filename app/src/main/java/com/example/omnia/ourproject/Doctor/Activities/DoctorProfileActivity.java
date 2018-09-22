package com.example.omnia.ourproject.Doctor.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.omnia.ourproject.Doctor.Classes.DoctorClass;
import com.example.omnia.ourproject.R;

import com.example.omnia.ourproject.SharedActivity.StartActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private ProgressDialog progressDialog;
    private String mCurrentPhotoPath,PhotoUrl;
    private DatabaseReference mDatabase;
    private Uri photoURI,filePath;
    private static int Camera_Request=1;
    private static int REQUEST_IMAGE_CAPTURE=2;
    private String doctorCategory,doctorUniversity,doctorCertification,doctorHalf,doctorHour,doctorCountry;
    private ImageView MenuPhoto,SearchImage;
    private RatingBar Rate;
    private CircleImageView PersonalPhoto,ChoosePhoto;
    private DoctorClass aClass;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private View header,v;
    private TextView NameSlide,EmailSlide;
    private RatingBar DoctorRateSlide;
    private FloatingActionButton FloatingActionButton, FloatingActionButton1, FloatingActionButton3;
    private Dialog dialog;
    private TextView name, category, country, mail, phone, university, certification, price1, price2 ;
    ArrayList<String> certificationlist=new ArrayList<>();
    ArrayList<String> categoryliast=new ArrayList<>();

    private Intent intent1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_doctor_profile);
        Init();
        SetData();


        //region Float
        FloatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent1= new Intent(DoctorProfileActivity.this, DoctorEditActivity.class);
                intent1.putExtra("key","A");
                intent1.putExtra("name", name.getText().toString());
                intent1.putExtra("category", doctorCategory);
                intent1.putExtra("country", doctorCountry);
                intent1.putExtra("mail", mail.getText().toString());
                intent1.putExtra("phone", phone.getText().toString());
                intent1.putExtra("Uid",aClass.DoctorID);
                startActivity(intent1);
            }
        });

        FloatingActionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent1= new Intent(DoctorProfileActivity.this, DoctorEditActivity.class);
                intent1.putExtra("key","B");
                intent1.putExtra("university", doctorUniversity);
                intent1.putExtra("certification", doctorCertification);
                intent1.putExtra("Uid",aClass.DoctorID);
                startActivity(intent1);
            }
        });

        FloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialog();
            }
        });
        //endregion


        //region SlideButtonClick
        mNavigationView.setNavigationItemSelectedListener
                (new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.profile:
                                break;
                            case R.id.dashboard:
                                Intent DashIntent=new Intent(DoctorProfileActivity.this,DoctorDashboardActivity.class);
                                DashIntent.putExtra("DoctorClass",aClass);
                                startActivity(DashIntent);
                                break;
                            case R.id.logout:
                                Logout();
                                break;
                        }
                        return true;
                    }
                });
        //endregion


        //region DoctorProfileMenu
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(v);
        SearchImage.setVisibility(View.GONE);
        MenuPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START, true);
            }
        });
        //endregion

        CompleteData();


        ChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectPhotoDialog();
            }
        });

    }



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

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflator.inflate(R.layout.menu_doctor_dashboard_icon, null);
        MenuPhoto=v.findViewById(R.id.imageView4);
        SearchImage=v.findViewById(R.id.imageView3);

        PersonalPhoto=findViewById(R.id.Photo);
        Rate=findViewById(R.id.RB_rate);
        name = findViewById(R.id.name);
        category = findViewById(R.id.category);
        country = findViewById(R.id.country);
        mail = findViewById(R.id.mail);
        phone = findViewById(R.id.phone);
        university = findViewById(R.id.university);
        certification = findViewById(R.id.certification);
        price1 = findViewById(R.id.price1);
        price2 = findViewById(R.id.price2);

        mDrawerLayout =  findViewById(R.id.drawer);
        mNavigationView = findViewById(R.id.fr);
        header=mNavigationView.getHeaderView(0);
        NameSlide=header.findViewById(R.id.Name_Slide);
        EmailSlide=header.findViewById(R.id.Email_Slide);
        DoctorRateSlide=header.findViewById(R.id.RB_rate);

        FloatingActionButton = findViewById(R.id.edit4);
        FloatingActionButton1 = findViewById(R.id.edit1);
        FloatingActionButton3 = findViewById(R.id.edit3);

        ChoosePhoto=findViewById(R.id.IV_Click);
    }


    private void SetData() {

            Bundle extras = getIntent().getExtras();
            if (extras!=null) {
                aClass = (DoctorClass) extras.getSerializable("DoctorClass");
               if(aClass!=null)
               {
                   ImageLoader.getInstance().displayImage(aClass.DoctorPhotoUrl,
                           PersonalPhoto, new ImageLoadingListener() {
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
                   NameSlide.setText(aClass.DoctorName);
                   EmailSlide.setText(aClass.DoctorEmail);
                   DoctorRateSlide.setRating((float) aClass.DoctorRate);

                   name.setText(aClass.DoctorName);
                   phone.setText("Phone : "+ aClass.DoctorPhone);
                   mail.setText("Email : "+ aClass.DoctorEmail);
                   Rate.setRating((float)aClass.DoctorRate);
               }
            }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }

    //region Menu
    public boolean onCreateOptionsMenu (Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_doctor_dashboard, menu);
        return true;
    }

    public boolean onOptionsItemSelected (MenuItem item)
    {
        switch (item.getItemId()){
            /*case R.id.msg:
                Toast.makeText(this, "messages", Toast.LENGTH_SHORT).show();
                return true;
            case  R.id.request:
                Toast.makeText(this, "Notification", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.notification:
                Toast.makeText(this, "Notification", Toast.LENGTH_SHORT).show();
                return true;*/
        }
        return true;
    }

    //endregion


    private void Logout() {
        //region SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor edit = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson("");
        edit.putString("Object",json);
        edit.putString("Role", "Client");
        edit.commit();
        //endregion
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, StartActivity.class));
        finish();
    }


    private void ShowDialog(){

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.view_edit_price);

        final EditText editText30=dialog.findViewById(R.id.text30m);
        final EditText editText60=dialog.findViewById(R.id.text60m);
        Button buttonSet=dialog.findViewById(R.id.btn_price);

        editText30.setText(doctorHalf);
        editText60.setText(doctorHour);


        buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("Doctor").child(aClass.DoctorID).child("Doctor PriceHalf").setValue(Double.parseDouble(editText30.getText()+""));
                mDatabase.child("Doctor").child(aClass.DoctorID).child("Doctor PriceHour").setValue(Double.parseDouble(editText60.getText()+""));
                dialog.dismiss();
            }
        });

        dialog.show();

    }



    private void CompleteData()
    {

        Query query= FirebaseDatabase.getInstance().getReference().child("Doctor").child(aClass.DoctorID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    doctorCountry=dataSnapshot.child("Doctor Country").getValue()+"";
                    doctorUniversity=dataSnapshot.child("Doctor University").getValue()+"";
                    doctorHalf=""+dataSnapshot.child("Doctor PriceHalf").getValue();
                    doctorHour=""+dataSnapshot.child("Doctor PriceHour").getValue();
                    doctorCategory=""+dataSnapshot.child("Category").getValue();
                    doctorCertification=""+dataSnapshot.child("Doctor Certification").getValue();
                }
                fillData();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fillData() {
        if(!doctorCountry.isEmpty())
        {
            country.setText(doctorCountry);
        }
        price1.setText("Price For 30m "+ doctorHalf);
        price2.setText("Price For 60m "+ doctorHour);

        if(doctorUniversity.equals("null"))
        {
            university.setText("No University Yet ... ");
        }
        else {
            university.setText(doctorUniversity);
        }

        if(doctorCategory.equals("null"))
        {
            category.setText("No Category Yet ... ");
        }
        else {
            category.setText(doctorCategory.replace("[","").replace("]",""));
        }

        if(doctorCertification.equals("null"))
        {
            certification.setText("No Certification Yet ... ");
        }
        else {
            certification.setText(doctorCertification.replace("[","").replace("]",""));
        }

    }







    private void SelectPhotoDialog()
    {
        final CharSequence[] items = {"Take Photo", "Choose from Library","Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    ChooseImageCamera();
                } else if (items[item].equals("Choose from Library")) {
                    ChooseImageGallary();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void ChooseImageGallary()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select picture"),REQUEST_IMAGE_CAPTURE);
    }

    private void ChooseImageCamera()
    {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, Camera_Request);
            }
        }

    }

    private File createImageFile() throws IOException
    {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Camera_Request&&resultCode==RESULT_OK)
        {
            filePath=photoURI;
            if(filePath==null)
            {
                Toast.makeText(this, "Null", Toast.LENGTH_SHORT).show();
            }
            try {
                Toast.makeText(this, "C", Toast.LENGTH_SHORT).show();
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),filePath);
                //Toast.makeText(this, "1"+"\n"+filePath+"", Toast.LENGTH_SHORT).show();
                PersonalPhoto.setImageBitmap(bitmap);
                UploadImageID();
            }
            catch (Exception e)
            {
                Toast.makeText(this, "Error"+"\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        else if (requestCode==REQUEST_IMAGE_CAPTURE&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null)
        {
            filePath=data.getData();

            try {
                Toast.makeText(this, "G", Toast.LENGTH_SHORT).show();
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),filePath);
                //Toast.makeText(this, "1"+"\n"+filePath+"", Toast.LENGTH_SHORT).show();
                PersonalPhoto.setImageBitmap(bitmap);
                UploadImageID();
            }
            catch (Exception e)
            {
                Toast.makeText(this, "Error"+"\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
    }

    private void UploadImageID()
    {
        progressDialog.show();
        final String[] Url = new String[1];
        if (filePath!=null)
        {
            final StorageReference ref=storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Url[0] =taskSnapshot.getDownloadUrl().toString();
                            /*progressDialog.dismiss();*/
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful())
                            {
                                PhotoUrl=Url[0];
                                mDatabase.child("Doctor").child(aClass.DoctorID).child("PersonalPhoto").setValue(Url[0]);
                                progressDialog.dismiss();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DoctorProfileActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double process=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploading Photo "+((int)process)+" %");
                        }
                    });
        }
    }

}