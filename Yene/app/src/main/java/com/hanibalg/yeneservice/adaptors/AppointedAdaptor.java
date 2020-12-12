package com.hanibalg.yeneservice.adaptors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.activities.PaymentActivity;
import com.hanibalg.yeneservice.models.AppointmentJobModel;
import com.hanibalg.yeneservice.models.UserModel;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointedAdaptor extends RecyclerView.Adapter<AppointedAdaptor.JobViewHolder> {
    private Context mContext ;
    private List<AppointmentJobModel> mData;
    private List<UserModel> mUser;
    private BottomSheetBehavior mBottomSheetBehavior;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private DocumentReference reference, reviewReference;

    public AppointedAdaptor(Context context, List<AppointmentJobModel> mData,List<UserModel> mUser,BottomSheetBehavior mBottomSheetBehavior){
        this.mContext = context;
        this.mData = mData;
        this.mUser = mUser;
        this.mBottomSheetBehavior = mBottomSheetBehavior;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.appointed_job,parent,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointedAdaptor.JobViewHolder holder, int position) {
        String re = mData.get(position).getDocID();
        reference = firebaseFirestore.collection("JobsRequests").document(re);
        String full = mUser.get(position).getFirstName() + " " + mUser.get(position).getLastName();
        holder.name.setText(full);
        holder.desc.setText(mData.get(position).getProblem_description());
        try{
          holder.appointdate.setText(mData.get(position).getDate().toDate().toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        Picasso.get().load(mUser.get(position).getImage()).placeholder(R.drawable.background).into(holder.img);
        //
        //================layout================
        boolean isA = mData.get(position).JobAccepted();
        if(isA){
            holder.jobAcceptLayout.setVisibility(View.GONE);
            holder.jobRequestLayout.setVisibility(View.VISIBLE);
        }
        if(!isA){
            holder.jobAcceptLayout.setVisibility(View.VISIBLE);
            holder.jobRequestLayout.setVisibility(View.VISIBLE);
        }
        //======================================
//        holder.review.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//            }
//        });
        //get time and adjust with local time
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

//        SimpleDateFormat df = new SimpleDateFormat("MMM--dd-yyyy", Locale.getDefault());
//        Date format = new Date(String.valueOf(df));
//        String formattedDate = df.format(c);
//        Date u = mData.get(position).getDate().toDate();
//        if(format.getDate() > u.getDate()){
//            String po = String.valueOf(format.getSeconds() - u.getSeconds());
//            holder.countDay.setText(po);
//        }
        holder.acceptBtn.setOnClickListener(v -> reference.update("accepted",true).addOnSuccessListener(aVoid -> Toast.makeText(mContext, "Job accepted", Toast.LENGTH_SHORT).show()));
        holder.declineBtn.setOnClickListener(v ->{
            reference.update("accepted",false).addOnSuccessListener(aVoid -> Toast.makeText(mContext, "job Decline!", Toast.LENGTH_SHORT).show());
        });
        holder.pay.setOnClickListener(v -> {
            Intent pay = new Intent(mContext, PaymentActivity.class);
            mContext.startActivity(pay);
        });
        holder.review.setOnClickListener(v -> {
//            Intent rev = new Intent(mContext, ReviewActivity.class);
//            mContext.startActivity(rev);
//            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            builder.setCancelable(true);
//            builder.setIcon(R.drawable.ic_edit_black_24dp);
//            View dialogView= inflater.inflate(R.layout.rating_layout, null);
//            builder.setView(dialogView)
//                    .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int id) {
//
//                        }
//                    });
//
//            ImageView pro = dialogView.findViewById(R.id.p);
//            TextView nm = dialogView.findViewById(R.id.p_name);
//            TextView ser = dialogView.findViewById(R.id.p_service);
//            RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
//            EditText msg = dialogView.findViewById(R.id.text_comment);
//            Button btn = dialogView.findViewById(R.id.btnReview);
//
//            String ful = mUser.get(position).getFirstName() + " " + mUser.get(position).getLastName();
//            Picasso.get().load(mUser.get(position).getImage()).placeholder(R.drawable.placeholder_profile).into(pro);
//            nm.setText(ful);
//            ser.setText(mData.get(position).getTime());
//
//            //validate
//            btn.setOnClickListener(v1 -> {
//                String inputMsg = msg.getText().toString().trim();
//                double inputrate = ratingBar.getRating();
//                if(TextUtils.isEmpty(inputMsg)){
//                    msg.setError("review required!");
//                    return;
//                }
//                String p_id = mData.get(position).getService_provider_id();
//                String d_id = mData.get(position).getDocID();
//                saveReview(p_id,d_id,inputMsg,inputrate);
//            });
//            builder.create();
//            builder.show();
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);


        });
    }
    private void saveReview(String p_id, String d_id, String inputMsg, double inputrate) {
        Map<String, Object> r = new HashMap<>();
        r.put("job_id",d_id);
        r.put("rating", inputrate);
        r.put("comments", inputMsg);
        r.put("timestamp", Timestamp.now());
        reviewReference = firebaseFirestore.collection("Users").document(p_id).collection("rating").document();

        firebaseFirestore.collection("Users").document(p_id)
                .collection("rating")
                .add(r)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(mContext, "Thanks for review", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    public void removeItem(final int postion){
        try{
            reference.addSnapshotListener((documentSnapshot, e) -> {
                Toast.makeText(mContext, "data:"+ mData.get(postion).getDate()+ mData.get(postion).getTime(), Toast.LENGTH_SHORT).show();
                final String keyRef = mData.get(postion).getDocID();
                firebaseFirestore.collection("JobsRequests").document(keyRef)
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(mContext, "Appointment delete successfully:  "+keyRef, Toast.LENGTH_SHORT).show();
                            mData.remove(postion);
                            notifyItemRemoved(postion);
                        }).addOnFailureListener(e1 -> Toast.makeText(mContext, "can not delete Appointment", Toast.LENGTH_SHORT).show());
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void EditItem(final int postion){
        Map<String,Object> updateItem = new HashMap<>();
//        updateItem.put("date")
//        reference.addSnapshotListener((documentSnapshot, e) -> {
//            Toast.makeText(mContext, "data:"+ mData.get(postion).getDate()+ mData.get(postion).getTime(), Toast.LENGTH_SHORT).show();
//            final String keyRef = mData.get(postion).getDocID();
//            firebaseFirestore.collection("JobsRequests").document(keyRef)
//                    .update(updateItem)
//                    .addOnSuccessListener(aVoid -> {
//                        Toast.makeText(mContext, "Appointment delete successfully:  "+keyRef, Toast.LENGTH_SHORT).show();
//                        mData.remove(postion);
//                        notifyItemRemoved(postion);
//                    }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(mContext, "can not delete Appointment", Toast.LENGTH_SHORT).show();
//                }
//            });
//        });
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name,desc, appointdate,workingArea,countDay;
        ImageView img;
        Button pay , review;
        private Button acceptBtn,declineBtn;
        private LinearLayout jobAcceptLayout;
        private LinearLayout jobRequestLayout;

        private BottomSheetBehavior sheetBehavior;
        private LinearLayout bottom_sheet;

        public JobViewHolder(View itemView) {
            super(itemView);
            //=====================button layouts
            jobAcceptLayout = itemView.findViewById(R.id.accepted_layout);
            jobRequestLayout = itemView.findViewById(R.id.job_request_layout);
            //======================
            name = itemView.findViewById(R.id.provider_name);
            img = itemView.findViewById(R.id.provider_img);
            appointdate = itemView.findViewById(R.id.appoint_date);
            desc = itemView.findViewById(R.id.problem_desc);
            workingArea = itemView.findViewById(R.id.provider_working_area);
//            countDay = itemView.findViewById(R.id.count_time);
            pay =  itemView.findViewById(R.id.btn_pay);
            review =  itemView.findViewById(R.id.btn_review);
            acceptBtn = itemView.findViewById(R.id.btn_accept);
            declineBtn = itemView.findViewById(R.id.btn_decline);

        }

        @Override
        public void onClick(View v) {
            //Bottom sheet for rating
            Toast.makeText(v.getContext(), "click review", Toast.LENGTH_SHORT).show();

        }

        private void openBottom() {
            // callback for do something
            sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View view, int newState) {
                    switch (newState) {
                        case BottomSheetBehavior.STATE_HIDDEN:
                            break;
                        case BottomSheetBehavior.STATE_EXPANDED: {
//                            btn_bottom_sheet.setText("Close Sheet");
                        }
                        break;
                        case BottomSheetBehavior.STATE_COLLAPSED: {
//                            btn_bottom_sheet.setText("Expand Sheet");
                        }
                        break;
                        case BottomSheetBehavior.STATE_DRAGGING:
                            break;
                        case BottomSheetBehavior.STATE_SETTLING:
                            break;
                    }
                }

                @Override
                public void onSlide(@NonNull View view, float v) {

                }
            });
        }
    }
}

