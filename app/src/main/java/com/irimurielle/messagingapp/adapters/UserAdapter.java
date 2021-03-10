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
import com.irimurielle.messagingapp.R;
import com.irimurielle.messagingapp.models.User;
import com.irimurielle.messagingapp.ui.ChattingActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Context mContext;
    private List<User> mUsers;
    private boolean imChat;

    public UserAdapter(Context mContext, List<User> mUsers, boolean imChat) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.imChat = imChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_user, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        User user = mUsers.get(position);
        holder.name.setText(user.getName());
        if(user.getImageUrl().equals("default")){
            holder.profileImage.setImageResource(R.drawable.profile);
        }else {
            Glide.with(mContext).load(user.getImageUrl()).into(holder.profileImage);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChattingActivity.class);
                intent.putExtra("userid", user.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.profile_image) ImageView profileImage;
        @BindView(R.id.name) TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
