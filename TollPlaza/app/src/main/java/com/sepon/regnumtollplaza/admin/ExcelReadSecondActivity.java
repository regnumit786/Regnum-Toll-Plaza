package com.sepon.regnumtollplaza.admin;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sepon.regnumtollplaza.BaseActivity;
import com.sepon.regnumtollplaza.ChittagongActivity;
import com.sepon.regnumtollplaza.LoginActivity;
import com.sepon.regnumtollplaza.R;
import com.sepon.regnumtollplaza.pojo.Regular;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExcelReadSecondActivity extends BaseActivity {

    private static final String TAG = "ExcelReadActivity";

    // Declare variables
    private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile;
    File file;

    Button btnUpDirectory,btnSDCard;

    ArrayList<String> pathHistory;
    String lastDirectory;
    int count = 0;

    //ArrayList<XYValue> uploadData;
    ArrayList<NewReport> uploadData;
    List<NewReport> allreport;
    ArrayList<NewReport> uploadReport;
    ArrayList<NewReport> ctrlRreport;
    ListView lvInternalStorage;
    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    String studentId, thisDate;

    FirebaseDatabase database;
    DatabaseReference myRef;
    Map<String, Report> reportHashMap = new HashMap<>();

    FirebaseFirestore firebaseFirestore;


    String mCustomDate = null;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


      //  showDialog("Excel", "Hello");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        studentId = currentUser.getUid();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        Date todayDate = new Date();
        thisDate = currentDate.format(todayDate);


         database = FirebaseDatabase.getInstance();
         myRef = database.getReference("chittagong");

       //  firebaseFirestore = FirebaseFirestore.getInstance();


        lvInternalStorage = (ListView) findViewById(R.id.lvInternalStorage);
        btnUpDirectory = (Button) findViewById(R.id.btnUpDirectory);
        btnSDCard = (Button) findViewById(R.id.btnViewSDCard);


        //need to check the permissions
        checkFilePermissions();

        lvInternalStorage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lastDirectory = pathHistory.get(count);

                if(lastDirectory.equals(adapterView.getItemAtPosition(i))){
                    Log.d(TAG, "lvInternalStorage: Selected a file for upload: " + lastDirectory);

                  // final ProgressDialog dialog= ProgressDialog.show(ExcelReadActivity.this,"Reading Excel File", "Please wait....",true);


                    readExcelFileFromAssets(lastDirectory);

                   // dialog.dismiss();

//                    Intent intent = new Intent(getApplicationContext(), ChittagongActivity.class);
//                    startActivity(intent);

//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Log.d(TAG, "Excel Reading thread start.....");
//
//                        }
//                    }).start();
                    //TODO go for next
                }else{
                    count++;
                    pathHistory.add(count,(String) adapterView.getItemAtPosition(i));
                    checkInternalStorage();
                    Toast.makeText(ExcelReadSecondActivity.this, "Try Again!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "lvInternalStorage: " + pathHistory.get(count));
                }
            }


        });

        //Goes up one directory level
        btnUpDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count == 0){
                    Log.d(TAG, "btnUpDirectory: You have reached the highest level directory.");
                }else{
                    pathHistory.remove(count);
                    count--;
                    checkInternalStorage();
                    Log.d(TAG, "btnUpDirectory: " + pathHistory.get(count));
                }
            }
        });

        //Opens the SDCard or phone memory
        btnSDCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 0;
                pathHistory = new ArrayList<String>();
                pathHistory.add(count,System.getenv("EXTERNAL_STORAGE"));
                Log.d(TAG, "btnSDCard: " + pathHistory.get(count));
                checkInternalStorage();
            }
        });

    }

    private void checkInternalStorage() {
        Log.d(TAG, "checkInternalStorage: Started.");
        try{
            if (!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                toastMessage("No SD card found.");
            }
            else{
                // Locate the image folder in your SD Car;d
                file = new File(pathHistory.get(count));
                Log.d(TAG, "checkInternalStorage: directory path: " + pathHistory.get(count));
            }

            listFile = file.listFiles();
            // Create a String array for FilePathStrings
            FilePathStrings = new String[listFile.length];
            // Create a String array for FileNameStrings
            FileNameStrings = new String[listFile.length];

            for (int i = 0; i < listFile.length; i++) {
                // Get the path of the image file
                FilePathStrings[i] = listFile[i].getAbsolutePath();
                // Get the name image file
                FileNameStrings[i] = listFile[i].getName();

            }

            for (int i = 0; i < listFile.length; i++) {
                Log.d("Files", "FileName:" + listFile[i].getName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, FilePathStrings);
            lvInternalStorage.setAdapter(adapter);

        }catch(NullPointerException e){
            Log.e(TAG, "checkInternalStorage: NULLPOINTEREXCEPTION " + e.getMessage() );
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkFilePermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck += this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1001); //Any number
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    public void readExcelFileFromAssets(String filePath) {

        final ProgressDialog dialog= ProgressDialog.show(ExcelReadSecondActivity.this,"Reading Excel File", "Please wait....",true);
        uploadData = new ArrayList<NewReport>();
        allreport = new ArrayList<>();
        File inputFile = new File(filePath);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Excel Reading thread start.....");

                try {
                    //InputStream myInput;
                    InputStream inputStream = new FileInputStream(inputFile);
                    POIFSFileSystem myFileSystem = new POIFSFileSystem(inputStream);
                    HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
                    HSSFSheet mySheet = myWorkBook.getSheetAt(0);
                    Iterator<Row> rowIter = mySheet.rowIterator();
                    int rowno =0;

                    while (rowIter.hasNext()) {
                        // Log.e(TAG, " row no "+ rowno );
                        HSSFRow myRow = (HSSFRow) rowIter.next();
                        if(rowno >=2) {
                            Iterator<Cell> cellIter = myRow.cellIterator();
                            int colno =0;
                            String a="",b="",c="",d="",e="",f = "",g="",h="",I="",j="",k="",l="",m="",n="",o="",p="",q="",r="";
                            int tr;
                            while (cellIter.hasNext()) {
                                HSSFCell myCell = (HSSFCell) cellIter.next();
                                if (colno==0){
                                    a = myCell.toString();
                                }
                                else if (colno==1){
                                    b = myCell.toString();
                                }else if (colno==2){
                                    c = myCell.toString();
                                }else if (colno==3){
                                    d = myCell.toString();
                                }else if (colno==4){
                                    e = myCell.toString();
                                }else if (colno==5){
                                    f = myCell.toString();
                                }else if (colno==6){
                                    g = myCell.toString();
                                }else if (colno==7){
                                    h = myCell.toString();
                                }else if (colno==8){
                                    I = myCell.toString();
                                }else if (colno==9){
                                    j = myCell.toString();
                                }else if (colno==10){
                                    k = myCell.toString();
                                }else if (colno==11){
                                    l = myCell.toString();
                                }else if (colno==12){
                                    m = myCell.toString();
                                }else if (colno==13){
                                    n = myCell.toString();
                                }else if (colno==14){
                                    o = myCell.toString();
                                }else if (colno==15){
                                    p = myCell.toString();
                                }else if (colno==16){
                                    q = myCell.toString();
                                }else if (colno==17){
                                    r = myCell.toString();
                                }
                                colno++;
                                // Log.e(TAG, " Index :" + myCell.getColumnIndex() + " -- " + myCell.toString());
                            }
                            //Log.d(TAG, a + " -- "+ b+ "  -- "+ c+"  -- "+ d+"  -- "+ e+"  -- "+ f+"  -- "+ g+"  -- "+ h+"  -- "+ I+"  -- "+ j+"  -- "+ k+"\n");
//                            Report report = new Report(String.valueOf(a),String.valueOf(b),String.valueOf(c),String.valueOf(d),
//                                    String.valueOf(e),String.valueOf(f),String.valueOf(g),
//                                    String.valueOf(h),String.valueOf(I),String.valueOf(j),String.valueOf(k));

                            NewReport newReport = new NewReport(String.valueOf(a),String.valueOf(b),String.valueOf(c),String.valueOf(d),
                                    String.valueOf(e),String.valueOf(f),String.valueOf(g),
                                    String.valueOf(h),String.valueOf(I),String.valueOf(j),String.valueOf(k),String.valueOf(l),String.valueOf(m),
                                    String.valueOf(n),String.valueOf(o),String.valueOf(p),String.valueOf(q),String.valueOf(r));

                            if (newReport.getB().isEmpty()){

                            }else {

                                uploadData.add(newReport);
                                Log.e("Readed :", String.valueOf(uploadData.size()));
                                //allreport.add(report);
                            }
                        }
                        rowno++;
                    }
                } catch (Exception e) {
                    Log.e(TAG, "error "+ e.toString());
                }
                dialog.dismiss();
                serilizeReport();
            }
        }).start();
        Log.e(TAG, "report size "+ uploadData.size());



    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.adminmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.adduser:
                // write your code here

                Intent intent = new Intent(this, AddUserActivity.class);
                startActivity(intent);

                return true;

            case R.id.logout:
                // write your code here
                logout();
                return true;

            case R.id.delate:
                // write your code here

                Intent i = new Intent(getApplicationContext(), DeleteActivity.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        mAuth.getInstance().signOut();
        Intent login = new Intent(ExcelReadSecondActivity.this, LoginActivity.class);
        startActivity(login);
        ExcelReadSecondActivity.this.finish();
    }


    private void serilizeReport() {

       // showprogessdialog("serialize Data ");

        uploadReport = new ArrayList<>();
        ctrlRreport = new ArrayList<>();

            for (NewReport report : uploadData){

                String a = report.getI();
                if (a.equals("N/A")){
                    Log.e(TAG, report.getA());
                    ctrlRreport.add(report);
                }else {
                    uploadReport.add(report);
                }
            }

            //hiddenProgressDialog();
        Log.e(TAG, String.valueOf("All report: "+uploadData.size()));
        Log.e(TAG, String.valueOf("Crtl+R report: "+ctrlRreport.size()));
        Log.e(TAG, String.valueOf("Regular report: "+uploadReport.size()));

        //todo show alart dialog
        String msg = "All report: "+uploadData.size()+
                ",    Crtl+R report: "+ctrlRreport.size()+",     Regular report: "+uploadReport.size();




        ExcelReadSecondActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                //showAlertWithOKClick("Excel", msg);

                showDialog("Excel", msg);

            }
        });


    }

    private void showDialog(String excel, String msg) {
        final Dialog dialog = new Dialog(ExcelReadSecondActivity.this);
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setTitle(excel);
        TextView msgtext = dialog.findViewById(R.id.msg);
        msgtext.setText(msg);
        Button cancel = dialog.findViewById(R.id.cancel);
        Button upload = dialog.findViewById(R.id.upload);
        DatePicker datePicker = dialog.findViewById(R.id.datepicker);
        TextView dateshow = dialog.findViewById(R.id.dateshow);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ExcelReadSecondActivity.this, "cancel", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ExcelReadSecondActivity.this, "upload", Toast.LENGTH_SHORT).show();
                if (mCustomDate!=null){
                    // previous
                    Log.e("previous Date ", mCustomDate);
                    dialog.dismiss();
                    uploadRepoert(mCustomDate);

                }else {
                    //Today
                    Log.e("previous Date ", "null");
                    dialog.dismiss();
                    uploadReport();
                }
            }
        });

        Switch s = dialog.findViewById(R.id.switch1);
        s.setChecked(false);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(ExcelReadSecondActivity.this, ""+isChecked, Toast.LENGTH_SHORT).show();
                if (isChecked==true){
                    datePicker.setVisibility(View.VISIBLE);
                    dateshow.setVisibility(View.VISIBLE);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    String date = datePicker.getDayOfMonth()+"-"+ (datePicker.getMonth() + 1)+"-"+datePicker.getYear();
                    Log.e("Previous Date", date);

                    dateshow.setText(date);
                    datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

                        @Override
                        public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                           // Log.d("Date", "Year=" + year + " Month=" + (month + 1) + " day=" + dayOfMonth);
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            //String PreviousDate = sdf.format(datePicker.getCalendarView().getDate());
                            mCustomDate = sdf.format(datePicker.getCalendarView().getDate());
                            Log.e("Previous Date", mCustomDate);
                            //mCustomDate = datePicker.getDayOfMonth()+"-"+ (datePicker.getMonth() + 1)+"-"+datePicker.getYear();
                            dateshow.setText(mCustomDate);
                            Log.e("date", mCustomDate);


                        }
                    });
                }else {
                    datePicker.setVisibility(View.GONE);
                    dateshow.setVisibility(View.GONE);
                }

            }
        });

        dialog.show();

    }

//    public void showAlertWithOKClick(String title_str, String message) {
//        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//                this);
//        alertDialogBuilder.setTitle(title_str);
//        alertDialogBuilder.setMessage(message)
//                .setPositiveButton("Upload Report", new DialogInterface.OnClickListener() {
//
//            public void onClick(DialogInterface dialog, int id) {
//                dialog.cancel();
//
//                Toast.makeText(ExcelReadActivity.this, "nice", Toast.LENGTH_SHORT).show();
//                uploadRepoert();
//
//            }
//        });
//        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.setCancelable(false);
//        alertDialog.show();
//    }

    private void uploadReport() {  //toady report Upload fun

        showprogressdialog("Report Uploaded to server");

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Log.d(TAG, "Firebase Report-1 upload thread start.....");
//
//                for (int i=0;i<uploadReport.size();i++){
//                    Log.e(TAG, String.valueOf(i));
//                    Report report = uploadReport.get(i);
//                    myRef.child(thisDate).child("RegularReport").child(String.valueOf(i)).setValue(report);
//                   // reportHashMap.put(String.valueOf(i), report);
//
//                }
//                hiddenProgressDialog();
//                Intent intent = new Intent(ExcelReadActivity.this, ChittagongActivity.class);
//                startActivity(intent);
//            }
//        }).start();
        //serilizeRegularData(uploadReport);
        Regular regular = serilizeRegularData(uploadReport);
        myRef.child(thisDate).child("RegularReport").setValue(regular);

        for (int i=0;i<ctrlRreport.size();i++){
            Log.e(TAG, String.valueOf(i));
            NewReport report = ctrlRreport.get(i);
            myRef.child(thisDate).child("ctrlReport").child(String.valueOf(i)).setValue(report);

        }

        // this short info just for previous view
        Short s = new Short(String.valueOf(uploadData.size()), String.valueOf(ctrlRreport.size()), String.valueOf(uploadReport.size()));
        myRef.child(thisDate).child("short").setValue(s);
        hiddenProgressDialog();
        Intent intent = new Intent(ExcelReadSecondActivity.this, ChittagongActivity.class);
        startActivity(intent);
    }

    private Regular serilizeRegularData(ArrayList<NewReport> uploadReport) {
        ArrayList<NewReport> regularReport2 = new ArrayList<>();
        ArrayList<NewReport> regularReport3 = new ArrayList<>();
        ArrayList<NewReport> regularReport4 = new ArrayList<>();
        ArrayList<NewReport> regularReport5 = new ArrayList<>();
        ArrayList<NewReport> regularReport6 = new ArrayList<>();
        ArrayList<NewReport> regularReport7 = new ArrayList<>();
        ArrayList<NewReport> totallaxel = new ArrayList<>();
        String a2,a3,a4,a5,a6,a7;

        for (int i=0; i<uploadReport.size(); i++){
            NewReport report = uploadReport.get(i);
            String tc = report.getD();
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
        }
        Regular regular = new Regular(String.valueOf(regularReport2.size()),
                String.valueOf(regularReport3.size()),
                String.valueOf(regularReport4.size()),
                String.valueOf(regularReport5.size()),
                String.valueOf(regularReport6.size()),
                String.valueOf(regularReport7.size()),
                String.valueOf(uploadReport.size()));

        return regular;
    }


    private void uploadRepoert(String previousDate) {   //previous report Upload fun

        showprogressdialog("Report Uploaded to server");

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Log.d(TAG, "Firebase Report-1 upload thread start.....");
//
//                for (int i=0;i<uploadReport.size();i++){
//                    Log.e(TAG, String.valueOf(i));
//                    Report report = uploadReport.get(i);
//                    myRef.child(previousDate).child("RegularReport").child(String.valueOf(i)).setValue(report);
//                    reportHashMap.put(String.valueOf(i), report);
//
//                }
//                hiddenProgressDialog();
//                Intent intent = new Intent(ExcelReadActivity.this, ChittagongActivity.class);
//                startActivity(intent);
//            }
//        }).start();
        Regular regular = serilizeRegularData(uploadReport);
        myRef.child(previousDate).child("RegularReport").setValue(regular);

        for (int i=0;i<ctrlRreport.size();i++){
            Log.e(TAG, String.valueOf(i));
            NewReport report = ctrlRreport.get(i);
            myRef.child(previousDate).child("ctrlReport").child(String.valueOf(i)).setValue(report);

        }

        // this short info just for previous view
        Short s = new Short(String.valueOf(uploadData.size()), String.valueOf(ctrlRreport.size()), String.valueOf(uploadReport.size()));
        myRef.child(previousDate).child("short").setValue(s);
        hiddenProgressDialog();
        Intent intent = new Intent(ExcelReadSecondActivity.this, ChittagongActivity.class);
        startActivity(intent);
    }


    private void firestore(){
        firebaseFirestore = FirebaseFirestore.getInstance();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Firebase Report-1 upload thread start.....");

                for (int i=0;i<uploadReport.size();i++){
                    Log.e(TAG, String.valueOf(i));
                    NewReport report = uploadReport.get(i);
                    //myRef.child(thisDate).child("RegularReport").child(String.valueOf(i)).setValue(report);
                    firebaseFirestore.collection("Chittagong").document(thisDate).collection("regularReport").document(String.valueOf(i)).set(report);


                }

            }
        }).start();

        for (int i=0;i<ctrlRreport.size();i++){
            Log.e(TAG, String.valueOf(i));
            NewReport report = ctrlRreport.get(i);
            firebaseFirestore.collection("Chittagong").document(thisDate).collection("ctrlReport").document(String.valueOf(i)).set(report);

        }

    }


    private void pickData(){
        // Get Current Date
        final Calendar c = Calendar.getInstance();
       int mYear = c.get(Calendar.YEAR);
        int  mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                      //  txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}