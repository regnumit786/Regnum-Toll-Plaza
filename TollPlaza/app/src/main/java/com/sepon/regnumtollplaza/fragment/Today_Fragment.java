package com.sepon.regnumtollplaza.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.sepon.regnumtollplaza.ChorshindduActivity;
import com.sepon.regnumtollplaza.R;
import com.sepon.regnumtollplaza.adapter.TodayAdapter;
import com.sepon.regnumtollplaza.pojo.Norshinddi;
import com.sepon.regnumtollplaza.pojo.Tali;
import com.sepon.regnumtollplaza.utility.MyReceiver;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Today_Fragment extends Fragment {
    private BroadcastReceiver MyReceiver = null;
    private List<Tali> taliList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView grandtotal;
    private List<Norshinddi> todayreport;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tali_layout, container, false);

        recyclerView = view.findViewById(R.id.tali_recyclerview);
        grandtotal = view.findViewById(R.id.grand_total);

        MyReceiver = new MyReceiver();

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("kk:mm");
        dateFormat.format(date);

        Log.e("Time====", dateFormat.format(date));

        try {
            if(dateFormat.parse(dateFormat.format(date)).after(dateFormat.parse("00:00")) &&
                    dateFormat.parse(dateFormat.format(date)).before(dateFormat.parse("07:00")))
            {
                //show yesterday data in running fund
                String url = "http://103.197.206.140/api/yesterday.php";
                Log.e("Running URL", url);
                getDaysReport(url);

            }else{
                //show today data in running fund
                String url = "http://103.197.206.140/api/today.php";
                Log.e("Running URL", url);
                getDaysReport(url);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return view;
    }

    private void getDaysReport(String url) {
     //   arrayList = new ArrayList<>();
        todayreport = new ArrayList<>();
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "Getting Current Report From Server", "Please wait...", true);

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

                        }
                    }
                    serializeData(todayreport);
                    dialog.dismiss();

                }else {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "No report Found!", Toast.LENGTH_SHORT).show();
                }
               // dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void serializeData(List<Norshinddi> todayreport) {
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "Serialize Data ", "Please wait...", true);

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

            }else {
                Wheeler.add(todayreport.get(i));
            }
        }

        String rickshaw,motorcycle, wheeler, microbus, minibus, agrobus, minitruck,bigbus,threefourwheeler,sedancar,mediumtruck,heavytruck,trailerlong, vip;
        int rickshaw_total,motorcycle_total, wheeler_total, microbus_total, minibus_total, agrobus_total, minitruck_total,bigbus_total,threefourwheeler_total,sedancar_total,mediumtruck_total,heavytruck_total,trailerlong_total, vip_total, grand_total;

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

        rickshaw_total = Integer.parseInt(rickshaw)*5;
        motorcycle_total = Integer.parseInt(motorcycle)*10;
        wheeler_total = Integer.parseInt(wheeler)*60;
        microbus_total = Integer.parseInt(microbus)*60;
        minibus_total = Integer.parseInt(minibus)*75;
        agrobus_total = Integer.parseInt(agrobus)*90;
        minitruck_total = Integer.parseInt(minitruck)*115;
        bigbus_total = Integer.parseInt(bigbus)*135;
        threefourwheeler_total = Integer.parseInt(threefourwheeler)*15;
        sedancar_total = Integer.parseInt(sedancar)*40;
        mediumtruck_total = Integer.parseInt(mediumtruck)*150;
        heavytruck_total = Integer.parseInt(heavytruck)*300;
        trailerlong_total = Integer.parseInt(trailerlong)*375;

        grand_total = rickshaw_total+motorcycle_total+wheeler_total+microbus_total+minibus_total+agrobus_total+minitruck_total+bigbus_total+threefourwheeler_total+sedancar_total+mediumtruck_total+heavytruck_total+trailerlong_total;
        grandtotal.setText("Running Fund: "+grand_total+" tk");

        Tali tali = new Tali("Rickshaw Van", rickshaw,  String.valueOf(rickshaw_total), R.drawable.rickshaw, "5tk");
        Tali tali1 = new Tali("MotorCycle", motorcycle, String.valueOf(motorcycle_total), R.drawable.bike, "10tk");
        Tali tali2 = new Tali("Three Four Wheeler", threefourwheeler,String.valueOf(threefourwheeler_total), R.drawable.threefourwheeler, "15tk");
        Tali tali3 = new Tali("Sedan Car", sedancar,    String.valueOf(sedancar_total), R.drawable.sedan, "40tk");
        Tali tali4 = new Tali("Four Wheeler", wheeler,   String.valueOf(wheeler_total), R.drawable.fourwheeler, "60tk");
        Tali tali5 = new Tali("Micro Bus", microbus,    String.valueOf(microbus_total), R.drawable.microbus, "60tk");
        Tali tali6 = new Tali("Mini Bus", minibus,  String.valueOf(minibus_total), R.drawable.minibus, "75tk");
        Tali tali7 = new Tali("Agro Use", agrobus,  String.valueOf(agrobus_total), R.drawable.agro, "90tk");
        Tali tali8 = new Tali("Mini Truck", minitruck,  String.valueOf(minitruck_total), R.drawable.axel2, "115tk");
        Tali tali9 = new Tali("Big Bus", bigbus,    String.valueOf(bigbus_total), R.drawable.bus, "135tk");
        Tali tali10 = new Tali("Medium Truck", mediumtruck, String.valueOf(mediumtruck_total), R.drawable.mediumtruck, "150tk");
        Tali tali11 = new Tali("Heavy Truck", heavytruck,   String.valueOf(heavytruck_total), R.drawable.axel4, "300tk");
        Tali tali12 = new Tali("Trailer Long", trailerlong,  String.valueOf(trailerlong_total), R.drawable.axel6, "375tk");

        taliList.add(tali);
        taliList.add(tali1);
        taliList.add(tali2);
        taliList.add(tali3);
        taliList.add(tali4);
        taliList.add(tali5);
        taliList.add(tali6);
        taliList.add(tali7);
        taliList.add(tali8);
        taliList.add(tali9);
        taliList.add(tali10);
        taliList.add(tali11);
        taliList.add(tali12);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        TodayAdapter taliAdapter = new TodayAdapter(taliList, getActivity());
        recyclerView.setAdapter(taliAdapter);

        dialog.dismiss();
    }
}
