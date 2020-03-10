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

import com.example.yeneservice.Models.Service;
import com.example.yeneservice.PagesFragment.ServiceListProvidersActivity;
import com.example.yeneservice.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeHorizontalServicesAdapter extends RecyclerView.Adapter<HomeHorizontalServicesAdapter.MyViewHolder>  {
    private Context mContext ;
    private List<Service> mData ;

    public HomeHorizontalServicesAdapter(Context mContext, List<Service> mData){
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public HomeHorizontalServicesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view ;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_horizontal_view,parent,false);

        return new HomeHorizontalServicesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeHorizontalServicesAdapter.MyViewHolder holder, final int position) {
        holder.name.setText(mData.get(position).getTitle());
        Picasso.get().load(mData.get(position).getImg()).into(holder.img);
//        holder.img.setImageResource(mData.get(position).getImg());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ServiceListProvidersActivity.class);
//                // passing data to the book activity
                intent.putExtra("name",mData.get(position).getTitle());
                intent.putExtra("image",mData.get(position).getImg());
//                // start the activity
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
        TextView name, pay,desc, appointdate;
        ImageView img;
        CardView cardView ;

        public MyViewHolder(View itemView) {
            super(itemView);
            mview = itemView;

            name = mview.findViewById(R.id.service_name_title);
            img = mview.findViewById(R.id.img_placeholder);
            cardView =  mview.findViewById(R.id.horId);
        }

    }
}
