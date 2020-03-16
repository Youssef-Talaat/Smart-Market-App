package com.example.mdpsmartmarket.Classes;

import java.util.ArrayList;

public class Customer extends User {
    private ArrayList<Receipt> receipts;

    public ArrayList<Receipt> getReceipts() {
        return receipts;
    }

    public void setReceipts(ArrayList<Receipt> receipts) {
        this.receipts = receipts;
    }

    public ArrayList<Receipt> showHistory(){
        receipts = ((ArrayList<Receipt>) Receipt.view(null));
        return receipts;
    }

    public Cart startShopping(){
        Cart c = new Cart();
        c.setCustomerID(getLoggedUser().getID());
        c.setID(Cart.add(c));
        return c;
    }

    public void continueShopping(){

    }

    public void scanItem(Cart c, String productID){
        Item i = new Item(productID);
        c.addItemToCart(i);
        Cart.update(c);
    }

    public void checkOut(){

    }

    public void cancel(){

    }

    public void makePayment(){

    }

}
