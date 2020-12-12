package com.hanibalg.yeneservice.pages;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.hanibalg.yeneservice.Helper.CardSwiperHelper;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.activities.CalanderActivity;
import com.hanibalg.yeneservice.adaptors.AppointedAdaptor;
import com.hanibalg.yeneservice.models.AppointmentJobModel;
import com.hanibalg.yeneservice.models.UserModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppointedJobFragment extends Fragment {

    RecyclerView rv;
    AppointedAdaptor appointedAdaptor;
    List<AppointmentJobModel> mData;
    List<UserModel> mUser;
    ImageView sortBtn;
    private RelativeLayout bottom_sheet;
    private BottomSheetBehavior mBottomSheetBehavior;
    private TextView textViewRating;
    private RatingBar ratingBar;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private FloatingActionButton floatingActionButton;
    private LinearLayout noAppointmentLayout,mainRecyclerLayout;
    CardView llBottomSheet;


    public AppointedJobFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        return inflater.inflate(R.layout.fragment_appointed_job, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        noAppointmentLayout = view.findViewById(R.id.card_not_job);
        mainRecyclerLayout = view.findViewById(R.id.main_rv);
        floatingActionButton = view.findViewById(R.id.calenderId);
        floatingActionButton.setOnClickListener(v -> {
            Intent cal = new Intent(getActivity(), CalanderActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(cal);
        });
        //Bottom sheet for rating
        bottom_sheet = view.findViewById(R.id.rating_bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED)
                    floatingActionButton.setVisibility(View.GONE);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                Log.d(TAG, "onSlide: slideOffset" + slideOffset + "");
                floatingActionButton.setVisibility(View.VISIBLE);
                floatingActionButton.setAlpha(slideOffset);
            }
        });



        Toast.makeText(getContext(), "name: "+auth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
//        llBottomSheet = view.findViewById(R.id.bottom_sheet);
//        mBottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
//        initBottomSheet();

        //init
        textViewRating = view.findViewById(R.id.ratingText);
        ratingBar = view.findViewById(R.id.ratingBar);
        setRateTest();
        //Sort change view
        Spinner spinner = view.findViewById(R.id.layout_changer);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.Sort, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    //noting
                }
                else if(position == 1){
                    Toast.makeText(getActivity(), "Ascending" + parent.getSelectedItem(), Toast.LENGTH_SHORT).show();
                    sortByDate(position);
                }
                else if(position == 2){
                    Toast.makeText(getActivity(), "Descending", Toast.LENGTH_SHORT).show();
                    sortByReverseDate(position);
                }
                appointedAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rv = view.findViewById(R.id.recycler_view_job);
        //set data to recycler view
        rv.setHasFixedSize(true);

        mData = new ArrayList<>();
        mUser = new ArrayList<>();

        // Source can be CACHE, SERVER, or DEFAULT.
        Source source = Source.CACHE;
        firebaseFirestore.collection("JobsRequests")
                .whereEqualTo("accepted",true)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if(queryDocumentSnapshots.isEmpty()){
                        noAppointmentLayout.setVisibility(View.VISIBLE);
                        floatingActionButton.setVisibility(View.GONE);
                        mainRecyclerLayout.setVisibility(View.GONE);
                    }
                    for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                        if(doc.getDocument().exists()){
                            String doc_id = doc.getDocument().getId();
                            AppointmentJobModel appointmentJobModel = doc.getDocument().toObject(AppointmentJobModel.class);
                            appointmentJobModel.setDocID(doc_id);
                            if (appointmentJobModel.getJobAppointedUserID().equals(auth.getUid())){
                                noAppointmentLayout.setVisibility(View.GONE);
                                floatingActionButton.setVisibility(View.VISIBLE);
                                mainRecyclerLayout.setVisibility(View.VISIBLE);
                                getProviderDate(appointmentJobModel);
                                Log.d("requests",doc.getDocument().getData().toString());
                                Log.d("requests____",appointmentJobModel.toString());
//                            mData.add(appointmentJobModel);
                            }
                        }else {
                            noAppointmentLayout.setVisibility(View.VISIBLE);
                            floatingActionButton.setVisibility(View.GONE);
                        }
                    }
                });
        appointedAdaptor = new AppointedAdaptor(getContext(), mData,mUser,mBottomSheetBehavior);
        rv.setAdapter(appointedAdaptor);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(mLayoutManager);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(appointedAdaptor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rv.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED));
        }

        //edit and delete (swipe from left)
        CardSwiperHelper cardSwiperHelper = new CardSwiperHelper(getContext(),rv,200) {
            @Override
            public void initButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buffer) {
                buffer.add(new MyButton(getActivity(),
                        "Delete",
                        30,
                        R.drawable.ic_delete_black_24dp,
                        Color.parseColor("#FF3C30"),
                        pos -> deleteJob(viewHolder.getAdapterPosition())));
                buffer.add(new MyButton(getActivity(),
                        "Edit",
                        30,
                        R.drawable.ic_edit_black_24dp,
                        Color.parseColor("#FF9502"),
                        pos -> editRequestJob(viewHolder.getAdapterPosition())));
            }
        };

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

//    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                Rect outRect = new Rect();
                bottom_sheet.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    return true;
                }

            }
        }
        return dispatchTouchEvent(event);
//        return super.dispatchTouchEvent(event);
    }

    private void editRequestJob(int adapterPosition) {
        Toast.makeText(getActivity(), "Edit button is clicked", Toast.LENGTH_SHORT).show();
        initBottomSheet();
//        CardView llBottomSheet =  getActivity().getParent().findViewById(R.id.bottom_sheet);

        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        // set callback for changes
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // this part hides the button immediately and waits bottom sheet
                // to collapse to show
                mBottomSheetBehavior.setPeekHeight(650);
                if (BottomSheetBehavior.STATE_DRAGGING == newState) {
//                    fab.animate().scaleX(0).scaleY(0).setDuration(300).start();
                } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
//                    fab.animate().scaleX(1).scaleY(1).setDuration(300).start();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                fab.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
            }
        });
    }
    private void initBottomSheet() {
        // get the bottom sheet view
        // init the bottom sheet behavior
//        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        // change the state of the bottom sheet
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        // set callback for changes
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // this part hides the button immediately and waits bottom sheet
                // to collapse to show
                if (BottomSheetBehavior.STATE_DRAGGING == newState) {
//                    fab.animate().scaleX(0).scaleY(0).setDuration(300).start();
                } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
//                    fab.animate().scaleX(1).scaleY(1).setDuration(300).start();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                fab.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
            }
        });
//        final FloatingActionButton floatingActionButton;
//        floatingActionButton = findViewById(R.id.fab);
//        floatingActionButton.setOnClickListener(v -> {
//            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//            floatingActionButton.isOrWillBeHidden();
//        });
    }

    private void deleteJob(int adapterPosition) {
        SweetAlertDialog dialog =  new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText("Are you sure?");
        dialog.setContentText("Won't be able to recover this file!");
        dialog.setConfirmText("Yes,delete it!");
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelClickListener(sweetAlertDialog -> sweetAlertDialog.dismissWithAnimation());
        dialog.setConfirmClickListener(sDialog -> sDialog
                .setTitleText("Deleted!")
                .setContentText("Your appointemnt file has been deleted!")
//                .setConfirmText("OK")
                .setConfirmClickListener(sweetAlertDialog -> {
                    appointedAdaptor.removeItem(adapterPosition);
//                    appointedAdaptor.notifyItemRemoved(adapterPosition);
                })
                .setCancelClickListener(sweetAlertDialog -> sweetAlertDialog.dismissWithAnimation())
                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE));
        dialog.show();
    }

    private void sortByReverseDate(int position) {
        Collections.sort(mData,(l1,l2) ->l2.getTimestamp().compareTo(l1.getTimestamp()));
    }

    private void sortByDate(int position) {
        Collections.sort(mData,(l1,l2) ->l1.getTimestamp().compareTo(l2.getTimestamp()));
    }

    private void getProviderDate(AppointmentJobModel appointmentJobModel) {
        Log.d("request___ID",appointmentJobModel.getService_provider_id());
        firebaseFirestore.collection("Users")
                .document(appointmentJobModel.getService_provider_id())
                .get().addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()) {
                        noAppointmentLayout.setVisibility(View.GONE);
                        floatingActionButton.setVisibility(View.VISIBLE);
                        UserModel userModel = documentSnapshot.toObject(UserModel.class);
                        mData.add(appointmentJobModel);
                        mUser.add(userModel);
                        Log.d("request",documentSnapshot.getData().toString());
                        appointedAdaptor.notifyDataSetChanged();
                    }
                });
    }

    private void setRateTest() {
        float r = ratingBar.getRating();
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                switch ((int) rating){
                    case 1:
                        textViewRating.setText("Poor work");
                        break;
                    case 2:
                        textViewRating.setText("not much satisfied");
                        break;
                    case 3:
                        textViewRating.setText("good job");
                        break;
                    case 4:
                        textViewRating.setText("Nice Job");
                        break;
                    case 5:
                        textViewRating.setText("Excelent work");
                        break;
                    default:
                        textViewRating.setText("rate provider work");
                        break;
                }
            }
        });

    }

}