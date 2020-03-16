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
import com.example.mdpsmartmarket.Classes.HistoryRecyclerViewAdapter;
import com.example.mdpsmartmarket.Classes.Product;
import com.example.mdpsmartmarket.Classes.RecyclerViewAdapter;
import com.example.mdpsmartmarket.Classes.User;
import com.example.mdpsmartmarket.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryPageActivity extends AppCompatActivity implements HistoryRecyclerViewAdapter.ItemClickListener {
    private RecyclerView recyclerView;
    private Button cancel;
    private HistoryRecyclerViewAdapter adapter;
    private ArrayList<Cart> carts;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_page);

        recyclerView = findViewById(R.id.recView_history_receipts);
        cancel = findViewById(R.id.btn_history_cancel);

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                carts = (ArrayList<Cart>) Cart.view(null);
                for(int i = 0 ; i < carts.size() ; i++){
                    if(!carts.get(i).getCustomerID().equals(User.getLoggedUser().getID())){
                        carts.remove(i);
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setLayoutManager(new LinearLayoutManager(HistoryPageActivity.this));
                        adapter = new HistoryRecyclerViewAdapter(HistoryPageActivity.this, carts);
                        adapter.setClickListener(HistoryPageActivity.this);
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        });
        thread.start();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        //Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}
