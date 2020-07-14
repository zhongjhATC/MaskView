package com.example.maskview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        ((MaskPierceView) findViewById(R.id.maskPierceView)).setPiercePosition(dm.widthPixels / 2, dm.heightPixels / 2, 100);
    }
}
