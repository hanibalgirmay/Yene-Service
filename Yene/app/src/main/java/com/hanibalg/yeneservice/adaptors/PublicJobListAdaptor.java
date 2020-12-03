package com.hanibalg.yeneservice.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.models.JobListPublic;
import com.hanibalg.yeneservice.models.UserModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PublicJobListAdaptor extends RecyclerView.Adapter<PublicJobListAdaptor.JobView> {
    private List<JobListPublic> modelJobs;
    private List<UserModel> userModels;
    private Context context;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;

    public PublicJobListAdaptor(List<JobListPublic> modelJobs,List<UserModel> userModels, Context context){
        this.context = context;
        this.modelJobs = modelJobs;
        this.userModels = userModels;
    }

    @NonNull
    @Override
    public PublicJobListAdaptor.JobView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.card_job_view,parent,false);
        //init
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        return new PublicJobListAdaptor.JobView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PublicJobListAdaptor.JobView holder, int position) {

        String id = modelJobs.get(position).getUserID();

        holder.sName.setText(modelJobs.get(position).getService_type());
        holder.problem.setText(modelJobs.get(position).getProblem_desc());
        holder.jTime.setText(modelJobs.get(position).getJobDate());
        holder.service.setText(modelJobs.get(position).getServiceName());
//        if(userModels.get(position).getFirstName() != null && userModels.get(position).getLastName() != null){
//            String fn = userModels.get(position).getFirstName();
//            String ln = userModels.get(position).getLastName();
//            String fullName = fn + " " + ln;
//            holder.uName.setText(fullName);
//        }
        holder.uName.setText(userModels.get(position).getFirstName()+" "+userModels.get(position).getLastName());
        Picasso.get().load(userModels.get(position).getImage()).placeholder(R.drawable.placeholder_profile).into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return modelJobs.size();
    }

    public static class JobView extends RecyclerView.ViewHolder {
        TextView sName,jTime,problem,uName,timestamp,service;
         ImageView avatar;

        public JobView(@NonNull View itemView) {
            super(itemView);
            sName = itemView.findViewById(R.id.job_service_name);
            service = itemView.findViewById(R.id.job_service);
            jTime = itemView.findViewById(R.id.job_time);
            problem = itemView.findViewById(R.id.job_pro);
            avatar = itemView.findViewById(R.id.job_poster_img);
            uName = itemView.findViewById(R.id.job_poster_name);
            timestamp = itemView.findViewById(R.id.job_timestamp);
        }
    }
}
