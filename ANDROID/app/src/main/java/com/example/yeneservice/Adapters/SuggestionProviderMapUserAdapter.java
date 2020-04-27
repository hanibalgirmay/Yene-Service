package com.example.yeneservice.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yeneservice.Models.ServicesProvider;
import com.example.yeneservice.Models.UserModel;
import com.example.yeneservice.R;
import com.example.yeneservice.ServiceProviderProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SuggestionProviderMapUserAdapter extends RecyclerView.Adapter<SuggestionProviderMapUserAdapter.MyViewHolder>  {

    private Context mContext ;
    private List<UserModel> mData ;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public SuggestionProviderMapUserAdapter(Context mContext, List<UserModel> mData){
        this.mContext = mContext;
        this.mData = mData;
    }
    @NonNull
    @Override
    public SuggestionProviderMapUserAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
//        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_circle_image,parent,false);//        firebaseFirestore = FirebaseFirestore.getInstance();

        return new SuggestionProviderMapUserAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SuggestionProviderMapUserAdapter.MyViewHolder holder, final int position) {
//        holder.name.setText(mData.get(position).getFirstName());
//        holder.img.setImageResource(mData.get(position).get());
        Picasso.get().load(mData.get(position).getImage()).into(holder.img);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ServiceProviderProfileActivity.class);
                intent.putExtra("user_d",mData.get(position).getUsername());
                // start the activity
                mContext.startActivity(intent);

            }
        });
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View mview;
        TextView name;
        ImageView img;
        CardView cardView ;

        public MyViewHolder(View itemView) {
            super(itemView);
            mview = itemView;

//            name = mview.findViewById(R.id.name);
            img = mview.findViewById(R.id.user);
            cardView =  mview.findViewById(R.id.card_usr);
        }

    }
}
