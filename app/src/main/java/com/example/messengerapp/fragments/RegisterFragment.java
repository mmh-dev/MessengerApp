package com.example.messengerapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.messengerapp.Models.User;
import com.example.messengerapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterFragment extends Fragment {

    MaterialEditText username, email, password;
    Button register_btn;
    FirebaseAuth auth;  //needed for authorisation
    DatabaseReference reference;  // for storing a data

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);
        username = view.findViewById(R.id.username);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        register_btn = view.findViewById(R.id.register_btn);

        auth = FirebaseAuth.getInstance();

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(getContext(), "Complete all fields!", Toast.LENGTH_SHORT).show();
                }
                else if (txt_password.length() < 6){
                    Toast.makeText(getContext(), "Password must contain at least 6 symbols!", Toast.LENGTH_SHORT).show();
                }
                else {
                    register(txt_username, txt_email, txt_password);
                }
            }
        });

        return view;
    }
    public void register (final String username, final String email, String password){

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();
                            String userId = user.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users")
                                    .child(userId);

                            User user1 = new User();
                            user1.setId(userId);
                            user1.setUsername(username);
                            user1.setEmail(email);
                            user1.setImageUrl("default");
                            user1.setStatus("offline");
                            user1.setHasChat(false);

                            reference.setValue(user1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getContext(), "User is successfully registered!", Toast.LENGTH_SHORT).show();
                                        ViewPager layout = getActivity().findViewById(R.id.view_pager);
                                        layout.setCurrentItem(0);
                                    }
                                }
                            });
                        }
                    }
                });
    }
}