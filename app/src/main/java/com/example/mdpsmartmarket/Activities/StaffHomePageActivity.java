package com.example.mdpsmartmarket.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mdpsmartmarket.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StaffHomePageActivity extends AppCompatActivity {
    private Button logOut;
    private Button manageProducts;
    private Button acceptPayment;
    private Button refundProducts;
    private Button editProfile;
    private DatabaseReference Ref;
    private FirebaseDatabase database;
    private int backpressed=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home_page);

        manageProducts = findViewById(R.id.btn_staffhomepage_manageproducts);
        acceptPayment = findViewById(R.id.btn_staffhomepage_acceptpayment);
        refundProducts = findViewById(R.id.btn_staffhomepage_refund);
        editProfile = findViewById(R.id.btn_staffhomepage_editprofile);
        logOut = findViewById(R.id.btn_staffhomepage_logout);


        database = FirebaseDatabase.getInstance();
        Ref = database.getReference();

        manageProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StaffHomePageActivity.this, ManageProductsPageActivity.class));
            }
        });

        acceptPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StaffHomePageActivity.this, AcceptPaymentPageActivity.class));
            }
        });

        refundProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StaffHomePageActivity.this, RefundProductsPageActivity.class));
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StaffHomePageActivity.this, EditProfileActivity.class));
                finish();
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //destroy activity and go back
                finish();
            }
        });

    }
    @Override
    public void onBackPressed(){
        if(backpressed>=1){
            Intent exit = new Intent(Intent.ACTION_MAIN);
            exit.addCategory(Intent.CATEGORY_HOME);
            exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(exit);
            finish();
            System.exit(0);}

        else{
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            backpressed++;
        }}
}
