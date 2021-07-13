package com.example.firsttest.GUI.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.firsttest.Excel.ExcelBackground;
import com.example.firsttest.R;

import java.io.File;

public class SplashActivity extends AppCompatActivity {


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Log.d("SPLASHSCREEN", "Splash onCreate()");


        Context context = getApplicationContext();
        for (File file : context.getFilesDir().listFiles()) {
            Log.d("NEWFILE", file.getName());
        }


        ExcelBackground excelBackground = new ExcelBackground(this, context);
        excelBackground.start();


        Log.d("EXCEL", "Reading done!");

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
            finish();
        }, 3000);


    }

}