package com.example.yeneservice.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yeneservice.Models.ServicesProvider;
import com.example.yeneservice.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder>{
    private List<ServicesProvider> notificationList;
    private Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;

    public FavoriteAdapter(Context context, List<ServicesProvider> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }


    @NonNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_my_fav,parent,false);
        return new FavoriteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoriteAdapter.ViewHolder holder, final int position) {
        firebaseFirestore = FirebaseFirestore.getInstance();

        holder.name.setText(notificationList.get(position).getFirstName());
        holder.workingArea.setText(notificationList.get(position).getWorking_area());
        holder.about.setText(notificationList.get(position).getAbout_me());
        Picasso.get().load(notificationList.get(position).getProfile_img()).into(holder.circleImageView);
//        firebaseFirestore.collection("Users").document(from).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//                String name = documentSnapshot.getString("firstName");
//                String image = documentSnapshot.getString("image");
//
//                holder.name.setText(name);
//                Picasso.get().load(image).into(holder.circleImageView);
//            }
//        });
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

//            progressBar = (ProgressBar)view.findViewById(R.id.progress);
        }
    }

}
