<<<<<<< HEAD
package com.hanibalg.yeneservice.adaptors;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.activities.ProviderPageActivity;
import com.hanibalg.yeneservice.activities.ReportProblemActivity;
import com.hanibalg.yeneservice.config.BlurTransformation;
import com.hanibalg.yeneservice.models.ProviderModel;
import com.hanibalg.yeneservice.models.Reviews;
import com.hanibalg.yeneservice.models.ServiceListModel;
import com.hanibalg.yeneservice.models.UserModel;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoriteAdaptor extends RecyclerView.Adapter<FavoriteAdaptor.ViewHolder> {
    private List<UserModel> mProvider;
    private Context context;
    private String userId;
    private UserModel user;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;

    @NonNull
    @Override
    public FavoriteAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_bookmark,parent,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        return new FavoriteAdaptor.ViewHolder(view);
    }
    public FavoriteAdaptor(Context context,List<UserModel> mProvider){
        this.context = context;
        this.mProvider = mProvider;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdaptor.ViewHolder holder, int position) {
        user = mProvider.get(position);
        Glide.with(context)
                .load(mProvider.get(position).getImage())
                .placeholder(R.drawable.background)
                .centerCrop()
                .override(300,200)
                .transform(new BlurTransformation(context))
                .into(holder.proBg);

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
                removeFav(position);
            }
        });
        holder.cardView.setOnClickListener(v -> {
            providerInfo(user,userId);
        });
        /*
        OPTION BUTTON TO DISPLAY OPTION LIST
         */
        holder.optionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.setOnMenuItemClickListener(item -> {
                    // TODO Auto-generated method stub
                    switch (item.getItemId()) {
                        case R.id.delete_option:
                            Toast.makeText(context, "delete",
                                    Toast.LENGTH_SHORT).show();
                            removeFav(position);
                            return true;
                        case R.id.share_option:
                            Toast.makeText(context, "share",
                                    Toast.LENGTH_SHORT).show();
                            ArrayList<String> shareMe = new ArrayList<String>();
                            shareMe.add(mProvider.get(position).getImage());
                            shareMe.add(mProvider.get(position).getFirstName());
                            shareMe.add(mProvider.get(position).getEmail());
                            shareMe.add(mProvider.get(position).getUserId());
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                            sendIntent.putStringArrayListExtra(Intent.EXTRA_STREAM, shareMe);
//                            sendIntent.putExtra(Intent.EXTRA_TEXT, (Serializable) shareMe);
                            sendIntent.setType("text/*");
                            Intent shareIntent = Intent.createChooser(sendIntent, "Share service Provider");
                            context.startActivity(shareIntent);
                            return true;
                        case R.id.report_option:
                            Toast.makeText(context, "report",
                                    Toast.LENGTH_SHORT).show();
                            Intent rep = new Intent(context, ReportProblemActivity.class);
                            rep.putExtra("userId",mProvider.get(position).getUserId());
                            context.startActivity(rep);
                            return true;
                    }
                    return false;
                });
                popupMenu.inflate(R.menu.card_option);
                popupMenu.show();
            }
        });
        /*
        Get Rating collection
         */
        getRating(user,holder.ratingBar);
    }

    private void getRating(UserModel provider, RatingBar rateLayout){
        final float ratee;
        Log.d("Fav_user","+++++++"+provider.getFirstName());
        Log.d("Fav_user","++++++++++++++"+provider.getUserId());
        Log.d("Fav_user","+++++++++++++++++++++++"+provider.getDocId());
        try {
            firebaseFirestore.collection("Users")
                    .document(provider.getUserId())
                    .collection("rating")
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isComplete()){
                            final float[] total = {0};
                            final float[] count = {0};
                            final float[] average = new float[1];

                            for (DocumentChange doc: task.getResult().getDocumentChanges()){
                                Reviews reviews = doc.getDocument().toObject(Reviews.class);
                                reviews.setId(doc.getDocument().getId());
//                            ratee = reviews.getRating();
                                total[0] = total[0] + reviews.getRating();
                                count[0] = count[0] + 1;
                                average[0] = total[0] / count[0];

                                rateLayout.setRating(average[0]);

                            }
                        }
                    }).addOnFailureListener(e -> Log.d("BookmarkFragment",""+e.getMessage()));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void removeFav(int position) {
        Toast.makeText(context, "removed", Toast.LENGTH_SHORT).show();
        final String keyRef = mProvider.get(position).getDocId();
        try{
            firebaseFirestore.collection("Users")
                    .document(auth.getUid())
                    .collection("Favorite")
                    .document(keyRef)
                    .delete()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Toast.makeText(context, "unbookmarked", Toast.LENGTH_SHORT).show();
                            mProvider.remove(position);
                            notifyItemRemoved(position);
                        }
                    }).addOnFailureListener(e -> Toast.makeText(context, "error unbookmark", Toast.LENGTH_SHORT).show());

        }catch (Exception e){

        }
    }

    private void providerInfo(UserModel user, String userId){
        Log.d("Fav_user","_"+ user.getUserId());
        firebaseFirestore.collection("Service_Providers")
                .whereEqualTo("user_id",user.getUserId())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.getResult().isEmpty()){
                        Toast.makeText(context, "Provider did not exists anymore", Toast.LENGTH_SHORT).show();
                    }
                    if(task.isSuccessful()){
                        for (DocumentChange doc: task.getResult().getDocumentChanges()){
                            ProviderModel providerModel = doc.getDocument().toObject(ProviderModel.class);
                            final List<String> workingArea = providerModel.getSpeciality();
                            Intent nw = new Intent(context, ProviderPageActivity.class);
                            nw.putExtra("userData", (Serializable) user);
                            nw.putExtra("providerDataInfo", user.getUserId());
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
        private ImageView circleImageView,proBg,optionBtn;
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
            proBg = view.findViewById(R.id.proBg);
            optionBtn = view.findViewById(R.id.option);
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
=======
package com.hanibalg.yeneservice.adaptors;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.activities.ProviderPageActivity;
import com.hanibalg.yeneservice.activities.ReportProblemActivity;
import com.hanibalg.yeneservice.config.BlurTransformation;
import com.hanibalg.yeneservice.models.ProviderModel;
import com.hanibalg.yeneservice.models.Reviews;
import com.hanibalg.yeneservice.models.ServiceListModel;
import com.hanibalg.yeneservice.models.UserModel;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoriteAdaptor extends RecyclerView.Adapter<FavoriteAdaptor.ViewHolder> {
    private List<UserModel> mProvider;
    private Context context;
    private String userId;
    private UserModel user;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;

    @NonNull
    @Override
    public FavoriteAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_bookmark,parent,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        return new FavoriteAdaptor.ViewHolder(view);
    }
    public FavoriteAdaptor(Context context,List<UserModel> mProvider){
        this.context = context;
        this.mProvider = mProvider;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdaptor.ViewHolder holder, int position) {
        user = mProvider.get(position);
        Glide.with(context)
                .load(mProvider.get(position).getImage())
                .placeholder(R.drawable.background)
                .centerCrop()
                .override(300,200)
                .transform(new BlurTransformation(context))
                .into(holder.proBg);

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
                removeFav(position);
            }
        });
        holder.cardView.setOnClickListener(v -> {
            providerInfo(user,userId);
        });
        /*
        OPTION BUTTON TO DISPLAY OPTION LIST
         */
        holder.optionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.setOnMenuItemClickListener(item -> {
                    // TODO Auto-generated method stub
                    switch (item.getItemId()) {
                        case R.id.delete_option:
                            Toast.makeText(context, "delete",
                                    Toast.LENGTH_SHORT).show();
                            removeFav(position);
                            return true;
                        case R.id.share_option:
                            Toast.makeText(context, "share",
                                    Toast.LENGTH_SHORT).show();
                            ArrayList<String> shareMe = new ArrayList<String>();
                            shareMe.add(mProvider.get(position).getImage());
                            shareMe.add(mProvider.get(position).getFirstName());
                            shareMe.add(mProvider.get(position).getEmail());
                            shareMe.add(mProvider.get(position).getUserId());
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                            sendIntent.putStringArrayListExtra(Intent.EXTRA_STREAM, shareMe);
//                            sendIntent.putExtra(Intent.EXTRA_TEXT, (Serializable) shareMe);
                            sendIntent.setType("text/*");
                            Intent shareIntent = Intent.createChooser(sendIntent, "Share service Provider");
                            context.startActivity(shareIntent);
                            return true;
                        case R.id.report_option:
                            Toast.makeText(context, "report",
                                    Toast.LENGTH_SHORT).show();
                            Intent rep = new Intent(context, ReportProblemActivity.class);
                            rep.putExtra("userId",mProvider.get(position).getUserId());
                            context.startActivity(rep);
                            return true;
                    }
                    return false;
                });
                popupMenu.inflate(R.menu.card_option);
                popupMenu.show();
            }
        });
        /*
        Get Rating collection
         */
        getRating(user,holder.ratingBar);
    }

    private void getRating(UserModel provider, RatingBar rateLayout){
        final float ratee;
        Log.d("Fav_user","+++++++"+provider.getFirstName());
        Log.d("Fav_user","++++++++++++++"+provider.getUserId());
        Log.d("Fav_user","+++++++++++++++++++++++"+provider.getDocId());
        try {
            firebaseFirestore.collection("Users")
                    .document(provider.getUserId())
                    .collection("rating")
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isComplete()){
                            final float[] total = {0};
                            final float[] count = {0};
                            final float[] average = new float[1];

                            for (DocumentChange doc: task.getResult().getDocumentChanges()){
                                Reviews reviews = doc.getDocument().toObject(Reviews.class);
                                reviews.setId(doc.getDocument().getId());
//                            ratee = reviews.getRating();
                                total[0] = total[0] + reviews.getRating();
                                count[0] = count[0] + 1;
                                average[0] = total[0] / count[0];

                                rateLayout.setRating(average[0]);

                            }
                        }
                    }).addOnFailureListener(e -> Log.d("BookmarkFragment",""+e.getMessage()));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void removeFav(int position) {
        Toast.makeText(context, "removed", Toast.LENGTH_SHORT).show();
        final String keyRef = mProvider.get(position).getDocId();
        try{
            firebaseFirestore.collection("Users")
                    .document(auth.getUid())
                    .collection("Favorite")
                    .document(keyRef)
                    .delete()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Toast.makeText(context, "unbookmarked", Toast.LENGTH_SHORT).show();
                            mProvider.remove(position);
                            notifyItemRemoved(position);
                        }
                    }).addOnFailureListener(e -> Toast.makeText(context, "error unbookmark", Toast.LENGTH_SHORT).show());

        }catch (Exception e){

        }
    }

    private void providerInfo(UserModel user, String userId){
        Log.d("Fav_user","_"+ user.getUserId());
        firebaseFirestore.collection("Service_Providers")
                .whereEqualTo("user_id",user.getUserId())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.getResult().isEmpty()){
                        Toast.makeText(context, "Provider did not exists anymore", Toast.LENGTH_SHORT).show();
                    }
                    if(task.isSuccessful()){
                        for (DocumentChange doc: task.getResult().getDocumentChanges()){
                            ProviderModel providerModel = doc.getDocument().toObject(ProviderModel.class);
                            final List<String> workingArea = providerModel.getSpeciality();
                            Intent nw = new Intent(context, ProviderPageActivity.class);
                            nw.putExtra("userData", (Serializable) user);
                            nw.putExtra("providerDataInfo", user.getUserId());
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
        private ImageView circleImageView,proBg,optionBtn;
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
            proBg = view.findViewById(R.id.proBg);
            optionBtn = view.findViewById(R.id.option);
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
>>>>>>> 93fe2da156e9e07e2e292889abfd1898279d53b3
