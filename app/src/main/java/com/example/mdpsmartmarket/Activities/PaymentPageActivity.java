package com.example.mdpsmartmarket.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mdpsmartmarket.Classes.Cart;
import com.example.mdpsmartmarket.Classes.ManageUserRecyclerViewAdapter;
import com.example.mdpsmartmarket.Classes.RecyclerViewAdapter;
import com.example.mdpsmartmarket.R;

public class PaymentPageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private Button proceed;
    private Button cancel;
    private TextView amount;
    private Cart c;
    private double totalAmount = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);

        recyclerView = findViewById(R.id.recView_payment_boughtitems);
        cancel = findViewById(R.id.btn_paymentpage_cancel);
        amount= findViewById(R.id.txt_payment_totalamount);
        proceed= findViewById(R.id.btn_payment_proceed);


        c = (Cart) getIntent().getSerializableExtra("Cart");
        totalAmount = c.calculateCart();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setLayoutManager(new LinearLayoutManager(PaymentPageActivity.this));
                        adapter = new RecyclerViewAdapter(PaymentPageActivity.this, c);
                        //adapter.setClickListener(ManageUsersActivity.this);
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        });
        thread.start();

        amount.setText("Total Amount: " + totalAmount);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //destroy activity and go back
                finish();
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PaymentPageActivity.this, ProceedToPaymentActivity.class);
                i.putExtra("cartID", c.getID());
                startActivity(i);
                finish();

            }
        });

    }
}
