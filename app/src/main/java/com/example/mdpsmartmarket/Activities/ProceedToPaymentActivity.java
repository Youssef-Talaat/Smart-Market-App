package com.example.mdpsmartmarket.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mdpsmartmarket.Classes.Cart;
import com.example.mdpsmartmarket.Classes.User;
import com.example.mdpsmartmarket.R;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class ProceedToPaymentActivity extends AppCompatActivity {

    private ImageView qrImage;
    private Button finish;
    private String TAG="generateQrCode";
    private String cartID;
    private Bitmap bitmap;
    private QRGEncoder qrgEncoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceed_to_payment);

        qrImage= findViewById(R.id.img_proceed_qrview);
        finish= findViewById(R.id.btn_proceed_finish);

        cartID = getIntent().getStringExtra("cartID");

        if(cartID.length()>0){
            WindowManager manager=(WindowManager)getSystemService(WINDOW_SERVICE);
            Display display=manager.getDefaultDisplay();
            Point point= new Point();
            display.getSize(point);
            int width=point.x;
            int height=point.y;
            int smallerdimension= width<height ? width:height;
            smallerdimension=smallerdimension*3/4;
            qrgEncoder= new QRGEncoder(cartID, null, QRGContents.Type.TEXT,smallerdimension);
            try{
                bitmap=qrgEncoder.encodeAsBitmap();
                qrImage.setImageBitmap(bitmap);

            } catch (WriterException e) {
                Log.v(TAG,e.toString());
            }
        }

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
