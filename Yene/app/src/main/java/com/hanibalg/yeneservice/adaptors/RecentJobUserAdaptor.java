package com.hanibalg.yeneservice.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.models.UserModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecentJobUserAdaptor extends RecyclerView.Adapter<RecentJobUserAdaptor.UserView> {
    private Context context;
    private List<UserModel> mUser;

    public RecentJobUserAdaptor(Context context,List<UserModel> mUser){
        this.mUser = mUser;
        this.context = context;
    }
    @NonNull
    @Override
    public UserView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(context);
        view = mInflater.inflate(R.layout.recent_job,parent,false);
        return new UserView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserView holder, int position) {
        //Load image to view
        if(mUser.get(position).getImage().equals("default")){
            Picasso.get().load("https://www.icwukltd.co.uk/wp-content/uploads/2016/12/avatar-placeholder.png").placeholder(R.drawable.placeholder_profile).into(holder.image);
        }
        Picasso.get().load(mUser.get(position).getImage()).placeholder(R.drawable.placeholder_profile).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class UserView extends RecyclerView.ViewHolder {
        ImageView image;
        public UserView(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }
}
