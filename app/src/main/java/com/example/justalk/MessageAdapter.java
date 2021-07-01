package com.example.justalk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.justalk.ChatActivity.rImage;

public class MessageAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Messages> messagesArrayList;
    int SEND_ITEM = 1;
    int RECEIVE_ITEM = 2;

    public MessageAdapter(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        if(viewType == SEND_ITEM)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
            return new SenderViewHolder(view);
        }else
        {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_layout, parent, false);
            return new ReceiverViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Messages messages = messagesArrayList.get(position);

        if(holder.getClass()==(SenderViewHolder.class))
        {
            SenderViewHolder viewHolder = (SenderViewHolder) holder;

            viewHolder.sent_message.setText(messages.getMessage());
        }else
        {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;

            viewHolder.sent_message.setText(messages.getMessage());
            Picasso.get().load(rImage).into(viewHolder.receiver_image);
        }

    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {

        Messages messages = messagesArrayList.get(position);

        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderUid()))
            return SEND_ITEM;
        else return RECEIVE_ITEM;
    }

    class SenderViewHolder extends RecyclerView.ViewHolder{

        TextView sent_message;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            sent_message = itemView.findViewById(R.id.sender_message);
        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder {

        TextView sent_message;
        CircleImageView receiver_image;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            sent_message = itemView.findViewById(R.id.receiver_message);
            receiver_image = itemView.findViewById(R.id.receiver_image);
        }
    }

}
