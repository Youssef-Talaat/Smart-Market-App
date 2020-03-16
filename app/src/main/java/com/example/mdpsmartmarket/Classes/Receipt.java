package com.example.mdpsmartmarket.Classes;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class Receipt {
    private String ID;
    private String data;
    public static final String path = "Receipts";

    public Receipt(){

    }

    public Receipt(String ID, String data) {
        this.ID = ID;
        this.data = data;
    }

    public Receipt(String data) {
        this.data = data;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static void add(Receipt r){
        Database db = Database.getInstance();
        db.isert_push(path, r);
    }

    public static void edit(Receipt r){
        Database db = Database.getInstance();
        db.update(path, r.getID(), r);
    }

    public static void delete(String rID){
        Database db = Database.getInstance();
        db.delete(path, rID);
    }

    public static Object view(String rID){
        Database db = Database.getInstance();
        DataSnapshot dataSnapshot = db.view(path, rID);

        if(rID == null){
            Receipt r = dataSnapshot.getValue(Receipt.class);
            return r;
        }
        else {
            ArrayList<Receipt> r = new ArrayList<>();
            for(DataSnapshot ds : dataSnapshot.getChildren()){
                Receipt receipt = ds.getValue(Receipt.class);
                r.add(receipt);
            }
            return r;
        }
    }
}
