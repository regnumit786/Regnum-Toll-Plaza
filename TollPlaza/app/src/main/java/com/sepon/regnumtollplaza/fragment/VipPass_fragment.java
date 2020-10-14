package com.sepon.regnumtollplaza.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sepon.regnumtollplaza.R;
import com.sepon.regnumtollplaza.adapter.PreviousVipPassAdapter;
import com.sepon.regnumtollplaza.adapter.TodayAdapter;
import com.sepon.regnumtollplaza.pojo.Norshinddi;
import com.sepon.regnumtollplaza.pojo.Tali;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import lecho.lib.hellocharts.model.Line;
public class VipPass_fragment extends Fragment {
    private List<Tali> taliList = new ArrayList<>();
    private RecyclerView currentRecyclerView, previousRecyclerView;
    private TextView grandtotal;
    private List<Norshinddi> todayreport;
    private LinearLayout currentLayout, previousLayout;
    private View view;
    private String yesterdayAsString, vipDate, vipValue;
    private PreviousVipPassAdapter previousVipPassAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_vippass, container, false);

        FindView();
        currentRecyclerView.setHasFixedSize(true);
        currentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        previousRecyclerView.setHasFixedSize(true);
        previousRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("kk:mm", Locale.getDefault());
        dateFormat.format(date);

        Log.e("Time====", dateFormat.format(date));

        try {
            if(dateFormat.parse(dateFormat.format(date)).after(dateFormat.parse("00:00")) &&
                    dateFormat.parse(dateFormat.format(date)).before(dateFormat.parse("07:00")))
            {
                //show yesterday data in running fund
                String url = "http://103.95.99.140/api/yesterdayvippass.php";
                Log.e("Running URL", url);
                getDaysReport(url);

            }else{
                //show today data in running fund
                String url = "http://103.95.99.140/api/vippass.php";
                Log.e("Running URL", url);
                getDaysReport(url);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SetPreviousVipPass();

        RadioGroup radioGroup = view.findViewById(R.id.rd_group_vip_pass);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                View radioButton = radioGroup.findViewById(checkedId);
                int index = radioGroup.indexOfChild(radioButton);
                switch (index){
                    case 0:
                        previousLayout.setVisibility(View.GONE);
                        currentLayout.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        currentLayout.setVisibility(View.GONE);
                        previousLayout.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        return view;
    }

    private void FindView(){
        currentRecyclerView= view.findViewById(R.id.recyclerview_current_vip);
        previousRecyclerView= view.findViewById(R.id.recyclerview_previous_vip);
        grandtotal= view.findViewById(R.id.grand_total_count);
        currentLayout= view.findViewById(R.id.current_layout);
        previousLayout= view.findViewById(R.id.previous_layout);
    }

    private void SetPreviousVipPass(){
        FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
        DatabaseReference databaseReference= firebaseDatabase.getReference();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        for (int i=1; i<4; i++){
            Date todayDate= new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(todayDate);
            calendar.add(Calendar.DATE, -i);
            yesterdayAsString = dateFormat.format(calendar.getTime());
            Log.e("VipPreviousData", "date is: "+yesterdayAsString);

            databaseReference.child("Norshinddi").child("PreviousVip").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                   vipValue= Objects.requireNonNull(snapshot.child(yesterdayAsString).getValue()).toString();
                   Log.i("VipPassValue", vipValue);
                   previousVipPassAdapter= new PreviousVipPassAdapter(getContext(),yesterdayAsString, vipValue);
                   previousRecyclerView.setAdapter(previousVipPassAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Previous Vip Pass Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("Previous_Vip_Pass_Error", error.getMessage());
                }
            });
        }
    }

    private void getDaysReport(String url) {
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

                        }catch (Exception exception){
                            Toast.makeText(getContext(), "Exception: "+exception.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "Request Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
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

        int rickshaw,motorcycle, wheeler, microbus, minibus, agrobus, minitruck,bigbus,threefourwheeler,sedancar,mediumtruck,heavytruck,trailerlong, vip;

        rickshaw = Rickshaw_Van.size();
        motorcycle = MotorCycle.size();
        wheeler = Wheeler.size();
        microbus = Micro_Bus.size();
        minibus = Mini_Bus.size();
        agrobus = Agro_Use.size();
        minitruck =  Mini_Truck.size();
        bigbus = Big_Bus.size();
        threefourwheeler = three_four_Wheeler.size();
        sedancar =  Sedan_Car.size();
        mediumtruck = Medium_Truck.size();
        heavytruck = Heavy_Truck.size();
        trailerlong = Trailer_Long.size();

        int grand_total_count = rickshaw+motorcycle+wheeler+microbus+minibus+agrobus+minitruck+bigbus+threefourwheeler+sedancar+mediumtruck+heavytruck+trailerlong;
        grandtotal.setText("Total VIP Pass: "+grand_total_count);
        Log.i("Total_vip_pass", String.valueOf(grand_total_count));

        /*SharedPreferences preferences= getContext().getSharedPreferences("store_vip_pass", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= preferences.edit();
        editor.putString("vip_data", String.valueOf(grand_total_count));
        editor.apply();
        editor.commit();
        Log.i("Vip_pass_data", String.valueOf(grand_total_count));*/

        Tali tali = new Tali("Rickshaw Van", String.valueOf(rickshaw),  "0", R.drawable.rickshaw, "5tk");
        Tali tali1 = new Tali("MotorCycle", String.valueOf(motorcycle), "0", R.drawable.bike, "10tk");
        Tali tali2 = new Tali("Three Four Wheeler", String.valueOf(threefourwheeler), "0" , R.drawable.threefourwheeler, "15tk");
        Tali tali3 = new Tali("Sedan Car", String.valueOf(sedancar), "0" , R.drawable.sedan, "40tk");
        Tali tali4 = new Tali("Four Wheeler", String.valueOf(wheeler),   "0", R.drawable.fourwheeler, "60tk");
        Tali tali5 = new Tali("Micro Bus", String.valueOf(microbus),    "0", R.drawable.microbus, "60tk");
        Tali tali6 = new Tali("Mini Bus", String.valueOf(minibus),  "0", R.drawable.minibus, "75tk");
        Tali tali7 = new Tali("Agro Use", String.valueOf(agrobus), "0" , R.drawable.agro, "90tk");
        Tali tali8 = new Tali("Mini Truck", String.valueOf(minitruck), "0" , R.drawable.axel2, "115tk");
        Tali tali9 = new Tali("Big Bus", String.valueOf(bigbus), "0" , R.drawable.bus, "135tk");
        Tali tali10 = new Tali("Medium Truck", String.valueOf(mediumtruck), "0" , R.drawable.mediumtruck, "150tk");
        Tali tali11 = new Tali("Heavy Truck", String.valueOf(heavytruck), "0" , R.drawable.axel4, "300tk");
        Tali tali12 = new Tali("Trailer Long", String.valueOf(trailerlong), "0" , R.drawable.axel6, "375tk");

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

        currentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        currentRecyclerView.setHasFixedSize(true);
        TodayAdapter taliAdapter = new TodayAdapter(taliList, getActivity());
        currentRecyclerView.setAdapter(taliAdapter);

        dialog.dismiss();
    }
}
