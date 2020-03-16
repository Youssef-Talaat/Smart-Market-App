package com.example.mdpsmartmarket.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import android.widget.Toast;


import com.example.mdpsmartmarket.Classes.Cart;
import com.example.mdpsmartmarket.Classes.Customer;
import com.example.mdpsmartmarket.Classes.Item;
import com.example.mdpsmartmarket.Classes.Product;
import com.example.mdpsmartmarket.Classes.RecyclerViewAdapter;
import com.example.mdpsmartmarket.Portrait;
import com.example.mdpsmartmarket.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class StartShoppingPageActivity extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener {
    private RecyclerView recyclerView;
    private Button addItem;
    private Button cancel;
    private Button checkOut;
    private Cart c;
    private String scannedProduct = "";
    private RecyclerViewAdapter adapter;
    private DatabaseReference refView;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_shopping_page);

        Customer cus = new Customer();
        c = cus.startShopping();

        recyclerView = findViewById(R.id.recView_startshopping_scanneditems);
        addItem = findViewById(R.id.btn_startshopping_additem);
        cancel = findViewById(R.id.btn_startshopping_cancel);
        checkOut = findViewById(R.id.btn_startshopping_checkout);


        // set up the posts RecyclerView
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new RecyclerViewAdapter(this, c);
            adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //destroy activity and go back
                Cart.delete(c.getID());
                finish();
            }
        });

        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StartShoppingPageActivity.this, PaymentPageActivity.class);
                i.putExtra("Cart", c);
                startActivity(i);
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(StartShoppingPageActivity.this, "size: "+c.getItems().size(),Toast.LENGTH_LONG).show();
                scanow();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(this);
                alertdialogbuilder.setMessage(result.getContents() + "\n\nScan Again ?");
                scannedProduct = result.getContents();
                alertdialogbuilder.setTitle("Result Scanned");
                alertdialogbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scanow();
                    }
                });
                alertdialogbuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ///////////////////////////////////////////////////////////
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Product product = ((Product) Product.view(scannedProduct));
                                boolean alreadyExists = false;
                                for(int i = 0; i < c.getItems().size(); i++){
                                    if(c.getItems().get(i).getProductID().equals(product.getBarcodeNumber())){
                                        c.getItems().get(i).setQuantity(c.getItems().get(i).getQuantity() + 1);
                                        Cart.update(c);
                                        alreadyExists = true;
                                    }
                                }
                                if(!alreadyExists) {
                                    Item item = new Item();
                                    item.setUnitPrice(product.getPrice());
                                    item.setProductID(product.getBarcodeNumber());
                                    item.setQuantity(1);
                                    c.addItemToCart(item);
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter = new RecyclerViewAdapter(StartShoppingPageActivity.this, c);
                                        recyclerView.setAdapter(adapter);
                                    }
                                });
                            }
                        });
                        thread.start();
                        ///////////////////////////////////////////////////////////
                        //finish();
                    }
                });
                AlertDialog alertDialog = alertdialogbuilder.create();
                alertDialog.show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void scanow() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(Portrait.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan Your Barcode");
        integrator.initiateScan();
    }
}


