package com.hanibalg.yeneservice.adaptors;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.models.ProviderModel;
import com.hanibalg.yeneservice.models.UserModel;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class OnMapProviderListsAdaptor extends RecyclerView.Adapter<OnMapProviderListsAdaptor.MyViewHolder> {
    private static final String TAG = OnMapProviderListsAdaptor.class.getName();
    private Context mContext ;
    private List<ProviderModel> mData ;
    private List<UserModel> mUser ;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;

    public OnMapProviderListsAdaptor(Context mContext, List<ProviderModel> mData,List<UserModel> mUser) {
        this.mContext = mContext;
        this.mData = mData;
        this.mUser = mUser;
    }

    @NonNull
    @Override
    public OnMapProviderListsAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        view = mInflater.inflate(R.layout.card_map_provider,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnMapProviderListsAdaptor.MyViewHolder holder, int position) {
        holder.name.setText(mUser.get(position).getFirstName());
//        holder.workingArea.setText(mData.get(position).getSpeciality());
        List<String> workingArea = mData.get(position).getSpeciality();
        holder.workingArea.setText(workingArea.get(0));
        Picasso.get().load(mUser.get(position).getImage()).placeholder(R.drawable.background).into(holder.profile);
        //button
        holder.call.setOnClickListener(v -> {
            String p = mUser.get(position).getPhone();
            if(!p.isEmpty()){
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+p));
                mContext.startActivity(callIntent);
            }else {
                Toast.makeText(mContext, "No phone number", Toast.LENGTH_SHORT).show();
            }

        });
        holder.fav.setOnFavoriteChangeListener((buttonView, favorite) -> {
            if(buttonView.isFavorite()){
                Toast.makeText(mContext, "Provider is Bookmarked", Toast.LENGTH_SHORT).show();
                addToFav(mData.get(position).getUser_id());
            }else{
                Toast.makeText(mContext, "failed", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "appoint btn clicked", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                // Get the layout inflater
                LayoutInflater inflater =(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                builder.setTitle("Appoint Request");
                builder.setCancelable(true);
                View dialog = inflater.inflate(R.layout.pop_up_card_appoint,null);
                builder.setView(dialog)
                        // Add action buttons
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                EditText msg = dialog.findViewById(R.id.pop_desc);
                Button date = dialog.findViewById(R.id.pop_date);
                Button time = dialog.findViewById(R.id.pop_time);
                Button save = dialog.findViewById(R.id.pop_appoint);

                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleDateButton();
                    }
                });
                time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleTimeButton();
                    }
                });
                save.setOnClickListener(this);

                builder.create();
                builder.show();
            }
        });
    }

    private void addToFav(String userID) {
        if(!userID.isEmpty()){
            Map<String,Object> bData = new HashMap<>();
            bData.put("documentID",userID);
            bData.put("timestamp",Timestamp.now());
            DocumentReference reference;
            reference = FirebaseFirestore.getInstance().collection("Users").document(auth.getUid()).collection("Favorite").document();
            firebaseFirestore.collection("Users")
                    .document(auth.getUid())
                    .collection("Favorite")
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isComplete()){
                            for (DocumentChange doc: task.getResult().getDocumentChanges()){
                                String f = doc.getDocument().getId();
                                if(f.equals(userID)){
                                    Toast.makeText(mContext, "already bookmarked", Toast.LENGTH_SHORT).show();
                                } else{
                                    reference.set(bData);
                                    Toast.makeText(mContext, "Bookmarked successfull", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }).addOnFailureListener(e -> Log.d(TAG,e.getMessage()));
//            firebaseFirestore.collection("Users")
//                    .document(auth.getUid())
//                    .collection("Favorite")
//                    .add(bData)
//                    .addOnCompleteListener(task -> {
//                        if(task.isSuccessful()){
//                            Toast.makeText(mContext, "Bookmarked successfull", Toast.LENGTH_SHORT).show();
//                        }
//                    }).addOnFailureListener(e -> {
//                        Toast.makeText(mContext, "Error happeded", Toast.LENGTH_SHORT).show();
//                        Log.d(TAG,e.getMessage());
//                    });

        }
    }

    private void handleDateButton() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);
        long today = MaterialDatePicker.todayInUtcMilliseconds();

        //material
        calendar.set(Calendar.MONTH,Calendar.JANUARY);
        long JAN = calendar.getTimeInMillis();
        calendar.set(Calendar.MONTH,Calendar.DECEMBER);
        long DES = calendar.getTimeInMillis();

        //calander constrain
        CalendarConstraints.Builder conBuilder = new CalendarConstraints.Builder();
        conBuilder.setStart(JAN);
        conBuilder.setEnd(DES);

        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, (datePicker, year, month, date) -> {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(Calendar.YEAR, year);
            calendar1.set(Calendar.MONTH, month);
            calendar1.set(Calendar.DATE, date);
//            String dateText = DateFormat.format("EEEE, MMM d, yyyy", calendar1).toString();

//            da.setText(dateText);
        }, YEAR, MONTH, DATE);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void handleTimeButton() {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        boolean is24HourFormat = DateFormat.is24HourFormat(mContext);

        TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Log.i("time", "onTimeSet: " + hour + minute);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR, hour);
                calendar1.set(Calendar.MINUTE, minute);
//                String dateText = DateFormat.format("h:mm a", calendar1).toString();
//                t.setText(dateText);
            }
        }, HOUR, MINUTE, is24HourFormat);

        timePickerDialog.show();

    }

//    private void appoint(String msg) {
//        final String dateAppoint = da.getText().toString().trim();
//        final String timeAppoint = t.getText().toString().trim();
//        if(!TextUtils.isEmpty(msg) ){
//            Intent intent = mContext.getIntent();
//            UserModel u = (UserModel) intent.getSerializableExtra("userData");
//            String nm = intent.getExtras().getString("firstName");
//
//            Map<String, Object> appointMap = new HashMap<>();
//            appointMap.put("jobAppointedUserID", auth.getUid());
//            appointMap.put("service_provider_id", u.getUserID());
//            appointMap.put("problem_description", msg);
//            appointMap.put("date", dateAppoint);
//            appointMap.put("time", timeAppoint);
//            appointMap.put("timestamp", Timestamp.now());
//            appointMap.put("isAccepted", false);
//            appointMap.put("priority", "normal");
//
//            final DocumentReference ref = firebaseFirestore.collection("JobsAppointments").document();
//            firebaseFirestore.collection("JobsRequests").document().set(appointMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if(task.isSuccessful()){
//                        String myId = ref.getId();
//                        Toast.makeText(mContext, "Appointment successful.", Toast.LENGTH_LONG).show();
////                        finish();
//                    } else {
//                        String error = task.getException().getMessage();
//                        Toast.makeText(mContext, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();
//                    }
////                    progressBar.setVisibility(View.INVISIBLE);
//                }
//            });
//
//        }
//    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView name,workingArea,totalReview;
        ImageView profile,msg,call;
        MaterialFavoriteButton fav;
        RatingBar ratingBar;
        Button btn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_provider);
            profile = itemView.findViewById(R.id.provider_img);
            name = itemView.findViewById(R.id.provider_name);
            workingArea = itemView.findViewById(R.id.provider_specialize);
            totalReview = itemView.findViewById(R.id.totalReview);
            ratingBar = itemView.findViewById(R.id.providerRating);
            fav = itemView.findViewById(R.id.bookmark);
            btn = itemView.findViewById(R.id.appt);
            msg = itemView.findViewById(R.id.msgProvider);
            call = itemView.findViewById(R.id.callProvider);
        }
    }
}
