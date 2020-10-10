package com.example.messengerapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.messengerapp.Models.User;
import com.example.messengerapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment {

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    TextView user_name, user_email;
    ImageView profile_image_full;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        user_name = view.findViewById(R.id.user_name);
        user_email = view.findViewById(R.id.user_email);
        profile_image_full = view.findViewById(R.id.profile_image_full);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                user_name.setText(user.getUsername());
                user_email.setText(user.getEmail());
                if (user.getImageUrl().equals("default")){
                    profile_image_full.setImageResource(R.drawable.person_icon);
                }
                else {
                    Picasso.get().load(user.getImageUrl()).into(profile_image_full);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}