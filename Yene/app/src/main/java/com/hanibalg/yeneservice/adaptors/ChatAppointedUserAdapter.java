package com.hanibalg.yeneservice.adaptors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.models.MessageModel;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;


public class ChatAppointedUserAdapter extends RecyclerView.Adapter<ChatAppointedUserAdapter.ViewHolder> {

    boolean isImageFitToScreen;
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    FirebaseUser fuser;
    FirebaseFirestore firebaseFirestore;
    DocumentReference reference;
    FirebaseAuth auth;
    private static final String TAG = "MessageAdapter";
    private Context context;
    private List<MessageModel> mChat;
    private String imageurl;


    public ChatAppointedUserAdapter(Context context, List<MessageModel> mChat, String imageurl){
        this.mChat = mChat;
        this.context = context;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public ChatAppointedUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        if(viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent,false);
            return new ChatAppointedUserAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new ChatAppointedUserAdapter.ViewHolder(view);
        }
//        return null;
    }
    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull final ChatAppointedUserAdapter.ViewHolder holder, final int position) {
        MessageModel chat = mChat.get(position);
        reference  = FirebaseFirestore.getInstance().collection("Messages").document();
        holder.show_message.setText(chat.getMessage());
        //get time
        Date newD = (new Date(chat.getTimestamp().getSeconds()));
        SimpleDateFormat a = new SimpleDateFormat("HH:MM:SS", Locale.getDefault());

        holder.time.setText(a.format(newD));

        holder.messageLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, "clicked long...", Toast.LENGTH_SHORT).show();
//                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
//                        .setTitleText("Are you sure?")
//                        .setContentText("Won't be able to recover this file!")
//                        .setCancelText("No,cancel plx!")
//                        .setConfirmText("Yes,delete it!")
//                        .showCancelButton(true)
//                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sDialog) {
//                                sDialog.dismissWithAnimation();
//                                mChat.get(position).getDocId();
//                                removeItem(position);
////                                Toast.makeText(context, ""+mChat.get(position).getDocId(), Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .show();
                return true;
            }
        });
        Picasso.get().load(imageurl).into(holder.profile_images);
//        if(mChat.get(position).getImage() != null){
//            if(position == mChat.size() + 1){
//            Picasso.get().load(imageurl).into(holder.img);
//            holder.img.setVisibility(View.VISIBLE);
//            }
//        }
//        holder.img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent po = new Intent(context, ImageViewDetailActivity.class);
//                po.putExtra("ima",mChat.get(position).getImage());
//                context.startActivity(po);
//            }
//        });
        if(position == mChat.size() - 1){
            if(chat.isSeen()){
                holder.txtSeen.setText("Seen");
            } else {
                holder.txtSeen.setText("Delivered");
            }
        } else {
            holder.txtSeen.setVisibility(View.GONE);
        }
    }

    private void deleteMessage(final int a) {
        final Timestamp msgTime = mChat.get(a).getTimestamp();
        final String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public void removeItem(final int position){
//        mChat.remove(position);
        final String user_id = auth.getCurrentUser().getUid();

        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                Toast.makeText(context, "id :"+ mChat.get(position).getDocId(), Toast.LENGTH_SHORT).show();

                final String keyRef = mChat.get(position).getDocId();
//                Toast.makeText(context, ""+keyRef, Toast.LENGTH_SHORT).show();
                if(mChat.get(position).getSender().equals(user_id)){
                    firebaseFirestore.collection("Messages").document(keyRef)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context, "message delete successfully:  "+keyRef, Toast.LENGTH_SHORT).show();
                                    mChat.remove(position);
                                    notifyItemRemoved(position);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "can not delete message", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(context, "You can only delete your message", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        notifyItemRemoved(position);
    }

    public void EditMessage(){

    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView show_message;
        ImageView profile_images,img;
        TextView txtSeen, time;
        RelativeLayout messageLayout;

        ViewHolder(View itemView){
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            txtSeen = itemView.findViewById(R.id.txt_seen);
            time = itemView.findViewById(R.id.text_message_time);
            profile_images = itemView.findViewById(R.id.profile_images);
            img = itemView.findViewById(R.id.img);
            messageLayout = itemView.findViewById(R.id.messagelayout);

        }
    }
    public void setData(List<MessageModel> messages) {
        this.mChat = messages;
        notifyDataSetChanged();
        /*if(!messages.isEmpty()) {
            this.messages.addAll(messages);
            notifyItemRangeInserted(this.messages.size() - messages.size(), this.messages.size());
        }*/
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        } else{
            return MSG_TYPE_LEFT;
        }
    }
}
