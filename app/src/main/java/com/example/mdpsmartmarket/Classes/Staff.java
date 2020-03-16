package com.example.mdpsmartmarket.Classes;

import java.util.ArrayList;

public class Staff extends User {

    public void addProduct(Product p){
        Product.add(p);
    }

    public  void editProduct(Product p){
        Product.update(p);
    }

    public void deleteProduct(String productID){
        Product.delete(productID);
    }

    public ArrayList<Product> viewProduct(){
        ArrayList<Product> productsList = ((ArrayList<Product>) Product.view(null));
        return productsList;
    }

    public double calculateCart(String cartID){
        Cart c = (Cart) Cart.view(cartID);
        double total = 0.0;
        for(int i=0;i<c.getItems().size();i++){
            total += c.getItems().get(i).getUnitPrice() * c.getItems().get(i).getQuantity();
        }
        return total;
    }

    public double recievePayment(double requiredAmount, double paidAmount, Receipt receipt){
        if(requiredAmount <= paidAmount){
            return paidAmount - requiredAmount;
        }
        else
            return -1;
    }

    public void deductItemsFromStock(String cartID){
        Cart c = (Cart) Cart.view(cartID);
        for(int i=0;i<c.getItems().size();i++){
            //Deduct bought items amounts from stock
            /*ArrayList<Product> boughtProduct = Product.view(c.getItems().get(i).getProductID());
            boughtProduct.get(0).setQuantity(boughtProduct.get(0).getQuantity() - c.getItems().get(i).getQuantity());
            Product.update(boughtProduct.get(0));*/
        }
    }

    public double refundItems(ArrayList<Item> items){
        double totalRefund = 0.0;
        for (int i=0;i<items.size();i++){
            totalRefund += items.get(i).getUnitPrice() * items.get(i).getQuantity();

            //Return the refunded products to stock
            /*ArrayList<Product> refundedProduct = Product.view(items.get(i).getProductID());
            refundedProduct.get(0).setQuantity(refundedProduct.get(0).getQuantity() + items.get(i).getQuantity());
            Product.update(refundedProduct.get(0));*/
        }
        return totalRefund;
    }

}
