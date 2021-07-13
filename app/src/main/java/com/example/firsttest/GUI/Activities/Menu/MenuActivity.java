package com.example.firsttest.GUI.Activities.Menu;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.example.firsttest.Excel.ExcelObject;
import com.example.firsttest.GUI.Activities.MapsActivity;
import com.example.firsttest.LoadSave;
import com.example.firsttest.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class MenuActivity extends AppCompatActivity {

    private Switch switch_bio;
    private HashMap<Switch, String> hashMap = new HashMap<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu1);

        switch_bio = findViewById(R.id.switch_bio);

        getWidgets();

        Button b = (Button) findViewById(R.id.filterButton);
        Context context = getApplicationContext();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveStatesTemp();
                boolean clean = true;
                for (int id : LoadSave.MENU.getText().keySet()) {
                    if(!checkTextLengths(id)){
                        clean = false;
                    }
                }

                if(clean){
                    filter();
                    finish();
                }

            }
        });
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getWidgets(){

        LayoutInflater inflater = getLayoutInflater();
        LinearLayout l = (LinearLayout) inflater.inflate(R.layout.activity_menu1, null);

        Log.d("SAVING", "getWidgets: " + LoadSave.MENU.getText());

        for (int i = 0; i < l.getChildCount(); i++) {
            View view = l.getChildAt(i);
            if (view instanceof Switch){
                Switch currentSwitch = findViewById(view.getId());
                if(LoadSave.MENU.getSwitches().get(currentSwitch.getId()) != null){
                    currentSwitch.setChecked(LoadSave.MENU.getSwitches().get(currentSwitch.getId()));
                }else{
                    LoadSave.MENU.getSwitches().put(view.getId(), false);
                }
            }
            if (view instanceof AppCompatEditText){
                AppCompatEditText currentEditText = findViewById(view.getId());
                AtomicReference<AtomicInteger> limit = new AtomicReference<>(new AtomicInteger());
                Arrays.stream(currentEditText.getContentDescription().
                        toString().split(";")).
                        filter(str -> str.contains("maxLength")).
                        forEach(str -> limit.set(new AtomicInteger(Integer.parseInt(str.split(":")[1]))));

                currentEditText.addTextChangedListener(new TextHandler(currentEditText, limit.get().intValue()));

                if(LoadSave.MENU.getText().get(currentEditText.getId()) != null){
                    currentEditText.setText(LoadSave.MENU.getText().get(currentEditText.getId()));
                }else{
                    LoadSave.MENU.getText().put(view.getId(), "");
                }
            }
        }


    }

    private void saveStatesTemp(){

        for (Integer id:LoadSave.MENU.getSwitches().keySet()) {
            Switch currentSwitch = findViewById(id);
            LoadSave.MENU.getSwitches().put(id, currentSwitch.isChecked());
        }
        for (Integer id : LoadSave.MENU.getText().keySet()) {
            AppCompatEditText currentEditText = findViewById(id);
            LoadSave.MENU.getText().put(id, currentEditText.getText().toString());
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void filter(){


//        for (int id : LoadSave.MENU.getSwitches().keySet()) {
//            Switch currentSwitch = findViewById(id);
//            if(currentSwitch.isChecked()){
//                checked = true;
//                break;
//            }
//        }

        boolean boolSwitches = true;

        MapsActivity.tableObjectsFilter.clear();
        MapsActivity.tableObjectsFilter.addAll(LoadSave.allObjects);
        Log.d("TABLE", "size: " + MapsActivity.tableObjectsFilter.size());
        Log.d("TABLE", "size: " + MapsActivity.tableObjectsFilter.size());

        for (ExcelObject object : LoadSave.allObjects) {

                for (int id : LoadSave.MENU.getSwitches().keySet()) {
                    Switch currentSwitch = findViewById(id);
                    if (!checkContentSwitches(object, currentSwitch) && boolSwitches && currentSwitch.isChecked()) {
                        MapsActivity.tableObjectsFilter.remove(object);
                    } else if (!boolSwitches) {
                        break;
                    }
                }

                for (int id : LoadSave.MENU.getText().keySet()) {
                    AppCompatEditText currentEditText = findViewById(id);
                    if(currentEditText.getText().toString().length() != 0) {
                        if (!checkContentEditText(object, currentEditText)) {
                            MapsActivity.tableObjectsFilter.remove(object);
                        }
                    }
                }
//            }else{
//                MapsActivity.tableObjectsFilter.add(object);
//            }

//            if (switch_bio.isChecked() && object.isBio()){
//                MapsActivity.tableObjectsFilter.add(object);
//            }else if(!switch_bio.isChecked()){
//                MapsActivity.tableObjectsFilter.addAll(MapsActivity.tableObjects);
//            }


        }

        Log.d("TABLE", "filter: " + MapsActivity.tableObjectsFilter.size());
        MapsActivity.addMarkers(MapsActivity.tableObjectsFilter, false);


        
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean checkContentSwitches(ExcelObject excelObject, Switch currentSwitch){
        boolean bool = false;

        try {
            Method method = excelObject.getClass().getDeclaredMethod("is" + currentSwitch.getText().toString());
            bool = (boolean) method.invoke(excelObject);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return bool;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean checkContentEditText(ExcelObject excelObject, AppCompatEditText currentEditText){
        boolean bool = false;

            String[] description = currentEditText.getContentDescription().toString().split(";");
            int type = -1;
            for (String s : description) {
                if(s.contains("type")){
                    type = Integer.parseInt(s.split(":")[1]);
                }
            }

            if(type == EditTextTypes.PLZ.id){
                bool = excelObject.getPlz() == Integer.parseInt(currentEditText.getText().toString());
                Log.d("CHECKPLZ", "checkContentEditText: " + bool);

            }else if(type == EditTextTypes.CompanyName.id){

            }else if(type == EditTextTypes.Location.id){

            }else{}

        return bool;
    }

    private boolean checkTextLengths(int id){
        AppCompatEditText currentEditText = findViewById(id);
        String[] contentDesc = currentEditText.getContentDescription().toString().split(";");
        int minLength = 0, maxLength = Integer.MAX_VALUE;
        for (String s : contentDesc) {
            if(s.contains("minLength")){
                minLength = Integer.parseInt(s.split(":")[1]);
            }
            if(s.contains("maxLength")){
                maxLength = Integer.parseInt(s.split(":")[1]);
            }
        }
        String text = currentEditText.getText().toString();
        if((text.length() < minLength || text.length() > maxLength) && text.length() != 0) {
            return false;
        }
        return true;
    }


    private class TextHandler implements TextWatcher{

        private AppCompatEditText editText;
        private int limit;
        public TextHandler(AppCompatEditText editText, int limit){
            this.editText = editText;
            this.limit = limit;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            editText.getBackground().mutate().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.lightGreen), PorterDuff.Mode.SRC_ATOP);

            if(editText.getText().length() > limit) {
                editText.setText(editText.getText().subSequence(0, limit));
                editText.setSelection(editText.getText().length());
            }
            Log.d("TEXTWATCHER", "beforeTextChanged: " + editText.getText().length());
        }

        @Override
        public void afterTextChanged(Editable s) {

            if(!checkTextLengths(editText.getId())){
                editText.getBackground().mutate().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.red), PorterDuff.Mode.SRC_ATOP);
            }else{
                editText.getBackground().mutate().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.lightGreen), PorterDuff.Mode.SRC_ATOP);
            }
            //Test
        }
    }

}