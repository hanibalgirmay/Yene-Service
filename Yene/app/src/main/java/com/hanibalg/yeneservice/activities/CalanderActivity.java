package com.hanibalg.yeneservice.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.applandeo.materialcalendarview.model.EventDay;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hanibalg.yeneservice.MyEventDay;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.adaptors.EventAdaptor;
import com.hanibalg.yeneservice.models.AppointmentJobModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CalanderActivity extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private MyEventDay myEventDay;
    private List<AppointmentJobModel> mData;
    private EventAdaptor eventAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calander);

        CalendarView calendarView = findViewById(R.id.calander_view);
        RecyclerView recyclerView = findViewById(R.id.event_recyclerView); //recyclerview for event
        //init
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        List<EventDay> events = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        events.add(new MyEventDay(calendar, R.drawable.icons_new_job,"test",Color.parseColor("#228B22")));
        calendarView.setEvents(events);

        recyclerView.setHasFixedSize(true);
        mData = new ArrayList<>();
        firebaseFirestore.collection("JobsRequests")
                .whereEqualTo("isAccepted",true)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                        String doc_id = doc.getDocument().getId();
                        AppointmentJobModel appointmentJobModel = doc.getDocument().toObject(AppointmentJobModel.class);
                        appointmentJobModel.setDocID(doc_id);
                        if (appointmentJobModel.getJobAppointedUserID().equals(auth.getUid())){
                            Log.d("requests",doc.getDocument().getData().toString());
                            String r = new Date(appointmentJobModel.getDate().getSeconds()*1000).toLocaleString().substring(0,6);
                            Calendar ca = Calendar.getInstance();
                            Date ty = new Date(appointmentJobModel.getDate().getSeconds()*1000);
                            ca.setTime(ty);
                            Log.d("requestsDate","_"+r);
                            events.add(new MyEventDay(ca, R.drawable.working_time, "I am Event",Color.CYAN));
                            calendarView.setEvents(events);
//                            events.add(new EventDay(calendar, R.drawable.icons_planner, Color.parseColor("#228B22")));
//                            String ew = String.valueOf(appointmentJobModel.getDate().toDate());
                            mData.add(appointmentJobModel);
                            eventAdaptor.notifyDataSetChanged();
                        }
                    }
                });
        eventAdaptor = new EventAdaptor(this,mData);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(eventAdaptor);
        calendarView.setEvents(events);
        calendarView.setOnDayClickListener(eventDay -> {
            Calendar clickedDayCalendar = eventDay.getCalendar();
            Log.d("EventClicked",clickedDayCalendar+"");
        });
    }
}
