package com.example.mdpsmartmarket.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.mdpsmartmarket.Classes.Cart;
import com.example.mdpsmartmarket.Classes.HistoryRecyclerViewAdapter;
import com.example.mdpsmartmarket.Classes.ManageUserRecyclerViewAdapter;
import com.example.mdpsmartmarket.Classes.User;
import com.example.mdpsmartmarket.R;

import java.util.ArrayList;

public class ManageUsersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button addUser;
    private Button cancel;
    private ManageUserRecyclerViewAdapter adapter;
    private ArrayList<User> users;
    private int backpressed=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        recyclerView = findViewById(R.id.rec_manageusers_viewUsers);
        addUser = findViewById(R.id.btn_manageusers_adduser);
        cancel = findViewById(R.id.btn_manageusers_cancel);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                users = (ArrayList<User>) User.view(null);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setLayoutManager(new LinearLayoutManager(ManageUsersActivity.this));
                        adapter = new ManageUserRecyclerViewAdapter(ManageUsersActivity.this, users);
                        //adapter.setClickListener(ManageUsersActivity.this);
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        });
        thread.start();

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageUsersActivity.this, Add_EditUsersPageActivity.class));
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public void onBackPressed(){
        if(backpressed>=1){
            Intent exit = new Intent(Intent.ACTION_MAIN);
            exit.addCategory(Intent.CATEGORY_HOME);
            exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(exit);
            finish();
            System.exit(0);}

        else{
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            backpressed++;
        }
}}
