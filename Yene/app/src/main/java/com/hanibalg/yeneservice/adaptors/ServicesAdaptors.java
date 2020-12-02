package com.hanibalg.yeneservice.adaptors;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.activities.ListServiceActivity;
import com.hanibalg.yeneservice.models.servicesModel;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

public class ServicesAdaptors extends RecyclerView.Adapter<ServicesAdaptors.MyViews> {

    private List<servicesModel> mData;
    private Context context;

    public ServicesAdaptors(List<servicesModel> mData, Context context){
        this.mData = mData;
        this.context = context;
    }

    @NonNull
    @Override
    public ServicesAdaptors.MyViews onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        v = layoutInflater.inflate(R.layout.circular_service_list,parent,false);
        return new ServicesAdaptors.MyViews(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesAdaptors.MyViews holder, int position) {
        Picasso.get().load(mData.get(position).getService_icon()).placeholder(R.drawable.icons_circle_bubbles).into(holder.icon);

        //length
        String name = mData.get(position).getService_name();
        if(name.length() > 7){
            name = name.substring(0,6);
            holder.title.setText(name);
        }else{
            holder.title.setText(name);
        }

        //color
        Random r = new Random();
        int red = r.nextInt(255 - 0 + 1);
        int green = r.nextInt(255 - 0 + 1);
        int blue= r.nextInt(255 - 0 + 1);
        holder.linearLayout.setCardBackgroundColor(Color.rgb(red,green,blue));

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent c = new Intent(context, ListServiceActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.putExtra("categoryName",mData.get(position).getService_name());
                context.startActivity(c);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    class MyViews extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView icon;
        private CardView linearLayout;

        public MyViews(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.name);
            icon = itemView.findViewById(R.id.icons);
            linearLayout = itemView.findViewById(R.id.card);
        }
    }
}
