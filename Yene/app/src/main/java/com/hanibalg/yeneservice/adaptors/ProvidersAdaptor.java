package com.hanibalg.yeneservice.adaptors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.activities.ProviderPageActivity;
import com.hanibalg.yeneservice.activities.SocialActivity;
import com.hanibalg.yeneservice.models.ProviderModel;
import com.hanibalg.yeneservice.models.UserModel;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProvidersAdaptor extends RecyclerView.Adapter<ProvidersAdaptor.MyView> implements Filterable {
    private Context context;
    private List<ProviderModel> providerList;
    private List<UserModel> mUser;
    private List<ProviderModel> searcFilter;
    private static final int SERVICE_INDIVIDUAL = 0;
    private static final int SERVICE_ORGANIZATION = 1;

    public ProvidersAdaptor(Context context, List<ProviderModel> providerList,List<UserModel> mUser){
        this.context = context;
        this.providerList = providerList;
        this.mUser = mUser;
    }

    @NonNull
    @Override
    public ProvidersAdaptor.MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if(viewType == SERVICE_INDIVIDUAL){
            view = layoutInflater.inflate(R.layout.card_provider_individual,parent,false);
            return new ProvidersAdaptor.MyView(view);
        } else {
            view = layoutInflater.inflate(R.layout.card_provider_organization,parent,false);
            return new ProvidersAdaptor.MyView(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, final int position) {
        UserModel u = mUser.get(position);
        ProviderModel p = providerList.get(position);

        String fName = mUser.get(position).getFirstName();
        String lName = mUser.get(position).getLastName();
        String fullName = fName + " " +lName;

        holder.name.setText(fullName);
//        holder.work.setText(providerList.get(position).());
        Picasso
                .get()
                .load(mUser.get(position).getImage())
                .placeholder(R.drawable.placeholder_icon)
                .into(holder.img);
        final List<String> workingArea = providerList.get(position).getSpeciality();
        holder.cardView.setOnClickListener(v -> {
            //TOBE CHANGED
            Intent intent = new Intent(context, ProviderPageActivity.class);
            intent.putExtra("userData", (Serializable) u);
            intent.putExtra("providerDataInfo", p.getUser_id());
            intent.putExtra("providerDataSpe", String.valueOf(workingArea));
//            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(Activity,(View)holder.cardView,"appCard");
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    providerList = searcFilter;
                } else {
                    List<ProviderModel> filteredList = new ArrayList<>();
                    for (ProviderModel row : searcFilter) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCity().toLowerCase().contains(charString.toLowerCase()) || row.getGender().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }
                    searcFilter = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = searcFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                searcFilter = (ArrayList<ProviderModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class MyView extends RecyclerView.ViewHolder {
        View mView;
        TextView name,se,work;
        ImageView img;
        CardView cardView ;

        public MyView(View itemView){
            super(itemView);
            mView = itemView;

            name = mView.findViewById(R.id.name);
            work = mView.findViewById(R.id.area_work);
            img = mView.findViewById(R.id.profile_img);
            cardView =  mView.findViewById(R.id.card_v);
        }
    }
}
