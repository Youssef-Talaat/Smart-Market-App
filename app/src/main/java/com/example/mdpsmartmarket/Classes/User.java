package com.example.mdpsmartmarket.Classes;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class User {
    private String ID;
    private String fullName;
    private String email;
    private String password;
    private String userType;
    public static final String path = "Users";
    private static User loggedUser;

    public User(){

    }

    public User(String ID, String fullName, String email, String password, String userType) {
        this.ID = ID;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    public static void setLoggedUser(User loggedUser){
        User.loggedUser = loggedUser;
    }

    public static User getLoggedUser(){
        return User.loggedUser;
    }

    public static void add(User user){
        Database db = Database.getInstance();
        db.isert_setValue("Users",user.getID(), user);
    }

    public static void update(User user){
        Database db = Database.getInstance();
        db.update("Users",user.getID(), user);
    }

    public static void delete(String ID){
        User deletedUser = (User) User.view(ID);
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential authCredential = EmailAuthProvider.getCredential(deletedUser.getEmail(), deletedUser.getPassword());

        firebaseUser.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Database db = Database.getInstance();
                            db.delete("Users", ID);
                        }
                    }
                });
            }
        });

    }

    public static Object view(String uID){
        Database db = Database.getInstance();
        DataSnapshot dataSnapshot = db.view(path, uID);

        if(uID != null){
            User u = dataSnapshot.getValue(User.class);
            return u;
        }
        else {
            ArrayList<User> u = new ArrayList<>();
            for(DataSnapshot ds : dataSnapshot.getChildren()){
                User user = ds.getValue(User.class);
                u.add(user);
            }
            return u;
        }
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
