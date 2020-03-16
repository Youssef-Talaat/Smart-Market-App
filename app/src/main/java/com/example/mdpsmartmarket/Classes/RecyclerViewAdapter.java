package com.example.mdpsmartmarket.Classes;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mdpsmartmarket.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter  extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Cart cart, refundedCart;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Product product;
    private Item currentProduct;

    // data is passed into the constructor
    public RecyclerViewAdapter(Context context, Cart cart) {
        this.mInflater = LayoutInflater.from(context);
        this.cart = cart;
        this.refundedCart = null;
    }

    public RecyclerViewAdapter(Context context, Cart cart, Cart cart2) {
        this.mInflater = LayoutInflater.from(context);
        this.cart = cart;
        this.refundedCart = cart2;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.product_record, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if(refundedCart == null) {
            currentProduct = cart.getItems().get(position);
        }
        else {
            currentProduct = refundedCart.getItems().get(position);
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Product p = (Product) Product.view(currentProduct.getProductID());
                ((Activity) mInflater.getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        holder.productName.setText(p.getName());
                        holder.price.setText("Price: " + currentProduct.getUnitPrice() + " EGP");
                        holder.quantity.setText("Quantity: " + currentProduct.getQuantity());
                        //holder.productImage.setImageURI(Uri.parse(p.getImagePath()));
                        Picasso.get().load(p.getImagePath()).into(holder.productImage);
                        //Picasso.with(mInflater.getContext()).load(p.getImagePath()).into(holder.productImage);
                        //holder.productImage.setBackgroundResource(R.drawable.logo);
                        holder.editProduct.setVisibility(View.INVISIBLE);
                    }
                });

            }
        });
        thread.start();


        holder.increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //increment quantity
                if(refundedCart == null){
                    cart.getItems().get(position).setQuantity(cart.getItems().get(position).getQuantity() + 1);
                    Cart.update(cart);
                    holder.quantity.setText("Quantity: " + cart.getItems().get(position).getQuantity());
                }
                else if(refundedCart.getItems().get(position).getQuantity() + 1 <= cart.getItems().get(position).getQuantity()){
                    refundedCart.getItems().get(position).setQuantity(refundedCart.getItems().get(position).getQuantity() + 1);
                    holder.quantity.setText("Quantity: " + refundedCart.getItems().get(position).getQuantity());
                }

            }
        });
        holder.decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cart.getItems().get(position).getQuantity() > 0) {
                    if(refundedCart == null) {
                        cart.getItems().get(position).setQuantity(cart.getItems().get(position).getQuantity() - 1);
                        Cart.update(cart);
                        holder.quantity.setText("Quantity: " + cart.getItems().get(position).getQuantity());
                    }
                    else if(refundedCart.getItems().get(position).getQuantity() > 0){
                        refundedCart.getItems().get(position).setQuantity(refundedCart.getItems().get(position).getQuantity() - 1);
                        holder.quantity.setText("Quantity: " + refundedCart.getItems().get(position).getQuantity());
                    }
                }
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return cart.getItems().size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView productName;
        TextView price;
        TextView quantity;
        Button increment;
        Button decrement;
        ImageView productImage;
        Button editProduct;

        ViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.txt_productrecord_productname);
            price = itemView.findViewById(R.id.txt_productrecord_price);
            productImage = itemView.findViewById(R.id.img_productrecord_productimage);
            quantity = itemView.findViewById(R.id.txt_productrecord_quantity);
            increment = itemView.findViewById(R.id.btn_productrecord_addquantity);
            decrement = itemView.findViewById(R.id.btn_productrecord_removequantity);
            editProduct = itemView.findViewById(R.id.btn_productrecord_editproduct);
            //itemView.setOnClickListener(this);
            increment.setOnClickListener(this);
            decrement.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public Item getItem(int id) {
        return cart.getItems().get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


}
