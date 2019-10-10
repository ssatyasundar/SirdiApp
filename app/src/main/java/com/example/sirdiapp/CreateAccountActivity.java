package com.example.sirdiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {

    EditText new_emailf,new_passf;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mAuth = FirebaseAuth.getInstance();

        new_emailf=(EditText)findViewById(R.id.email_create);
        new_passf=(EditText)findViewById(R.id.pass_create);

        findViewById(R.id.sign_up).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.sign_up:
                register_user();
                break;

            case R.id.cancel:
                goto_home();
                break;
        }
    }

    private void goto_home(){
        startActivity(new Intent(this,LoginActivity.class));
    }

    private void register_user(){
        String new_email = new_emailf.getText().toString().trim();
        String new_pass = new_passf.getText().toString().trim();

        if(new_email.isEmpty()){
            new_emailf.setError("Email is required");
            new_emailf.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(new_email).matches()){
            new_emailf.setError("Enter Valid Email");
            new_emailf.requestFocus();
            return;
        }

        if(new_pass.length()<6){
            new_passf.setError("Minimum length of password should be 6");
            new_passf.requestFocus();
            return;
        }

        if(new_pass.isEmpty()){
            new_passf.setError("Password is required");
            new_passf.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(new_email,new_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CreateAccountActivity.this, "User Registered Sucessfully", Toast.LENGTH_SHORT).show();
                    goto_home();
                } else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(CreateAccountActivity.this, "Email Already Registered", Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(CreateAccountActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}