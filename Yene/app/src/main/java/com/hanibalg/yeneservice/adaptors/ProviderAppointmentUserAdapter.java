package com.hanibalg.yeneservice.adaptors;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.activities.MessageActivity;
import com.hanibalg.yeneservice.models.MessageModel;
import com.hanibalg.yeneservice.models.ProviderModel;
import com.hanibalg.yeneservice.models.UserModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.annotation.Nullable;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ProviderAppointmentUserAdapter extends RecyclerView.Adapter<ProviderAppointmentUserAdapter.MyViewHolder>{
    private Context mContext ;
    private List<ProviderModel> mData ;
    private List<UserModel> mUser ;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private boolean isChat;
    String theLastMessage;

    public ProviderAppointmentUserAdapter(Context mContext, List<ProviderModel> mData,List<UserModel> mUser, boolean isChat){
        this.mContext = mContext;
        this.mData = mData;
        this.mUser = mUser;
        this.isChat = isChat;
    }
    @NonNull
    @Override
    public ProviderAppointmentUserAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_appointed_user,parent,false);
        // firebaseFirestore = FirebaseFirestore.getInstance();
        return new ProviderAppointmentUserAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProviderAppointmentUserAdapter.MyViewHolder holder, final int position) {
        final String id = mData.get(position).getUser_id();
        holder.name.setText(mUser.get(position).getFirstName());
        holder.work.setText(mData.get(position).getPhone_number());
        Picasso.get().load(mUser.get(position).getImage()).placeholder(R.drawable.placeholder_profile).into(holder.img);
        if(!isChat){
            lastMessage(id, holder.lastMsg);
        } else {
            holder.lastMsg.setVisibility(View.GONE);
        }
        if(isChat){
            if(mUser.get(position).getStatus().equals("online")){
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            } else {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        } else {
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("providerID",mData.get(position).getUser_id());
                Toast.makeText(mContext, "providerID: "+ mData.get(position).getUser_id(), Toast.LENGTH_SHORT).show();
                // start the activity
                mContext.startActivity(intent);

            }
        });
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        View mview;
        TextView name,se,work;
        ImageView img, img_on, img_off;
        CardView cardView ;
        private TextView lastMsg;

        public MyViewHolder(View itemView) {
            super(itemView);
            mview = itemView;

            name = mview.findViewById(R.id.name);
            work = mview.findViewById(R.id.area_work);
            img = mview.findViewById(R.id.profile_img);
            lastMsg = mview.findViewById(R.id.last_msg);
            img_on = mview.findViewById(R.id.img_on);
            img_off = mview.findViewById(R.id.img_off);
            cardView =  mview.findViewById(R.id.card_v);
        }

    }
    //check last message
    private void lastMessage(final String Userid, final TextView lastMsg){
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        DocumentReference reference = FirebaseFirestore.getInstance().;
        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("Messages").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Log.d(TAG,"Error: "+ e.getMessage());
                }
                for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                    MessageModel chatModel = doc.getDocument().toObject(MessageModel.class);
                    if(chatModel.getReceiver().equals(firebaseUser.getUid()) && chatModel.getSender().equals(Userid) || chatModel.getReceiver().equals(Userid) && chatModel.getSender().equals(firebaseUser.getUid())){
                        theLastMessage = chatModel.getMessage();
                    }
                }
                switch (theLastMessage){
                    case "default":
                        lastMsg.setText("No Message");
                        break;
                    default:
                       lastMsg.setText(theLastMessage);
                       break;
                }
                theLastMessage = "default";
            }
        });
    }
}
