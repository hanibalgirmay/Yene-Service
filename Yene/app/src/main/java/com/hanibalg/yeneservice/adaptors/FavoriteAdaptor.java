package com.hanibalg.yeneservice.adaptors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.activities.ProviderPageActivity;
import com.hanibalg.yeneservice.models.ProviderModel;
import com.hanibalg.yeneservice.models.ServiceListModel;
import com.hanibalg.yeneservice.models.UserModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteAdaptor extends RecyclerView.Adapter<FavoriteAdaptor.ViewHolder> {
    private List<UserModel> mProvider;
    private UserModel mUser;
    private ServiceListModel mService;
    private Context context;

    public FavoriteAdaptor(Context context,List<UserModel> mProvider){
        this.context = context;
        this.mProvider = mProvider;
    }
    @NonNull
    @Override
    public FavoriteAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_bookmark,parent,false);
//        firebaseFirestore = FirebaseFirestore.getInstance();
//        auth = FirebaseAuth.getInstance();
        return new FavoriteAdaptor.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdaptor.ViewHolder holder, int position) {
//        mUser = mProvider.get(position).getUserId();
//        mService = mProvider.get(position).getServiceListId();

        holder.name.setText(mProvider.get(position).getFirstName());
//        holder.workingArea.setText(mProvider.get(position).serviceList());
        Picasso.get().load(mProvider.get(position).getImage()).into(holder.circleImageView);
//        holder.ratingBar.setRating(mProvider.get(position).getRating());
        holder.favoriteButton.setFavorite(true);
        holder.ratingBar.setRating(3.8f);
        holder.favoriteButton.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                favorite = true;
                if (buttonView.isFavorite()){
//                    removeFav(position);
                }else {
//                    removeFav(position);
                    Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.cardView.setOnClickListener(v -> {
            Intent nw = new Intent(context, ProviderPageActivity.class);
            nw.putExtra("userID", mProvider.get(position).getUserID());
//                nw.putExtra("firstName", mProvider.get(position).getFirstName());
            context.startActivity(nw);
        });
    }

    @Override
    public int getItemCount() {
        return mProvider.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View view;
        private ImageView circleImageView;
        private TextView name;
        private RatingBar ratingBar;
        private TextView about;
        MaterialFavoriteButton favoriteButton;
        CardView cardView;

        private TextView workingArea;
        private ProgressBar progressBar;
        public ViewHolder(View itemView){
            super(itemView);
            view=itemView;
            circleImageView = view.findViewById(R.id.pro);
            name = (TextView)view.findViewById(R.id.nm);
            workingArea = (TextView)view.findViewById(R.id.workingArea);
            ratingBar = view.findViewById(R.id.rating);
            about = view.findViewById(R.id.about);
            favoriteButton = view.findViewById(R.id.img_fav);
            cardView = view.findViewById(R.id.card);

//            progressBar = (ProgressBar)view.findViewById(R.id.progress);
        }
    }
}
