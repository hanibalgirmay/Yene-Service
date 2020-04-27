package com.example.yeneservice.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yeneservice.Models.AppointementModel;
import com.example.yeneservice.PaymentActivity;
import com.example.yeneservice.R;
import com.example.yeneservice.ReviewActivity;
import com.example.yeneservice.ServiceProviderProfileActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.annotation.Nullable;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.MyViewHolder> {
    private Context mContext ;
    private List<AppointementModel> mData ;
    private PopupWindow mPopupWindow;
    private RelativeLayout mRelativeLayout;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    DocumentReference reference;
    private static final String TAG = "ServiceProvider";
    Snackbar snackbar;

    public AppointmentAdapter(Context mContext, List<AppointementModel> mData){
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_appointment,parent,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final AppointmentAdapter.MyViewHolder holder, final int position) {
        reference = firebaseFirestore.collection("Appointments").document();
        holder.name.setText(mData.get(position).getName());
        holder.appointdate.setText(mData.get(position).getDate());
        holder.desc.setText(mData.get(position).getDesc());
        Picasso.get().load(mData.get(position).getImage()).into(holder.img);
//        holder.img.setImageResource(mData.get(position).getImg());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PaymentActivity.class);
                // passing data to the book activity
//                intent.putExtra("my_id",mData.get(position).getUserId());
                intent.putExtra("userID",mData.get(position).getServiceProviderId());
                mContext.startActivity(intent);

            }
        });
        holder.review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                re.putExtra("userID",mData.get(position).getServiceProviderId())
                final Intent re = new Intent(mContext, ReviewActivity.class);
//                String id = documentSnapshot.getId();
                re.putExtra("documentID",mData.get(position).getDocId());
                re.putExtra("desc",mData.get(position).getDesc());
                re.putExtra("provider_id",mData.get(position).getServiceProviderId());
                mContext.startActivity(re);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void removeItem(final int postion){
        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Toast.makeText(mContext, "data:"+ mData.get(postion).getDate()+ mData.get(postion).getTime(), Toast.LENGTH_SHORT).show();
//                documentSnapshot.getDocumentReference();
                final String keyRef = mData.get(postion).getDocId();
                firebaseFirestore.collection("Appointments").document(keyRef)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(mContext, "Appointment delete successfully:  "+keyRef, Toast.LENGTH_SHORT).show();
                                mData.remove(postion);
                                notifyItemRemoved(postion);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mContext, "can not delete Appointment", Toast.LENGTH_SHORT).show();
                    }
                });

                Toast.makeText(mContext, "doc id"+ mData.get(postion).getDocId(), Toast.LENGTH_SHORT).show();

            }
        });
//        mData.remove(postion);
//        notifyItemRemoved(postion);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, pay,desc, appointdate;
        ImageView img;
        Button cardView , review;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
//            pay = itemView.findViewById(R.id.pay);
            img = itemView.findViewById(R.id.profile);
            appointdate = itemView.findViewById(R.id.dd);
            desc = itemView.findViewById(R.id.descr);
            cardView =  itemView.findViewById(R.id.book);
            review =  itemView.findViewById(R.id.review);
        }

    }
}
