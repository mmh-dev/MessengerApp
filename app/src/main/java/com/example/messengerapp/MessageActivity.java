package com.example.messengerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messengerapp.Models.Chat;
import com.example.messengerapp.Models.User;
import com.example.messengerapp.ui.main.MessageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    EditText messageText;
    ImageButton send_btn;
    Intent intent;
    List<Chat> chatList;
    RecyclerView chatRecyclerView;
    MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar_chat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username_chat);
        messageText = findViewById(R.id.message_text);
        send_btn = findViewById(R.id.send_btn);
        chatRecyclerView = findViewById(R.id.chat_recycler_view);
        chatRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(manager);

        intent = getIntent();
        final String userID = intent.getStringExtra("chatUserID");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                username.setText(user.getUsername());
                if (user.getImageUrl().equals("default")){
                    profile_image.setImageResource(R.drawable.person_icon);
                }
                else {
                    Picasso.get().load(user.getImageUrl()).into(profile_image);
                }
                readMessages (firebaseUser.getUid(), userID, user.getImageUrl());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MessageActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = messageText.getText().toString();
                if (!msg.equals("")) {
                    MessageActivity.this.sendMessage(firebaseUser.getUid(), userID, msg);

                } else {
                    Toast.makeText(MessageActivity.this, "Empty message!", Toast.LENGTH_SHORT).show();
                }
                messageText.setText("");
            }
        });
    }

    private void readMessages(final String sender, final String receiver, final String imageUrl) {
        chatList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(receiver) && chat.getSender().equals(sender) ||
                    chat.getReceiver().equals(sender) && chat.getSender().equals(receiver)){
                        chatList.add(chat);
                    }
                }
                adapter = new MessageAdapter(chatList, MessageActivity.this, imageUrl);
                chatRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MessageActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Log.i("chat", String.valueOf(chatList.size()));
    }

    private void sendMessage(final String sender, final String receiver, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, String> map = new HashMap<>();
        map.put("sender", sender);
        map.put("receiver", receiver);
        map.put("message", message);
        reference.child("Chats").push().setValue(map);

        final DatabaseReference refHasChat = FirebaseDatabase.getInstance().getReference().child("Users");
        refHasChat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user.getId().equals(sender) || user.getId().equals(receiver)){
                        if (user.isHasChat() == false){
                            refHasChat.child(sender).child("hasChat").setValue(true);
                            refHasChat.child(receiver).child("hasChat").setValue(true);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}