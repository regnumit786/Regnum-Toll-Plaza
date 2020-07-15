package com.sepon.regnumtollplaza.fragment.chittagong;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.common.reflect.TypeToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.sepon.regnumtollplaza.AxelDetailsActivity;
import com.sepon.regnumtollplaza.R;
import com.sepon.regnumtollplaza.admin.NewReport;
import com.sepon.regnumtollplaza.admin.Report;
import com.sepon.regnumtollplaza.pojo.Regular;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class  Today_Chittagong_fragment extends Fragment implements View.OnClickListener {

    private CardView card2,card3,card4,card5,card6,card7;
    private TextView r1,r2,r3,r4,r5,r6,cr1,cr2,cr3,cr4,cr5,cr6;
    private String a2,a3,a4,a5,a6,a7;
    private String c2,c3,c4,c5,c6,c7;
    private int ct,rt;

    private String thisDate, shareDate;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    TextView today_toptext;
    ArrayList<Report> allctrlReport;
    ArrayList<Report> test;

    private ArrayList<Report> ca2,ca3,ca4,ca5,ca6,ca7,catotal;
    private ArrayList<Report> ra2,ra3,ra4,ra5,ra6,ra7,ratotal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.today_chittagong_fragment, container, false);

        initilize(view);

        getDate();
        getstoreDatetosharepref();
        Log.e("share ", shareDate);
        isTodayReportAvillable();

        return view;
    }

    private void isTodayReportAvillable() {

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("chittagong").child(thisDate);
        ProgressDialog dialog = ProgressDialog.show(getActivity(), "Checking todays report", "Please wait...", true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    dialog.dismiss();

                    getRegularTotal();
                    getCtrlRdata();
                    storeDatetosharepref(thisDate);

                }else {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "report not update yet", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
            }
        });
    }

    private void getRegularTotal(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("chittagong").child(thisDate).child("RegularReport");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Regular regular = dataSnapshot.getValue(Regular.class);
                setInfo(regular);
                saveRegular(regular);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setInfo(Regular regular) {
        rt = Integer.parseInt(regular.getAxelT());
        Log.e("axel2", regular.getAxel2());
        r1.setText("Regular : "+regular.getAxel2());

        Log.e("axel3", regular.getAxel3());
        r2.setText("Regular : "+regular.getAxel3());

        Log.e("axel4", regular.getAxel4());
        r3.setText("Regular : "+regular.getAxel4());

        Log.e("axel5", regular.getAxel5());
        r4.setText("Regular : "+regular.getAxel5());

        Log.e("axel6", regular.getAxel6());
        r5.setText("Regular : "+regular.getAxel6());

        Log.e("axel7", regular.getAxel7());
        r6.setText("Regular : "+regular.getAxel7());

    }

    private void getRegularReportFromFirebase() {
         ArrayList<Report> regularReport2 = new ArrayList<>();
         ArrayList<Report> regularReport3 = new ArrayList<>();
         ArrayList<Report> regularReport4 = new ArrayList<>();
         ArrayList<Report> regularReport5 = new ArrayList<>();
         ArrayList<Report> regularReport6 = new ArrayList<>();
         ArrayList<Report> regularReport7 = new ArrayList<>();
         ArrayList<Report> totallaxel = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("chittagong").child(thisDate).child("RegularReport");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Report report = singleSnapshot.getValue(Report.class);
                    String tc = report.getTcClass();
                    //Truck 2 Axle
                    if (tc.equals("Truck 2 Axle")){
                        regularReport2.add(report);
                    }else if (tc.equals("Truck 3 Axle")){
                        regularReport3.add(report);
                    }else if (tc.equals("Truck 4 Axle")){
                        regularReport4.add(report);
                    }else if (tc.equals("Truck 5 Axle")){
                        regularReport5.add(report);
                    }else if (tc.equals("Truck 6 Axle")){
                        regularReport6.add(report);
                    }else if (tc.equals("Truck 7 Axle")){
                        regularReport7.add(report);
                    }else {

                    }
                    totallaxel.add(report);

                }
                rt = totallaxel.size();
                a2 = String.valueOf(regularReport2.size());
                a3 = String.valueOf(regularReport3.size());
                a4 = String.valueOf(regularReport4.size());
                a5 = String.valueOf(regularReport5.size());
                a6 = String.valueOf(regularReport6.size());
                a7 = String.valueOf(regularReport7.size());

                Log.e("totaltr", String.valueOf(rt));
                //toptext.setText("Total Axel Report: "+String.valueOf(regularReport.size()));

                Log.e("axel2", a2);
                r1.setText("Regular : "+a2);

                Log.e("axel3", a3);
                r2.setText("Regular : "+a3);

                Log.e("axel4", a4);
                r3.setText("Regular : "+a4);

                Log.e("axel5", a5);
                r4.setText("Regular : "+a5);

                Log.e("axel6", a6);
                r5.setText("Regular : "+a6);

                Log.e("axel7", a7);
                r6.setText("Regular : "+a7);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCtrlRdata() {
         ArrayList<NewReport> ctrlReport2 = new ArrayList<>();
         ArrayList<NewReport> ctrlReport3 = new ArrayList<>();
         ArrayList<NewReport> ctrlReport4 = new ArrayList<>();
         ArrayList<NewReport> ctrlReport5 = new ArrayList<>();
         ArrayList<NewReport> ctrlReport6 = new ArrayList<>();
         ArrayList<NewReport> ctrlReport7 = new ArrayList<>();
         ArrayList<NewReport> allctrlReport = new ArrayList<>();

        /**
         * Error : android.content.res.Resources$Theme android.content.Context.getTheme()
         */
        ProgressDialog dialog = ProgressDialog.show(getActivity(), "Getting Ctrl+R Report From Server", "Please wait...", true);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("chittagong").child(thisDate).child("ctrlReport");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    NewReport report = singleSnapshot.getValue(NewReport.class);
                    String tc = report.getD();
                    if (tc != null){
                        //Truck 2 Axle
                        if (tc.equals("Truck 2 Axle")){
                            ctrlReport2.add(report);
                        }else if (tc.equals("Truck 3 Axle")){
                            ctrlReport3.add(report);
                        }else if (tc.equals("Truck 4 Axle")){
                            ctrlReport4.add(report);
                        }else if (tc.equals("Truck 5 Axle")){
                            ctrlReport5.add(report);
                        }else if (tc.equals("Truck 6 Axle")){
                            ctrlReport5.add(report);
                        }else if (tc.equals("Truck 7 Axle")){
                            ctrlReport7.add(report);
                        }else {

                        }
                    }

                    allctrlReport.add(report);
                    ct = allctrlReport.size();
                    Log.e("report insert", String.valueOf(ct));

                }

                c2  = String.valueOf(ctrlReport2.size());
                Log.e("axel2", c2);
                cr1.setText("Ctrl+R  : "+c2);

                c3 = String.valueOf(ctrlReport3.size());
                Log.e("axel3", c3);
                cr2.setText("Ctrl+R  : "+c3);

                c4 = String.valueOf(ctrlReport4.size());
                Log.e("axel4", c4);
                cr3.setText("Ctrl+R  : "+c4);

                c5 = String.valueOf(ctrlReport5.size());
                Log.e("axel5", c5);
                cr4.setText("Ctrl+R  : "+c5);

                c6 = String.valueOf(ctrlReport6.size());
                Log.e("axel6", c6);
                cr5.setText("Ctrl+R  : "+c6);

                c7 = String.valueOf(ctrlReport7.size());
                Log.e("axel7", c7);
                cr6.setText("Ctrl+R  : "+c7);

                int tl = ct+rt;
                today_toptext.setText("Total: "+tl+",    Total Crtl+R : "+ct+",      Total Regular : "+rt);
                dialog.dismiss();

                //store list to sharePreference
                saveArrayList(allctrlReport, "ctrl");
                saveArrayList(ctrlReport2, "ctrlA2");
                saveArrayList(ctrlReport3, "ctrlA3");
                saveArrayList(ctrlReport4, "ctrlA4");
                saveArrayList(ctrlReport5, "ctrlA5");
                saveArrayList(ctrlReport6, "ctrlA6");
                saveArrayList(ctrlReport7, "ctrlA7");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDate(){
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        Date todayDate = new Date();
        thisDate = currentDate.format(todayDate);
    }

    private void initilize(View view) {
        r1 = view.findViewById(R.id.axel2amount);
        r2 = view.findViewById(R.id.axel3amount);
        r3 = view.findViewById(R.id.axel4amount);
        r4 = view.findViewById(R.id.axel5amount);
        r5 = view.findViewById(R.id.axel6amount);
        r6 = view.findViewById(R.id.axel7amount);

        cr1 = view.findViewById(R.id.axectr2Ramount);
        cr2 = view.findViewById(R.id.axectrl3Ramount);
        cr3 = view.findViewById(R.id.axectrl4Ramount);
        cr4 = view.findViewById(R.id.axectrl5Ramount);
        cr5 = view.findViewById(R.id.axectrl6Ramount);
        cr6 = view.findViewById(R.id.axectrl7Ramount);

        today_toptext = view.findViewById(R.id.today_toptext);
    }

    public void saveArrayList(ArrayList<NewReport> list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();

    }

    public void saveRegular(Regular regular){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(regular);
        editor.putString("regular", json);
        editor.apply();
    }

    public Regular getRegular(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Gson gson = new Gson();
        String json = prefs.getString("regular", null);
        Regular regular = gson.fromJson(json, Regular.class);
        return regular;


    }

    public ArrayList<Report> getArrayList(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<Report>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void storeDatetosharepref(String date){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("date", date);
        editor.apply();
    }

    public void getstoreDatetosharepref(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        shareDate = prefs.getString("date", "");
    }

    @SuppressLint("SetTextI18n")
    private void getsharePreferencedata2() {
        ca2 = new ArrayList<>();
        ca2 = getArrayList("ctrlA2");

        ca3 = new ArrayList<>();
        ca3 = getArrayList("ctrlA3");

        ca4 = new ArrayList<>();
        ca4 = getArrayList("ctrlA4");

        ca5 = new ArrayList<>();
        ca5 = getArrayList("ctrlA5");

        ca6 = new ArrayList<>();
        ca6 = getArrayList("ctrlA6");

        ca7 = new ArrayList<>();
        ca7 = getArrayList("ctrlA7");

        catotal = new ArrayList<>();
        catotal = getArrayList("ctrl");

        c2 = String.valueOf(ca2.size());
        c3 = String.valueOf(ca3.size());
        c4 = String.valueOf(ca4.size());
        c5 = String.valueOf(ca5.size());
        c6 = String.valueOf(ca6.size());
        c7 = String.valueOf(ca7.size());
        ct = catotal.size();
        int tl = ct+rt;


        today_toptext.setText("Total: "+tl+",   Crtl+R : "+ct+",   Regular : "+rt);
        cr1.setText("Ctrl+R   : "+c2);
        cr2.setText("Ctrl+R   : "+c3);
        cr3.setText("Ctrl+R   : "+c4);
        cr4.setText("Ctrl+R   : "+c5);
        cr5.setText("Ctrl+R   : "+c6);
        cr6.setText("Ctrl+R   : "+c7);

    }

    private void getsharePreferencedata1() {
        ra2 = new ArrayList<>();
        ra2 = getArrayList("regularA2");

        ra3 = new ArrayList<>();
        ra3 = getArrayList("regularA3");

        ra4 = new ArrayList<>();
        ra4 = getArrayList("regularA4");

        ra5 = new ArrayList<>();
        ra5 = getArrayList("regularA5");

        ra6 = new ArrayList<>();
        ra6 = getArrayList("regularA6");

        ra7 = new ArrayList<>();
        ra7 = getArrayList("regularA7");

        ratotal = new ArrayList<>();
        ratotal = getArrayList("regular");

        rt = ratotal.size();
        a2 = String.valueOf(ra2.size());
        a3 = String.valueOf(ra3.size());
        a4 = String.valueOf(ra4.size());
        a5 = String.valueOf(ra5.size());
        a6 = String.valueOf(ra6.size());
        a7 = String.valueOf(ra7.size());


        r1.setText("Regular : "+a2);
        r2.setText("Regular : "+a3);
        r3.setText("Regular : "+a4);
        r4.setText("Regular : "+a5);
        r5.setText("Regular : "+a6);
        r6.setText("Regular : "+a7);

       }

    public void initizCard(View view){
        card2 = view.findViewById(R.id.c2);
            card2.setOnClickListener(this);
        card3 = view.findViewById(R.id.c3);
          card3.setOnClickListener(this);
        card4 = view.findViewById(R.id.c4);
          card4.setOnClickListener(this);
        card5 = view.findViewById(R.id.c5);
          card5.setOnClickListener(this);
        card6 = view.findViewById(R.id.c6);
          card6.setOnClickListener(this);
        card7 = view.findViewById(R.id.c7);
          card7.setOnClickListener(this);

      }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.c2:
                axelDetailsActivity("ctrlA2");
                break;
            case R.id.c3:
                axelDetailsActivity("ctrlA3");
                break;
            case R.id.c4:
                axelDetailsActivity("ctrlA4");
                break;
            case R.id.c5:
                axelDetailsActivity("ctrlA5");
                break;
            case R.id.c6:
                axelDetailsActivity("ctrlA6");
                break;
            case R.id.c7:
                axelDetailsActivity("ctrlA7");
                break;
        }

    }

    public void axelDetailsActivity(String axel){
        int t  = getArrayList(axel).size();
        if (t ==0){
            Toast.makeText(getActivity(), "No Car Found", Toast.LENGTH_SHORT).show();
        }else {
            Intent i = new Intent(getActivity(), AxelDetailsActivity.class);
            i.putExtra("axel", axel);
            startActivity(i);
        }

    }
}
