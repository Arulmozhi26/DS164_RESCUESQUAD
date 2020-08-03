package com.example.try2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
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

public class UserActivity extends AppCompatActivity {

    private Button Button1,Button2,Button3,Button4;
    private FusedLocationProviderClient mFusedLocationProviderClient2;
    String MyLatitude, MyLongitude;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        myDb = new DatabaseHelper(this);
        mFusedLocationProviderClient2 = LocationServices.getFusedLocationProviderClient(UserActivity.this);
        Button1=findViewById(R.id.button01);
        Button2=findViewById(R.id.button02);
        Button3=findViewById(R.id.button02);
        Button4=findViewById(R.id.button03);


        Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSos();
            }
        });
        Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserActivity.this, ListDataActivity2.class));
            }
        });
        Button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
    private void sendSos(){
        int permissionCheck = ContextCompat.checkSelfPermission(UserActivity.this, Manifest.permission.SEND_SMS);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(UserActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                        Toast.makeText(UserActivity.this, "not sent", Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else {
            ActivityCompat.requestPermissions(UserActivity.this, new String[]{Manifest.permission.SEND_SMS}, 0);
        }

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