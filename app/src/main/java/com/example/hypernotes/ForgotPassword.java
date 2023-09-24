package com.example.hypernotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText mForgotPassword;
    private Button mPasswordRecoverButton;
    private TextView mGoBackToLogIn;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        firebaseAuth=FirebaseAuth.getInstance();

        mForgotPassword=findViewById(R.id.forgotpassword);
        mPasswordRecoverButton=findViewById(R.id.passwordRecoverButton);
        mGoBackToLogIn=findViewById(R.id.goBackToLogIn);




        mGoBackToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(ForgotPassword.this,MainActivity.class);
              startActivity(intent);
            }
        });

        mPasswordRecoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=mForgotPassword.getText().toString().trim();
                if(mail.isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "Enter your mail first",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    //We have to send mail

                    firebaseAuth.sendPasswordResetEmail(mail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),
                                        "Mail Sent, You can recover your password using mail",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotPassword.this,
                                        MainActivity.class));
                            }
                            else{
                                Toast.makeText(getApplicationContext(),
                                        "Email is Wrong or Account Does Not Exist",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
            }
        });


    }
}