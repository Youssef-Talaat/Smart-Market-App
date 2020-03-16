package com.example.mdpsmartmarket.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditUsersPageActivity extends AppCompatActivity {
    private EditText fullName;
    private EditText email;
    private EditText password;
    private EditText reEnterPassword;
    private TextView backToWelcome;
    private TextView userType;
    private Button editProfile;
    private Button cancel;
    private FirebaseAuth firebaseAuth;
    private User editedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_users_page);

        fullName = findViewById(R.id.edTxt_editusers_name);
        email = findViewById(R.id.edTxt_editusers_email);
        password = findViewById(R.id.edTxt_editusers_password);
        reEnterPassword = findViewById(R.id.edTxt_editusers_repassword);
        editProfile = findViewById(R.id.btn_editusers_updateprofile);
        userType = findViewById(R.id.txt_editusers_usertype);
        cancel = findViewById(R.id.btn_editusers_cancel);

        firebaseAuth = FirebaseAuth.getInstance();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                editedUser = (User) User.view(getIntent().getStringExtra("UserID"));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fullName.setText(editedUser.getFullName());
                        email.setText(editedUser.getEmail());
                        userType.setText("UserType: " + editedUser.getUserType());

                        editProfile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                //FirebaseUser user1 = firebaseAuth.getCurrentUser();
                                //AuthCredential credential = EmailAuthProvider.getCredential("user@example.com", "password1234");
                                //user1.reauthenticate(credential);
                                //user.updateEmail(email.getText().toString().trim());
                                //user.updatePassword(password.getText().toString().trim());

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
                                                        editedUser.setFullName(fullName.getText().toString());
                                                        editedUser.setEmail(email.getText().toString());
                                                        editedUser.setPassword(password.getText().toString());
                                                        User.update(editedUser);

                                                        Toast.makeText(EditUsersPageActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    } else {
                                                        Toast.makeText(EditUsersPageActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                        else {
                                            Toast.makeText(EditUsersPageActivity.this,"Passwords does not match",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else {
                                        Toast.makeText(EditUsersPageActivity.this, "Password must be atleast 8 characters", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(EditUsersPageActivity.this,"Please fill all the required fields",Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                });
            }
        });
        thread.start();




        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //destroy activity and go back
                finish();
            }
        });

    }
}
