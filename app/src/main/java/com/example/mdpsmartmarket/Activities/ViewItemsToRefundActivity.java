package com.example.mdpsmartmarket.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mdpsmartmarket.Classes.Cart;
import com.example.mdpsmartmarket.Classes.Item;
import com.example.mdpsmartmarket.Classes.Product;
import com.example.mdpsmartmarket.Classes.RecyclerViewAdapter;
import com.example.mdpsmartmarket.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewItemsToRefundActivity extends AppCompatActivity {

    private RecyclerView productsList;
    private Button refundProducts;
    private Button cancel;
    private Cart cart;
    private RecyclerViewAdapter adapter;
    private ArrayList<Item> refundedItems;
    private Cart refundedCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items_to_refund);

        productsList = findViewById(R.id.recView_viewitemstorefund_itemstorefund);
        refundProducts = findViewById(R.id.btn_viewitemstorefund_refundproducts);
        cancel = findViewById(R.id.btn_viewitemstorefund_cancel);

        productsList.setLayoutManager(new LinearLayoutManager(this));
        productsList.setAdapter(adapter);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String cartID = getIntent().getStringExtra("Scanned Code");
                cart = ((Cart) Cart.view(cartID));
                refundedCart = ((Cart) Cart.view(cartID));

                for(int i = 0 ; i < refundedCart.getItems().size() ; i++){
                    refundedCart.getItems().get(i).setQuantity(0);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ViewItemsToRefundActivity.this, "ddd: "+cart.getItems().get(0).getQuantity(), Toast.LENGTH_SHORT).show();
                        adapter = new RecyclerViewAdapter(ViewItemsToRefundActivity.this, cart, refundedCart);
                        productsList.setAdapter(adapter);
                    }
                });
            }
        });
        thread.start();

        refundProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(int i = 0; i < refundedCart.getItems().size(); i++) {
                            cart.getItems().get(i).setQuantity(cart.getItems().get(i).getQuantity() - refundedCart.getItems().get(i).getQuantity());

                            Product product = (Product) Product.view(cart.getItems().get(i).getProductID());
                            product.setQuantity(product.getQuantity() + refundedCart.getItems().get(i).getQuantity());
                            Product.update(product);

                            if(cart.getItems().get(i).getQuantity() == 0){
                                cart.getItems().remove(i);
                                refundedCart.getItems().remove(i);
                            }
                            Cart.update(cart);
                        }
                    }
                });
                thread1.start();
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
