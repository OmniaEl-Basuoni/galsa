package com.example.omnia.ourproject.Patient.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jean.jcplayer.JcAudio;
import com.example.jean.jcplayer.JcPlayerView;
import com.example.jean.jcplayer.JcStatus;
import com.example.omnia.ourproject.R;
import com.example.omnia.ourproject.SharedClasses.ChatMessage;
import com.github.pavlospt.roundedletterview.RoundedLetterView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.IOException;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by 3ZT on 03-Dec-17.
 */

public class GroupChatAdapter extends BaseAdapter {
    private String OwnerID;
    private JcPlayerView playerView;
    private DialogPlus dialog;
    Context context;
    List<ChatMessage> chatMessageList;
    private RelativeLayout.LayoutParams params;

    public GroupChatAdapter(Context context,
                            List<ChatMessage> chatMessageList,String OwnerID)
    {
        this.OwnerID=OwnerID;
        this.context=context;
        this.chatMessageList = chatMessageList;
        params = new RelativeLayout.LayoutParams(MATCH_PARENT,WRAP_CONTENT);
    }

    @Override
    public int getCount() {
        return chatMessageList.size();
    }

    @Override
    public Object getItem(int i) {
       return chatMessageList.get(i).getMessageTime();
    }

    @Override
    public long getItemId(int i) {
        return chatMessageList.indexOf(getItem(i));
    }

    class ViewHolderClass
    {

        RoundedLetterView roundedLetterView;
        RelativeLayout Container,AllInOne;
        ProgressBar progressBar;
        CardView frameLayout;
        LinearLayout cardViewVoice;
        ImageView ImageView,StateView,audioPlay;
        TextView MessageText,text;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        ViewHolderClass holder=null;

        LayoutInflater mIFlater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View row=mIFlater.inflate(R.layout.view_message,viewGroup,false);

        holder=new ViewHolderClass();

        holder.audioPlay=row.findViewById(R.id.play);
        holder.cardViewVoice=row.findViewById(R.id.voice);
        holder.text=row.findViewById(R.id.text);
        holder.StateView=row.findViewById(R.id.StateView);
        holder.ImageView=row.findViewById(R.id.photoView);
        holder.MessageText=row.findViewById(R.id.message_text);
        holder.Container=row.findViewById(R.id.Respon);
        holder.progressBar=row.findViewById(R.id.PB);
        holder.frameLayout=row.findViewById(R.id.FL_Image);
        holder.AllInOne=row.findViewById(R.id.Container);
        holder.roundedLetterView=row.findViewById(R.id.Sender);


        //region CheckMessage
        if(chatMessageList.get(i).getMessageState().equals("NotSend"))
        {
            holder.StateView.setBackgroundColor(context.getResources().getColor(R.color.colorFeed4));
        }
        else if(chatMessageList.get(i).getMessageState().equals("Send"))
        {
            holder.StateView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
        }
        else {
            holder.StateView.setBackgroundColor(context.getResources().getColor(R.color.colorFeed));
        }
        //endregion


        //region CheckType
        if(chatMessageList.get(i).getType().equals("Photo"))
        {
            holder.cardViewVoice.setVisibility(View.GONE);
            holder.MessageText.setVisibility(View.GONE);

            final ViewHolderClass finalHolder = holder;
            ImageLoader.getInstance().displayImage(chatMessageList.get(i).getMessageText(),
                    holder.ImageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    finalHolder.progressBar.setVisibility(View.VISIBLE);
                    finalHolder.ImageView.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    finalHolder.progressBar.setVisibility(View.VISIBLE);
                    finalHolder.ImageView.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    finalHolder.progressBar.setVisibility(View.GONE);
                    finalHolder.ImageView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                }
            });
        }
        else if (chatMessageList.get(i).getType().equals("Voice"))
        {
            holder.frameLayout.setVisibility(View.GONE);
            holder.MessageText.setVisibility(View.GONE);


            final MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer.setDataSource(chatMessageList.get(i).getMessageText());
            } catch (IOException e) {
                e.printStackTrace();
            }


            final ViewHolderClass finalHolder1 = holder;
            finalHolder1.audioPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(chatMessageList.get(i).getMessageText());
                }
            });


        }
        else {
            holder.cardViewVoice.setVisibility(View.GONE);
            holder.frameLayout.setVisibility(View.GONE);
            holder.MessageText.setText(chatMessageList.get(i).getMessageText());
        }
        //endregion





        //region CheckSender
        if(chatMessageList.get(i).getMessageOwner().equals(OwnerID))
        {
            holder.StateView.setVisibility(View.VISIBLE);
            params.setMarginStart(dpToPx(60));
            holder.Container.setGravity(Gravity.END);
            holder.Container.setLayoutParams(params);
            holder.AllInOne.setBackground(context.getResources().getDrawable(R.drawable.lightsolid_withoutstroke_15dp));
            holder.MessageText.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
        else {
            holder.roundedLetterView.setVisibility(View.VISIBLE);
            holder.roundedLetterView.setTitleText(chatMessageList.get(i).getMessageOwner().substring(0,3));
            holder.StateView.setVisibility(View.GONE);
            params.setMarginEnd(100);
            holder.Container.setGravity(Gravity.START);
            holder.Container.setLayoutParams(params);
            holder.AllInOne.setBackground(context.getResources().getDrawable(R.drawable.backgrounsolid_withoutstroke_15dp));
            holder.MessageText.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            holder.text.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
            holder.audioPlay.setImageResource(R.drawable.ic_play2);

        }
        //endregion




        return row;
    }

    private void showDialog(String s)
    {
         dialog = DialogPlus.newDialog(context)
                 .setContentHolder(new ViewHolder(R.layout.view_player))
                .setExpanded(false)
                 .setGravity(Gravity.TOP)
                 .setOnDismissListener(new OnDismissListener() {
                     @Override
                     public void onDismiss(DialogPlus dialog) {
                         playerView.kill();
                     }
                 })
                .create();
         playerView=dialog.getHolderView().findViewById(R.id.jcplayer);
         playerView.playAudio(JcAudio.createFromURL("",s));
         playerView.registerStatusListener(new JcPlayerView.JcPlayerViewStatusListener() {
             @Override
             public void onPausedStatus(JcStatus jcStatus) {
             }

             @Override
             public void onContinueAudioStatus(JcStatus jcStatus) {
             }

             @Override
             public void onPlayingStatus(JcStatus jcStatus) {

             }

             @Override
             public void onTimeChangedStatus(JcStatus jcStatus) {

             }

             @Override
             public void onCompletedAudioStatus(JcStatus jcStatus) {
                dialog.dismiss();
             }

             @Override
             public void onPreparedAudioStatus(JcStatus jcStatus) {

             }
         });
        dialog.show();
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    //endregion

}
