package com.sepon.regnumtollplaza.fragment.chittagong;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.google.common.reflect.TypeToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.sepon.regnumtollplaza.R;
import com.sepon.regnumtollplaza.adapter.ChittagongPreviousAdapter;
import com.sepon.regnumtollplaza.admin.Short;
import com.sepon.regnumtollplaza.pojo.Previous_pojo;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Regular_fragment extends Fragment {

    private String thisDate, shareDate;
    private static String TAG = "Regular_fragment";
    Previous_pojo previous_pojo;
    ArrayList<Previous_pojo> list;
    ArrayList<Previous_pojo> list2 = new ArrayList<>();
    private RecyclerView recyclerView;
    ChittagongPreviousAdapter adapter;
    LinearLayout linearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.regular_axel_layout, container, false);

        recyclerView = view.findViewById(R.id.chittagong_previous);
        list = new ArrayList<>();

        getDate();
        Log.e(TAG, "Get data");

        for (int i = 1; i <= 7; i++) {
            String yes = yesterday(i);
            getInfo(yes);
        }
        Log.e(TAG, "List size: " + list.size());
        adapter = new ChittagongPreviousAdapter(list, getActivity());
        recyclerView.setAdapter(adapter);
        saveArrayList(list, "previous_report");

        return view;
    }

    private void getDate(){

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        Date todayDate = new Date();
        thisDate = currentDate.format(todayDate);
    }

    public void getstoreDatetosharepref(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        shareDate = prefs.getString("date", "");
    }

    public ArrayList<Previous_pojo> getArrayList(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<Previous_pojo>>() {}.getType();
        return gson.fromJson(json, type);
    }

    private void getInfo(String yes) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference reference = database.getReference("chittagong");
        DatabaseReference reference = database.getReference("chittagong").child(yes).child("short");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Short sh = dataSnapshot.getValue(Short.class);
                if (sh != null){
                    String total = String.valueOf(sh.getTotal());
                    String regular = String.valueOf(sh.getRegular());
                    String ctrl = String.valueOf(sh.getCtrlR());

                    Log.e(TAG, String.valueOf(sh.getTotal()));
                    Log.e(TAG, String.valueOf(sh.getRegular()));
                    Log.e(TAG, String.valueOf(sh.getCtrlR()));
                    Log.e(TAG, yes);

                    Previous_pojo previous_pojo = new Previous_pojo(yes, ctrl, regular, total);
                    list.add(previous_pojo);

                }
                Log.e(TAG, yes+"/"+list.size());
                adapter = new ChittagongPreviousAdapter(list, getActivity());
                recyclerView.setAdapter(adapter);
 //               saveArrayList(list, "previous_report");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private String yesterday(int i){
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        cal.add(Calendar.DATE, -i); // number represents number of days
        String yesterday = dateFormat.format(cal.getTime());

        return yesterday;
    }

    public void saveArrayList(ArrayList<Previous_pojo> list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }

    private void set_adapter(ArrayList<Previous_pojo> list){
        adapter = new ChittagongPreviousAdapter(list, getActivity());
        recyclerView.setAdapter(adapter);
    }
}
