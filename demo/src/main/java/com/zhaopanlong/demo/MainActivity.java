package com.zhaopanlong.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.zhaopanlong.platenum.PlateNumView;

public class MainActivity extends AppCompatActivity {
    PlateNumView plateNumView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        plateNumView = findViewById(R.id.plateNumView);
        plateNumView.setPlateNum("渝A124AFEF545");
    }

    public void clearPlate(View view) {
        plateNumView.clearPlateNum(true);
    }
}
