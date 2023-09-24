package com.example.hypernotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    private TextInputLayout mEmailInputLayout, mPasswordInputLayout;
    private EditText mLogInEmail, mLogInPassword;
    private RelativeLayout mLogIn, mGoToSignUp;
    private TextView mGoToForgotPassword;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmailInputLayout=findViewById(R.id.email);
        mPasswordInputLayout=findViewById(R.id.password);
        mLogInEmail=findViewById(R.id.login_email);
        mLogInPassword=findViewById(R.id.login_password);
        mLogIn=findViewById(R.id.login);
        mGoToForgotPassword=findViewById(R.id.gotoforgotpassword);
        mGoToSignUp=findViewById(R.id.gotosingup);

        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        if(firebaseUser!=null){
            finish();
            startActivity(new Intent(MainActivity.this,NotesActivity.class));
        }



        mGoToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Signup.class));
            }
        });

        mGoToForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ForgotPassword.class));
            }
        });


        mLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=mLogInEmail.getText().toString().trim();
                String password=mLogInPassword.getText().toString().trim();

                if(mail.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(),"All Fields Are Required",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    // Login the user

                    firebaseAuth.signInWithEmailAndPassword(mail,password).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(task.isSuccessful()){
                                        checkMailVerification();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),
                                                "Account Doesn't Exist", Toast.LENGTH_SHORT)
                                                .show();
                                    }


                                }
                            });


                }


            }
        });



    }


    private void checkMailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser.isEmailVerified()==true){
            Toast.makeText(getApplicationContext(),
                            "Logged In", Toast.LENGTH_SHORT)
                    .show();
            finish();
            startActivity(new Intent(MainActivity.this,NotesActivity.class));
        }
        else{
            Toast.makeText(getApplicationContext(),
                            "Verify your mail first", Toast.LENGTH_SHORT)
                    .show();
            firebaseAuth.signOut();
        }
    }



}