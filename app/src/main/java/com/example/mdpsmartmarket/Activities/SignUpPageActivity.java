package com.example.mdpsmartmarket.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mdpsmartmarket.Classes.User;
import com.example.mdpsmartmarket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpPageActivity extends AppCompatActivity {
    private EditText fullName;
    private EditText email;
    private EditText password;
    private EditText reEnterPassword;
    private TextView backToWelcome;
    private Button signUp;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference Ref;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        fullName = findViewById(R.id.edTxt_signup_name);
        email = findViewById(R.id.edTxt_signup_email);
        password = findViewById(R.id.edTxt_signup_password);
        reEnterPassword = findViewById(R.id.edTxt_signup_repassword);
        backToWelcome = findViewById(R.id.txt_signup_login);
        signUp = findViewById(R.id.btn_signup_signup);

        firebaseAuth = FirebaseAuth.getInstance();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String User_email = email.getText().toString().trim();
                String User_password = password.getText().toString().trim();

                database = FirebaseDatabase.getInstance();
                Ref = database.getReference().child("Users");

                if(!fullName.getText().toString().matches("") && !email.getText().toString().matches(""))
                {
                    if(password.getText().toString().length() >= 8)
                    {
                        if(password.getText().toString().matches(reEnterPassword.getText().toString()))
                        {

                firebaseAuth.createUserWithEmailAndPassword(User_email, User_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            User customer = new User();
                            customer.setFullName(fullName.getText().toString());
                            customer.setID(firebaseAuth.getUid());
                            customer.setEmail(email.getText().toString());
                            customer.setUserType("Customer");
                            customer.setPassword(password.getText().toString());

                            User.add(customer);
                            User.setLoggedUser(customer);

                            Toast.makeText(SignUpPageActivity.this, "Registration Done", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpPageActivity.this, HomePageActivity.class));
                        } else {
                            Toast.makeText(SignUpPageActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                        }
                        else {
                            Toast.makeText(SignUpPageActivity.this,"Passwords does not match",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(SignUpPageActivity.this, "Password must be atleast 8 characters", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(SignUpPageActivity.this,"Please fill all the required fields",Toast.LENGTH_SHORT).show();
                }
            }
        });

        backToWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
