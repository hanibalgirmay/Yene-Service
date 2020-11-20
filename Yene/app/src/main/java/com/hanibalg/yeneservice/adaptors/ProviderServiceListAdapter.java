package com.hanibalg.yeneservice.adaptors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.activities.ProviderPageActivity;
import com.hanibalg.yeneservice.models.ProviderModel;
import com.hanibalg.yeneservice.models.ServiceListModel;
import com.hanibalg.yeneservice.models.UserModel;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class ProviderServiceListAdapter extends RecyclerView.Adapter<ProviderServiceListAdapter.MyViewHolder>{
    private Context mContext ;
    private List<ProviderModel> mData ;
    private List<UserModel> mUser ;
    private List<ServiceListModel> mService ;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    Activity activity;
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    public ProviderServiceListAdapter(Activity activity,
                                      List<ProviderModel> mData, List<UserModel> mUser){
        this.activity= activity;
        this.mData = mData;
        this.mUser = mUser;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view ;
        firebaseFirestore = FirebaseFirestore.getInstance();
        if(viewType == MSG_TYPE_LEFT){
            view = LayoutInflater.from(activity).inflate(R.layout.card_provider_individual,parent,false);//
            return new ProviderServiceListAdapter.MyViewHolder(view);
        } else {
            view = LayoutInflater.from(activity).inflate(R.layout.card_provider_organization,parent,false);
            return new ProviderServiceListAdapter.MyViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final UserModel uModel = mUser.get(position);
        final ProviderModel pModels = mData.get(position);

        holder.name.setText(mUser.get(position).getFirstName());
//        holder.img.setImageResource(mData.get(position).get());
        Picasso.get().load(mUser.get(position).getImage()).into(holder.img);
//        holder.img.setImageResource(mData.get(position).get());
        final List<String> workingArea = mData.get(position).getSpeciality();
        holder.work.setText(workingArea.get(0));
        holder.cardView.setOnClickListener(v -> {
            Intent page = new Intent(activity, ProviderPageActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // start the activity
            page.putExtra("userData", (Serializable) uModel);
            page.putExtra("providerDataInfo", pModels.getUser_id());
//            page.putExtra("providerLocationInfo", (Parcelable) pModels.getLocationId());
            page.putExtra("providerDataSpe", String.valueOf(workingArea));
            page.putExtra("providerDataAbout",mData.get(position).getAbout_me());
            page.putExtra("providerExpr",mData.get(position).getExperience());
            page.putExtra("providerType",mData.get(position).getType());
            page.putExtra("providerEducation",mData.get(position).getEducationLevel());
            page.putExtra("providerAddress",mData.get(position).getAddress());
//            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)activity,(View)holder.cardView,"appCard");
            activity.startActivity(page); //,options.toBundle()

        });
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }

    //search service provider
    public void search(String name){
        name = name.toLowerCase(Locale.getDefault());
        mData.clear();
        if(name.length() == 0){
//            mData.addAll()
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View mview;
        TextView name,se,work;
        ImageView img;
        CardView cardView ;

        public MyViewHolder(View itemView) {
            super(itemView);
            mview = itemView;

            name = mview.findViewById(R.id.name);
            work = mview.findViewById(R.id.area_work);
            img = mview.findViewById(R.id.profile_img);
            cardView =  mview.findViewById(R.id.card_v);
        }

    }

    @Override
    public int getItemViewType(int position) {
//        fuser = FirebaseAuth.getInstance().getCurrentUser();
        String type_service = "organization";
        if(mData.get(position).getType().equals(type_service)){
            return MSG_TYPE_RIGHT;
        } else{
            return MSG_TYPE_LEFT;
        }
    }

}
