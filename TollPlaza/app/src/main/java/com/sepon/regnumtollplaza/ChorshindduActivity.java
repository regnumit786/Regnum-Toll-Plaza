package com.sepon.regnumtollplaza;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sepon.regnumtollplaza.main.SectionsPagerAdapter;
import com.sepon.regnumtollplaza.pojo.Norshinddi;
import com.sepon.regnumtollplaza.utility.ApiClient;
import com.sepon.regnumtollplaza.utility.IApiClient;
import com.sepon.regnumtollplaza.utility.MyReceiver;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ChorshindduActivity extends AppCompatActivity {
    private BroadcastReceiver MyReceiver = null;
    TextView title;
    String plazaname;
    ImageButton backBtn;
    LinearLayout backLayout;

    private List<Norshinddi> todayreport;
    ArrayList<Norshinddi> arrayList;

    String url = "http://103.197.206.140/api/today.php";

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        title = findViewById(R.id.title);

        Intent intent = getIntent();
        plazaname = intent.getStringExtra("plazaName");
        title.setText(plazaname);

        backLayout = findViewById(R.id.backLayout);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getDaysReport(String url) {
        arrayList = new ArrayList<>();
        todayreport = new ArrayList<>();
        final ProgressDialog dialog = ProgressDialog.show(this, "Getting Current Report From Server", "Please wait...", true);

        final RequestQueue requestQueue = Volley.newRequestQueue(ChorshindduActivity.this);
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

                      //  Log.e("NOrshinddi", norshinddi.getAmount());
                        //arrayList.add(norshinddi);
                        arrayList.add(i, norshinddi);
                        todayreport.add(norshinddi);
                       // Log.e("Arraylist ", String.valueOf(arrayList.size()));

                    }catch (Exception p){

                    }
                }

                Log.e("Arraylist ", String.valueOf(arrayList.size()));
                Log.e("List Today ", String.valueOf(todayreport.size()));

                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
    }

}