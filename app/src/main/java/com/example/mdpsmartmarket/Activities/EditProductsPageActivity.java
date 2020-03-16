package com.example.mdpsmartmarket.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mdpsmartmarket.Classes.Product;
import com.example.mdpsmartmarket.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

public class EditProductsPageActivity extends AppCompatActivity {

    private EditText barcodeNo;
    private EditText productName;
    private EditText quantity;
    private EditText price;
    private ImageView viewImage;
    private Button uploadImage;
    private Button cancel;
    private Button saveProduct;
    private Product product;
    private Uri imageUri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private boolean isFound;
    private ProgressDialog progressDialog;


    public static final String FB_STORAGE_PATH = "Products images/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_products_page);

        storageReference = FirebaseStorage.getInstance().getReference();

        barcodeNo = findViewById(R.id.edTxt_editproducts_barcode);
        productName = findViewById(R.id.edTxt_editproducts_name);
        quantity = findViewById(R.id.edTxt_editproducts_quantity);
        price = findViewById(R.id.edTxt_editproducts_price);
        viewImage = findViewById(R.id.imgView_editproducts_viewimage);
        uploadImage = findViewById(R.id.btn_editproducts_uploadimage);
        saveProduct = findViewById(R.id.btn_editproducts_saveproduct);
        cancel = findViewById(R.id.btn_editproducts_cancel);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                product = (Product) Product.view(getIntent().getStringExtra("ProductID"));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        barcodeNo.setText(product.getBarcodeNumber());
                        productName.setText(product.getName());
                        quantity.setText(""+product.getQuantity());
                        price.setText(""+product.getPrice());
                        Picasso.get().load(product.getImagePath()).into(viewImage);
                        barcodeNo.setEnabled(false);
                    }
                });
            }
        });
        thread.start();


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //destroy activity and go back
                finish();
            }
        });

        saveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFound = false;
                //upload image for the product
                if(productName.getText().toString().length() > 0 && quantity.getText().toString().length() > 0
                        && price.getText().toString().length() > 0 && barcodeNo.getText().toString().length() > 0)
                {

                    ///////////////////////////////////////////////////////////////////////////////////
                    if(imageUri != null)
                    {

                        progressDialog = new ProgressDialog(EditProductsPageActivity.this);
                        progressDialog.setTitle("Uploading image");
                        progressDialog.show();


                            final StorageReference ref = storageReference.child(FB_STORAGE_PATH + System.currentTimeMillis()+"."+getImageUri(imageUri));


                            ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    progressDialog.dismiss();
                                    Toast.makeText(EditProductsPageActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();

                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            /////////////////////////////////////////////////////////////////////////////////
                                            product.setBarcodeNumber(barcodeNo.getText().toString());
                                            product.setName(productName.getText().toString());
                                            product.setPrice(Double.parseDouble(price.getText().toString()));
                                            product.setQuantity(Integer.parseInt(quantity.getText().toString()));
                                            product.setImagePath(uri.toString());
                                            Product.update(product);
                                            finish();
                                            ///////////////////////////////////////////////////////////////////////////////////
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    progressDialog.dismiss();
                                    Toast.makeText(EditProductsPageActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                    progressDialog.setMessage("Uploaded "+ (int)progress + " %");
                                }
                            });

                        ///////////////////////////////////////////////////////////////////////////////////
                    }
                    else{
                        product.setBarcodeNumber(barcodeNo.getText().toString());
                        product.setName(productName.getText().toString());
                        product.setPrice(Double.parseDouble(price.getText().toString()));
                        product.setQuantity(Integer.parseInt(quantity.getText().toString()));
                        product.setImagePath(product.getImagePath());
                        Product.update(product);
                        finish();
                    }
                    ///////////////////////////////////////////////////////////////////////////////////
                }
                else{
                    Toast.makeText(EditProductsPageActivity.this, "Please fill all the required feilds",Toast.LENGTH_SHORT).show();
                }
            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select image"),REQUEST_CODE);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            imageUri = data.getData();
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                viewImage.setImageBitmap(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getImageUri (Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

}
