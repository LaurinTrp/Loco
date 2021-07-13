package com.example.firsttest.Excel;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.firsttest.GUI.Activities.MapsActivity;
import com.example.firsttest.LoadSave;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class ExcelReader {

    public static String date;
    public boolean read;

    public static void readExcel(Activity activity, InputStream inputStream) {

        Workbook wbook;

        Log.d("EXCELSTART", "Starting");
        try {


            ArrayList<ExcelObject> excelObjects = new ArrayList<>();

            WorkbookSettings ws = new WorkbookSettings();
            ws.setEncoding("Cp1252");
            wbook = Workbook.getWorkbook(inputStream, ws);

            Sheet sheet = wbook.getSheet(0);

            Pattern pattern = Pattern.compile(".*zuletzt bearbeitet: .*");
            Cell dateCell = sheet.findCell(pattern, 0, 0, 10, 10, false);
            date = dateCell.getContents();

            Log.d("DATE", date);
            Log.d("DATESAVED", "" + LoadSave.date);

            if (LoadSave.date != null) {
                if (!date.equals(LoadSave.date)) {

                    Log.d("READMESSAGE", "Reading from Server");

                    Cell plzCell = sheet.findCell("PLZ");

                    int counter = plzCell.getRow();
                    Cell[] currentCell;


                    while (true) {
                        counter++;
                        currentCell = sheet.getRow(counter);

                        if (currentCell[0].getContents().length() == 0) {
                            break;
                        }

                        ExcelObject object = new ExcelObject();
                        object.setPlz(Integer.parseInt(currentCell[1].getContents()));
                        object.setMatchCode(currentCell[2].getContents());
                        object.setCompanyName(currentCell[3].getContents());
                        object.setLocation(currentCell[4].getContents());
                        object.setArticles(currentCell[7].getContents().split(","));
                        Log.d("BIO", currentCell[8].getContents().trim().toLowerCase());
                        object.setBio(currentCell[8].getContents().trim().toLowerCase().equals("x"));
                        object.setLatLng(getLatLng(activity, object));



                        Log.d("EXCEL", object.getString());


                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                excelObjects.add(object);
                                MapsActivity.tableObjects = excelObjects;
                                MapsActivity.addMarkers(MapsActivity.tableObjects, false);
                            }
                        });

                    }

                    LoadSave.allObjects = excelObjects;

                    Log.d("EXCEL", "readExcel: " + LoadSave.allObjects.size());

                } else {
                    Log.d("READMESSAGE", "Reading from Saved");
                }
            }

            } catch(IOException e){
                Log.d("DATE", "Dead");
                e.printStackTrace();
            } catch(BiffException e){
                Log.d("DATE", "Dead");
                e.printStackTrace();
            }

    }

    private static LatLng getLatLng(Activity activity, ExcelObject object) throws IOException {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocationName(object.getLocation() + " " + object.getPlz(), 1);
        if(addresses.size() != 0){
            Address address = addresses.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            return latLng;
        }
        return null;
    }

    public static boolean isExcel(InputStream inputStream) {

        try {
            Workbook workbook = Workbook.getWorkbook(inputStream);
            return true;
        } catch (IOException | BiffException e) {
            e.printStackTrace();
            return false;
        }
    }

}
