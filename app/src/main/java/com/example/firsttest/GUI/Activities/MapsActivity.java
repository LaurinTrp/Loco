package com.example.firsttest.GUI.Activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.firsttest.Excel.ExcelObject;
import com.example.firsttest.GUI.Activities.Menu.MenuActivity;
import com.example.firsttest.LoadSave;
import com.example.firsttest.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.firsttest.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static GoogleMap mMap;
    private ActivityMapsBinding binding;
    private static boolean mapReady = false;
    public static ArrayList<ExcelObject> tableObjects = new ArrayList<>(), tableObjectsFilter = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Log.d("CREATION", "onCreate() was successful");

        Button b = findViewById(R.id.button2);
        Intent intentMenu = new Intent(this, MenuActivity.class);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentMenu);
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        Log.d("SAVEINSTANCE", "onSaveInstanceState: FUCK YOU");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("RESUME", "TEST");

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        addMarkers(tableObjectsFilter, false);

    }

    public static synchronized void addMarkers(ArrayList<ExcelObject> list, boolean cameraMove){
        if(mapReady) {
            mMap.clear();
            Log.d("MARKERS", list.toString());
            for (ExcelObject object : list) {
                if(object.getLatLng() != null){
                    mMap.addMarker(new MarkerOptions().position(object.getLatLng()).title(object.getCompanyName()));
                }

            }
            if(list.size() != 0 && cameraMove){
                mMap.moveCamera(CameraUpdateFactory.newLatLng(tableObjects.get(tableObjects.size() - 1).getLatLng()));
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mapReady = true;

        addMarkers(tableObjects, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Context context = getApplicationContext();

        try {
            LoadSave.save(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}