package com.example.yeneservice.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yeneservice.Models.ReviewsModel;
import com.example.yeneservice.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyViewHolder> {
    private Context mContext ;
    private List<ReviewsModel> mData ;

    public ReviewsAdapter(Context mContext, List<ReviewsModel> mData){
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ReviewsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_review_view,parent,false);
        return new ReviewsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.MyViewHolder holder, final int position) {
        holder.name.setText(mData.get(position).getFirstName());
        holder.rate.setRating(mData.get(position).getRate());
        holder.desc.setText(mData.get(position).getContent());
        Picasso.get().load(mData.get(position).getImage()).into(holder.img);
//        holder.img.setImageResource(mData.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, dat, desc;
        RatingBar rate;
        ImageView img;
//        Button cardView ;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nm);
//            dat = itemView.findViewById(R.id.pay);
            rate = itemView.findViewById(R.id.rating);
            img = itemView.findViewById(R.id.pro);
            desc = itemView.findViewById(R.id.comments);
        }

    }
}
