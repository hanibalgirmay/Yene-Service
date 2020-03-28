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
import com.example.yeneservice.ServiceProviderProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeServiceAdapter extends RecyclerView.Adapter<HomeServiceAdapter.MyViewHolder>{

    private Context context ;
    private List<Service> mData ;

    public HomeServiceAdapter(Context mContext, List<Service> mData){
        this.context = mContext;
        this.mData = mData;
    }
    @NonNull
    @Override
    public HomeServiceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view ;
        LayoutInflater mInflater = LayoutInflater.from(context);
        view = mInflater.inflate(R.layout.card_home_service,parent,false);
//        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_home_service_view,parent,false);

        return new HomeServiceAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeServiceAdapter.MyViewHolder holder, final int position) {
        holder.name.setText(mData.get(position).getTitle());
        Picasso.get().load(mData.get(position).getImg()).into(holder.img);
//        holder.img.setImageResource(mData.get(position).getImg());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ServiceListProvidersActivity.class);
                // passing data to the book activity
                intent.putExtra("name",mData.get(position).getTitle());
                intent.putExtra("serviceID",mData.get(position).getServiceID());
                intent.putExtra("image",mData.get(position).getImg());
                // start the activity
                context.startActivity(intent);

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

            name = mview.findViewById(R.id.na);
            img = mview.findViewById(R.id.AA);
            cardView =  mview.findViewById(R.id.AAA);
        }

    }
}
