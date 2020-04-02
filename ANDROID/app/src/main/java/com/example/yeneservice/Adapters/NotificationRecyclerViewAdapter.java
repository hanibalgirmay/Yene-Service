package com.example.yeneservice.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yeneservice.Models.NotificationModel;
import com.example.yeneservice.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationRecyclerViewAdapter.ViewHolder>{
    private List<NotificationModel> notificationList;
    private Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
//    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

    public NotificationRecyclerViewAdapter(Context context, List<NotificationModel> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,final int position) {
       firebaseFirestore = FirebaseFirestore.getInstance();
//        auth.getCurrentUser();
        holder.message.setText(notificationList.get(position).getMessages());
        String from = notificationList.get(position).getFrom();
        // Recieve data
//        Intent intent = getIntent();
//        final String tte = getIntent().getExtras().getString("name").toLowerCase();
        firebaseFirestore.collection("Users").document(from).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String name = documentSnapshot.getString("firstName");
                String image = documentSnapshot.getString("image");

                holder.name.setText(name);
                Picasso.get().load(image).into(holder.circleImageView);
            }
        });
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent s = new Intent(context, ShowNotificationDetailActivity.class);
//                s.putExtra("message", notificationList.get(position).getMessages());
//                s.putExtra("from_user_id", notificationList.get(position).getFrom());
//                context.startActivity(s);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        private View view;
        private CircleImageView circleImageView;
        private TextView name;
        private RelativeLayout relativeLayout;

        private TextView message;
        private ProgressBar progressBar;
        public ViewHolder(View itemView){
            super(itemView);
            view=itemView;
            circleImageView = (CircleImageView)view.findViewById(R.id.listview_image);
            name = (TextView)view.findViewById(R.id.listview_name);
            message = (TextView)view.findViewById(R.id.listview_message);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.clk);
            progressBar = (ProgressBar)view.findViewById(R.id.progress);
        }
    }
}
