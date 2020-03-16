package com.example.mdpsmartmarket.Classes;

import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable {
    private String ID;
    private String productID;
    private int quantity;
    private double unitPrice;
    public static final String path = "Items";

    public Item(){

    }

    public Item(String productID){
        this.productID = productID;
        Product p = ((Product) Product.view(productID));
        this.unitPrice = p.getPrice();
        this.quantity = 1;
    }

    public Item(String ID, String productID, int quantity, double totalPrice) {
        this.ID = ID;
        this.productID = productID;
        this.quantity = quantity;
        this.unitPrice = totalPrice;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public static void add(Item i){
        Database db = Database.getInstance();
        db.isert_push(path, i);
    }

    public static void edit(Item i){
        Database db = Database.getInstance();
        db.update(path, i.getID(), i);
    }

    public static void delete(String iID){
        Database db = Database.getInstance();
        db.delete(path, iID);
    }

    public static Object view(String iID){
        Database db = Database.getInstance();
        DataSnapshot dataSnapshot = db.view(path, iID);

        if(iID == null){
            Item i = dataSnapshot.getValue(Item.class);
            return i;
        }
        else {
            ArrayList<Item> i = new ArrayList<>();
            for(DataSnapshot ds : dataSnapshot.getChildren()){
                Item item = ds.getValue(Item.class);
                i.add(item);
            }
            return i;
        }
    }
}
