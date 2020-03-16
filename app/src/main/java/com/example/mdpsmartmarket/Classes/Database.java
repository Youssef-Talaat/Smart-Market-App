package com.example.mdpsmartmarket.Classes;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static java.lang.Thread.sleep;

public class Database {
    private static FirebaseAuth firebaseAuth;
    private DatabaseReference Ref, refView;
    private FirebaseDatabase database;
    private static Database instance;
    private static DataSnapshot dataSnapshot;
    private boolean readed;

    private Database() {
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        Ref = database.getReference();
    }

    public static Database getInstance(){
        if(instance == null) {
            Database.instance = new Database();
        }
        return Database.instance;
    }

    public static FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public static void setFirebaseAuth(FirebaseAuth firebaseAuth) {
        Database.firebaseAuth = firebaseAuth;
    }

    public void isert_setValue(String path, String ID, Object obj){
        Ref.child(path).child(ID).setValue(obj);
    }

    public String isert_push(String path, Object obj){
        String pushedID = Ref.child(path).push().getKey();
        Ref.child(path).child(pushedID).setValue(obj);
        return pushedID;
    }

    public DataSnapshot view(String path, String ID) {
        if(ID != null){
            refView = Ref.child(path).child(ID);
        }
        else{
            refView = Ref.child(path);
        }
        refView.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("MYTEXT0 : ", "Helloooooooooooooooooo");
                if(dataSnapshot.exists()){
                    Log.d("MYTEXT0.1 : ", "Helloooooooooooooooooo");
                    Database.dataSnapshot = dataSnapshot;
                    Log.d("MYTEXT0.2 : ", "Helloooooooooooooooooo");
                    readed = true;
                    Log.d("MYTEXT0.3 : ", "Helloooooooooooooooooo");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        while (!readed){
            try {
                sleep(100);
                Log.d("MYTEXT2 : ", "Helloooooooooooooooooo");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d("MYTEXT3 : ", "Helloooooooooooooooooo");
        readed = false;
        return Database.dataSnapshot;
    }

    public void update(String path, String ID, Object obj){
        HashMap<String, Object> map = new HashMap<>();
        map.put(ID, obj);
        Ref.child(path).updateChildren(map);
    }


    public void delete(String path, String ID){
        Ref.child(path).child(ID).removeValue();
    }
}
