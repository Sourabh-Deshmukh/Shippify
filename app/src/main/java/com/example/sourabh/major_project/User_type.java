package com.example.sourabh.major_project;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class User_type extends AppCompatActivity {
    private Button button;
    private FirebaseAuth mAuth;
    private RadioGroup radioGroup;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient mGoogleApiClient;
    private String personId;
    private DatabaseReference mDatabase;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private String MyPREFERENCES="myPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);

        button = (Button) findViewById(R.id.b);
        radioGroup = (RadioGroup) findViewById(R.id.radio);
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


        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.rb1:
                        editor.putString(personId, "user");
                        editor.commit();
                       /* myRef.child("User").child(personId).child("userType").setValue("user");
                        //writeNewUser("SD", "User");*/
                        Intent i = new Intent(User_type.this, User.class);
                        startActivity(i);
                        break;

                    case R.id.rb2:
                        editor.putString(personId, "shipping company");
                        editor.commit();
                        /*myRef.child("User").child(personId).child("userType").setValue("shipping company");*/
                        Intent i2 = new Intent(User_type.this, Shipping_companys.class);
                        startActivity(i2);
                        break;

                    case R.id.rb3:
                        editor.putString(personId, "Delivery boy");
                        editor.commit();
                        /*myRef.child("User").child(personId).child("userType").setValue("Delivery boy");*/
                        Intent i3 = new Intent(User_type.this, Delivery_boy.class);
                        startActivity(i3);
                        break;
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String s = (shared.getString(personId, ""));
        if(s.equals("user")){
            Intent i = new Intent(User_type.this, User.class);
            startActivity(i);
        }
        if(s.equals("shipping company")){
            Intent i2 = new Intent(User_type.this, Shipping_companys.class);
            startActivity(i2);
        }
        if(s.equals("Delivery boy")){
            Intent i3 = new Intent(User_type.this, Delivery_boy.class);
            startActivity(i3);
        }


        /*
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("It's Loading, hold your breath  ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();//displays the progress bar
        long delayInMillis = 5000;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                progressBar.dismiss();
            }
        }, delayInMillis);


        myRef.child("User").child(personId).child("userType").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String s= (String) dataSnapshot.getValue();
                    Toast.makeText(User_type.this,"Welcome",Toast.LENGTH_SHORT).show();
                    if(s.equals("user")){
                        Intent i = new Intent(User_type.this, User.class);
                        startActivity(i);
                    }
                    if(s.equals("shipping company")){
                        Intent i2 = new Intent(User_type.this, Shipping_companys.class);
                        startActivity(i2);
                    }
                    if(s.equals("Delivery boy")){
                        Intent i3 = new Intent(User_type.this, Delivery_boy.class);
                        startActivity(i3);
                    }
                }/*else{
                    Intent i4=new Intent(User_type.this,Fir_screen.class);
                    startActivity(i4);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/


}

    /* private void writeNewUser(String email, String usertype) {
             Users user = new Users(email, usertype);

             myRef.child("User").child(email).setValue(user);
         }*/
    private void signOut() {
        mAuth.signOut();
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...

                        Intent intent = new Intent(User_type.this, Fir_screen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                });


    }

    @Override
    public void onBackPressed() {

    }

}

