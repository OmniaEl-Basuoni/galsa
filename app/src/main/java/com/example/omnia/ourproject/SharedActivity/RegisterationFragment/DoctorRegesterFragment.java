package com.example.omnia.ourproject.SharedActivity.RegisterationFragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omnia.ourproject.AutoCompleteCountry.Adapter.CountryAdapter;
import com.example.omnia.ourproject.AutoCompleteCountry.Class.CountriesClass;
import com.example.omnia.ourproject.AutoCompleteCountry.Class.Country;
import com.example.omnia.ourproject.AutoCompleteCountry.Remote.ApiUtlis;
import com.example.omnia.ourproject.AutoCompleteCountry.Remote.UserService;
import com.example.omnia.ourproject.Doctor.Classes.DoctorClass;
import com.example.omnia.ourproject.R;
import com.example.omnia.ourproject.SharedActivity.StartActivity;
import com.example.omnia.ourproject.SharedClasses.SharedParameters;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class DoctorRegesterFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private CountryAdapter countryAdapter;
    private ArrayList<Country> countryList=new ArrayList<>();
    private AutoCompleteTextView completeTextView;
    private UserService userService;

    private String IdPhotoUrl,mCurrentPhotoPath;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private Uri filePath,photoURI,filePathCV;
    private static int Camera_Request=1;
    private static int REQUEST_IMAGE_CAPTURE=2;
    private static int PICK_PDF_CODE_CV=3;

    private DoctorClass Doctor,DoctorClass;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    private ImageView UploadCv,UploadID;
    private RadioButton radioButtonMale,radioButtonFemale;
    private String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private CountryCodePicker ccp;
    private EditText Username, Password, Retry_Password, Email, NationalID, editTextCarrierNumber;
    private TextView textViewBirthdate,textViewShowBirth,textViewIDPath,textViewCVPath;
    private TextInputLayout textInputLayoutUsername, textInputLayoutPassword,
            textInputLayoutRetryPassword, textInputLayoutEmail, textInputLayoutNatiobalID;
    private Button buttonRegister;
    private boolean WithApi;
    private String Uid,GenderFlag,stringUsername,stringEmail;
    private View view;

    public DoctorRegesterFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public DoctorRegesterFragment(boolean withApi) {
        this.WithApi=withApi;
    }
    @SuppressLint("ValidFragment")
    public DoctorRegesterFragment(boolean withApi, String Username, String Email,String uid)
    {
        this.WithApi=withApi;
        this.stringUsername=Username;
        this.stringEmail=Email;
        this.Uid=uid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_doctor_regester, container, false);
        InitComponent();
        AutoCountry();
        //region SetDataFromAPI
        if(WithApi)
        {
            Username.setText(stringUsername);
            Email.setText(stringEmail);
            Password.setText("**********");
            Retry_Password.setText("**********");
        }
        //endregion

        //region RadioButton
        radioButtonMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    GenderFlag= SharedParameters.Gender.Male+"";
                }
                else {
                    GenderFlag=SharedParameters.Gender.Female+"";
                }
            }
        });

        radioButtonFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked)
                {
                    GenderFlag=SharedParameters.Gender.Male+"";
                }
                else {
                    GenderFlag=SharedParameters.Gender.Female+"";
                }
            }
        });
        //endregion


        //region Birthday
        textViewBirthdate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar now=Calendar.getInstance();
                DatePickerDialog datePickerDialog= DatePickerDialog.newInstance(DoctorRegesterFragment.this
                        ,now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setTitle("DatePicker");
                datePickerDialog.show(getFragmentManager(),"DatePicker");
            }
        });
        //endregion


        UploadID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectPhotoDialog();
            }
        });

        UploadCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPDF_CV();
            }
        });

        //Button Register
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidUser() && ValidPassword() && ValidTryPassword() && ValidEmail() && ValidID() && ValidGender() && ValidBirthdate() && ValidPhone()) {
                    if (WithApi) {
                        DoctorRegisterWithAPI();
                    } else {
                        DoctorRegister();
                    }
                }
            }
        });

        return view;
    }

    private void InitComponent() {

        completeTextView=view.findViewById(R.id.ET_Country);
        userService= ApiUtlis.getUserService();

        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("Uploading...");

        radioButtonMale=(RadioButton)view.findViewById(R.id.RB_Male);
        radioButtonFemale=(RadioButton)view.findViewById(R.id.RB_Female);

        Username = (EditText) view.findViewById(R.id.ET_Username);
        Password = (EditText) view.findViewById(R.id.ET_Password);
        Retry_Password = (EditText) view.findViewById(R.id.ET_Password1);
        Email = (EditText) view.findViewById(R.id.ET_Email);
        NationalID = (EditText) view.findViewById(R.id.ET_ID);
        textViewBirthdate = (TextView) view.findViewById(R.id.TV_Birthdate);
        textViewShowBirth=(TextView)view.findViewById(R.id.TV_ShowBirthdate);


        textInputLayoutUsername = (TextInputLayout) view.findViewById(R.id.TIL_Username);
        textInputLayoutPassword = (TextInputLayout) view.findViewById(R.id.TIL_Password);
        textInputLayoutRetryPassword = (TextInputLayout) view.findViewById(R.id.TIL_Password1);
        textInputLayoutEmail = (TextInputLayout) view.findViewById(R.id.TIL_Email);
        textInputLayoutNatiobalID = (TextInputLayout) view.findViewById(R.id.TIL_ID);

        buttonRegister = (Button) view.findViewById(R.id.BT_next);


        ccp = (CountryCodePicker) view.findViewById(R.id.ccp);
        editTextCarrierNumber = (EditText) view.findViewById(R.id.editText_carrierNumber);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        ccp.registerCarrierNumberEditText(editTextCarrierNumber);

        textViewIDPath=view.findViewById(R.id.TV_ID);
        textViewCVPath=view.findViewById(R.id.TV_CV);
        UploadCv=view.findViewById(R.id.Bt_UploadCV);
        UploadID=view.findViewById(R.id.Bt_UploadID);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();


    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date= String.format("%d/%d/%d",dayOfMonth,monthOfYear+1,year);
        textViewShowBirth.setText(date);
    }

    private boolean ValidEmail()
    {
        boolean isValid=true;
        if(Email.getText().toString().isEmpty())
        {
            textInputLayoutEmail.setError("This field is required");
            isValid=false;
        }
        else if ((!Email.getText().toString().trim().matches(emailPattern))&&isValid)
        {
            textInputLayoutEmail.setError("This email address is invalid");
            isValid=false;
        }
        else {
            textInputLayoutEmail.setErrorEnabled(false);
            isValid=true;
        }
        return isValid;
    }
    private boolean ValidPassword()
    {
        boolean isValid;
        if(Password.getText().toString().length()<5)
        {
            textInputLayoutPassword.setError("This field must more than 5 ");
            isValid=false;
        }

        else {
            textInputLayoutPassword.setErrorEnabled(false);
            isValid=true;
        }
        return isValid;
    }
    private boolean ValidTryPassword()
    {
        boolean isValid;
        if (Retry_Password.getText().toString().length()==0)
        {
            textInputLayoutRetryPassword.setError("This field is required");
            isValid=false;
        }
        else if(!(Password.getText().toString().equals(Retry_Password.getText().toString())))
        {
            textInputLayoutRetryPassword.setError("Don't Match");
            isValid=false;
        }

        else {
            textInputLayoutRetryPassword.setErrorEnabled(false);
            isValid=true;
        }
        return isValid;
    }
    private boolean ValidUser()
    {
        boolean isValid;
        if(Username.getText().toString().length()<5)
        {
            textInputLayoutUsername.setError("This field must more than 5 ");
            isValid=false;
        }

        else {
            textInputLayoutUsername.setErrorEnabled(false);
            isValid=true;
        }
        return isValid;
    }
    private boolean ValidID()
    {
        boolean isValid;
        if(NationalID.getText().toString().length()<1)
        {
            textInputLayoutNatiobalID.setError("This field is required ");
            isValid=false;
        }

        else {
            textInputLayoutNatiobalID.setErrorEnabled(false);
            isValid=true;
        }
        return isValid;
    }
    private boolean ValidBirthdate()
    {
        boolean isValid;
        if(textViewShowBirth.getText().toString().isEmpty())
        {
            isValid=false;
        }
        else {
            isValid=true;
        }
        return isValid;
    }
    private boolean ValidGender()
    {
        boolean isValid;
        if(GenderFlag.isEmpty())
        {
            isValid=false;
        }
        else {
            isValid=true;
        }
        return isValid;
    }
    private boolean ValidPhone()
    {
        boolean isValid;
        if(!ccp.isValidFullNumber())
        {
            isValid=false;
            Toast.makeText(getActivity(), "Wrong Number", Toast.LENGTH_SHORT).show();
        }
        else {
            isValid=true;
        }
        return isValid;
    }

    //Auth

    private void Verification(final FirebaseUser user)
    {
        mAuth = FirebaseAuth.getInstance();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "Email sent.");
                            //Toast.makeText(getActivity(), user.getUid(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    //DOCTOR

    private void DoctorRegister()
    {
        CreateDoctor();
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(Doctor.DoctorEmail, Doctor.DoctorPassword)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.show();
                            Log.d("G", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Verification(user);
                            Uid=user.getUid();
                            mDatabase.child("Doctor").child(user.getUid()).setValue(Doctor);
                            mDatabase.child("Doctor").child(Uid).child("PersonalPhoto").setValue(
                                    "https://firebasestorage.googleapis.com/v0/b/jalsa-e9ec7.appspot.com/o/images%2Fdoctor.png?alt=media&token=159fcd7d-558e-481c-80bc-4e76e8f1d053");
                            UploadImageID();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("G", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            /*updateUI(null);*/
                        }

                    }
                });
    }


    private void DoctorRegisterWithAPI()
    {
        progressDialog.show();
        CreateDoctor();
        mAuth = FirebaseAuth.getInstance();
        mDatabase.child("Doctor").child(Uid).setValue(Doctor)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            mDatabase.child("Doctor").child(Uid).child("PersonalPhoto").setValue(
                                    "https://firebasestorage.googleapis.com/v0/b/jalsa-e9ec7.appspot.com/o/images%2Fdoctor.png?alt=media&token=159fcd7d-558e-481c-80bc-4e76e8f1d053");
                            UploadImageID();
                        }
                    }
                });
    }

    private void CreateDoctor()
    {
        String DoctorName=Username.getText()+"";
        String DoctorEmail=Email.getText()+"";
        String DoctorPassword=Password.getText()+"";
        String DoctorPhone=ccp.getFullNumber().toString();
        String DoctorNationalID=NationalID.getText()+"";
        String DoctorGender=GenderFlag;
        String DoctorBirth=textViewShowBirth.getText().toString();
        Doctor=new DoctorClass(DoctorName,DoctorEmail,DoctorPassword,DoctorPhone,DoctorNationalID,DoctorGender,DoctorBirth);
    }




    //Photo


    private void SelectPhotoDialog()
    {

        final CharSequence[] items = {"Take Photo", "Choose from Library","Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
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
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void ChooseImageGallary()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select picture"),REQUEST_IMAGE_CAPTURE);
    }
    private File createImageFile() throws IOException
    {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void ChooseImageCamera()
    {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                 photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, Camera_Request);
            }
        }

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
                Toast.makeText(getActivity(), "Null", Toast.LENGTH_SHORT).show();
            }
            try {

                //Bitmap bitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),filePath);
                //Toast.makeText(this, "1"+"\n"+filePath+"", Toast.LENGTH_SHORT).show();
                textViewIDPath.setText("/New image/photo");
                }
                catch (Exception e)
                {
                    Toast.makeText(getActivity(), "Error"+"\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
        }
        else if (requestCode==REQUEST_IMAGE_CAPTURE&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null)
        {
            filePath=data.getData();

            try {

                //Bitmap bitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),filePath);
                //Toast.makeText(this, "1"+"\n"+filePath+"", Toast.LENGTH_SHORT).show();
                textViewIDPath.setText(filePath.getPath());
            }
            catch (Exception e)
            {
                Toast.makeText(getActivity(), "Error"+"\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        else if (requestCode==PICK_PDF_CODE_CV&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null)
        {
            filePathCV=data.getData();

            try {

                //Bitmap bitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),filePath);
                //Toast.makeText(this, "1"+"\n"+filePath+"", Toast.LENGTH_SHORT).show();
                textViewCVPath.setText(filePathCV.getPath());
            }
            catch (Exception e)
            {
                Toast.makeText(getActivity(), "Error"+"\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
        }
    }
    private void UploadImageID()
    {
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
                                IdPhotoUrl=Url[0];
                                mDatabase.child("Doctor").child(Uid).child("IDPhoto").setValue(Url[0]);
                                uploadFileCV(filePathCV);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double process=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploading ID "+((int)process)+" %");
                        }
                    });
        }
    }



    //PDF

    private void getPDF_CV() {
        //for greater than lolipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getActivity().getPackageName()));
            startActivity(intent);
            return;
        }

        //creating an intent for file chooser
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PDF_CODE_CV);
    }

    private void uploadFileCV(Uri data) {
        final String[] Url = new String[1];
        StorageReference sRef = storageReference.child("Uploads/" + System.currentTimeMillis() + ".pdf");
        sRef.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Url[0] =taskSnapshot.getDownloadUrl().toString();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            mDatabase.child("Doctor").child(Uid).child("CVPDF").setValue(Url[0]);

                            SharedParameters.DoctorUid =Uid;
                            SharedParameters.DoctorName=Doctor.DoctorName;
                            DoctorClass=new DoctorClass(
                                    Uid,
                                    Doctor.DoctorBirth,
                                    Doctor.DoctorEmail,
                                    Doctor.DoctorGender,
                                    Doctor.DoctorName,
                                    Doctor.DoctorNationalID,
                                    Doctor.DoctorPhone,
                                    "https://firebasestorage.googleapis.com/v0/b/jalsa-e9ec7.appspot.com/o/images%2Fdoctor.png?alt=media&token=159fcd7d-558e-481c-80bc-4e76e8f1d053",
                                    IdPhotoUrl+"",
                                    0
                            );
                            Intent intent=new Intent(getActivity(), StartActivity.class);
                            intent.putExtra("DoctorClass",DoctorClass);
                            progressDialog.dismiss();
                            startActivity(intent);
                            getActivity().finish();

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getActivity().getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploading PDF "+((int)progress)+" %");
                    }
                });

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
