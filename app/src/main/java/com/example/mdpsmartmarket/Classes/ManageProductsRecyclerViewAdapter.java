package com.example.mdpsmartmarket.Classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mdpsmartmarket.Activities.EditProductsPageActivity;
import com.example.mdpsmartmarket.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ManageProductsRecyclerViewAdapter  extends RecyclerView.Adapter<ManageProductsRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Product> products;
    private LayoutInflater mInflater;
    private Product currentProduct;
    private ItemClickListener mClickListener;

    public ManageProductsRecyclerViewAdapter(Context context, ArrayList<Product> products) {
        this.mInflater = LayoutInflater.from(context);
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.product_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        currentProduct = products.get(position);
        holder.productName.setText(currentProduct.getName());
        holder.price.setText("Price: " + currentProduct.getPrice() + " EGP");
        holder.quantity.setText("Quantity: " + currentProduct.getQuantity());
        Picasso.get().load(currentProduct.getImagePath()).into(holder.productImage);
        holder.increment.setVisibility(View.INVISIBLE);
        holder.decrement.setVisibility(View.INVISIBLE);
        holder.editProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mInflater.getContext(), EditProductsPageActivity.class);
                intent.putExtra("ProductID", products.get(position).getBarcodeNumber());
                mInflater.getContext().startActivity(intent);
                /*Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        products = (ArrayList<Product>) Product.view(null);
                    }
                });
                thread.start();*/
            }
        });


    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView productName;
        TextView price;
        TextView quantity;
        ImageView productImage;
        Button increment;
        Button decrement;
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
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public Product getItem(int id) {
        return products.get(id);
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
