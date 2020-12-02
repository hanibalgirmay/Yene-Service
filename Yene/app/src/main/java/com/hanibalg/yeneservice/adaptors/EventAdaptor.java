package com.hanibalg.yeneservice.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.Timestamp;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.models.AppointmentJobModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventAdaptor extends RecyclerView.Adapter<EventAdaptor.myEvent> {
    private Context context;
    private List<AppointmentJobModel> mDate;

    public EventAdaptor(Context context,List<AppointmentJobModel>mDate){
        this.context = context;
        this.mDate = mDate;
    }
    @NonNull
    @Override
    public myEvent onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_calender_card,parent,false);
        return new myEvent(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myEvent holder, int position) {
        holder.time.setText(mDate.get(position).getTime());
        holder.problem_desc.setText(mDate.get(position).getProblem_description());
        holder.todo.setText("Work morning");
        String r = new Date(mDate.get(position).getDate().getSeconds()*1000).toLocaleString().substring(0,6);
        holder.jobTime.setText(r);
        //check when was job for
        Date y = new Date(mDate.get(position).getDate().getSeconds()*1000);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-DD", Locale.getDefault());
        String formattedDate = df.format(c);
        String formt = df.format(y);
        if(formattedDate.compareTo(formt)>0){
            holder.materialCardView.setClickable(true);
            holder.materialCardView.setStrokeWidth(3);
            holder.materialCardView.setCardElevation(5);
        }else{
            holder.materialCardView.setStrokeWidth(0);
            holder.materialCardView.setCardElevation(0);
            holder.materialCardView.setClickable(false);
        }
    }

    @Override
    public int getItemCount() {
        return mDate.size();
    }

    public class myEvent extends RecyclerView.ViewHolder{
        TextView time,problem_desc,todo,jobTime;
        MaterialCardView materialCardView;

        public myEvent(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.event_time);
            problem_desc = itemView.findViewById(R.id.event_desc);
            todo = itemView.findViewById(R.id.event_note);
            jobTime = itemView.findViewById(R.id.event_job_time);
            materialCardView = itemView.findViewById(R.id.layout_event);
        }
    }
}
