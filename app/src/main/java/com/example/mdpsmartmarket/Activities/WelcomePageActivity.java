package com.example.mdpsmartmarket.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mdpsmartmarket.Classes.Cart;
import com.example.mdpsmartmarket.Classes.User;
import com.example.mdpsmartmarket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomePageActivity extends AppCompatActivity {

    private Button logIn;
    private Button signUp;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private EditText password;
    private EditText username;
    private DatabaseReference refView;
    private FirebaseDatabase database;
   private int backpressed=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        logIn = findViewById(R.id.btn_welcome_login);
        signUp = findViewById(R.id.btn_welcome_signup);
        username = findViewById(R.id.edTxt_welcome_username);
        password = findViewById(R.id.edTxt_welcome_password);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().matches("") || password.getText().toString().matches(""))
                {
                    Toast.makeText(WelcomePageActivity.this,"Please fill all the required fields",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    check((username.getText().toString()),password.getText().toString());
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomePageActivity.this, SignUpPageActivity.class));
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


 }
    private void check(String Username, String Password)
    {
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(Username,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    progressDialog.dismiss();

                    ////////////////////////////////////////////View Users
                    database = FirebaseDatabase.getInstance();
                    refView = database.getReference().child("Users").child(firebaseAuth.getUid());
                    refView.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                User u = dataSnapshot.getValue(User.class);
                                User.setLoggedUser(u);

                                if(u.getUserType().equals("Customer")) {
                                    startActivity(new Intent(WelcomePageActivity.this, HomePageActivity.class));
                                }
                                else if(u.getUserType().equals("Staff")){
                                    startActivity(new Intent(WelcomePageActivity.this, StaffHomePageActivity.class));
                                }
                                else if(u.getUserType().equals("Admin")){
                                    startActivity(new Intent(WelcomePageActivity.this, ManageUsersActivity.class));
                                }
                                username.setText("");
                                password.setText("");
                                Toast.makeText(WelcomePageActivity.this,"Login Done",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(WelcomePageActivity.this,"Incorrect username or password",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
