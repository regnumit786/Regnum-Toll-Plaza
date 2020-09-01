package com.sepon.regnumtollplaza.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sepon.regnumtollplaza.R;
import com.sepon.regnumtollplaza.pojo.Norshinddi;
import com.sepon.regnumtollplaza.utility.ApiClient;
import com.sepon.regnumtollplaza.utility.IApiClient;
import com.sepon.regnumtollplaza.utility.VipPassCount;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public class CharsindorGraph extends Fragment {
    private View view;
    private String TAG= "Charsindor_Fragment";
    private BarChart charsindor_barChart;
    private AnyChartView anyChartView;
    private String getTaka;
    private String url = "http://103.95.99.140/api/yesterday.php";
    private ArrayList<Norshinddi> todayreport= new ArrayList<>();
    private ArrayList<BarEntry> setWeekValue= new ArrayList<>();
    private String[] subString;
    private List<String> storeAllData;
    private int total;
    private TextView loading_Text, totalTextRickshaw, totalTextMotorcycle, totalTextFourWheeler, totalTextMicroBus, totalTextMiniBus, totalTextAgroBus, totalTextMiniTruck,
            totalTextBigBus, totalTextTFWheeler, totalTextSeedanCar, totalTextMediumTruck, totalTextHavvyTruck, totalTextTrailerLong, totalTextVip, totalTextVehiclesCount, totalTextWithVip;

    private String rickshaw,motorcycle, wheeler, microbus, minibus, agrobus, minitruck,bigbus,
            threefourwheeler,sedancar,mediumtruck, heavytruck,trailerlong, vip;

    private LinearLayout linearLayout_1, linearLayout_2, linearLayout_3, linearLayout_4, linearLayout_5, linearLayout_6, linearLayout_7, linearLayout_8,
            linearLayout_9, linearLayout_10, linearLayout_11, linearLayout_12, linearLayout_13, linearLayout_14, linearLayout_15, linearLayout_16;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view= inflater.inflate(R.layout.chasindor_graph_fragment, container, false);

         FindAllView();
         LayoutView();

         charsindor_barChart= view.findViewById(R.id.charsindor_barChart);
         anyChartView= view.findViewById(R.id.charsindor_anyChartView);
         storeAllData= new ArrayList<>();

        InVisiableAxel();
        getDaysReport(url);

        RadioGroup radioGroup = view.findViewById(R.id.charsindor_graph_view);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = radioGroup.findViewById(checkedId);
                int index = radioGroup.indexOfChild(radioButton);

                switch (index) {
                    case 0:
                        AnyChartSetup();
                        charsindor_barChart.setVisibility(View.GONE);
                        anyChartView.setVisibility(View.VISIBLE);
                        VisiableAxel();
                        break;
                    case 1:
                        WeeklyBarChart();
                        anyChartView.setVisibility(View.GONE);
                        charsindor_barChart.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        return view;
    }

    private void LayoutView(){
        linearLayout_1= view.findViewById(R.id.linear_layout_1);
        linearLayout_2= view.findViewById(R.id.linear_layout_2);
        linearLayout_3= view.findViewById(R.id.linear_layout_3);
        linearLayout_4= view.findViewById(R.id.linear_layout_4);
        linearLayout_5= view.findViewById(R.id.linear_layout_5);
        linearLayout_6= view.findViewById(R.id.linear_layout_6);
        linearLayout_7= view.findViewById(R.id.linear_layout_7);
        linearLayout_8= view.findViewById(R.id.linear_layout_8);
        linearLayout_9= view.findViewById(R.id.linear_layout_9);
        linearLayout_10= view.findViewById(R.id.linear_layout_10);
        linearLayout_11= view.findViewById(R.id.linear_layout_11);
        linearLayout_12= view.findViewById(R.id.linear_layout_12);
        linearLayout_13= view.findViewById(R.id.linear_layout_13);
        linearLayout_14= view.findViewById(R.id.linear_layout_14);
        linearLayout_15= view.findViewById(R.id.linear_layout_15);
        linearLayout_16= view.findViewById(R.id.linear_layout_16);
    }

    private void InVisiableAxel(){
        loading_Text.setVisibility(View.VISIBLE);
        linearLayout_1.setVisibility(View.GONE);
        linearLayout_2.setVisibility(View.GONE);
        linearLayout_3.setVisibility(View.GONE);
        linearLayout_4.setVisibility(View.GONE);
        linearLayout_5.setVisibility(View.GONE);
        linearLayout_6.setVisibility(View.GONE);
        linearLayout_7.setVisibility(View.GONE);
        linearLayout_8.setVisibility(View.GONE);
        linearLayout_9.setVisibility(View.GONE);
        linearLayout_10.setVisibility(View.GONE);
        linearLayout_11.setVisibility(View.GONE);
        linearLayout_12.setVisibility(View.GONE);
        linearLayout_13.setVisibility(View.GONE);
        linearLayout_14.setVisibility(View.GONE);
        linearLayout_15.setVisibility(View.GONE);
        linearLayout_16.setVisibility(View.GONE);
    }

    private void VisiableAxel(){
        loading_Text.setVisibility(View.GONE);
        linearLayout_1.setVisibility(View.VISIBLE);
        linearLayout_2.setVisibility(View.VISIBLE);
        linearLayout_3.setVisibility(View.VISIBLE);
        linearLayout_4.setVisibility(View.VISIBLE);
        linearLayout_5.setVisibility(View.VISIBLE);
        linearLayout_6.setVisibility(View.VISIBLE);
        linearLayout_7.setVisibility(View.VISIBLE);
        linearLayout_8.setVisibility(View.VISIBLE);
        linearLayout_9.setVisibility(View.VISIBLE);
        linearLayout_10.setVisibility(View.VISIBLE);
        linearLayout_11.setVisibility(View.VISIBLE);
        linearLayout_12.setVisibility(View.VISIBLE);
        linearLayout_13.setVisibility(View.VISIBLE);
        linearLayout_14.setVisibility(View.VISIBLE);
        linearLayout_15.setVisibility(View.VISIBLE);
        linearLayout_16.setVisibility(View.VISIBLE);
    }

    private void FindAllView(){
        loading_Text= view.findViewById(R.id.loading_text);
        totalTextRickshaw= view.findViewById(R.id.total_text_rickshaw_Van);
        totalTextMotorcycle= view.findViewById(R.id.total_text_motorcycle);
        totalTextFourWheeler= view.findViewById(R.id.total_text_four_wheeler);
        totalTextMicroBus= view.findViewById(R.id.total_text_micro_bus);
        totalTextMiniBus= view.findViewById(R.id.total_text_mini_bus);
        totalTextAgroBus= view.findViewById(R.id.total_text_agro_bus);
        totalTextMiniTruck= view.findViewById(R.id.total_text_mini_truck);
        totalTextBigBus= view.findViewById(R.id.total_text_big_bus);
        totalTextTFWheeler= view.findViewById(R.id.total_text_t_f_wheeler);
        totalTextSeedanCar= view.findViewById(R.id.total_text_seedan_car);
        totalTextMediumTruck= view.findViewById(R.id.total_text_midium_truck);
        totalTextHavvyTruck= view.findViewById(R.id.total_text_havvy_truck);
        totalTextTrailerLong= view.findViewById(R.id.total_text_trailer_long);
        totalTextVip= view.findViewById(R.id.total_text_vip);
        totalTextVehiclesCount= view.findViewById(R.id.total_text_vehicles_count);
        totalTextWithVip= view.findViewById(R.id.total_text_vehicles_with_vip);
    }

    private void LoadAllAxel(){
        totalTextRickshaw.setText(rickshaw);
        totalTextMotorcycle.setText(motorcycle);
        totalTextFourWheeler.setText(wheeler);
        totalTextMicroBus.setText(microbus);
        totalTextMiniBus.setText(minibus);
        totalTextAgroBus.setText(agrobus);
        totalTextMiniTruck.setText(minitruck);
        totalTextBigBus.setText(bigbus);
        totalTextTFWheeler.setText(threefourwheeler);
        totalTextSeedanCar.setText(sedancar);
        totalTextMediumTruck.setText(mediumtruck);
        totalTextHavvyTruck.setText(heavytruck);
        totalTextTrailerLong.setText(trailerlong);

        int r= Integer.parseInt(rickshaw);
        int m= Integer.parseInt(motorcycle);
        int w= Integer.parseInt(wheeler);
        int m1= Integer.parseInt(microbus);
        int m2= Integer.parseInt(minibus);
        int a= Integer.parseInt(agrobus);
        int m3= Integer.parseInt(minitruck);
        int b= Integer.parseInt(bigbus);
        int t= Integer.parseInt(threefourwheeler);
        int s= Integer.parseInt(sedancar);
        int m4= Integer.parseInt(mediumtruck);
        int h= Integer.parseInt(heavytruck);
        int t1= Integer.parseInt(trailerlong);

        total= r+m+w+m1+m2+a+m3+b+t+s+m4+h+t1;
        totalTextVehiclesCount.setText(String.valueOf(total));

        String vipUrl= "http://103.95.99.140/api/yesterdayvippass.php";
        getVipReport(vipUrl);

    }

    private void getVipReport(String url) {
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("allResponseVip", String.valueOf(response.length()));
                totalTextVip.setText(String.valueOf(" "+response.length()));
                totalTextWithVip.setText(String.valueOf(response.length()+total));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "VolleyError"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void WeeklyBarChart(){
        BarDataSet weekDataSet= new BarDataSet(weeklyBarChartValue(),"Weekly Data Set In Last 8 Day's");
        weekDataSet.setColors(Color.BLUE);
        BarData weekPieData= new BarData(weekDataSet);
        charsindor_barChart.setData(weekPieData);
        charsindor_barChart.setDrawBarShadow(true);
        charsindor_barChart.invalidate();
    }

    private ArrayList<BarEntry> weeklyBarChartValue() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference= database.getReference();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        for (int i=1; i<8; i++) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, -i);
            String previousDate = dateFormat.format(new Date(cal.getTimeInMillis()));
            Log.e("Alldate", "date is: "+i+": " + previousDate);

            reference.child("Norshinddi").child(previousDate).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    getTaka= dataSnapshot.child("dayTotalAmount").getValue().toString();
                    subString= getTaka.split("\\stk");
                    storeAllData.add(subString[0]);
                    Log.i("spritcheckzero",String.valueOf(subString[0]));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Error:"+ databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        for (int i=0; i< storeAllData.size(); i++){
            setWeekValue.add(new BarEntry((i+2),Integer.parseInt(storeAllData.get(i))));
            Log.e(TAG, "insert= "+i+1+": " +storeAllData.get(i));
        }
        Log.i("Seekcheck",String.valueOf(setWeekValue.size()));
        storeAllData.clear();
        return setWeekValue;
    }

    private void AnyChartSetup() {
        Pie pie= AnyChart.pie();
        List<DataEntry> entriData= new ArrayList<>();

        entriData.add(new ValueDataEntry("Ricksha Van", Integer.parseInt(rickshaw)));
        entriData.add(new ValueDataEntry("Motorcycle",Integer.parseInt(motorcycle)));
        entriData.add(new ValueDataEntry("4Wheeler",Integer.parseInt(wheeler)));
        entriData.add(new ValueDataEntry("Micro Bus",Integer.parseInt(microbus)));
        entriData.add(new ValueDataEntry("Mini Bus",Integer.parseInt(minibus)));
        entriData.add(new ValueDataEntry("Agro Use",Integer.parseInt(agrobus)));
        entriData.add(new ValueDataEntry("Mini Truck",Integer.parseInt(minitruck)));
        entriData.add(new ValueDataEntry("Big Bus",Integer.parseInt(bigbus)));
        entriData.add(new ValueDataEntry("3 4 Wheeler",Integer.parseInt(threefourwheeler)));
        entriData.add(new ValueDataEntry("Sedan Car",Integer.parseInt(sedancar)));
        entriData.add(new ValueDataEntry("Medium Truck",Integer.parseInt(mediumtruck)));
        entriData.add(new ValueDataEntry("Heavy Truck",Integer.parseInt(heavytruck)));
        entriData.add(new ValueDataEntry("Trailer Long",Integer.parseInt(trailerlong)));
        entriData.add(new ValueDataEntry("Vip Pass",Integer.parseInt(vip)));

        pie.data(entriData);
        anyChartView.setChart(pie);

        LoadAllAxel();

    }

    private void getDaysReport(String url) {
        todayreport = new ArrayList<>();
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("Response", String.valueOf(response.length()));

                if (response.length()!=0){
                    for (int i=0; i<response.length();i++) {
                        try {

                            JSONObject j =  response.getJSONObject(i);
                            //  Log.e("Amount", j.getString("amount"));
                            Norshinddi norshinddi = new Norshinddi(j.getString("Agro_Use"),
                                    j.getString("amount"),
                                    j.getString("Big_Bus"),
                                    j.getString("date_time"),
                                    j.getString("Heavy_Truck"),
                                    j.getString("Medium_Truck"),
                                    j.getString("Micro_Bus"),
                                    j.getString("Mini_Bus"),
                                    j.getString("Mini_Truck"),
                                    j.getString("MotorCycle"),
                                    j.getString("pass_id"),
                                    j.getString("RegNO"),
                                    j.getString("Rickshaw_Van"),
                                    j.getString("Sedan_Car"),
                                    j.getString("three_four_Wheeler"),
                                    j.getString("Trailer_Long"),
                                    j.getString("4Wheeler"));
                            todayreport.add(norshinddi);

                        }catch (Exception p){
                            p.printStackTrace();
                        }
                    }
                    serializeData(todayreport);
                }else {
                    Toast.makeText(getActivity(), "No report Found!", Toast.LENGTH_SHORT).show();
                }
                // dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getNetworkTimeMs();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void serializeData(List<Norshinddi> todayreport) {
        ArrayList<Norshinddi> Rickshaw_Van = new ArrayList<>();
        ArrayList<Norshinddi> MotorCycle = new ArrayList<>();
        ArrayList<Norshinddi> three_four_Wheeler = new ArrayList<>();
        ArrayList<Norshinddi> Sedan_Car = new ArrayList<>();
        ArrayList<Norshinddi> Wheeler = new ArrayList<>();
        ArrayList<Norshinddi> Micro_Bus = new ArrayList<>();
        ArrayList<Norshinddi> Mini_Bus = new ArrayList<>();
        ArrayList<Norshinddi> Agro_Use = new ArrayList<>();
        ArrayList<Norshinddi> Mini_Truck = new ArrayList<>();
        ArrayList<Norshinddi> Big_Bus = new ArrayList<>();
        ArrayList<Norshinddi> Medium_Truck = new ArrayList<>();
        ArrayList<Norshinddi> Heavy_Truck = new ArrayList<>();
        ArrayList<Norshinddi> Trailer_Long = new ArrayList<>();
        ArrayList<Norshinddi> VIP = new ArrayList<>();

        Log.e("LIst :  ", String.valueOf(todayreport.size()));
        for (int i=0; i<todayreport.size();i++){

            if (todayreport.get(i).getMicroBus().equals("1")){
                Micro_Bus.add(todayreport.get(i));

            }else if (todayreport.get(i).getAgroUse().equals("1")){
                Agro_Use.add(todayreport.get(i));

            }else if (todayreport.get(i).getBigBus().equals("1")){
                Big_Bus.add(todayreport.get(i));

            }else if (todayreport.get(i).getHeavyTruck().equals("1")){
                Heavy_Truck.add(todayreport.get(i));

            }else if (todayreport.get(i).getMediumTruck().equals("1")){
                Medium_Truck.add(todayreport.get(i));

            }else if (todayreport.get(i).getMiniBus().equals("1")){
                Mini_Bus.add(todayreport.get(i));

            }else if (todayreport.get(i).getMiniTruck().equals("1")){
                Mini_Truck.add(todayreport.get(i));

            }else if (todayreport.get(i).getMotorCycle().equals("1")){
                MotorCycle.add(todayreport.get(i));

            }else if (todayreport.get(i).getRickshawVan().equals("1")){
                Rickshaw_Van.add(todayreport.get(i));

            }else if (todayreport.get(i).getSedanCar().equals("1")){
                Sedan_Car.add(todayreport.get(i));

            }else if (todayreport.get(i).getThreeFourWheeler().equals("1")){
                three_four_Wheeler.add(todayreport.get(i));

            }else if (todayreport.get(i).getTrailerLong().equals("1")){
                Trailer_Long.add(todayreport.get(i));

            }else if (todayreport.get(i).getWheeler().equals("1")){
                Wheeler.add(todayreport.get(i));

            }else {
                VIP.add(todayreport.get(i));
                //  Log.e("NUll ","error" );
            }
        }

        int rickshaw_total,motorcycle_total, wheeler_total, microbus_total, minibus_total, agrobus_total, minitruck_total, bigbus_total,
                threefourwheeler_total,sedancar_total,mediumtruck_total,heavytruck_total,trailerlong_total, vip_total, grand_total;


        rickshaw = String.valueOf(Rickshaw_Van.size());
        motorcycle = String.valueOf(MotorCycle.size());
        wheeler = String.valueOf(Wheeler.size());
        microbus = String.valueOf(Micro_Bus.size());
        minibus = String.valueOf(Mini_Bus.size());
        agrobus = String.valueOf(Agro_Use.size());
        minitruck =  String.valueOf(Mini_Truck.size());
        bigbus = String.valueOf(Big_Bus.size());
        threefourwheeler = String.valueOf(three_four_Wheeler.size());
        sedancar =  String.valueOf(Sedan_Car.size());
        mediumtruck = String.valueOf(Medium_Truck.size());
        heavytruck = String.valueOf(Heavy_Truck.size());
        trailerlong = String.valueOf(Trailer_Long.size());
        vip = String.valueOf(VIP.size());

        Log.e("Rickshaw_Van", rickshaw);
        Log.e("MotorCycle", motorcycle);
        Log.e("Wheeler ", wheeler);
        Log.e("Micro_Bus ", microbus);
        Log.e("Mini_Bus ", minibus);
        Log.e("Agro_Use ", agrobus);
        Log.e("Mini_Truck ", minitruck);
        Log.e("Big_Bus ", bigbus);
        Log.e("three_four_Wheeler ",threefourwheeler);
        Log.e("Sedan_Car ", sedancar);
        Log.e("Medium_Truck ", mediumtruck);
        Log.e("Heavy_Truck ",heavytruck);
        Log.e("Trailer_Long ", trailerlong);
        Log.e("Vip ", vip);

    }
}
