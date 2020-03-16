package com.example.mdpsmartmarket.Classes;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class Product {

    private String barcodeNumber;
    private String name;
    private int quantity;
    private double price;
    private String imagePath;
    public static final String path = "Products";

    ///////////////////////////////////////////////
    public static void add(Product p){
        Database db = Database.getInstance();
        db.isert_setValue("Products",p.getBarcodeNumber(), p);
    }

    public static void update(Product p){
        Database db = Database.getInstance();
        db.isert_setValue("Products",p.getBarcodeNumber(), p);
    }

    public static void delete(String pID){
        Database db = Database.getInstance();
        db.delete("Products", pID);
    }

    public static Object view(String pID){
        Database db = Database.getInstance();
        DataSnapshot dataSnapshot = db.view(path, pID);

        if(pID != null){
            Product p = dataSnapshot.getValue(Product.class);
            return p;
        }
        else {
            ArrayList<Product> p = new ArrayList<>();
            for(DataSnapshot ds : dataSnapshot.getChildren()){
                Product product = ds.getValue(Product.class);
                p.add(product);
            }
            return p;
        }
    }
///////////////////////////////////////////////


    public String getBarcodeNumber() {
        return barcodeNumber;
    }

    public void setBarcodeNumber(String barcodeNumber) {
        this.barcodeNumber = barcodeNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
