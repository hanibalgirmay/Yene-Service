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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.activities.ProviderPageActivity;
import com.hanibalg.yeneservice.models.ProviderModel;
import com.hanibalg.yeneservice.models.ServiceListModel;
import com.hanibalg.yeneservice.models.UserModel;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class FavoriteAdaptor extends RecyclerView.Adapter<FavoriteAdaptor.ViewHolder> {
    private List<UserModel> mProvider;
    private Context context;
    private UserModel user;
    private FirebaseFirestore firebaseFirestore;

    public FavoriteAdaptor(Context context,List<UserModel> mProvider){
        this.context = context;
        this.mProvider = mProvider;
    }
    @NonNull
    @Override
    public FavoriteAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_bookmark,parent,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
//        auth = FirebaseAuth.getInstance();
        return new FavoriteAdaptor.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdaptor.ViewHolder holder, int position) {
        user = mProvider.get(position);
//        mService = mProvider.get(position).getServiceListId();

        holder.name.setText(mProvider.get(position).getFirstName());
//        holder.workingArea.setText(mProvider.get(position).serviceList());
        Picasso.get().load(mProvider.get(position).getImage()).into(holder.circleImageView);
//        holder.ratingBar.setRating(mProvider.get(position).getRating());
        holder.favoriteButton.setFavorite(true);
        holder.ratingBar.setRating(3.8f);
        holder.favoriteButton.setOnFavoriteChangeListener((buttonView, favorite) -> {
            favorite = true;
            if (buttonView.isFavorite()){
//                    removeFav(position);
            }else {
//                    removeFav(position);
                Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
            }
        });
        holder.cardView.setOnClickListener(v -> {
            providerInfo(user);
//            Intent nw = new Intent(context, ProviderPageActivity.class);
//            nw.putExtra("userID", mProvider.get(position).getUserID());
//            context.startActivity(nw);
        });
    }

    private void providerInfo(UserModel user){
        firebaseFirestore.collection("Service_Providers")
                .whereEqualTo("user_id",user.getUserID())
                .get()
                .addOnCompleteListener((OnCompleteListener<QuerySnapshot>) task -> {
                    if(task.getResult().isEmpty()){
                        Toast.makeText(context, "Provider did not exists anymore", Toast.LENGTH_SHORT).show();
                    }
                    if(task.isSuccessful()){
                        for (DocumentChange doc: task.getResult().getDocumentChanges()){
                            ProviderModel providerModel = doc.getDocument().toObject(ProviderModel.class);

                            final List<String> workingArea = providerModel.getSpeciality();
                            Intent nw = new Intent(context, ProviderPageActivity.class);
                            nw.putExtra("userData", (Serializable) user);
                            nw.putExtra("providerDataInfo", user.getUserID());
                            nw.putExtra("providerDataSpe", String.valueOf(workingArea));
                            context.startActivity(nw);
                        }
                    }
                }).addOnFailureListener(e -> e.getStackTrace());
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
