package com.hanibalg.yeneservice.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.models.Reviews;
import com.hanibalg.yeneservice.models.UserModel;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder>  {
    private Context mContext;
    private List<Reviews> mData;
    private List<UserModel> mUser;

    public ReviewAdapter(Context mContext, List<Reviews> mData,List<UserModel> mUser) {
        this.mContext = mContext;
        this.mData = mData;
        this.mUser = mUser;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_review_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Reviews album = mData.get(position);
        holder.name.setText(mUser.get(position).getFirstName());
        holder.com.setText(album.getComments());
        holder.ratingBar.setRating(album.getRating());
        // loading album cover using Glide library
        Glide.with(mContext).load(mUser.get(position).getImage()).placeholder(R.drawable.placeholder_profile).into(holder.thumbnail);
//        holder.overflow.setOnClickListener(view -> Toast.makeText(mContext,"Card view clicked "+ getItemId(position), Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, com;
        public ImageView thumbnail;
        public RatingBar ratingBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nm);
            thumbnail = itemView.findViewById(R.id.pro);
            ratingBar = itemView.findViewById(R.id.rating);
            com = itemView.findViewById(R.id.comments);
        }
    }
}
