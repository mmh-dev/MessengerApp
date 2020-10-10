package com.example.messengerapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.messengerapp.MessageActivity;
import com.example.messengerapp.Models.User;
import com.example.messengerapp.R;
import com.example.messengerapp.ui.main.PeopleAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {

    DatabaseReference reference;
    RecyclerView chatsRecyclerView;
    FirebaseUser firebaseUser;
    List<User> userListHasChats = new ArrayList<>();
    PeopleAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        chatsRecyclerView = view.findViewById(R.id.chats_recycler_view);
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userListHasChats.clear();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    if (user.isHasChat() == true && !user.getId().equals(firebaseUser.getUid())){
                        userListHasChats.add(user);
                    }

                }
                chatsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new PeopleAdapter(userListHasChats);
                chatsRecyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new PeopleAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(getActivity(), MessageActivity.class);
                        intent.putExtra("chatUserID", userListHasChats.get(position).getId());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}