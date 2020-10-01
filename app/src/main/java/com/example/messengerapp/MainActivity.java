package com.example.messengerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messengerapp.Models.User;
import com.example.messengerapp.ui.main.SectionsPagerAdapter;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    CircleImageView profileImage;
    TextView username;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

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
                    profileImage.setImageResource(R.mipmap.ic_launcher);
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

    }

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}