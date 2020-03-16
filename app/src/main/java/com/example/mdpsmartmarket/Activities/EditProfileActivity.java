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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {
    private EditText fullName;
    private EditText email;
    private EditText password;
    private EditText reEnterPassword;
    private TextView backToWelcome;
    private Button editProfile;
    private Button cancel;
    private Button removeAccount;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        fullName = findViewById(R.id.edTxt_editprofile_name);
        email = findViewById(R.id.edTxt_editprofile_email);
        password = findViewById(R.id.edTxt_editprofile_password);
        reEnterPassword = findViewById(R.id.edTxt_editprofile_repassword);
        editProfile = findViewById(R.id.btn_editprofile_updateprofile);
        removeAccount = findViewById(R.id.btn_editprofile_deleteaccount);
        cancel = findViewById(R.id.btn_editprofile_cancel);

        firebaseAuth = FirebaseAuth.getInstance();

        if(User.getLoggedUser().getUserType().equals("Staff")) {
            removeAccount.setEnabled(false);
        }

        fullName.setText(User.getLoggedUser().getFullName());
        email.setText(User.getLoggedUser().getEmail());

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String User_email = email.getText().toString().trim();
                String User_password = password.getText().toString().trim();

                FirebaseUser user = firebaseAuth.getCurrentUser();
                user.updateEmail(email.getText().toString().trim());
                user.updatePassword(password.getText().toString().trim());

                if(!fullName.getText().toString().matches("") && !email.getText().toString().matches(""))
                {
                    if(password.getText().toString().length() >= 8)
                    {
                        if(password.getText().toString().matches(reEnterPassword.getText().toString()))
                        {
                            firebaseAuth.updateCurrentUser(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        //User editUser = User.getLoggedUser();
                                        User.getLoggedUser().setFullName(fullName.getText().toString());
                                        User.getLoggedUser().setEmail(email.getText().toString());
                                        User.getLoggedUser().setPassword(password.getText().toString());
                                        User.update(User.getLoggedUser());

                                        Toast.makeText(EditProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(EditProfileActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(EditProfileActivity.this,"Passwords does not match",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(EditProfileActivity.this, "Password must be atleast 8 characters", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(EditProfileActivity.this,"Please fill all the required fields",Toast.LENGTH_SHORT).show();
                }

            }
        });

        removeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        User.delete(User.getLoggedUser().getID());
                        User.setLoggedUser(null);
                        finish();
                    }
                });
                thread.start();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //destroy activity and go back
                finish();
            }
        });
    }
}
