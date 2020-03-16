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

public class ReceiverPaymentPageActivity extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener {

    private RecyclerView productsList;
    private RecyclerViewAdapter adapter;
    private Button collectCash;
    private Button cancel;
    private Cart cart;
    private Item item;
    private String scannedCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_payment_page);

        productsList = findViewById(R.id.recView_receiverpaymentpage_itemsbought);
        collectCash = findViewById(R.id.btn_receiverpaymentpage_collectcash);
        cancel = findViewById(R.id.btn_receiverpaymentpage_cancel);

        cart = new Cart();

        productsList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, cart);
        adapter.setClickListener(ReceiverPaymentPageActivity.this);
        productsList.setAdapter(adapter);

        scannedCode = getIntent().getStringExtra("Scanned Code");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                cart = ((Cart) Cart.view(scannedCode));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new RecyclerViewAdapter(ReceiverPaymentPageActivity.this, cart);
                        productsList.setAdapter(adapter);
                    }
                });
            }
        });
        thread.start();

        collectCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //collect money from customer and deduct the products amounts from stock
                 FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference Ref = database.getReference().child(Product.path);
                    Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                for(int i = 0; i < cart.getItems().size(); i++) {
                                    item = cart.getItems().get(i);
                                    Product product = dataSnapshot.child(item.getProductID()).getValue(Product.class);
                                    product.setQuantity(product.getQuantity() - item.getQuantity());
                                    Product.update(product);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                cart.setPaid(true);
                Cart.update(cart);
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

    @Override
    public void onItemClick(View view, int position) {

    }
}
