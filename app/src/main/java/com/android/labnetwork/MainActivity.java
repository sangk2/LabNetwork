package com.android.labnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.labnetwork.lab1.Lab1Activity;
import com.android.labnetwork.lab1.SplashScreenActivity;
import com.android.labnetwork.lab2.Lab2Activity;
import com.android.labnetwork.lab3.Lab3Activity;

public class MainActivity extends AppCompatActivity{

    private Button btnLab1, btnLab2, btnLab3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLab1 = findViewById(R.id.btnLab1);
        btnLab1.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, SplashScreenActivity.class));
        });
        btnLab2 = findViewById(R.id.btnLab2);
        btnLab2.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, Lab2Activity.class));
        });
        btnLab3 = findViewById(R.id.btnLab3);
        btnLab3.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, Lab3Activity.class));
        });

    }
}