package com.example.sourabh.major_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.graphics.Color.WHITE;

/**
 * Created by Sourabh on 14-03-2018.
 */

public class CardViewActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView; //View Group class
    private RecyclerView.Adapter<MainActivity.DataObjectHolder> mAdapter;//provide a
    // binding from an app-specific data set
    // to views that are displayed within a RecyclerView.

    private RecyclerView.LayoutManager mLayoutManager;//responsible for measuring and positioning item views within a
    // RecyclerView as well as determining the policy for when
    // to recycle item views that are no longer visible to the user.

    private static String LOG_TAG = "CardViewActivity";
    int index,i=0;
    DataObject obj;//reference of a class which provides the data
    private DatabaseReference mDatabase;
    FirebaseDatabase database;
    DatabaseReference myRef;
    double costSum;
    double distance;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private String place, place1,personId, truckType;
    LatLng origin,destination;
    private GoogleSignInAccount acct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);


        Bundle extras = getIntent().getExtras();
        distance = (double) extras.get("distance");
        origin = (LatLng) extras.get("origin");
        destination = (LatLng) extras.get("destination");

        place = (String) extras.get("place");
        place1 = (String) extras.get("place1");

        costSum = (double) extras.get("costSum");
        costSum=costSum+(distance*10);
        truckType = (String) extras.get("truckType");
        // number = Double.parseDouble(distance);
        Log.d(LOG_TAG, "onCreate cardView #############: "+distance+"######"+costSum);


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



        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        //making an object
        //mRecyclerView.setHasFixedSize(true);
        //changes in adapter content cannot change the size of the RecyclerView itself
        mLayoutManager = new LinearLayoutManager(this);//wiring the LinearLayout
        mRecyclerView.setLayoutManager(mLayoutManager);
        //providing a layout to recycler view
        mAdapter = new MainActivity(CardViewActivity.this,getDataSet());//preparing data to show in recycer view
        mRecyclerView.setAdapter(mAdapter);//setting the adapter

        // Code to Add an item with default animation
        ((MainActivity) mAdapter).addItem(obj, index);//calling additem from adapter class

        // Code to remove an item with default animation
        ((MainActivity) mAdapter).deleteItem(index);
    }


    @Override
    protected void onResume() {
        super.onResume();
        ((MainActivity) mAdapter).setOnItemClickListener(new MainActivity
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
//                Toast.makeText(CardViewActivity.this,"clicked"+
//                        position,Toast.LENGTH_SHORT).show();
                //change1- the views are reused thats why background color is repeated..

                myRef.child("order").child(personId).child("latlngplace").setValue(origin);
                myRef.child("order").child(personId).child("latlngplace1").setValue(destination);
                myRef.child("order").child(personId).child("place").setValue(place);
                myRef.child("order").child(personId).child("place1").setValue(place1);
                myRef.child("order").child(personId).child("PhotoUrl").setValue(acct.getPhotoUrl().toString());
                myRef.child("order").child(personId).child("User_name").setValue(acct.getDisplayName());
                myRef.child("order").child(personId).child("driver_id").setValue(driverKey.get(position).toString());

//                myRef.child("order").child(personId).child("Number").setValue();
                Intent i= new Intent(CardViewActivity.this,User.class);
                startActivity(i);
                v.setBackgroundColor(WHITE);
                v.setBackgroundColor(WHITE);

                Log.i(LOG_TAG, " Clicked on Item " + position);
            }
        });
    }
    final ArrayList<String> driverKey = new ArrayList<>();
    private ArrayList<DataObject> getDataSet() {
        final ArrayList<DataObject> results = new ArrayList<>();

        myRef.child("activeDrivers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Log.e("img", postSnapshot.toString());
                    try {
                        if(postSnapshot.child("truckType").getValue().equals(truckType)) {

                            Log.e("img dance", postSnapshot.toString());
                            DataObject obj = new DataObject(" Name: " + postSnapshot.child("Name").getValue(),
                                    "Total " + costSum, postSnapshot.child("PhotoUrl").getValue().toString());
                            results.add(i, obj);
                            driverKey.add(i,postSnapshot.getKey());
                            i++;

                        }
                    }catch (Exception e){};
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//        for (int index = 0; index < 20; index++) {
//            DataObject obj = new DataObject("Some Primary Text " + index,
//                    "Secondary " + index);
//            results.add(index, obj);
//        }
        return results;
    }

}
