package com.example.mdpsmartmarket.Classes;

import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;
import java.util.ArrayList;

public class Cart implements Serializable{
    private String ID;
    private String customerID;
    private ArrayList<Item> items;
    private Boolean paid = false;
    public static final String path = "Carts";


    public Cart(){
        items = new ArrayList<>();
    }

    public Cart(String ID, String customerID) {
        this.ID = ID;
        this.customerID = customerID;
        this.items = new ArrayList<>();
    }

    public Cart(String ID, String customerID, ArrayList<Item> items) {
        this.ID = ID;
        this.customerID = customerID;
        this.items = items;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public static String add(Cart c){
        Database db = Database.getInstance();
        String pushedID = db.isert_push(path, c);
        return pushedID;
    }

    public static void update(Cart c){
        Database db = Database.getInstance();
        db.update(path, c.getID(), c);
    }

    public static void delete(String cID){
        Database db = Database.getInstance();
        db.delete(path, cID);
    }

    public static Object view(String cID){
        Database db = Database.getInstance();
        DataSnapshot dataSnapshot = db.view(path, cID);

        if(cID != null){
            Cart c = dataSnapshot.getValue(Cart.class);
            return c;
        }
        else {
            ArrayList<Cart> c = new ArrayList<>();
            for(DataSnapshot ds : dataSnapshot.getChildren()){
                Cart cart = ds.getValue(Cart.class);
                c.add(cart);
            }
            return c;
        }
    }

    public void addItemToCart(Item i){
        items.add(i);
        Cart.update(this);
    }

    public double calculateCart(){
        double total = 0.0;
        for(int i=0;i<items.size();i++){
            total += items.get(i).getUnitPrice() * items.get(i).getQuantity();
        }
        return total;
    }

}
