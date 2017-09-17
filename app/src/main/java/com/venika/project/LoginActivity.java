package com.venika.project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText mLoginEmailField;
    private EditText mLoginPasswordField;
    private Button mLoginBtn;
    private Button mNeddAcc;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUser;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();


        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUser.keepSynced(true);

        mProgress = new ProgressDialog(this);
        mProgress.setCanceledOnTouchOutside(false);


        mLoginPasswordField = (EditText) findViewById(R.id.loginPasswordField);
        mLoginEmailField = (EditText) findViewById(R.id.loginEmailField);
        mNeddAcc = (Button) findViewById(R.id.needAcc);
        mLoginBtn = (Button) findViewById(R.id.loginBtn);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLogin();
            }
        });

        mNeddAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegister();
            }
        });
    }

    private void checkLogin() {

        String email = mLoginEmailField.getText().toString().trim();
        String password = mLoginPasswordField.getText().toString().trim();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            mProgress.setMessage("Checking Login...");
            mProgress.show();

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        checkUserExist();
                    } else {
                        mProgress.dismiss();
                        Toast.makeText(LoginActivity.this, "You don't have account", Toast.LENGTH_LONG).show();
                    }

                }
            });

        }
    }

    private void checkUserExist() {

        if(mAuth.getCurrentUser() !=null) {
            final String user_id = mAuth.getCurrentUser().getUid();
            mDatabaseUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(user_id)) {
                        Intent mainintent = new Intent(LoginActivity.this, MainActivity.class);
                        mainintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainintent);
                    }
                    else {
                        Intent setupintent = new Intent(LoginActivity.this, SigninActivity.class);
                        setupintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupintent);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            mProgress.dismiss();
        }
    }
    private void goToRegister() {

        Intent nedd_acc = new Intent(LoginActivity.this, SigninActivity.class);
        nedd_acc.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(nedd_acc);

    }
}
