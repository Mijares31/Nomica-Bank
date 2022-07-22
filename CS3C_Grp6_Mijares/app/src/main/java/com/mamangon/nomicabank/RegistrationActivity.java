package com.mamangon.nomicabank;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    EditText etfullname, etemail, etpassword, etphone;
    String userID;
    Button btnregister;
    TextView tvbtnLogin, accountbalance;
    ProgressBar pBar;
    FirebaseAuth firebaseAuthAuth;
    FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        etfullname = findViewById(R.id.txtFirstname);
        etemail = findViewById(R.id.txtEmail);
        etpassword = findViewById(R.id.txtPassword);
        etphone = findViewById(R.id.txtPhone);

        accountbalance = findViewById(R.id.myAmount);

        tvbtnLogin = findViewById(R.id.TV_Login);
        btnregister = findViewById(R.id.btnRegister);

        firebaseAuthAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        pBar = findViewById(R.id.PB_Preloader);

        tvbtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openLogin = new Intent(RegistrationActivity.this, MainActivity.class);
                startActivity(openLogin);
            }
        });
        if (firebaseAuthAuth.getCurrentUser() != null){
            finish();
        }
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etemail.getText().toString().trim();
                String pass = etpassword.getText().toString().trim();
                String mphone = etphone.getText().toString().trim();
                String fullname = etfullname.getText().toString().trim();
                String currentBalance = accountbalance.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    etemail.setError("Email is require");
                    return;
                }

                if (TextUtils.isEmpty(pass)){
                    etpassword.setError("Password is required");
                    return;
                }

                if (pass.length() < 6){
                    etpassword.setError("Password must be greater than 6 characters");
                    return;
                }

                pBar.setVisibility(View.VISIBLE);

                firebaseAuthAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(RegistrationActivity.this, "Account created. ", Toast.LENGTH_SHORT).show();
                            userID = firebaseAuthAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("fName",fullname);
                            user.put("email",email);
                            user.put("phone",mphone);
                            user.put("balance", currentBalance);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "onSuccess : user Profile is created for " + userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure " + e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
                        }else{
                            Toast.makeText(RegistrationActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            pBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

    }
}