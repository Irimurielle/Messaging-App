package com.irimurielle.messagingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.irimurielle.messagingapp.R;
import com.irimurielle.messagingapp.models.Chat;
import com.irimurielle.messagingapp.models.User;
import com.irimurielle.messagingapp.ui.ChattingActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChattingAdapter extends RecyclerView.Adapter<ChattingAdapter.ViewHolder>{

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    private Context mContext;
    private List<Chat> mChat;
    private String imageUrl;

    FirebaseUser firebaseUser;

    public ChattingAdapter(Context mContext, List<Chat> mChat, String imageUrl) {
        this.mContext = mContext;
        this.mChat = mChat;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public ChattingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_right, parent, false);
            return new ChattingAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_left, parent, false);
            return new ChattingAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChattingAdapter.ViewHolder holder, int position) {

        Chat chat = mChat.get(position);

        holder.mMessageShown.setText(chat.getMessage());

        if(imageUrl.equals("default")){
            holder.profileImage.setImageResource(R.drawable.profile);
        }else{
            Glide.with(mContext).load(imageUrl).into(holder.profileImage);

        }if (position == mChat.size()-1){
            if (chat.isIsseen()){
                holder.seen.setText("Seen");
            } else {
                holder.seen.setText("Delivered");
            }
        } else {
            holder.seen.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.profile_image)
        CircleImageView profileImage;
        @BindView(R.id.messageShown)
        TextView mMessageShown;
        @BindView(R.id.seen)
        TextView seen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}

