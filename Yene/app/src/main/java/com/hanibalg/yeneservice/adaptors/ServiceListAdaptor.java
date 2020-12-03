<<<<<<< HEAD
package com.hanibalg.yeneservice.adaptors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.activities.ProvidersUserListActivity;
import com.hanibalg.yeneservice.models.ServiceListModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ServiceListAdaptor extends RecyclerView.Adapter<ServiceListAdaptor.MyViewHolder> {

    private Context context;
    private List<ServiceListModel> serviceList;

    public ServiceListAdaptor(Context context, List<ServiceListModel> serviceList){
        this.context = context;
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public ServiceListAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.card_service_list,parent,false);

        return new ServiceListAdaptor.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        String na;
//        if(serviceList.get(position).getName().length() > 9){
//            holder.name.setText(serviceList.get(position).getName().substring(9));
//        }else{
//            
//        }
        holder.name.setText(serviceList.get(position).getName());
//        holder.img.setImageResource(serviceList.get(position).getImages());
        Picasso
                .get()
                .load(serviceList.get(position).getImage())
                .placeholder(R.drawable.placeholder_icon)
                .into(holder.img);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ProvidersUserListActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("title", serviceList.get(position).getName());
                intent.putExtra("icon", serviceList.get(position).getImage());
//                intent.putExtra("category", serviceList.get(position).getCategory());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        View mview;
        TextView name, pay,desc, appointdate;
        ImageView img;
        CardView cardView;

        public MyViewHolder(View itemView){
            super(itemView);
            mview = itemView;

            name = mview.findViewById(R.id.na);
            img = mview.findViewById(R.id.AA);
            cardView =  mview.findViewById(R.id.AAA);
        }
    }
}
=======
package com.hanibalg.yeneservice.adaptors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.activities.ProvidersUserListActivity;
import com.hanibalg.yeneservice.models.ServiceListModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ServiceListAdaptor extends RecyclerView.Adapter<ServiceListAdaptor.MyViewHolder> {

    private Context context;
    private List<ServiceListModel> serviceList;

    public ServiceListAdaptor(Context context, List<ServiceListModel> serviceList){
        this.context = context;
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public ServiceListAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.card_service_list,parent,false);

        return new ServiceListAdaptor.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        String na;
//        if(serviceList.get(position).getName().length() > 9){
//            holder.name.setText(serviceList.get(position).getName().substring(9));
//        }else{
//            
//        }
        holder.name.setText(serviceList.get(position).getName());
//        holder.img.setImageResource(serviceList.get(position).getImages());
        Picasso
                .get()
                .load(serviceList.get(position).getImage())
                .placeholder(R.drawable.placeholder_icon)
                .into(holder.img);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ProvidersUserListActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("title", serviceList.get(position).getName());
                intent.putExtra("icon", serviceList.get(position).getImage());
//                intent.putExtra("category", serviceList.get(position).getCategory());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        View mview;
        TextView name, pay,desc, appointdate;
        ImageView img;
        CardView cardView;

        public MyViewHolder(View itemView){
            super(itemView);
            mview = itemView;

            name = mview.findViewById(R.id.na);
            img = mview.findViewById(R.id.AA);
            cardView =  mview.findViewById(R.id.AAA);
        }
    }
}
>>>>>>> 93fe2da156e9e07e2e292889abfd1898279d53b3
