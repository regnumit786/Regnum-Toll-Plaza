package com.sepon.regnumtollplaza.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sepon.regnumtollplaza.BaseActivity;
import com.sepon.regnumtollplaza.R;

import java.util.Calendar;

public class DeleteActivity extends BaseActivity {

    EditText etdate;
    String mCustomDate=null;
    Button delatebtn;
    DatePicker datePicker;
    TextView dateshow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delate);


        datePicker = findViewById(R.id.datepicker);
        dateshow = findViewById(R.id.dateshow);
        delatebtn = findViewById(R.id.delatebtn);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        dateshow.setText(datePicker.getDayOfMonth()+"-"+ (datePicker.getMonth() + 1)+"-"+datePicker.getYear());
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                // Log.d("Date", "Year=" + year + " Month=" + (month + 1) + " day=" + dayOfMonth);
                mCustomDate = datePicker.getDayOfMonth()+"-"+ (datePicker.getMonth() + 1)+"-"+datePicker.getYear();
                dateshow.setText(mCustomDate);
                Log.e("date", mCustomDate);


            }
        });


        delatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showprogressdialog("Deleting....");
               if (mCustomDate==null){
                   Toast.makeText(DeleteActivity.this, "Please Select Date First..", Toast.LENGTH_SHORT).show();
               }else {
                   FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                   DatabaseReference reference = firebaseDatabase.getReference("chittagong").child(mCustomDate);
                   // reference.removeValue();
                   reference.removeValue(new DatabaseReference.CompletionListener() {
                       @Override
                       public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                           hiddenProgressDialog();
                           Toast.makeText(DeleteActivity.this, "Successfully Delate", Toast.LENGTH_SHORT).show();
                       }
                   });
               }

                hiddenProgressDialog();
            }
        });


    }
}
