package com.example.try2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
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
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.material.navigation.NavigationView;
import com.shreyaspatil.MaterialDialog.MaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;
import java.util.List;

public class MapsActivityMain extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private View mapView;
    private Button btnFind;
    private RippleBackground rippleBg;
    private ImageButton  imgbtn2;
    private MaterialDialog mSimpleDialog;

    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    androidx.appcompat.widget.Toolbar toolbar;
    String id,latlongupdate;

    List<String> storelatlng =new ArrayList<>();
    List<LatLng> latLngList = new ArrayList<>();
    List<LatLng> latLngList2 = new ArrayList<>();


    private FusedLocationProviderClient mFusedLocationProviderClient, mLocationProviderClient;
    private Location mLastKnownLocation;
    private LocationCallback locationCallback, mLocationCallback;
    private final float DEFAULT_ZOOM = 18;

    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper, geofenceHelper2;

    private float GEOFENCE_RADIUS = 1000, GEOFENCE_RADIUS2 = 4000;
    private String GEOFENCE_ID = "G1", GEOFENCE_ID2 = "G2";
    public LatLng latLng = new LatLng(12.4240, 92.7669);
    private LatLng latLng2 = new LatLng(12.392340, 92.771720);

    private LatLng latpoly1 = new LatLng(12.910765, 80.150219), latpoly2 = new LatLng(12.909175, 80.147792), latpoly3 = new LatLng(12.907523, 80.151622), latpoly4 = new LatLng(12.910221, 80.153907);
    private LatLng l1 = new LatLng(12.3558682, 92.7647959), l2 = new LatLng(12.3682262, 92.8009330), l3 = new LatLng(12.3936828, 92.8106905), l4 = new LatLng(12.4141389, 92.8027093), l5 = new LatLng(12.4294877, 92.7794508), l6 = new LatLng(12.4253098, 92.7515344), l7 = new LatLng(12.3919420, 92.7321021);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_main);

        imgbtn2 = findViewById(R.id.cam);
        btnFind = findViewById(R.id.btn_find);
        rippleBg = findViewById(R.id.ripple_bg);
        toolbar = findViewById(R.id.toolbar);
        nav = findViewById(R.id.navmenu);
        drawerLayout = findViewById(R.id.drawer);
        geofencingClient = LocationServices.getGeofencingClient(this);
        geofenceHelper = new GeofenceHelper(this);
        geofenceHelper2 = new GeofenceHelper(this);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(MapsActivityMain.this);


        latLngList.add(latpoly1);
        latLngList.add(latpoly2);
        latLngList.add(latpoly3);
        latLngList.add(latpoly4);

        latLngList2.add(l1);
        latLngList2.add(l2);
        latLngList2.add(l3);
        latLngList2.add(l4);
        latLngList2.add(l5);
        latLngList2.add(l6);
        latLngList2.add(l7);


        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menumap:
                        startActivity(new Intent(MapsActivityMain.this, MapsActivityMain.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.edit_sosnos:
                        startActivity(new Intent(MapsActivityMain.this, ListDataActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.form:
                        Intent formintent = new Intent(MapsActivityMain.this, FormViewActivity.class);
                        formintent.putExtra("userid", id);
                        startActivity(formintent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.formedit:
                        startActivity(new Intent(MapsActivityMain.this, FormEditActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.logout:dialoguelogout();
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

                    }
                }, 1000);

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
                        if (ContextCompat.checkSelfPermission(MapsActivityMain.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MapsActivityMain.this, new String[]{Manifest.permission.CAMERA}, 100);
                        } else {
                            Intent imgintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(imgintent, 100);
                        }
                    }
                }, 2000);
            }
        });

        mLocationProviderClient=LocationServices.getFusedLocationProviderClient(MapsActivityMain.this);
        mLocationCallback=new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(locationResult==null){
                        return;
                }
                Location location2=locationResult.getLastLocation();
                latlongupdate=location2.getLatitude()+","+location2.getLongitude();
                storelatlng.add(latlongupdate);
                Toast.makeText(MapsActivityMain.this,location2.getLatitude()+","+location2.getLongitude(),Toast.LENGTH_SHORT).show();
            }
        };
        getLocationUpdates();


    }

    private void dialoguelogout() {
        mSimpleDialog = new MaterialDialog.Builder(this)
                .setTitle("Logout?")
                .setMessage("Are you sure want to LOGOUT?")
                .setCancelable(false)
                .setPositiveButton("Logout", R.drawable.ic_delete, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MapsActivityMain.this, LoginActivity.class));
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Cancel", R.drawable.ic_close, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        Toast.makeText(getApplicationContext(), "Cancelled!", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                })
                .build();
    }

    private void MyMessage() {

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

        polygon1();
        polygon2();
        //handleMapLongClick(latLng);
        handleMapLongClick2(latLng2);

    }


    /*private void handleMapLongClick(LatLng latLng) {
        addCircle(latLng, GEOFENCE_RADIUS);
        addGeofence(latLng, GEOFENCE_RADIUS);
    }*/
    private void handleMapLongClick2(LatLng latLng) {
        addCircle2(latLng, GEOFENCE_RADIUS2);
        addGeofence2(latLng, GEOFENCE_RADIUS2);

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

    private void addGeofence(LatLng latLng, float radius) {

        Geofence geofence = geofenceHelper.getGeofence(GEOFENCE_ID, latLng, radius, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT);
        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);
        PendingIntent pendingIntent = geofenceHelper.getPendingIntent();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = geofenceHelper.getErrorString(e);
                    }
                });
    }

    private void addGeofence2(LatLng latLng2, float radius2) {

        Geofence geofence2 = geofenceHelper2.getGeofence(GEOFENCE_ID2, latLng2, radius2, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT);
        GeofencingRequest geofencingRequest = geofenceHelper2.getGeofencingRequest(geofence2);
        PendingIntent pendingIntent = geofenceHelper2.getPendingIntent();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = geofenceHelper2.getErrorString(e);

                    }
                });
    }


    /*private void addCircle(LatLng latLng, float radius) {
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255, 255, 0,0));
        circleOptions.fillColor(Color.argb(64, 255, 0,0));
        circleOptions.strokeWidth(4);
        mMap.addCircle(circleOptions);
    }*/
    private void addCircle2(LatLng latLng2, float radius2) {
        CircleOptions circleOptions2 = new CircleOptions();
        circleOptions2.center(latLng2);
        circleOptions2.radius(radius2);
        circleOptions2.strokeColor(Color.argb(0, 0, 0, 0));
        circleOptions2.fillColor(Color.argb(0, 0, 0, 0));
        circleOptions2.strokeWidth(6);
        mMap.addCircle(circleOptions2);
    }

    private void polygon1() {
        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.addAll(latLngList);
        polygonOptions.strokeColor(Color.argb(255, 0, 255, 0));
        polygonOptions.fillColor(Color.argb(64, 0, 255, 0));
        polygonOptions.strokeWidth(6);
        mMap.addPolygon(polygonOptions);

    }

    private void polygon2() {
        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.addAll(latLngList2);
        polygonOptions.strokeColor(Color.argb(255, 0, 255, 0));
        polygonOptions.fillColor(Color.argb(30, 0, 255, 0));
        polygonOptions.strokeWidth(6);
        mMap.addPolygon(polygonOptions);

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
