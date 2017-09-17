package com.venika.project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SigninActivity extends AppCompatActivity {

    private EditText mNameFiled;
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mMobileFiled;
    private EditText mAddressField;
    private EditText mHistoryField;
    private Button mRegisterBtn;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCanceledOnTouchOutside(false);

        mNameFiled = (EditText) findViewById(R.id.nameField);
        mEmailField = (EditText) findViewById(R.id.emailField);
        mHistoryField = (EditText) findViewById(R.id.historyField);
        mMobileFiled = (EditText) findViewById(R.id.mobileField);
        mAddressField = (EditText) findViewById(R.id.addressField);
        mPasswordField = (EditText) findViewById(R.id.passwordField);
        mRegisterBtn = (Button) findViewById(R.id.RegisterBtn);
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startRegister();

            }


        });

    }

    private void startRegister() {

        final String name = mNameFiled.getText().toString().trim();
        final String email = mEmailField.getText().toString().trim();
        String password = mPasswordField.getText().toString().trim();
        final String mobile = mMobileFiled.getText().toString().trim();
        final String Address = mAddressField.getText().toString().trim();
        final String History = mHistoryField.getText().toString().trim();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            mProgressDialog.setMessage("Sigining up...");
            mProgressDialog.show();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user_db = mDatabase.child(user_id);
                        current_user_db.child("name").setValue(name);
                        current_user_db.child("mobile").setValue(mobile);
                        current_user_db.child("email").setValue(email);
                        current_user_db.child("Address").setValue(Address);
                        current_user_db.child("History").setValue(History);

                        mProgressDialog.dismiss();

                        Intent mainIntent = new Intent(SigninActivity.this, MainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);

                    }

                }
            });


        }

    }
}
