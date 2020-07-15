package com.sepon.regnumtollplaza;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.sepon.regnumtollplaza.adapter.AxelAdapter;
import com.sepon.regnumtollplaza.admin.Report;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AxelDetailsActivity extends AppCompatActivity {

    String axel;
    RecyclerView recyclerView;
    AxelAdapter axelAdapter;
    ArrayList<Report> axelList;

    ArrayList<Report> lan1;
    ArrayList<Report> lan2;
    ArrayList<Report> lan3;
    ArrayList<Report> lan4;
    ArrayList<Report> lan5;
    ArrayList<Report> dayShift;
    ArrayList<Report> nightShift;

    TextView lan1text, lan2text,lan3text,lan4text,lan5text, shift;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_axel_details);

        Intent i = getIntent();
        axel = i.getStringExtra("axel");
        Toast.makeText(this, axel, Toast.LENGTH_SHORT).show();

        axelList = new ArrayList<>();
        axelList = getArrayList(axel);
        int t  = getArrayList(axel).size();

        recyclerView = findViewById(R.id.axel_recyclerview);
        axelAdapter = new AxelAdapter(axelList, AxelDetailsActivity.this);
        recyclerView.setAdapter(axelAdapter);

        lan1text = findViewById(R.id.lan_1);
        lan2text = findViewById(R.id.lan_2);
        lan3text = findViewById(R.id.lan_3);
        lan4text = findViewById(R.id.lan_4);
        lan5text = findViewById(R.id.lan_5);
        //shift = findViewById(R.id.shift_wise);

        calculateLan();
      //  shiftwise();

    }

    public ArrayList<Report> getArrayList(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<Report>>() {}.getType();
        return gson.fromJson(json, type);
    }


    private void calculateLan() {

        lan1 = new ArrayList<>();
        lan2 = new ArrayList<>();
        lan3 = new ArrayList<>();
        lan4 = new ArrayList<>();
        lan5 = new ArrayList<>();

        for (int i=0; i<axelList.size(); i++){
             String lan =  axelList.get(i).getLane();
             if (lan.equals("1.0")){
                 lan1.add(axelList.get(i));

             }else if (lan.equals("2.0")){
                 lan2.add(axelList.get(i));

             }else if (lan.equals("3.0")){
                 lan3.add(axelList.get(i));

             }else if (lan.equals("4.0")){
                 lan4.add(axelList.get(i));

             }else if (lan.equals("5.0")){
                 lan5.add(axelList.get(i));

             }
        }

        //1
        int lan1_car = lan1.size();
        if (lan1_car==0){
           lan1text.setVisibility(View.GONE);
        }else {
           lan1text.setText("Lan-1: "+lan1_car);
        }
        //2
        int lan2_car = lan2.size();
        if (lan2_car==0){
            lan2text.setVisibility(View.GONE);
        }else {
            lan2text.setText("Lan-2: "+lan2_car);
        }
        //3
        int lan3_car = lan3.size();
        if (lan3_car==0){
            lan3text.setVisibility(View.GONE);
        }else {
            lan3text.setText("Lan-3: "+lan3_car);
        }
        //4
        int lan4_car = lan4.size();
        if (lan4_car==0){
            lan4text.setVisibility(View.GONE);
        }else {
            lan4text.setText("Lan-4: "+lan4_car);
        }
        int lan5_car = lan5.size();
        if (lan5_car==0){
            lan5text.setVisibility(View.GONE);
        }else {
            lan5text.setText("Lan-5: "+lan5_car);
        }

       // lan.setText("Lan1-"+lan1_car+" , "+"Lan2-"+lan2_car+" , "+"Lan3-"+lan3_car+" , "+"Lan4-"+lan4_car+" , "+"Lan5-"+lan5_car );

    }

    /*

    private void shiftwise() {
        dayShift = new ArrayList<>();
        nightShift = new ArrayList<>();

        for (int i = 0; i < axelList.size(); i++) {
            String time = axelList.get(i).getDateTime();
            if (time) {

                dayShift.add(axelList.get(i));

            } else if (lan.equals("2.0")) {
                lan2.add(axelList.get(i));

            }
        }

    }

    */

}
