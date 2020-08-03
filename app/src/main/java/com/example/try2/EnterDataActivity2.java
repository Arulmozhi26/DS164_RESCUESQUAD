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
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class EnterDataActivity2 extends AppCompatActivity {

    private static final String TAG = "EnterDataActivity";

    DatabaseHelper02 mDatabaseHelper02;
    private Button btnAdd;
    private ImageButton back;
    private EditText editText1,editText2;
    private TextView textView1;
    String MyLatitude,MyLongitude;
    FusedLocationProviderClient mfusedLocationProviderClient2;

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

                                if (editText1!=null && editText2!=null) {
                                    AddData(newEntry1,newEntry2,newEntry3);
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



    }

    public void AddData(String newEntry1,String newEntry2,String newEntry3) {
        boolean insertData = mDatabaseHelper02.addData(newEntry1,newEntry2,newEntry3);

        if (insertData) {
            toastMessage("Data Successfully Inserted!");
        } else {
            toastMessage("Something went wrong");
        }
    }


    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
