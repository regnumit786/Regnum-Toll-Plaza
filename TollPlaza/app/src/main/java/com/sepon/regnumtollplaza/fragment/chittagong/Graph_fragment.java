package com.sepon.regnumtollplaza.fragment.chittagong;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.common.reflect.TypeToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.sepon.regnumtollplaza.R;
import com.sepon.regnumtollplaza.pojo.Previous_pojo;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Graph_fragment extends Fragment {
    private static String TAG = "Graph_fragment";

    private BarChart barChart;
    private AnyChartView anyChartView;
    private DatabaseReference myRef;
    private List<String> pickPreviousDate;
    private String pickDate;
    private TextView controlRData, totalRegularData, calculatePercent;
    private double parseControl_R, parseTotalRegular, calculatePercentage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grapg_fragment, container, false);
        barChart= view.findViewById(R.id.barChart);
        anyChartView= view.findViewById(R.id.anyChartView);
        controlRData= view.findViewById(R.id.control_R_data);
        totalRegularData= view.findViewById(R.id.total_regular_data);
        calculatePercent= view.findViewById(R.id.calculate_percent);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("chittagong");
        pickPreviousDate= new ArrayList<>();
        ShowPercentage();

        barChart.setVisibility(View.GONE);

        AnyChartSetup();

        RadioGroup radioGroup = view.findViewById(R.id.graph_view);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = radioGroup.findViewById(checkedId);
                int index = radioGroup.indexOfChild(radioButton);

                switch (index) {
                    case 0:
                        AnyChartSetup();
                        barChart.setVisibility(View.GONE);
                        anyChartView.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        WeeklyBarChart();
                        anyChartView.setVisibility(View.GONE);
                        barChart.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        return view;
    }

    private void AnyChartSetup() {
        Pie pie= AnyChart.pie();
        List<DataEntry> entriData= new ArrayList<>();

        int Axel2,Axel3,Axel4,Axel5,Axel6,Axel7;

        Axel2 = getArrayList("ctrlA2").size();
        Axel3 = getArrayList("ctrlA3").size();
        Axel4 = getArrayList("ctrlA4").size();
        Axel5 = getArrayList("ctrlA5").size();
        Axel6 = getArrayList("ctrlA6").size();
        Axel7 = getArrayList("ctrlA7").size();

        entriData.add(new ValueDataEntry("Axel 2",Axel2));
        entriData.add(new ValueDataEntry("Axel 3",Axel3));
        entriData.add(new ValueDataEntry("Axel 4",Axel4));
        entriData.add(new ValueDataEntry("Axel 5",Axel5));
        entriData.add(new ValueDataEntry("Axel 6",Axel6));
        entriData.add(new ValueDataEntry("Axel 7",Axel7));

        pie.data(entriData);
        anyChartView.setChart(pie);

    }

    private void WeeklyBarChart(){
        BarDataSet weekDataSet= new BarDataSet(weeklyBarDataValue(),"Weekly Data Set");
        weekDataSet.setColors(Color.BLUE);
        BarData weekPieData= new BarData(weekDataSet);
        barChart.setData(weekPieData);
        barChart.setDrawBarShadow(true);
        barChart.invalidate();
    }

    private ArrayList<BarEntry> weeklyBarDataValue(){
        ArrayList<BarEntry> setWeekValue= new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date todayDate = new Date();
        String currentDate = dateFormat.format(todayDate);
        Log.e(TAG, "CurrentDate: "+ currentDate);

        for (int i=0; i<7; i++) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, -i);
            String previousDate = dateFormat.format(new Date(cal.getTimeInMillis()));
            Log.e(TAG, "date is: "+i+": " + previousDate);

            myRef.child(previousDate).child("short").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    pickDate = dataSnapshot.child("ctrlR").getValue().toString();
                    pickPreviousDate.add(pickDate);
                    Log.e(TAG, "loop date is: "+ pickDate);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Error:"+ databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        for (int i=0; i< pickPreviousDate.size(); i++){
            setWeekValue.add(new BarEntry((i+1),Integer.parseInt(pickPreviousDate.get(i))));
            Log.e(TAG, "insert= "+i+1+": " +pickPreviousDate.get(i));
        }
        pickPreviousDate.clear();
        return setWeekValue;
    }

    private void ShowPercentage(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date todayDate = new Date();
        String currentDate = dateFormat.format(todayDate);
        myRef.child(currentDate).child("short").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String totalControl_R= dataSnapshot.child("ctrlR").getValue().toString();
                String totalRegular= dataSnapshot.child("total").getValue().toString();
                Log.e(TAG, "total Control R: "+totalControl_R);
                Log.e(TAG, "totalRegular: "+totalRegular);
                String control= "Control R: "+totalControl_R;
                String regular= "Total Regular: "+totalRegular;
                controlRData.setText(control);
                totalRegularData.setText(regular);
                parseControl_R= Integer.parseInt(totalControl_R);
                parseTotalRegular= Integer.parseInt(totalRegular);
                calculatePercentage= ((parseControl_R/parseTotalRegular)*100);
                @SuppressLint("DefaultLocale") String calculate_value= String.format("%.2f", calculatePercentage);
                calculatePercent.setText(calculate_value+"%");

                Log.e(TAG, "Calculate percentage: "+calculatePercentage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private ArrayList<Previous_pojo> getArrayList(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<Previous_pojo>>() {}.getType();
        return gson.fromJson(json, type);
    }

}


