package com.example.firsttest.Excel;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.firsttest.LoadSave;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ExcelBackground extends Thread implements Runnable{

    private Context context;
    private Activity activity;

    public ExcelBackground(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void run() {
        super.run();

        File file = new File(context.getFilesDir(), "Table.xls");
            InputStream is = null;
            try {
                is = new URL("http://192.168.178.65:8000//Table.xls").openStream();
                Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Log.d("SERVERFILE", file.getAbsolutePath());

            } catch (IOException e) {
                Log.d("ERROR", e.getLocalizedMessage());




            }
        try {
            LoadSave.load(context);
        } catch (ClassNotFoundException | IOException e2) {
            e2.printStackTrace();
            Log.d("READMESSAGE", "Reading from Asset");

            AssetManager assetManager = activity.getAssets();
            InputStream inputStream = null;
            try {

                inputStream = assetManager.open("Table.xls");
                ExcelReader.readExcel(activity, inputStream);
                LoadSave.save(context);

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        try {

            ExcelReader.readExcel(activity, new FileInputStream(file));

            LoadSave.save(context);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
