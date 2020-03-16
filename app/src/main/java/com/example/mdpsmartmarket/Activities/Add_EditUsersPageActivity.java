package com.example.mdpsmartmarket.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mdpsmartmarket.Classes.User;
import com.example.mdpsmartmarket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Add_EditUsersPageActivity extends AppCompatActivity {
    private EditText fullName;
    private EditText email;
    private EditText password;
    private EditText reEnterPassword;
    private Spinner userType;
    private Button signUp;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference Ref;
    private FirebaseDatabase database;
    //comment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__edit_users_page);

        fullName = findViewById(R.id.edTxt_addeditusers_name);
        email = findViewById(R.id.edTxt_addeditusers_email);
        password = findViewById(R.id.edTxt_addeditusers_password);
        reEnterPassword = findViewById(R.id.edTxt_addeditusers_repassword);
        signUp = findViewById(R.id.btn_addeditusers_signup);
        userType = findViewById(R.id.spin_addeditusers_usertype);

        String[] items = new String[]{"Admin", "Customer", "Staff"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        userType.setAdapter(adapter);

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
                                        customer.setUserType(userType.getSelectedItem().toString());
                                        customer.setPassword(password.getText().toString());

                                        User.add(customer);

                                        Toast.makeText(Add_EditUsersPageActivity.this, "Registration Done", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(Add_EditUsersPageActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(Add_EditUsersPageActivity.this,"Passwords does not match",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(Add_EditUsersPageActivity.this, "Password must be atleast 8 characters", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(Add_EditUsersPageActivity.this,"Please fill all the required fields",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
