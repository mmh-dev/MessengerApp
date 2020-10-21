package com.example.messengerapp.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messengerapp.Models.Chat;
import com.example.messengerapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.Holder> {

    List<Chat> chatList;
    private final int message_sender = 0;
    private final int message_receiver = 1;
    private Context context;
    String imageUrl;
    FirebaseUser firebaseUser;

    public MessageAdapter(List<Chat> chatList, Context context, String imageUrl) {
        this.chatList = chatList;
        this.context = context;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == message_sender){
            view = LayoutInflater.from(context).inflate(R.layout.chat_sender_layout, parent, false);
        }
        else {
            view = LayoutInflater.from(context).inflate(R.layout.chat_receiver_layout, parent, false);
        }

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {
        Chat chat = chatList.get(position);
        holder.show_message.setText(chat.getMessage());
        if (imageUrl.equals("default")){
            holder.profile_image_chat.setImageResource(R.drawable.person_icon);
        }
        else {
            Picasso.get().load(imageUrl).into(holder.profile_image_chat);
        }
        if (position == chatList.size()-1){
            if (chat.getIsSeen().equals("seen")){
                holder.delivery_status.setText("Seen");
            }
            else {
                holder.delivery_status.setText("Delivered");
            }
        }
        else {
            holder.delivery_status.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TextView show_message, delivery_status;
        CircleImageView profile_image_chat;

        public Holder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            profile_image_chat = itemView.findViewById(R.id.user_avatar);
            delivery_status = itemView.findViewById(R.id.delivery_status);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatList.get(position).getSender().equals(firebaseUser.getUid())){
            return message_sender;
        }
        else {
         return message_receiver;
        }
    }
}
