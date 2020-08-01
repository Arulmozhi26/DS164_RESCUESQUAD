package com.example.try2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.material.navigation.NavigationView;
import com.skyfishjy.library.RippleBackground;
public class MapsActivityMain extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private View mapView;
    private Button btnFind;
    private RippleBackground rippleBg;
    private ImageButton imageButton,imgbtn2;
    String MyLatitude,MyLongitude;
    DatabaseHelper myDb;
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    TextView textView1,textView2;
    androidx.appcompat.widget.Toolbar toolbar;
    String id;

    private FusedLocationProviderClient mFusedLocationProviderClient,mFusedLocationProviderClient2;

    private Location mLastKnownLocation;

    private LocationCallback locationCallback;
    private final float DEFAULT_ZOOM = 18;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_main);
        myDb = new DatabaseHelper(this);

        imageButton = findViewById(R.id.sos);
        imgbtn2=findViewById(R.id.cam);
        btnFind = findViewById(R.id.btn_find);
        rippleBg = findViewById(R.id.ripple_bg);
        toolbar=findViewById(R.id.toolbar);
        nav=findViewById(R.id.navmenu);
        drawerLayout=findViewById(R.id.drawer);
        //textView1=findViewById(R.id.name001);
        //textView2=findViewById(R.id.email001);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);

        if(signInAccount != null) {
            //textView1.setText(signInAccount.getDisplayName());
            //textView2.setText(signInAccount.getEmail());
            //id = signInAccount.getId();
        }

        setSupportActionBar(toolbar);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.menumap:startActivity(new Intent(MapsActivityMain.this, MapsActivityMain.class));
                                        drawerLayout.closeDrawer(GravityCompat.START);
                                        break;
                    case R.id.edit_sosnos:startActivity(new Intent(MapsActivityMain.this,ListDataActivity.class));
                                            drawerLayout.closeDrawer(GravityCompat.START);
                                            break;
                    case R.id.form:Intent formintent = new Intent(MapsActivityMain.this, FormViewActivity.class);
                                    formintent.putExtra("userid",id);
                                    startActivity(formintent);
                                    drawerLayout.closeDrawer(GravityCompat.START);
                                    break;
                    case R.id.formedit:startActivity(new Intent(MapsActivityMain.this,FormEditActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.logout:startActivity(new Intent(MapsActivityMain.this,LogoutActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }

                return true;
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();


        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapsActivityMain.this);
        mFusedLocationProviderClient2= LocationServices.getFusedLocationProviderClient(MapsActivityMain.this);
        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng currentMarkerLocation = mMap.getCameraPosition().target;
                rippleBg.startRippleAnimation();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rippleBg.stopRippleAnimation();
                        startActivity(new Intent(MapsActivityMain.this, ListDataActivity2.class));
                    }
                }, 1000);

            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rippleBg.startRippleAnimation();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rippleBg.stopRippleAnimation();
                        int permissionCheck= ContextCompat.checkSelfPermission(MapsActivityMain.this, Manifest.permission.SEND_SMS);
                        if(permissionCheck== PackageManager.PERMISSION_GRANTED){
                            if(ActivityCompat.checkSelfPermission(MapsActivityMain.this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                                return;
                            }
                            else{
                                mFusedLocationProviderClient2.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        if(location!=null){
                                            double lat=location.getLatitude();
                                            double lng=location.getLongitude();
                                            MyLongitude=String.valueOf(lng);
                                            MyLatitude=String.valueOf(lat);
                                            MyMessage();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MapsActivityMain.this,"not sent",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                        else{
                            ActivityCompat.requestPermissions(MapsActivityMain.this,new String[]{Manifest.permission.SEND_SMS},0);

                        }

                    }
                }, 2000);

            }
        });
        imgbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rippleBg.startRippleAnimation();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rippleBg.stopRippleAnimation();
                        if(ContextCompat.checkSelfPermission(MapsActivityMain.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(MapsActivityMain.this,new String[]{Manifest.permission.CAMERA},100);
                        }
                        else {
                            Intent imgintent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(imgintent,100);
                        }

                    }
                }, 2000);

            }
        });

    }

    private void MyMessage() {
        String phonenumber;
        String message;
        message="https://www.google.com/maps/place/"+MyLatitude+","+MyLongitude;
        Cursor res=myDb.getData();
        if(res.getCount()!=0) {
            while (res.moveToNext()) {
                phonenumber="+91"+res.getString(1);
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phonenumber, null, message, null, null);

                Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();



            }
        }
        else {
            Toast.makeText(this, "Please enter Phone Numbers list ", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 180);
        }

        //check if gps is enabled or not and then request user to enable it
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(MapsActivityMain.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(MapsActivityMain.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
            }
        });

        task.addOnFailureListener(MapsActivityMain.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(MapsActivityMain.this, 51);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 51) {
            if (resultCode == RESULT_OK) {
                getDeviceLocation();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            } else {
                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if (locationResult == null) {
                                            return;
                                        }
                                        mLastKnownLocation = locationResult.getLastLocation();
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                    }
                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                            }
                        } else {
                            Toast.makeText(MapsActivityMain.this, "unable to get last location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    /*private void logout(){

    }

    }*/
}
