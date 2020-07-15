package com.sepon.regnumtollplaza.fragment.chittagong;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sepon.regnumtollplaza.R;
import com.sepon.regnumtollplaza.admin.Report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class CtrlR_fragment extends Fragment {

    private TextView a2, a3, a4, a5, a6,a7;
    private ArrayList<Report> ctrlReport = new ArrayList<>();
    private ArrayList<Report> axel2Report= new ArrayList<>();
    private ArrayList<Report> axel3Report= new ArrayList<>();
    private ArrayList<Report> axel4Report= new ArrayList<>();
    private ArrayList<Report> axel5Report= new ArrayList<>();
    private ArrayList<Report> axel6Report= new ArrayList<>();
    private ArrayList<Report> axel7Report= new ArrayList<>();
    private String thisDate;
    private  ProgressDialog dialog;
    private  TextView toptext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.ctrlr_axel_layout, container, false);

        a2 = view.findViewById(R.id.axel2amount);
        a3 = view.findViewById(R.id.axel3amount);
        a4 = view.findViewById(R.id.axel4amount);
        a5 = view.findViewById(R.id.axel5amount);
        a6 = view.findViewById(R.id.axel6amount);
        a7 = view.findViewById(R.id.axel7amount);
        toptext = view.findViewById(R.id.toptext);

        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        Date todayDate = new Date();
        thisDate = currentDate.format(todayDate);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("chittagong").child(thisDate);
        dialog = ProgressDialog.show(getActivity(), "Checking todays report...", "Please wait...", true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    // Toast.makeText(getActivity(), "report  update yet", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    //getRegularReportFromFirebase();
                }else {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "report not update yet", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
            }
        });
        return view;
    }

    private void getRegularReportFromFirebase() {
        dialog = ProgressDialog.show(getActivity(), "Getting Report From Server", "Please wait...", true);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("chittagong").child(thisDate).child("ctrlReport");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Report report = singleSnapshot.getValue(Report.class);
                    String tc = report.getTcClass();
                    //Truck 2 Axle
                    if (tc.equals("Truck 2 Axle")){
                        axel2Report.add(report);
                    }else if (tc.equals("Truck 3 Axle")){
                        axel3Report.add(report);
                    }else if (tc.equals("Truck 4 Axle")){
                        axel4Report.add(report);
                    }else if (tc.equals("Truck 5 Axle")){
                        axel5Report.add(report);
                    }else if (tc.equals("Truck 6 Axle")){
                        axel6Report.add(report);
                    }else if (tc.equals("Truck 7 Axle")){
                        axel7Report.add(report);
                    }else {

                    }

                    ctrlReport.add(report);
                    dialog.dismiss();
                }
                Log.e("totaltr", String.valueOf(ctrlReport.size()));
                toptext.setText("Total ctrl+R Axel Report: "+String.valueOf(ctrlReport.size()));
                Log.e("axel2", String.valueOf(axel2Report.size()));
                a2.setText(String.valueOf(axel2Report.size()));
                Log.e("axel3", String.valueOf(axel3Report.size()));
                a3.setText(String.valueOf(axel3Report.size()));
                Log.e("axel4", String.valueOf(axel4Report.size()));
                a4.setText(String.valueOf(axel4Report.size()));
                Log.e("axel5", String.valueOf(axel5Report.size()));
                a5.setText(String.valueOf(axel5Report.size()));
                Log.e("axel6", String.valueOf(axel6Report.size()));
                a6.setText(String.valueOf(axel6Report.size()));
                Log.e("axel7", String.valueOf(axel7Report.size()));
                a7.setText(String.valueOf(axel7Report.size()));

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
