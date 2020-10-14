package com.sepon.regnumtollplaza.admin;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
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
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class ExcelReadActivity extends BaseActivity {

    private static final String TAG = "ExcelReadActivity";

    String[] FilePathStrings;
    String[] FileNameStrings;
    File[] listFile;
    File file;

    Button btnUpDirectory,btnSDCard;

    ArrayList<String> pathHistory;
    ArrayList<String> lastlist;

    String lastDirectory;
    int count = 0;

    //ArrayList<XYValue> uploadData;
    ArrayList<Report> uploadData;
    List<Report> allreport;
    ArrayList<Report> uploadReport;
    ArrayList<Report> ctrlRreport;
    ListView lvInternalStorage;
    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    String studentId, thisDate;

    FirebaseDatabase database;
    DatabaseReference myRef;
    //Map<String, Report> reportHashMap = new HashMap<>();

    FirebaseFirestore firebaseFirestore;
    String mCustomDate = null;

    List<String> excelFilePath;
    //public static final String EXTENSION=".xls", EXTENSION1=".xlsx";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        studentId = currentUser.getUid();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date todayDate = new Date();
        thisDate = currentDate.format(todayDate);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("chittagong");

        lvInternalStorage = (ListView) findViewById(R.id.lvInternalStorage);
        btnUpDirectory = (Button) findViewById(R.id.btnUpDirectory);
        btnSDCard = (Button) findViewById(R.id.btnViewSDCard);

        excelFilePath= new ArrayList<>();
        checkFilePermissions();

        lvInternalStorage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lastDirectory = pathHistory.get(count);
                if(lastDirectory.equals(adapterView.getItemAtPosition(i))){
                    Log.d(TAG, "lvInternalStorage: Selected a file for upload: " + lastDirectory);
                    readExcelFileFromAssets(lastDirectory);
                }else{
                    count++;
                    pathHistory.add(count,(String) adapterView.getItemAtPosition(i));
                    checkInternalStorage();
                    Toast.makeText(ExcelReadActivity.this, "Try Again!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ExcelReadActivity.this, "Select Excel File", Toast.LENGTH_SHORT).show();
                checkInternalStorage();
            }
        });

        lastlist= new ArrayList<>();
    }

    private void checkInternalStorage() {
        Log.d(TAG, "checkInternalStorage: Started.");
        try{
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                toastMessage("No SD card found.");
            }
            else{
                Log.d(TAG, "checkOutOfCondition");
                file = new File(pathHistory.get(count));
                Log.d(TAG, "checkInternalStorage: directory path: " + pathHistory.get(count));
            }
            Log.d(TAG, "checkOutOfCondition");
            listFile = file.listFiles();
            assert listFile != null;
            FilePathStrings = new String[listFile.length];
            FileNameStrings = new String[listFile.length];
            Log.d(TAG, "List of file: "+listFile.length);

            for (int i = 0; i < listFile.length; i++) {
                FilePathStrings[i] = listFile[i].getAbsolutePath();
                FileNameStrings[i] = listFile[i].getName();
            }
            for (int i = 0; i < listFile.length; i++){
                Log.d("Files", "FileName:" + listFile[i].getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, FilePathStrings);
            lvInternalStorage.setAdapter(adapter);
        }catch(NullPointerException e){
            toastMessage(e.getMessage());
            Log.e(TAG, "checkInternalStorage: NULLPOINTEREXCEPTION " + e.getMessage());
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
     * param_message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    public void readExcelFileFromAssets(String filePath) {

        final ProgressDialog dialog= ProgressDialog.show(ExcelReadActivity.this,"Reading Excel File", "Please wait....",true);
        uploadData = new ArrayList<Report>();
        allreport = new ArrayList<>();
        File inputFile = new File(filePath);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Excel Reading thread start.....");

                try {
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
                            String a="",b="",c="",d="",e="",f = "",g="",h="",I="",j="",k="";
                            int tr;
                            while (cellIter.hasNext()) {
                                HSSFCell myCell = (HSSFCell) cellIter.next();
                                if (colno==0){
                                    a = myCell.toString();
                                }else if (colno==1){
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
                                }
                                colno++;
                            }
                            Report report = new Report(String.valueOf(a),String.valueOf(b),String.valueOf(c),String.valueOf(d),String.valueOf(e),String.valueOf(f),String.valueOf(g),
                                    String.valueOf(h),String.valueOf(I),String.valueOf(j),String.valueOf(k));
                            if (report.getDateTime().isEmpty()){

                            }else {
                                uploadData.add(report);
                                Log.e("Read :", String.valueOf(uploadData.size()));
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
        Intent login = new Intent(ExcelReadActivity.this, LoginActivity.class);
        startActivity(login);
        ExcelReadActivity.this.finish();
    }

    private void serilizeReport() {

       // showprogessdialog("serialize Data ");

        uploadReport = new ArrayList<>();
        ctrlRreport = new ArrayList<>();

            for (Report report : uploadData){

                String a = report.getAxleWiseWeight();
                if (a.isEmpty() || a.equals("0.0")){
                    Log.e(TAG, report.getTransactionNumber());
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
        String msg = "All report: "+uploadData.size()+ ",    Crtl+R report: "+ctrlRreport.size()+",     Regular report: "+uploadReport.size();




        ExcelReadActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                //showAlertWithOKClick("Excel", msg);

                showDialog("Excel", msg);

            }
        });
    }

    private void showDialog(String excel, String msg) {
        final Dialog dialog = new Dialog(ExcelReadActivity.this);
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
                Toast.makeText(ExcelReadActivity.this, "cancel", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ExcelReadActivity.this, "upload", Toast.LENGTH_SHORT).show();
                if (mCustomDate!=null){
                    // previous
                    Log.e("previous Date ", mCustomDate);
                    dialog.dismiss();
                    uploadRepoert(mCustomDate);
                    /**
                     * User notify to uploaded data
                     */
                    //UploadNotification();
                }else {
                    //Today
                    Log.e("previous Date ", "null");
                    dialog.dismiss();
                    uploadRepoert();
                    /**
                     * User notify to uploaded data
                     */
                    //UploadNotification();
                }
            }
        });

        Switch s = dialog.findViewById(R.id.switch1);
        s.setChecked(false);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(ExcelReadActivity.this, ""+isChecked, Toast.LENGTH_SHORT).show();
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

    private void uploadRepoert() {
        showprogressdialog("Report Uploaded to server");
        Regular regular = serilizeRegularData(uploadReport);
        myRef.child(thisDate).child("RegularReport").setValue(regular);

        for (int i=0;i<ctrlRreport.size();i++){
            Log.e(TAG, String.valueOf(i));
            Report report = ctrlRreport.get(i);
            myRef.child(thisDate).child("ctrlReport").child(String.valueOf(i)).setValue(report);

        }
        // this short info just for previous view
        Short s = new Short(String.valueOf(uploadData.size()), String.valueOf(ctrlRreport.size()), String.valueOf(uploadReport.size()));
        myRef.child(thisDate).child("short").setValue(s);
        hiddenProgressDialog();
        UploadNotification();
        Intent intent = new Intent(ExcelReadActivity.this, ChittagongActivity.class);
        startActivity(intent);
    }

    private void UploadNotification() {
        Intent intent = new Intent(this, ExcelReadActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Report Uploaded")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.logo, "today report upload", pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(0, notification.build());
    }

    private Regular serilizeRegularData(ArrayList<Report> uploadReport) {
        ArrayList<Report> regularReport2 = new ArrayList<>();
        ArrayList<Report> regularReport3 = new ArrayList<>();
        ArrayList<Report> regularReport4 = new ArrayList<>();
        ArrayList<Report> regularReport5 = new ArrayList<>();
        ArrayList<Report> regularReport6 = new ArrayList<>();
        ArrayList<Report> regularReport7 = new ArrayList<>();
        ArrayList<Report> totallaxel = new ArrayList<>();
        String a2,a3,a4,a5,a6,a7;

        for (int i=0; i<uploadReport.size(); i++){
            Report report = uploadReport.get(i);
            String tc = report.getTcClass();
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
        Regular regular = serilizeRegularData(uploadReport);
        myRef.child(previousDate).child("RegularReport").setValue(regular);

        for (int i=0;i<ctrlRreport.size();i++){
            Log.e(TAG, String.valueOf(i));
            Report report = ctrlRreport.get(i);
            myRef.child(previousDate).child("ctrlReport").child(String.valueOf(i)).setValue(report);

        }
        // this short info just for previous view
        Short s = new Short(String.valueOf(uploadData.size()), String.valueOf(ctrlRreport.size()), String.valueOf(uploadReport.size()));
        myRef.child(previousDate).child("short").setValue(s);
        hiddenProgressDialog();
        UploadNotification();
        Intent intent = new Intent(ExcelReadActivity.this, ChittagongActivity.class);
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
                    Report report = uploadReport.get(i);
                    //myRef.child(thisDate).child("RegularReport").child(String.valueOf(i)).setValue(report);
                    firebaseFirestore.collection("Chittagong").document(thisDate).collection("regularReport").document(String.valueOf(i)).set(report);
                }
            }
        }).start();
        for (int i=0;i<ctrlRreport.size();i++){
            Log.e(TAG, String.valueOf(i));
            Report report = ctrlRreport.get(i);
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