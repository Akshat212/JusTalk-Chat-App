package com.example.justalk;

import android.content.Intent;
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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    HomeActivity homeActivity;
    ArrayList<Users> list;

    public UserAdapter(HomeActivity homeActivity, ArrayList<Users> list) {
        this.homeActivity = homeActivity;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(homeActivity).inflate(R.layout.item_user_draw, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users users = list.get(position);

        if(FirebaseAuth.getInstance().getCurrentUser() != null)
            if(list.get(position).equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                list.remove(list.get(position));

            }

        holder.user_name.setText(users.getName());
        holder.user_status.setText(users.getStatus());
        Picasso.get().load(users.imageUri).into(holder.user_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homeActivity, ChatActivity.class);
                intent.putExtra("name", users.getName());
                intent.putExtra("ReceiverImage", users.imageUri);
                intent.putExtra("uid", users.getUid());
                homeActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView user_image;
        TextView user_name , user_status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            user_image = itemView.findViewById(R.id.user_image);
            user_name = itemView.findViewById(R.id.user_name);
            user_status = itemView.findViewById(R.id.user_status);
        }
    }

}
