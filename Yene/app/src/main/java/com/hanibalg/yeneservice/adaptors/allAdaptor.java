package com.hanibalg.yeneservice.adaptors;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.activities.ListServiceActivity;
import com.hanibalg.yeneservice.activities.ProvidersUserListActivity;
import com.hanibalg.yeneservice.models.ServiceListModel;
import com.hanibalg.yeneservice.models.servicesModel;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

public class allAdaptor extends RecyclerView.Adapter<allAdaptor.MyViewHolder>{
    private Context context;
    private List<servicesModel> serviceList;
    private FirebaseStorage firebaseStorage;

    public allAdaptor(Context context, List<servicesModel> serviceList){
        this.context = context;
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public allAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.all_catagory_card,parent,false);
        firebaseStorage = FirebaseStorage.getInstance();
        return new allAdaptor.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Picasso.get().load(serviceList.get(position).getImg()).placeholder(R.drawable.icons_circle_bubbles).into(holder.img);
        //length
        String name = serviceList.get(position).getService_name();
        if(name.length() > 7){
            name = name.substring(0,6);
            holder.name.setText(name);
        }else{
            holder.name.setText(name);
        }

        //color
        Random r = new Random();
        int red = r.nextInt(255 - 0 + 1);
        int green = r.nextInt(255 - 0 + 1);
        int blue= r.nextInt(255 - 0 + 1);
        holder.cardView.setCardBackgroundColor(Color.rgb(red,green,blue));

        holder.cardView.setOnClickListener(v -> {
            Intent c = new Intent(context, ListServiceActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            c.putExtra("categoryName",serviceList.get(position).getService_name());
            context.startActivity(c);
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
        MaterialCardView cardView;

        public MyViewHolder(View itemView){
            super(itemView);
            mview = itemView;

            name = mview.findViewById(R.id.na);
            img = mview.findViewById(R.id.AA);
            cardView =  mview.findViewById(R.id.AAA);
        }
    }
}
