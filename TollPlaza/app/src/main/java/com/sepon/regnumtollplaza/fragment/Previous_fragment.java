package com.sepon.regnumtollplaza.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.common.reflect.TypeToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.sepon.regnumtollplaza.R;
import com.sepon.regnumtollplaza.adapter.PreviousAdapter;
import com.sepon.regnumtollplaza.adapter.TodayAdapter;
import com.sepon.regnumtollplaza.admin.Report;
import com.sepon.regnumtollplaza.pojo.Norshinddi;
import com.sepon.regnumtollplaza.pojo.PreviousDetails;
import com.sepon.regnumtollplaza.pojo.Previous_pojo;
import com.sepon.regnumtollplaza.pojo.Tali;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Previous_fragment extends Fragment {
    private PreviousAdapter previousAdapter;
    private RecyclerView previousRecyclerview;
    private ArrayList<PreviousDetails> previousDetailsList;// = new ArrayList<>();
    private String thisDate, shareDate;
    private DatabaseReference databaseReference;
    private String url = "http://103.95.99.140/api/yesterday.php";
    private List<Norshinddi> todayreport;// = new ArrayList<>();

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    ArrayList<PreviousDetails> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.previous_fragment, container, false);

        previousRecyclerview = view.findViewById(R.id.previous_recyclerview);

        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        Date todayDate = new Date();
        thisDate = currentDate.format(todayDate);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Norshinddi");
        getDaysReport(url);

        return view;
    }

    private void getPreviousReportFromFirebase() {
      FirebaseDatabase  database = FirebaseDatabase.getInstance();
      DatabaseReference databaseReference = database.getReference("Norshinddi");
      databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                previousDetailsList = new ArrayList<>();
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    PreviousDetails previousDetails = singleSnapshot.getValue(PreviousDetails.class);
                    //assert previousDetails != null;
                    Log.e("p: ", previousDetails.getDate());
                    Log.e("p: ", previousDetails.getDayTotalAmount());
                    Log.e("p: ", previousDetails.getVichelAmount());
                    previousDetailsList.add(previousDetails);
                }

                previousAdapter = new PreviousAdapter(previousDetailsList, getContext());
                previousRecyclerview.setAdapter(previousAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Database Error: "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDaysReport(String url) {
        //   arrayList = new ArrayList<>();
        todayreport = new ArrayList<>();
      //  final ProgressDialog dialog = ProgressDialog.show(getActivity(), "Getting Current Report From Server", "Please wait...", true);

        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("Response", String.valueOf(response.length()));

                for (int i=0; i<response.length();i++){
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

                //   Log.e("List Today ", String.valueOf(todayreport.size()));
                serializeData(todayreport);
              //  dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void serializeData(List<Norshinddi> todayreport) {
     //   final ProgressDialog dialog = ProgressDialog.show(getActivity(), "Serialize Data ", "Please wait...", true);
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

        //Log.e("LIst :  ",total_vichel);
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
        String total_vichel = String.valueOf(todayreport.size()-VIP.size());

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
        vip = String.valueOf(VIP.size());

        Log.e("P_Rickshaw_Van", rickshaw);
        Log.e(" P_MotorCycle", motorcycle);
        Log.e("P_Wheeler ", wheeler);
        Log.e("P_Micro_Bus ", microbus);
        Log.e("P_Mini_Bus ", minibus);
        Log.e("P_Agro_Use ", agrobus);
        Log.e("P_Mini_Truck ", minitruck);
        Log.e("P_Big_Bus ", bigbus);
        Log.e("P_three_four_Wheeler ",threefourwheeler);
        Log.e("P_Sedan_Car ", sedancar);
        Log.e("P_Medium_Truck ", mediumtruck);
        Log.e("P_Heavy_Truck ",heavytruck);
        Log.e("P_Trailer_Long ", trailerlong);
        Log.e("P_Vip ", vip);

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

        Log.e("rickshaw_total", String.valueOf(rickshaw_total));
        Log.e(" motorcycle_total", String.valueOf(motorcycle_total));
        Log.e("wheeler_total ", String.valueOf(wheeler_total));
        Log.e("microbus_total ", String.valueOf(microbus_total));
        Log.e("minibus_total ", String.valueOf(minibus_total));
        Log.e("agrobus_total ", String.valueOf(agrobus_total));
        Log.e("minitruck_total ", String.valueOf(minitruck_total));
        Log.e("bigbus_total ", String.valueOf(bigbus_total));
        Log.e("threefourwheeler_total ", String.valueOf(threefourwheeler_total));
        Log.e("sedancar_total ", String.valueOf(sedancar_total));
        Log.e("mediumtruck_total ", String.valueOf(mediumtruck_total));
        Log.e("heavytruck_total ", String.valueOf(heavytruck_total));
        Log.e("trailerlong_total ", String.valueOf(trailerlong_total));

        grand_total = rickshaw_total+motorcycle_total+wheeler_total+microbus_total+minibus_total+agrobus_total+minitruck_total+bigbus_total+threefourwheeler_total+sedancar_total+mediumtruck_total+heavytruck_total+trailerlong_total;


        /*String yesterday = getYesterday();
        int storePreviousTaka= grand_total;
        PreviousDetails previousDetails1 = new PreviousDetails(yesterday, total_vichel,grand_total+" tk");
        myRef.child(yesterday).setValue(previousDetails1);
        //storeDatetosharepref(thisDate);
        getPreviousReportFromFirebase();*/

        String getYesterdayDate= getYesterday();
        PreviousDetails previousDetailsDataSetFirebase= new PreviousDetails(getYesterdayDate, total_vichel, grand_total+ " tk");
        myRef.child(getYesterdayDate).setValue(previousDetailsDataSetFirebase);
        getPreviousReportFromFirebase();

    }

    /*public void storeDatetosharepref(String date){
        SharedPreferences sharedPref2= getSharedPreferences("STORE_PRODUCT_DETAILS_DATA", Context.MODE_PRIVATE);
        editor.putString("date", date);
        editor.apply();
    }*/

    public void getstoreDatetosharepref(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        shareDate = prefs.getString("date", "");
    }

    public void saveArrayList(ArrayList<PreviousDetails> list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!

    }

    public ArrayList<PreviousDetails> getArrayList(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<PreviousDetails>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public String getYesterday(){
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String date = dateFormat.format(cal.getTime());
        Log.i("Show_Data", date);
        return date;
    }

}
