package com.sepon.regnumtollplaza;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.sepon.regnumtollplaza.adapter.PlazaAdapter;
import com.sepon.regnumtollplaza.admin.AddUserActivity;
import com.sepon.regnumtollplaza.admin.ExcelReadActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private GridLayoutManager mRecyclerGridMan;
    List<Plaza> plazalist = new ArrayList<>();
    PlazaAdapter mPlazaAdapter;
    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    String studentId, thisDate;
   // private static int mVersion = R.string.version;
    private static int mVersion = 2;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    TextView warningText;
    Button openGmail;

    int[] image_list= {R.drawable.charshindo_logo, R.drawable.tollview, R.drawable.chittagong_logo};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        studentId = currentUser.getUid();

        warningText = findViewById(R.id.warning);
        openGmail = findViewById(R.id.openGmail);

        recyclerView = findViewById(R.id.Plaza_recyclerview);
        int numberOfColumns = 2;
        mRecyclerGridMan = new GridLayoutManager(this, numberOfColumns);
        recyclerView.setLayoutManager(mRecyclerGridMan);

        generateplaza();

        checkVersion();

        //getinstenceID();
    }

    private void checkVersion() {

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("version");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int version = dataSnapshot.getValue(Integer.class);
                Log.e("Version", String.valueOf(version));
                if (mVersion != version){
                    recyclerView.setVisibility(View.GONE);
                    Log.e("App version", String.valueOf(mVersion));
                    warningText.setVisibility(View.VISIBLE);
                    warningText.setText("please check your Email that new Version is release... Upgrade your App!");
                   // Toast.makeText(MainActivity.this, "please Upgrade your App!", Toast.LENGTH_SHORT).show();
                    openGmail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                            startActivity(intent);
                            startActivity(Intent.createChooser(intent, getString(R.string.ChoseEmailClient)));
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void generateplaza() {
        Plaza plaza = new Plaza(R.drawable.charshindo_logo,"Charsindur");
        Plaza plaza1 = new Plaza(R.drawable.charshindo_logo,"Manikganj");
        Plaza plaza3 = new Plaza(R.drawable.charshindo_logo,"Chittagong");
        plazalist.add(plaza);
        plazalist.add(plaza1);
        plazalist.add(plaza3);
        mPlazaAdapter = new PlazaAdapter(image_list, plazalist, MainActivity.this);
        recyclerView.setAdapter(mPlazaAdapter);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.boss, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.logout:
                // write your code here
                logout();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        mAuth.getInstance().signOut();
        Intent login = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(login);
        MainActivity.this.finish();
    }

    public void getinstenceID(){

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("MainActivity", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        Log.d("MainActivity", token);
                        Toast.makeText(MainActivity.this, "FCM token"+token, Toast.LENGTH_SHORT).show();

                    }
                });
    }
}
