package com.example.yeneservice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SearchProviderActivity extends AppCompatActivity {
    private BottomSheetBehavior bottomSheetBehavior;
    Button filterBtn;
    SearchView searchView;
    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    Query reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_provider);

        //Intitalize firebase
        firebaseFirestore = FirebaseFirestore.getInstance();
        reference = FirebaseFirestore.getInstance().collection("Users").orderBy("firstName");

        initComponent();
//        filterBtn = findViewById(R.id.flt);
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.searchRecycler);
        recyclerView.setHasFixedSize(true);


//        filterBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//            }
//        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchProvider(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchProvider(newText);
                return false;
            }
        });

    }

    //search service provider
    private void searchProvider(String name){
        Query query = reference.whereEqualTo("isProvider",true).startAt(name).endAt(name + "\uf8ff");

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("searchResult_", "listen:error", e);
                    return;
                }
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()){
                    switch (dc.getType()) {
                        case ADDED:
                            Log.d("searchResult_", "New usr: " + dc.getDocument().getData());
                            break;
                        case MODIFIED:
                            Log.d("searchResult_", "Modified usr: " + dc.getDocument().getData());
                            break;
                        case REMOVED:
                            Log.d("searchResult_", "Removed usr: " + dc.getDocument().getData());
                            break;
                    }

                }
            }
        });
    }

    private void initComponent() {
        // get the bottom sheet view
        LinearLayout llBottomSheet = findViewById(R.id.filter);

        // init the bottom sheet behavior
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);

        // change the state of the bottom sheet
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        // set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

    }
}
