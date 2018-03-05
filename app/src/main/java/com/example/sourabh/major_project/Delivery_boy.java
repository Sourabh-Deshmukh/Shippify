package com.example.sourabh.major_project;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Delivery_boy extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
//private MapView mapView;
private GoogleMap mMap;
    double latitude;
    double longitude;
    private GoogleMap map;
    Location location;
    private FloatingActionButton fab;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private DatabaseReference mDatabase;
    FirebaseDatabase database;
    static DatabaseReference myRef;
    private GeoDataClient mGeoDataClient;
    PlaceDetectionClient mPlaceDetectionClient;
    FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private static final int DEFAULT_ZOOM = 15;
    private Location mLastKnownLocation;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final String TAG = "@@@@@@@@@@@@@@@@@@";
    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private CameraPosition mCameraPosition;
    SupportMapFragment mapFragment;
    double currentLatitude;
    double currentLongitude;
    String[] version={"Mini","PickUp","Tipper","Truck","BigTruck"};
    int[] images={R.mipmap.mini,R.mipmap.pickup,R.mipmap.tipper,R.mipmap.truck,R.mipmap.bigtruck};
    Spinner spin;
    TextView t;
    LatLng latLng;
    public static String personId;
    private String MyPREFERENCES="preference";
    static final int[] i = {0};
     ProgressBar progressBar;
        String s="y";
        boolean b=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_boy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapViews);
        mapFragment.getMapAsync(this);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        progressBar=(ProgressBar)findViewById(R.id.progressBar2) ;


        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        database = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        myRef = database.getReferenceFromUrl("https://majorproject-e14b6.firebaseio.com/");
        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            personId = acct.getId();
        }
        new performBackgroundTask().execute();
        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        final Switch s=(Switch)findViewById(R.id.tog_btn);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    myRef.child("activeDrivers").child(personId).
                            setValue(latLng);
                }
            }
        });





      /*  spin=(Spinner)findViewById(R.id.spineer1);


        MyAdapter ad=new MyAdapter(this,version,images);
        spin.setAdapter(ad);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent,View view,int position,long id){
                Toast.makeText(getApplicationContext(),version[position],Toast.LENGTH_SHORT).show();
                int x=position+1;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
*/
        /*SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();

        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String str = (shared.getString(personId, ""));

        editor.putString(i,"0");
        editor.commit();*/



//if(!personId.equals(myRef.child("truckType").child(personId).getKey()))
        getData();

}

void getData(){




}


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    private void signOut() {
        mAuth.signOut();
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(Delivery_boy.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...

                        Intent intent = new Intent(Delivery_boy.this, Fir_screen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                });
    }

    private void getLocationPermission() {
    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Do other setup activities here too, as described elsewhere in this tutorial.
        getLocationPermission();
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                View locationButton = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).
                        getParent()).findViewById(Integer.parseInt("2"));
                RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
// position on right bottom
                rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                rlp.setMargins(0, 0, 30, 30);

                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }



    private void getDeviceLocation() {
    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            currentLatitude = mLastKnownLocation.getLatitude();
                            currentLongitude =mLastKnownLocation.getLongitude();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(currentLatitude,
                                            currentLongitude), DEFAULT_ZOOM));
                             latLng = new LatLng(currentLatitude, currentLongitude);

                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
             return;
        }
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_delivery_boy_drawer, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_delivery_boy_drawer, menu);

       /* MenuItem item = menu.findItem(R.id.tog);
        item.setActionView(R.layout.app_bar_delivery_boy);
        final Switch s=(Switch)menu.findItem(R.id.tog).getActionView().findViewById(R.id.tog_btn);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(Delivery_boy.this,"dance its done",Toast.LENGTH_LONG).show();
                }
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.google.com"));
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
                    signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @SuppressLint("StaticFieldLeak")
    class performBackgroundTask extends AsyncTask<String,Integer,String> {
        //private ProgressBar Dialog = new ProgressBar(Delivery_boy.this);

        @Override
        protected String doInBackground(String... params) {
            myRef.child("truckType").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                        if(personId.equals(child.getKey())){
                        //    b=false;
                            s="n";
                            Log.d(TAG, "onDataChange@@@@@@@@@@@@@@@@: "+child.getKey());
                            Toast.makeText(Delivery_boy.this,"do in bC=ACKGROUND"+child.getKey(),
                                    Toast.LENGTH_LONG).show();
                            break;
                        }
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });

            return s;
        }

        protected void onPreExecute() {
            //  Dialog.setMessage("Please wait...");
            // Dialog.show();
            Toast.makeText(Delivery_boy.this,"on pre execute",Toast.LENGTH_LONG).show();
        }

        protected void onPostExecute(String unused) {
            try {/*
                if (Dialog.isShowing()) {
                    Dialog.dismiss();

                }*/
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(Delivery_boy.this,"on post execute",Toast.LENGTH_LONG).show();


                if(unused.equals("y")){
                    Log.d(TAG, "@@@@@@@@@@@@@@@@:  onstart  "+b);


                        Log.d(TAG, "@@@@@@@@@@@@@@@@: i==00");
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Delivery_boy.this);
                        View mView = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
                        mBuilder.setTitle("Choose your vehicle type");
                        spin = (Spinner) mView.findViewById(R.id.spinner);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Delivery_boy.this,
                                android.R.layout.simple_spinner_item,
                                getResources().getStringArray(R.array.trucktype));
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin.setAdapter(adapter);
                        mBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myRef.child("truckType").child(personId).child("truckType").
                                        setValue(spin.getSelectedItem().toString());
                                dialog.dismiss();
                            }
                        });
                        mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.exit(1);

                            }
                        });
                        mBuilder.setView(mView);
                        AlertDialog a = mBuilder.create();
                        a.show();

                    }


                // do your Display and data setting operation here
            } catch (Exception e) {

            }


        }
    }
}



