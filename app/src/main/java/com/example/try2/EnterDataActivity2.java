package com.example.try2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class EnterDataActivity2 extends AppCompatActivity {

    private static final String TAG = "EnterDataActivity";

    DatabaseHelper02 mDatabaseHelper02;
    private Button btnAdd;
    private ImageButton back;
    private EditText editText1,editText2;
    private TextView textView1;
    String MyLatitude,MyLongitude;
    String latlongupdate,store;
    List<String> storelatlng =new ArrayList<>();
    FusedLocationProviderClient mfusedLocationProviderClient2, mLocationProviderClient;
    LocationCallback  mLocationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_data2);
        editText1 = (EditText) findViewById(R.id.edittxt1);
        editText2 = (EditText) findViewById(R.id.edittxt2);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        back=(ImageButton) findViewById(R.id.imageButton);
        textView1=(TextView) findViewById(R.id.latlng);
        mDatabaseHelper02 = new DatabaseHelper02(this);
        mfusedLocationProviderClient2= LocationServices.getFusedLocationProviderClient(EnterDataActivity2.this);
        mLocationProviderClient= LocationServices.getFusedLocationProviderClient(EnterDataActivity2.this);



        if(ActivityCompat.checkSelfPermission(EnterDataActivity2.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            return;
        }
        else{
            mfusedLocationProviderClient2.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location!=null){
                        double lat=location.getLatitude();
                        double lng=location.getLongitude();
                        MyLongitude=String.valueOf(lng);
                        MyLatitude=String.valueOf(lat);
                        textView1.setText(MyLatitude+","+MyLongitude);
                        btnAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                String newEntry1 = editText1.getText().toString();
                                String newEntry2 = editText2.getText().toString();
                                String newEntry3 =MyLatitude+","+MyLongitude;
                                String newEntry4=store;

                                if (editText1!=null && editText2!=null) {
                                    AddData(newEntry1,newEntry2,newEntry3,newEntry4);
                                    editText1.setText("");
                                    editText2.setText("");
                                    Intent intent = new Intent(EnterDataActivity2.this, ListDataActivity2.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    toastMessage("You must put something in the text field!");
                                }
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EnterDataActivity2.this,"not sent",Toast.LENGTH_LONG).show();
                }
            });
        }



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterDataActivity2.this, ListDataActivity2.class);
                startActivity(intent);
                finish();
            }
        });


        mLocationProviderClient=LocationServices.getFusedLocationProviderClient(EnterDataActivity2.this);
        mLocationCallback=new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(locationResult==null){
                    return;
                }
                Location location2=locationResult.getLastLocation();
                latlongupdate=location2.getLatitude()+","+location2.getLongitude();
                store=store+","+latlongupdate;
            }
        };
        getLocationUpdates();
    }

    public void AddData(String newEntry1, String newEntry2, String newEntry3, String newEntry4) {
        boolean insertData = mDatabaseHelper02.addData(newEntry1,newEntry2,newEntry3,newEntry4);

        if (insertData) {
            toastMessage("Data Successfully Inserted!");
        } else {
            toastMessage("Something went wrong");
        }
    }


    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
    private void getLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback,null );

    }
}
