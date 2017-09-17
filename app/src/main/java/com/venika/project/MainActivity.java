package com.venika.project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import static android.R.color.black;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseUser;
    private ProgressDialog mprogress;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String uid = null, final_str;
    private Firebase mRef;
    private TextView mName, mEmail, mHistory, mBmiResult, mBmiValue;
    private EditText mWeight, mHeight;
    private Button mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");

        mName = (TextView) findViewById(R.id.userName);
        mEmail = (TextView) findViewById(R.id.UserEmail);
        mHistory = (TextView) findViewById(R.id.UserHistory);
        mBmiValue = (TextView) findViewById(R.id.bmiValue);
        mBmiResult = (TextView) findViewById(R.id.bmiResult);

        mWeight = (EditText) findViewById(R.id.weightFiled);
        mHeight = (EditText) findViewById(R.id.heightField);

        mSubmit = (Button) findViewById(R.id.SubmitBtn);


        mDatabaseUser.keepSynced(true);

        mAuth = FirebaseAuth.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    Intent Loginintent = new Intent(MainActivity.this, LoginActivity.class);
                    Loginintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(Loginintent);

                }
                else {
                    uid = mAuth.getCurrentUser().getUid();
                    mDatabaseUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String name = dataSnapshot.child(uid).child("name").getValue(String.class);
                            mName.setText(name);
                            String email = dataSnapshot.child(uid).child("email").getValue(String.class);
                            mEmail.setText(email);
                            String history = dataSnapshot.child(uid).child("History").getValue(String.class);
                            mHistory.setText(history);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        };
        mprogress = new ProgressDialog(this);
        mprogress.setCanceledOnTouchOutside(false);

        checkUserExist();

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findBmi();
            }
        });


    }
    private float calculateBMI (float weight, float height) {
        return (float) (weight / (height * height)*10000);
    }
    private void findBmi() {
        String w = mWeight.getText().toString();
        float weight = Float.parseFloat(w);
        String h = mHeight.getText().toString();
        float height = Float.parseFloat(h);

        float bmiValue = calculateBMI(weight, height);
        String result = String.valueOf(bmiValue);
        mBmiValue.setText(result);

        String value = interpretBMI(bmiValue);
        mBmiResult.setText(value);

    }

    private String interpretBMI(float bmiValue) {

        if (bmiValue < 16) {
            return "Severely underweight :(";
        } else if (bmiValue < 18.5) {

            return "Underweight :(";
        } else if (bmiValue < 25) {

            return "Normal :)";
        } else if (bmiValue < 30) {

            return "Overweight !";
        } else {
            return "Obese";
        }
    }

    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }
    private void checkUserExist() {

        if(mAuth.getCurrentUser() != null) {

            final String user_id = mAuth.getCurrentUser().getUid();

            mDatabaseUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (!dataSnapshot.hasChild(user_id)) {

                        Intent setupintent = new Intent(MainActivity.this,SigninActivity.class);
                        setupintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupintent);

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_logout){
            logout();
        }
        if(item.getItemId() == R.id.eyeTest){
            Intent setupintent = new Intent(MainActivity.this, EyeTest.class);
            setupintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(setupintent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mAuth.signOut();
    }
}
