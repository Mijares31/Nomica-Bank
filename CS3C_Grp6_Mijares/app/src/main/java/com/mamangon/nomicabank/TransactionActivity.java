package com.mamangon.nomicabank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TransactionActivity extends AppCompatActivity {
    EditText txtnumber, txtamount;
    Button bsend, bback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        txtamount = findViewById(R.id.ET_AmountUpdate);
        txtnumber = findViewById(R.id.ET_UserPhone);
        bsend = findViewById(R.id.btnSend);
        bback = findViewById(R.id.btnBack);

        bsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String amountsend = txtamount.getText().toString().trim();
                String usernumber = txtnumber.getText().toString().trim();
                Toast.makeText(TransactionActivity.this, "You send an amount of " + amountsend + " to " + usernumber, Toast.LENGTH_SHORT).show();
                Intent backtoDashboard = new Intent(TransactionActivity.this, DashboardActivity.class);
                startActivity(backtoDashboard);
            }

        });
        bback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtoDashboard = new Intent(TransactionActivity.this, DashboardActivity.class);
                startActivity(backtoDashboard);
            }
        });    }
}