package com.example.mdpsmartmarket.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import com.example.mdpsmartmarket.Classes.ManageProductsRecyclerViewAdapter;
import com.example.mdpsmartmarket.Classes.Product;
import com.example.mdpsmartmarket.Classes.RecyclerViewAdapter;
import com.example.mdpsmartmarket.R;

import java.util.ArrayList;

public class ManageProductsPageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ManageProductsRecyclerViewAdapter adapter;
    private Button addProduct;
    private Button cancel;

    private ArrayList<Product> productArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_products_page);

        recyclerView = findViewById(R.id.recView_manageproducts_productsrecyclerview);
        addProduct = findViewById(R.id.btn_manageproducts_addproduct);
        cancel = findViewById(R.id.btn_manageproducts_cancel);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                productArrayList = ((ArrayList<Product>) Product.view(null));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setLayoutManager(new LinearLayoutManager(ManageProductsPageActivity.this));
                        adapter = new ManageProductsRecyclerViewAdapter(ManageProductsPageActivity.this, productArrayList);
                        //adapter.setClickListener(this);
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        });
        thread.start();


        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageProductsPageActivity.this, AddProductsPageActivity.class));
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        productArrayList = ((ArrayList<Product>) Product.view(null));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.setLayoutManager(new LinearLayoutManager(ManageProductsPageActivity.this));
                                adapter = new ManageProductsRecyclerViewAdapter(ManageProductsPageActivity.this, productArrayList);
                                //adapter.setClickListener(this);
                                recyclerView.setAdapter(adapter);
                            }
                        });
                    }
                });
                thread.start();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //destroy activity and go back
                finish();
            }
        });
    }
}
