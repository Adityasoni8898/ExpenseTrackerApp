package com.example.dailyshoppinglist.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dailyshoppinglist.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText rePassword;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        email = findViewById(R.id.et_email_signup);
        password = findViewById(R.id.et_password_signup);
        rePassword = findViewById(R.id.et_password_rep_signup);
        TextView alreadyAccount = findViewById(R.id.tv_already_account);
        Button btRegister = findViewById(R.id.bt_signup);
        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);

        alreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mEmail = email.getText().toString().trim();
                String mPassword = password.getText().toString().trim();
                String mRePassword = rePassword.getText().toString().trim();

                if(!TextUtils.equals(mPassword, mRePassword)) {
                    rePassword.setError("Re-entry is wrong");
                    Toast.makeText(getApplicationContext(), "passwords must be same", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(mEmail)) {
                    email.setError("Required...");
                    return;
                }
                if(TextUtils.isEmpty(mPassword)) {
                    password.setError("Required...");
                    return;
                }

                dialog.setMessage("Processing");
                dialog.show();

                mAuth.createUserWithEmailAndPassword(mEmail,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}