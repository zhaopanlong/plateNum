package com.zhaopanlong.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.zhaopanlong.platenum.PlateNumView;

public class MainActivity extends AppCompatActivity {
    PlateNumView plateNumView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        plateNumView = findViewById(R.id.plateNumView);
        plateNumView.setPlateNum("渝A124121212");
        plateNumView.setmPlateNumViewTextWatcher(new PlateNumView.PlateNumViewTextWatcher() {
            @Override
            public void onTextChanged(String s) {
                Log.i("main",plateNumView.getPlateNum());
            }
        });
    }

    public void clearPlate(View view) {
        plateNumView.clearPlateNum(true);
    }
}
