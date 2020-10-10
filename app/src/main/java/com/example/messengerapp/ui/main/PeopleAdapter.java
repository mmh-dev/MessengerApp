package com.example.messengerapp.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.Holder> {

    List<User> userList;
    OnItemClickListener listener;

    public void setOnItemClickListener (OnItemClickListener listener){
        this.listener = listener;
    }

    public PeopleAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_item, parent, false);
        return new Holder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {
        holder.user_name_people.setText(userList.get(position).getUsername());
        User user = userList.get(position);
        if (user.getImageUrl().equals("default")){
            holder.profile_image_people.setImageResource(R.drawable.person_icon);
        }
        else {
            Picasso.get().load(user.getImageUrl()).into(holder.profile_image_people);
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TextView user_name_people;
        CircleImageView profile_image_people;

        public Holder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            user_name_people = itemView.findViewById(R.id.user_name_people);
            profile_image_people = itemView.findViewById(R.id.profile_image_people);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    listener.onItemClick(position);
                }
            });

        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
