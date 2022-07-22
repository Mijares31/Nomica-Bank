package com.mamangon.nomicabank;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class DashboardActivity extends AppCompatActivity {
    TextView fullname, tvphone, cbalance;
    Button sendmoney;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String uID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        fullname = findViewById(R.id.TVName);
        tvphone = findViewById(R.id.TVPhone);
        cbalance = findViewById(R.id.TV_AccountMoney);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        sendmoney = findViewById(R.id.btnSendMoney);
        uID = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(uID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                tvphone.setText(documentSnapshot.getString("phone"));
                fullname.setText(documentSnapshot.getString("fName"));
                cbalance.setText(documentSnapshot.getString("balance"));
            }
        });

        sendmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openTransfer = new Intent(DashboardActivity.this, TransactionActivity.class);
                startActivity(openTransfer);
                finish();
            }
        });

    }

    public void logoutbtn(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}