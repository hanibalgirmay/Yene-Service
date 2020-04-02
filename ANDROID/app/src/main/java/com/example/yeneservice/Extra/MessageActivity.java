package com.example.yeneservice.Extra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yeneservice.Adapters.ChatAppointedUserAdapter;
import com.example.yeneservice.Models.ChatModel;
import com.example.yeneservice.Models.UserModel;
import com.example.yeneservice.Notification.Client;
import com.example.yeneservice.Notification.NotficationData;
import com.example.yeneservice.Notification.NotificationSender;
import com.example.yeneservice.Notification.MyResponse;
import com.example.yeneservice.Notification.Token;
import com.example.yeneservice.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {
    CircleImageView profile_images;
    TextView username, status;
    ImageButton btn_send,img;
    ImageView chat_img;
    EditText text_send;

    FirebaseUser fuser;
    DocumentReference documentReference;
    ChatAppointedUserAdapter chatAdapter;
    List<ChatModel> mchat;
    RecyclerView recyclerView;
    private StorageReference storageReference;
    private FirebaseAuth auth;
    private String user_id;
    APIService apiService;
    RelativeLayout Bottom;
    private FirebaseFirestore firebaseFirestore;
    Intent intent;
    private Uri mainImageURI = null;
    private boolean isChanged = false;
    private Bitmap compressedImageFile;
    private static final String TAG = "MessageActivity";
    int SELECT_IMAGE = 0;
    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MessageActivity.this,AppointedUsersActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                finish();
            }
        });

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        user_id = auth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recycler_view);
        Bottom = findViewById(R.id.bottom);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        profile_images = findViewById(R.id.profile_images);
        username = findViewById(R.id.username);
        status = findViewById(R.id.status);
        btn_send = findViewById(R.id.btn_send);
        img = findViewById(R.id.img_btn);
        chat_img = findViewById(R.id.img);
        text_send = findViewById(R.id.text_send);

//        if(text_send.callOnClick()){
//            text_send.setBackground(R.color.aluminum);
//        }

        intent = getIntent();
        final String idd = intent.getStringExtra("providerID");
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);
            }
        });


        assert idd != null;
        firebaseFirestore.collection("Users").document(idd).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        String fname = task.getResult().getString("firstName");
                        String lname = task.getResult().getString("lastName");
                        String email = task.getResult().getString("email");
                        String phone = task.getResult().getString("phone");
                        String image = task.getResult().getString("image");
                        String sta = task.getResult().getString("status");

                        username.setText(fname);
                        status.setText(sta);
                        Picasso.get().load(image).into(profile_images);
                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(MessageActivity.this, "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();
                }
//                progressBar.setVisibility(View.INVISIBLE);
//                btnSignUp.setEnabled(true);
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;
                String msg = text_send.getText().toString();
                if(!msg.equals("")){
                    sendMessage(fuser.getUid(),idd, msg);
                } else {
                    Toast.makeText(MessageActivity.this,"You can not send empty message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });

        firebaseFirestore.collection("Users").document(idd).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        String imgg = task.getResult().getString("image");

                        readMessage(auth.getUid(),idd, imgg);
                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(MessageActivity.this, "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();
                }
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.chat_call){
            intent = getIntent();
            final String userid = intent.getStringExtra("providerID");
            firebaseFirestore.collection("Users").document(userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        if(task.getResult().exists()){
                            String phone = task.getResult().getString("phone");

                            final Intent cal = new Intent(Intent.ACTION_DIAL);
                            String t = "tel:"+phone;
                            cal.setData(Uri.parse(t));
                            startActivity(cal);
                        }
                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(MessageActivity.this, "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();
                    }
                }
            });
            return true;
        }
        if (id == R.id.action_msg) {
//            startActivity(new Intent(MessageActivity.this, AppointementUserActivity.class));
            return true;
        } else {

        }
        return super.onOptionsItemSelected(item);
    }

    private void seenMessage(final String user_id){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Messages").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Log.d(TAG,"Error: "+ e.getMessage());
                }
                for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                    if(doc.getType() == DocumentChange.Type.ADDED){
                        ChatModel chat = doc.getDocument().toObject(ChatModel.class);
//                        ServicesProvider serv = doc.getDocument().toObject(ServicesProvider.class);
                        if(chat.getReceiver().equals(auth.getUid()) && chat.getSender().equals(user_id)){
                            HashMap<String, Object> seenHash = new HashMap();
                            seenHash.put("isSeen", true);
                            documentReference.update(seenHash);
                        }

                    }else {
                        Log.d(TAG, "Error getting documents: ");
                    }
                }
            }
        });

    }

    private void updateToken(String token){
        FirebaseFirestore firebaseFirestore;
        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference reference = FirebaseFirestore.getInstance().collection("Token").document();
        Token token1 = new Token(token);
        firebaseFirestore.collection("Tokens").document(auth.getCurrentUser().getUid()).set(token1);
    }

    private void sendMessage(String sender, final String receiver, String message){
        intent = getIntent();
        final String reciverid = intent.getStringExtra("providerID");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",user_id);
        hashMap.put("receiver", reciverid);
        hashMap.put("message", message);
        hashMap.put("isSeen", false);
        hashMap.put("timestamp", Timestamp.now());
        firebaseFirestore.collection("Messages").add(hashMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
//                    Intent mainIntent = new Intent(MessageActivity.this, MessageActivity.class);
////                    mainIntent.putExtra("doccumentId",myId);
//                    startActivity(mainIntent);
//                    finish();
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(MessageActivity.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();
                }
//                    progressBar.setVisibility(View.INVISIBLE);
            }
        });
        final String msg = message;

        firebaseFirestore.collection("Users").document(auth.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                UserModel userModel = documentSnapshot.toObject(UserModel.class);
                if(notify){
                    sendNotification(receiver,userModel.getUsername(),msg);
                }
                notify = false;
            }
        });
    }

    private void sendNotification(String receiver, final String username, final String msg) {
        DocumentReference tokens = FirebaseFirestore.getInstance().collection("Tokens").document();
        Query query = tokens.collection("Tokens").orderBy(receiver);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                    Token token = doc.getDocument().toObject(Token.class);
                    NotficationData data = new NotficationData(fuser.getUid(),R.drawable.ic_notifications_black_24dp,username+
                            ": "+msg,"New Message",user_id);
                    NotificationSender sender = new NotificationSender(data,token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code() == 200){
                                        if(response.body().success != 1){
                                            Toast.makeText(MessageActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }
        });
    }

    private void readMessage(final String myid, final String useriD, final String imageurl){
        mchat = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Messages").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Log.d(TAG,"Error: "+ e.getMessage());
                }
                for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                    if(doc.getType() == DocumentChange.Type.ADDED){
                        final String id = doc.getDocument().getId();
                        ChatModel chat = doc.getDocument().toObject(ChatModel.class);
                        chat.setDocId(id);
//                        ServicesProvider serv = doc.getDocument().toObject(ServicesProvider.class);
                        if(myid.equals(chat.getReceiver()) && useriD.equals(chat.getSender()) || useriD.equals(chat.getReceiver()) && myid.equals(chat.getSender())){
                            mchat.add(chat);
                        }
                        chatAdapter = new ChatAppointedUserAdapter(MessageActivity.this, mchat, imageurl);
                        recyclerView.setAdapter(chatAdapter);

                    }else {
                        Log.d(TAG, "Error getting documents: ");
                    }
                }
            }
        });
        updateToken(FirebaseInstanceId.getInstance().getToken());
    }

    private void status(String status){
        documentReference = firebaseFirestore.collection("Users").document(fuser.getUid());
        HashMap<String,Object>hashMap = new HashMap<>();
        hashMap.put("status", status);
        documentReference.update(hashMap);
    }
}
