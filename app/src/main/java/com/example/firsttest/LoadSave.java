package com.example.firsttest;

import android.content.Context;
import android.util.Log;

import com.example.firsttest.GUI.Activities.MapsActivity;
import com.example.firsttest.Excel.ExcelObject;
import com.example.firsttest.Excel.ExcelReader;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class LoadSave {

    public static String date = "";

    public static ArrayList<ExcelObject> allObjects = new ArrayList<>();

    public static void save(Context context) throws IOException {

        Log.d("LOADSAVE", "Saving start");

        File file = new File(context.getFilesDir(), "TableSave.ser");

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));

        oos.writeObject(ExcelReader.date);
        oos.writeObject(allObjects);

        Log.d("LOADSAVE", "Saving done");

        oos.close();
    }

    public static void load(Context context) throws IOException, ClassNotFoundException {

        Log.d("LOADSAVE", "Loading start");

        File file = new File(context.getFilesDir(), "TableSave.ser");

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));

        date = (String)ois.readObject();
        allObjects = (ArrayList<ExcelObject>) ois.readObject();

        MapsActivity.tableObjects.addAll(allObjects);

        Log.d("LOADSAVE", "Table: " + MapsActivity.tableObjects.size() + "\t Saved: " + allObjects.size());
        for (ExcelObject object : MapsActivity.tableObjects) {
            object.setLatLng(new LatLng(object.getLat(), object.getLng()));
        }

        Log.d("LOADSAVE", "Loading done");

        ois.close();
    }


    public static class MENU {

        private static HashMap<Integer, Boolean> switches = new HashMap<>();
        private static HashMap<Integer, String> text = new HashMap<>();

        public static HashMap<Integer, Boolean> getSwitches() {
            return switches;
        }
        public static HashMap<Integer, String> getText() {
            return text;
        }
    }

}
