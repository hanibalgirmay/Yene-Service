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
import com.example.yeneservice.R;
import com.example.yeneservice.ServiceProviderProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ServiceProviderAdapter extends RecyclerView.Adapter<ServiceProviderAdapter.MyViewHolder> {
    private Context mContext ;
    private List<ServicesProvider> mData ;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public ServiceProviderAdapter(Context mContext, List<ServicesProvider> mData){
        this.mContext = mContext;
        this.mData = mData;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
//        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list_service,parent,false);//        firebaseFirestore = FirebaseFirestore.getInstance();

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.name.setText(mData.get(position).getFirstName());
//        holder.img.setImageResource(mData.get(position).get());
        Picasso.get().load(mData.get(position).getProfile_img()).into(holder.img);
        holder.work.setText(mData.get(position).getWorking_area());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ServiceProviderProfileActivity.class);
                intent.putExtra("documentID",mData.get(position).getDocumentId());
                intent.putExtra("userID",mData.get(position).getUserID());
                intent.putExtra("firstName",mData.get(position).getFirstName());
                intent.putExtra("lastname",mData.get(position).getLastName());
                intent.putExtra("address",mData.get(position).getAddress());
                intent.putExtra("working_area",mData.get(position).getWorking_area());
                intent.putExtra("about_me",mData.get(position).getAbout());
                intent.putExtra("long",mData.get(position).getLongt().getLongitude());
                intent.putExtra("lat",mData.get(position).getLongt().getLatitude());
                intent.putExtra("img",mData.get(position).getProfile_img());
                intent.putExtra("fragmentRev",mData.get(position).getUserID());
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
        TextView name,se,work;
        ImageView img;
        CardView cardView ;

        public MyViewHolder(View itemView) {
            super(itemView);
            mview = itemView;

            name = mview.findViewById(R.id.name);
            work = mview.findViewById(R.id.area_work);
            img = mview.findViewById(R.id.profile_img);
            cardView =  mview.findViewById(R.id.card_v);
        }

    }

}
