package com.sepon.regnumtollplaza.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.core.Repo;
import com.sepon.regnumtollplaza.R;

import java.util.ArrayList;

public class ReportUploadActivity extends AppCompatActivity {

    private static final String TAG = "ReportUploadActivity";
    ArrayList<Report> uploadData;
    ArrayList<Report> regularuploadData;
    ArrayList<Report> ctrluploadData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_upload);



        uploadData = new ArrayList<>();
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("report");
        uploadData = (ArrayList<Report>) args.getSerializable("report");


        Log.e(TAG, String.valueOf(uploadData.size()));
    }
}
