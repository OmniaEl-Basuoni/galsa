package com.example.omnia.ourproject.Doctor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.omnia.ourproject.Doctor.Classes.DoctorClass;
import com.example.omnia.ourproject.Patient.Activities.PatientViewDoctorActivity;
import com.example.omnia.ourproject.Patient.Classes.PatientClass;
import com.example.omnia.ourproject.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 3ZT on 10/23/2017.
 */

public class SearchRequestAdapter extends RecyclerView.Adapter<SearchRequestAdapter.MyViewHolder> {

    private Context mContext;
    private PatientClass aClass;
    private List<DoctorClass> albumList;
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewName,textViewCategories,textViewHalf;
        public CircleImageView imageViewDoctor;
        public RatingBar ratingBar;
        public CardView relativeLayoutParent;

        public MyViewHolder(View view) {
            super(view);
            textViewHalf=view.findViewById(R.id.textViewHalf);
            textViewName =  view.findViewById(R.id.TV_DoctorName);
            textViewCategories =  view.findViewById(R.id.TV_Categories);
            imageViewDoctor=view.findViewById(R.id.IV_Doctor);
            ratingBar= view.findViewById(R.id.RB_rate);
            relativeLayoutParent=view.findViewById(R.id.R_Parent);
        }
    }


    public SearchRequestAdapter(Context mContext, List<DoctorClass> albumList, PatientClass aClass ) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.aClass=aClass;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_search, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final DoctorClass album = albumList.get(position);
        holder.textViewName.setText(album.DoctorName);
        holder.ratingBar.setRating((float) album.DoctorRate);
        holder.textViewCategories.setText(album.Categories.toString().replace("]","").replace("[",""));
        holder.textViewHalf.setText((mContext.getResources().getString(R.string.fees)+album.DoctorHalfPrice).replace(".0",""));
        holder.textViewHalf.setVisibility(View.VISIBLE);

        ImageLoader.getInstance().displayImage(album.DoctorPhotoUrl, holder.imageViewDoctor, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                //finalHolder.progressBar.setVisibility(View.VISIBLE);
                holder.imageViewDoctor.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                //finalHolder.progressBar.setVisibility(View.GONE);
                holder.imageViewDoctor.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                //finalHolder.progressBar.setVisibility(View.GONE);
                holder.imageViewDoctor.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                //finalHolder.progressBar.setVisibility(View.GONE);
                holder.imageViewDoctor.setVisibility(View.INVISIBLE);
            }
        });

        holder.relativeLayoutParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // SharedParameters.DoctorUid=album.DoctorID;
              //  Toast.makeText(mContext, album.DoctorID, Toast.LENGTH_SHORT).show();
             //  mContext.startActivity(new Intent(mContext,PatientViewDoctorActivity.class));
                        Intent intent=new Intent(mContext,PatientViewDoctorActivity.class);
                        intent.putExtra("Patient ID",aClass.PatientID);
                        intent.putExtra("Doctor ID",album.DoctorID);
                        intent.putExtra("Patient Name",aClass.PatientName);
                        mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

}

