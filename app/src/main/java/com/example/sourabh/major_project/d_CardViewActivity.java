package com.example.sourabh.major_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.model.LatLng;

import java.util.ArrayList;

import static android.graphics.Color.WHITE;
import static android.graphics.Color.convert;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by DELL on 15-03-2018.
 */

public class d_CardViewActivity extends Activity {
    private RecyclerView mRecyclerView; //View Group class
    private RecyclerView.Adapter<d_MainActivity.d_DataObjectHolder> mAdapter;//provide a
    // binding from an app-specific data set
    // to views that are displayed within a RecyclerView.
    GoogleSignInAccount acct;
    double number;
    private RecyclerView.LayoutManager mLayoutManager;//responsible for measuring and positioning item views within a
    // RecyclerView as well as determining the policy for when
    // to recycle item views that are no longer visible to the user.

    private static String LOG_TAG = "CardViewActivity";
    int index;
    d_DataObject obj;//reference of a class which provides the data
    private DatabaseReference mDatabase;
    FirebaseDatabase database;
    DatabaseReference myRef;
    double costSum;
    double distance;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private String personId, truckType, place, place1;
    Button butt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_activity_card_view);

//
//        Bundle extras = getIntent().getExtras();
//        distance = (double) extras.get("distance");
//        place = (String) extras.get("place");
//        place1 = (String) extras.get("place1");
//
//        costSum = (double) extras.get("costSum");
//        costSum=costSum+(distance*10);
//        truckType = (String) extras.get("truckType");
        // number = Double.parseDouble(distance);
        Log.d(TAG, "onCreate cardView #############: "+distance+"######"+costSum);


        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        database = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        myRef = database.getReferenceFromUrl("https://majorproject-e14b6.firebaseio.com/");
        acct = GoogleSignIn.getLastSignedInAccount(this);
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        if (currentFirebaseUser != null) {
            personId =  currentFirebaseUser.getUid();

        }
        mRecyclerView = (RecyclerView) findViewById(R.id.d_my_recycler_view);
        //making an object
        //mRecyclerView.setHasFixedSize(true);
        //changes in adapter content cannot change the size of the RecyclerView itself
        mLayoutManager = new LinearLayoutManager(this);//wiring the LinearLayout
        mRecyclerView.setLayoutManager(mLayoutManager);
        //providing a layout to recycler view
        mAdapter = new d_MainActivity(d_CardViewActivity.this, getDataSet());//preparing data to show in recycer view
        mRecyclerView.setAdapter(mAdapter);//setting the adapter

        // Code to Add an item with default animation
        ((d_MainActivity) mAdapter).addItem(obj, index);//calling additem from adapter class

        // Code to remove an item with default animation
        ((d_MainActivity) mAdapter).deleteItem(index);
    }


    @Override
    protected void onResume() {
        super.onResume();
        ((d_MainActivity) mAdapter).setOnItemClickListener(new d_MainActivity
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Toast.makeText(getApplicationContext(),"clicked"+
                        position,Toast.LENGTH_SHORT).show();
                //change1- the views are reused thats why background color is repeated..
//                myRef.child("order").child(personId).child("place").setValue(place);
//                myRef.child("order").child(personId).child("place1").setValue(place1);
//                myRef.child("order").child(personId).child("Number").setValue();
                Intent i= new Intent(d_CardViewActivity.this,NAVIGATIONA.class);
//                Log.i(LOG_TAG, " places.get(position) " + places.get(position));
//                Log.i(LOG_TAG, " places.get(position) " + places1.get(position));
                Log.i("origin_long"," "+position);
                Log.i("origin_long"," "+origin_long);

//
//                Log.i("origin_lat"," "+origin_lat.get(position));
//                Log.i("origin_long"," "+origin_long.get(position));
//
//                Log.i("des_lat"," "+des_lat.get(position));
//                Log.i("des_long"," "+des_long.get(position));




                i.putExtra("origin_lat",origin_lat.get(position));
                i.putExtra("origin_long",origin_long.get(position));

                i.putExtra("des_lat",des_lat.get(position));
                i.putExtra("des_long",des_long.get(position));
                startActivity(i);
                v.setBackgroundColor(WHITE);

                Log.i(LOG_TAG, " Clicked on Item " + position);
            }
        });
    }
    int i=0;
    final ArrayList<Double> origin_lat = new ArrayList<>();
    final ArrayList<Double> origin_long = new ArrayList<>();
    final ArrayList<Double> des_lat = new ArrayList<>();
    final ArrayList<Double> des_long = new ArrayList<>();

    private ArrayList<d_DataObject> getDataSet() {
        final ArrayList<d_DataObject> results = new ArrayList<>();


        myRef.child("order").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Log.e("ppshot.getKey()", postSnapshot.child("driver_id").getValue()+"  "+personId);
                    try {
                        if(postSnapshot.child("driver_id").getValue().equals(personId)) {
                            Log.e("img dance", postSnapshot.toString());
                            d_DataObject obj = new d_DataObject("Name: "+postSnapshot.child("User_name").getValue(),
                                    " Source:   " + postSnapshot.child("place").getValue(),
                                    "Destination:   " + postSnapshot.child("place1").getValue(),
                                    postSnapshot.child("PhotoUrl").getValue().toString());
                            results.add(i, obj);
                            Log.e(TAG, "onDataChange: in d card "+postSnapshot.child("latlngplace1").child("latitude").getValue() );
                            origin_lat.add(i,  (double)postSnapshot.child("latlngplace").child("latitude").getValue());
                            origin_long.add(i, (double) postSnapshot.child("latlngplace").child("longitude").getValue());
                            des_lat.add(i, (double) postSnapshot.child("latlngplace1").child("latitude").getValue());
                            des_long.add(i, (double) postSnapshot.child("latlngplace1").child("longitude").getValue());

                            i++;

                        }
                    }catch (Exception e){};
                }
                if(i==0){Toast.makeText(d_CardViewActivity.this,"Not found",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//
//        for (int index = 0; index < 20; index++) {
//            DataObject obj = new DataObject(" Name "+acct.getDisplayName() ,
//                    "Total "+costSum);
//            results.add(index, obj);
//        }
        return results;
    }
}
