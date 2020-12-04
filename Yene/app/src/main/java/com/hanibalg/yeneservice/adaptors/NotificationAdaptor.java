package com.hanibalg.yeneservice.adaptors;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.activities.ViewNotificationDetailActivity;
import com.hanibalg.yeneservice.models.AppointmentJobModel;
import com.hanibalg.yeneservice.models.NotificationModel;
import com.hanibalg.yeneservice.models.UserModel;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class NotificationAdaptor extends RecyclerView.Adapter<NotificationAdaptor.MyView> {
    private Context context;
    private List<NotificationModel>models;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference reference;
    private String TAG = NotificationAdaptor.class.getName();
    

    public NotificationAdaptor(Context context,List<NotificationModel>models){
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public NotificationAdaptor.MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.card_notification,parent,false);
        //init firebase
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        reference = FirebaseFirestore.getInstance().collection("Users").document(auth.getUid());
        return new MyView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdaptor.MyView holder, int position) {
        String user = models.get(position).getFrom().trim();
        firebaseFirestore.collection("Users").document(user).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                UserModel i = task.getResult().toObject(UserModel.class);
                Log.d("Notification_fragment", user);
                String img = task.getResult().getString("image");
                String fname = task.getResult().getString("firstName");
//                if(userModel != null){
                holder.name.setText(fname);
                Picasso.get().load(img).placeholder(R.drawable.placeholder_profile).into(holder.profile);
//                }
            }
        });
//        holder.time.setText(models.get(position).getTimestamp().);
        holder.msg.setText(models.get(position).getNotificationDescription());
        holder.layout.setOnClickListener(v -> {
            Intent detail = new Intent(context, ViewNotificationDetailActivity.class);
            detail.putExtra("userID", models.get(position).getFrom().trim());
            detail.putExtra("dataMsg", models.get(position).getNotificationDescription());
            detail.putExtra("jobID", models.get(position).getJobID());
//            detail.putExtra("dataType", models.get(position).getNotificationType());
//            detail.putExtra("dataSeen", models.get(position).isSeen());
            context.startActivity(detail);
        });
        String documentID = models.get(position).getDocId();
//        holder.accept.setOnClickListener(v -> {
//             updateNotification(documentID,true);
//            Toast.makeText(context, "accept clicked", Toast.LENGTH_SHORT).show();
//        });
//        holder.decline.setOnClickListener(v -> {
//           updateNotification(documentID,false);
//            Toast.makeText(context, "decline clicked", Toast.LENGTH_SHORT).show();
//        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    private void updateNotification(String id, boolean status){
        firebaseFirestore.collection("Users").document(auth.getUid()).collection("Notifications")
                .document(id).update("seen",status);
    }
    class MyView extends RecyclerView.ViewHolder {
        private TextView name,time,msg;
        private ImageView profile;
        private CardView layout;
        private Button accept,decline;

        MyView(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.not_name);
            time = itemView.findViewWithTag(R.id.not_time);
            msg = itemView.findViewById(R.id.not_msg);
            profile = itemView.findViewById(R.id.not_pro);
            layout = itemView.findViewById(R.id.card_layout);
//            accept = itemView.findViewById(R.id.btn_accept);
//            decline = itemView.findViewById(R.id.btn_decline);
        }
    }
}