package com.example.geoslave;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.geoslave.Logic.NetworkUtil;

public class NoInternetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        Button CheckBT = findViewById(R.id.CheckBT);
        CheckBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtil.isNetworkConnected(NoInternetActivity.this)) {
                    startActivity(new Intent(NoInternetActivity.this, LoginActivity.class));
                } else {
                    Toast.makeText(NoInternetActivity.this, "No Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}