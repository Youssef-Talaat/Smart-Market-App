package com.example.mdpsmartmarket.Classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mdpsmartmarket.Activities.EditUsersPageActivity;
import com.example.mdpsmartmarket.R;

import java.util.ArrayList;

public class ManageUserRecyclerViewAdapter extends RecyclerView.Adapter<ManageUserRecyclerViewAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private ArrayList<User> users;
    private User currentUser;
    private ItemClickListener mClickListener;

    public ManageUserRecyclerViewAdapter(Context context, ArrayList<User> users) {
        this.mInflater = LayoutInflater.from(context);
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.user_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        currentUser = users.get(position);
        holder.fullname.setText("Name: " + currentUser.getFullName());
        holder.usertype.setText("Type: " + currentUser.getUserType());
        //holder.userImage.setBackgroundResource(R.drawable.logo);
        holder.editUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mInflater.getContext(), "name: " + users.get(position).getFullName(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(mInflater.getContext(), EditUsersPageActivity.class);
                intent.putExtra("UserID", users.get(position).getID());
                mInflater.getContext().startActivity(intent);
                /*Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        users = (ArrayList<User>) User.view(null);
                    }
                });
                thread.start();*/
            }
        });
        holder.removeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mInflater.getContext(),"delete",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView fullname;
        TextView usertype;
        ImageView userImage;
        Button editUser;
        Button removeUser;

        ViewHolder(View itemView) {
            super(itemView);
            fullname = itemView.findViewById(R.id.txt_user_record_fullname);
            usertype = itemView.findViewById(R.id.txt_user_record_usertype);
            editUser = itemView.findViewById(R.id.btn_user_record_edit);
            removeUser = itemView.findViewById(R.id.btn_user_record_removeuser);
            //itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public User getItem(int id) {
        return users.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
