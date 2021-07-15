package com.example.firsttest;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.firsttest.GUI.Activities.MapsActivity;
import com.example.firsttest.Excel.ExcelObject;
import com.example.firsttest.Excel.ExcelReader;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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

        @RequiresApi(api = Build.VERSION_CODES.N)
        public static void saveMenu(Context context, String fileName)  {

            try {

                File file = new File(context.getFilesDir(), "template_" + fileName + ".ser");
                if(!file.exists()){
                    file.createNewFile();
                }


                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                oos.writeObject(switches);
                oos.writeObject(text);
                oos.flush();
                oos.close();

            }catch (IOException e){
                e.printStackTrace();
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public static List<String> showSaved(Context context) {
            File file = new File(context.getFilesDir().toString());
            List<String> list = Arrays.asList(file.list().clone());
            List<String> filtered = list.stream().filter(str -> str.contains("template_")).collect(Collectors.toList());
            return filtered;
        }

        public static void loadMenu(Context context, String fileName) throws IOException, ClassNotFoundException {
            File file = new File(context.getFilesDir(), "templates/" + fileName+".ser");
            if(!file.exists()){
                file.createNewFile();
            }
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            switches = (HashMap<Integer, Boolean>) ois.readObject();
            text = (HashMap<Integer, String>) ois.readObject();
            ois.close();
        }

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
