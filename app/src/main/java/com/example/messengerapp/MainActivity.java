package com.example.messengerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messengerapp.Models.Chat;
import com.example.messengerapp.Models.User;
import com.example.messengerapp.ui.main.PeopleAdapter;
import com.example.messengerapp.ui.main.SectionsPagerAdapterMain;
import com.google.android.material.tabs.TabLayout;
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

public class MainActivity extends AppCompatActivity {

    NotificationManagerCompat notificationManager;
    CircleImageView profileImage;
    TextView username;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    PeopleAdapter adapter;
    List<Chat> unreadMessageList = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();
        profileImage = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                username.setText(user.getUsername());
                if (user.getImageUrl().equals("default")){
                    profileImage.setImageResource(R.drawable.person_icon);
                }
                else {
                    Picasso.get().load(user.getImageUrl()).into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Chats");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getIsSeen().equals("delivered")){
                        unreadMessageList.add(chat);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        notificationMessage();

    }

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManager = NotificationManagerCompat.from(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        SectionsPagerAdapterMain sectionsPagerAdapterMain = new SectionsPagerAdapterMain(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager_main);
        viewPager.setAdapter(sectionsPagerAdapterMain);
        TabLayout tabs = findViewById(R.id.tabs_main);
        tabs.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        SearchView searchView = (SearchView)menu.findItem(R.id.search_icon).getActionView();
        searchView.setQueryHint("Search contacts...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logout_icon ){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, AuthActivity.class));
            finish();
        };

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void status(String status){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> statusMap = new HashMap();
        statusMap.put("status", status);
        reference.updateChildren(statusMap);
    }

    @Override
    protected void onPause() {
        status("offline");
        super.onPause();
    }

    @Override
        protected void onResume() {
        status("online");
        super.onResume();
    }

    private void notificationMessage(){
        if (unreadMessageList.size() > 1){
            Notification messageNotification = new NotificationCompat.Builder(this, com.example.messengerapp.Notification.CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.person_icon)
                    .setContentTitle("New message from" + unreadMessageList.get(0).getSender())
                    .setContentText(unreadMessageList.get(0).getMessage())
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .build();
            notificationManager.notify(1, messageNotification);
            Log.i("tag",  String.valueOf(unreadMessageList.size()));
        }

    }
}