package com.example.zenthread;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatWin extends AppCompatActivity {

    String reciverImg, reciverUid,reciverName,senderUID;
    CircleImageView profile;
    TextView reciverNName;
    CardView sendbtn;
    EditText textmsg;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    public static String senderImg;
    public static String reciverIImg;
    String senderRoom, reciverRoom;
    RecyclerView messagesAdpter;
    ArrayList<msgModelclass> messagessArrayList;
    messagesAdpter mmesagesAdpter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_win);

        database= FirebaseDatabase.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();



        reciverName= getIntent().getStringExtra("nameee");
        reciverImg= getIntent().getStringExtra("reciverImg");
        reciverUid= getIntent().getStringExtra("uid");

        messagessArrayList= new ArrayList<>();
       sendbtn= findViewById(R.id.sendbtnn);
       textmsg= findViewById(R.id.textmsg);


        reciverNName= findViewById(R.id.recivername);
        profile= findViewById(R.id.profileimgg);

        messagesAdpter = findViewById(R.id.msgadpter);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messagesAdpter.setLayoutManager(linearLayoutManager);
        mmesagesAdpter= new messagesAdpter(chatWin.this,messagessArrayList);
        messagesAdpter.setAdapter(mmesagesAdpter);


        Picasso.get().load(reciverImg).into(profile);
        reciverNName.setText("", TextView.BufferType.valueOf(reciverName));

        senderUID = firebaseAuth.getUid();
        senderRoom= senderUID+reciverUid;
        reciverRoom= reciverUid+senderUID;

        DatabaseReference reference= database.getReference().child("user").child(Objects.requireNonNull(firebaseAuth.getUid()));
        DatabaseReference chatreference= database.getReference().child("chats").child(senderRoom).child("messages");

        chatreference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagessArrayList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()
                ) {
                    msgModelclass messages= dataSnapshot.getValue(msgModelclass.class);
                    messagessArrayList.add(messages);
                }
               mmesagesAdpter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              senderImg= Objects.requireNonNull(snapshot.child("profilepic").getValue()).toString();
              reciverIImg= reciverImg;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        sendbtn.setOnClickListener(v -> {
            String message= textmsg.getText().toString();
            if(message.isEmpty()){
                Toast.makeText(chatWin.this, "Enter The Message First!", Toast.LENGTH_SHORT).show();
            }
            textmsg.setText("");
            Date date= new Date();
            msgModelclass messagess = new msgModelclass(message,senderUID,date.getTime());
            database = FirebaseDatabase.getInstance();
            database.getReference().child("chats").child(senderRoom).child("messages").push().setValue(messagess).addOnCompleteListener(task -> database.getReference().child("chats").child(reciverRoom).child("messages").push().setValue(messagess).addOnCompleteListener(task1 -> {

            }));


        });

    }
}