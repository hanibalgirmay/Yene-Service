package com.hanibalg.yeneservice.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.adaptors.RecentJobUserAdaptor;
import com.hanibalg.yeneservice.adaptors.ReviewAdapter;
import com.hanibalg.yeneservice.models.AppointmentJobModel;
import com.hanibalg.yeneservice.models.Reviews;
import com.hanibalg.yeneservice.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {
    private ReviewAdapter reviewAdapter;
    private List<UserModel> mUser;
    private List<Reviews> mReview;
    private String id;
    private RatingBar ratingBar;
    private TextView totalDislpay;
    private String TAG = ReviewActivity.class.getName();
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        RecyclerView recyclerView = findViewById(R.id.recent_jobFull);
        totalDislpay = findViewById(R.id.totalReview);
        ratingBar = findViewById(R.id.totalReviewRate);

        recyclerView.setHasFixedSize(true);
        firebaseFirestore = FirebaseFirestore.getInstance();
        Intent op = getIntent();
        if(op !=null ){
            id = op.getStringExtra("providerID");
        }
        mReview = new ArrayList<>();
        mUser = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(this,mReview,mUser);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(reviewAdapter);
        getFeedBacks(id);
    }

    private void getFeedBacks(String providerID) {
        Log.d("ProviderInfomrationId","DATA"+providerID);
//        if(u != null){
        firebaseFirestore
                .collection("Users")
                .document(providerID)
                .collection("rating")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    final float[] total = {0};
                    final float[] count = {0};
                    final float[] average = new float[1];

                    for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                        String i = doc.getDocument().getId();
                        Reviews reviews = doc.getDocument().toObject(Reviews.class);
                        reviews.setId(i);
                        assert reviews != null;
                        total[0] = total[0] + reviews.getRating();
                        count[0] = count[0] + 1;
                        average[0] = total[0] / count[0];
                        ratingBar.setRating(average[0]);
                        String d = average[0]+"";
                        totalDislpay.setText(d);
                        getJobInfo(reviews);

//                            Log.d("UserReviewJob", "Data does not exist");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("UserReviewJob",e.getMessage());
                });

//        }

    }

    private void getJobInfo(Reviews jobId) {
//        Log.d("UserReviewJob",jobId.getJobId());
        if(jobId.getJob_id() != null){
            firebaseFirestore.collection("JobsRequests")
                    .document(jobId.getJob_id())
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            AppointmentJobModel appointmentJobModel = task.getResult().toObject(AppointmentJobModel.class);
                            if (appointmentJobModel != null){
                                getUser(jobId,appointmentJobModel);
                                Log.d("UserReviewJob","provider"+appointmentJobModel.getService_provider_id());
                            }
                        }
                    }).addOnFailureListener(e -> Log.d("UserReviewJob",e.getMessage()));
        }
    }

    private void getUser(Reviews jobId, AppointmentJobModel appointmentJobModel) {
        firebaseFirestore.collection("Users")
                .document(appointmentJobModel.getService_provider_id())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        UserModel userModel = task.getResult().toObject(UserModel.class);
                        mUser.add(userModel);
                        mReview.add(jobId);
                        reviewAdapter.notifyDataSetChanged();
                    }
                });
    }
}
