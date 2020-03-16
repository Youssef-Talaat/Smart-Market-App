package com.example.mdpsmartmarket.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mdpsmartmarket.Classes.User;
import com.example.mdpsmartmarket.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomePageActivity extends AppCompatActivity {
    private Button logOut;
    private Button startShopping;
    private Button history;
    private Button editProfile;
    private DatabaseReference Ref;
    private int backpressed=0;

    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        startShopping = findViewById(R.id.btn_home_startshopping);
        history = findViewById(R.id.btn_home_history);
        editProfile = findViewById(R.id.btn_home_editprofile);
        logOut = findViewById(R.id.btn_home_logout);


        database = FirebaseDatabase.getInstance();
        Ref = database.getReference();

        startShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, StartShoppingPageActivity.class));
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, HistoryPageActivity.class));
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, EditProfileActivity.class));
                finish();
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //destroy activity and go back
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
