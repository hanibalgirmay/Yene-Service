package com.example.yeneservice.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yeneservice.Models.ServicesProvider;
import com.example.yeneservice.R;
import com.example.yeneservice.ServiceProviderProfileActivity;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder>{
    private List<ServicesProvider> notificationList;
    private Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    DocumentReference reference;

    public FavoriteAdapter(Context context, List<ServicesProvider> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }


    @NonNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_my_fav,parent,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        return new FavoriteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoriteAdapter.ViewHolder holder, final int position) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        reference = firebaseFirestore.collection("Users/"+auth.getUid()+"/Favorite").document();

        holder.name.setText(notificationList.get(position).getFirstName());
        holder.workingArea.setText(notificationList.get(position).getWorking_area());
        holder.about.setText(notificationList.get(position).getAbout_me());
        Picasso.get().load(notificationList.get(position).getProfile_img()).into(holder.circleImageView);
        holder.ratingBar.setRating(notificationList.get(position).getRating());
        holder.favoriteButton.setFavorite(true);
        holder.favoriteButton.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                favorite = true;
                if (buttonView.isFavorite()){
                    removeFav(position);
                }else {
                    removeFav(position);
                    Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nw = new Intent(context, ServiceProviderProfileActivity.class);
                nw.putExtra("userID", notificationList.get(position).getUserID());
                nw.putExtra("firstName", notificationList.get(position).getFirstName());
                nw.putExtra("working_area", notificationList.get(position).getWorking_area());
                nw.putExtra("about_me", notificationList.get(position).getAbout_me());
                nw.putExtra("lastname", notificationList.get(position).getLastName());
                nw.putExtra("img", notificationList.get(position).getProfile_img());
                context.startActivity(nw);
            }
        });
    }

    private void removeFav(final int postion) {
        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Toast.makeText(context, "data:"+ notificationList.get(postion).getDocumentId()+ notificationList.get(postion).getFirstName(), Toast.LENGTH_SHORT).show();
//                documentSnapshot.getDocumentReference();
                final String keyRef = notificationList.get(postion).getDocumentId();
                firebaseFirestore.collection("Users/"+auth.getUid()+"/Favorite").document(keyRef)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Favorite delete successfully:  "+keyRef, Toast.LENGTH_SHORT).show();
                                notificationList.remove(postion);
                                notifyItemRemoved(postion);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "can not delete Favorite", Toast.LENGTH_SHORT).show();
                    }
                });

//                Toast.makeText(context, "doc id"+ notificationList.get(postion).getDocId(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
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
