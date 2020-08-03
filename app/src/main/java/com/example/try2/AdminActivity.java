package com.example.try2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class AdminActivity extends AppCompatActivity {

    String MyLatitude, MyLongitude;
    private FusedLocationProviderClient mFusedLocationProviderClient2;
    private Button Button1,Button2,Button3,Button4,Button5;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mFusedLocationProviderClient2 = LocationServices.getFusedLocationProviderClient(AdminActivity.this);
        myDb = new DatabaseHelper(this);

        Button1=findViewById(R.id.button1);
        Button2=findViewById(R.id.button2);
        Button3=findViewById(R.id.button3);
        Button4=findViewById(R.id.button4);
        Button5=findViewById(R.id.button5);

        Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, ListDataActivity2.class));
            }
        });

        Button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSos();
            }

        });

        Button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(AdminActivity.this,MapsActivityMain.class));
            }
        });

    }

    private void sendSos(){
        int permissionCheck = ContextCompat.checkSelfPermission(AdminActivity.this, Manifest.permission.SEND_SMS);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(AdminActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            } else {
                mFusedLocationProviderClient2.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double lat = location.getLatitude();
                            double lng = location.getLongitude();
                            MyLongitude = String.valueOf(lng);
                            MyLatitude = String.valueOf(lat);
                            MyMessage();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminActivity.this, "not sent", Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else {
            ActivityCompat.requestPermissions(AdminActivity.this, new String[]{Manifest.permission.SEND_SMS}, 0);
        }

    }
    private void call911(){
        String phone="tel:911";
        Intent intent=new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(phone));
        startActivity(intent);
    }
    private void MyMessage() {
        String phonenumber;
        String message;
        message = "https://www.google.com/maps/place/" + MyLatitude + "," + MyLongitude;
        Cursor res = myDb.getData();
        if (res.getCount() != 0) {
            while (res.moveToNext()) {
                phonenumber = "+91" + res.getString(1);
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phonenumber, null, message, null, null);
                Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();
            }
        } else {
            phonenumber = "+91 9790842921";
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phonenumber, null, message, null, null);
            Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();

        }
    }
}
