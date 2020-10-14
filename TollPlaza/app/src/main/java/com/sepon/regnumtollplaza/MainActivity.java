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
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    LinearLayout layoutMainActivity;
    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    String studentId;
    private static int mVersion = 2;
    TextView warningText;
    private Button btnCharsindur, btnChittagong, btnManikganj, openGmail;
    private ImageView imgCharsindur, imgChittagong, imgManikganj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FindView();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        studentId = currentUser.getUid();

        checkVersion();
        GoForAccessData();
    }

    private void FindView(){
        layoutMainActivity= findViewById(R.id.layout_main_activity);
        warningText = findViewById(R.id.warning);
        openGmail = findViewById(R.id.openGmail);
        btnCharsindur= findViewById(R.id.btn_charsindur_plaza);
        btnChittagong= findViewById(R.id.btn_chittagong_plaza);
        btnManikganj= findViewById(R.id.btn_manikganj_plaza);
        imgCharsindur= findViewById(R.id.img_charsindur_plaza);
        imgChittagong= findViewById(R.id.img_chittagong_plaza);
        imgManikganj= findViewById(R.id.img_manikganj_plaza);
    }

    private void checkVersion() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("version");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int version = dataSnapshot.getValue(Integer.class);
                Log.e("Version", String.valueOf(version));
                if (mVersion != version){
                    layoutMainActivity.setVisibility(View.GONE);
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
                Toast.makeText(MainActivity.this, "Database_Error: "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GoForAccessData(){
        btnCharsindur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                currentUser = mAuth.getCurrentUser();
                assert currentUser != null;
                String email = currentUser.getEmail();

                assert email != null;
                if (email.equals("usermamun@gmail.com")) { //here block the user
                    Toast.makeText(MainActivity.this, "Sorry you are not eligible.", Toast.LENGTH_SHORT).show();

                } else {
                    Intent intent = new Intent(MainActivity.this, ChorshindduActivity.class);
                    intent.putExtra("plazaName", "Charsindur");
                    startActivity(intent);
                }
            }
        });

        btnChittagong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChittagongActivity.class);
                intent.putExtra("plazaName", "Chittagong");
                startActivity(intent);
            }
        });

        btnManikganj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ManikGong_Axel.class);
                intent.putExtra("plazaName", "Manikganj");
                startActivity(intent);
            }
        });

        imgCharsindur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                currentUser = mAuth.getCurrentUser();
                assert currentUser != null;
                String email = currentUser.getEmail();

                assert email != null;
                if (email.equals("usermamun@gmail.com")) { //here block the user
                    Toast.makeText(MainActivity.this, "Sorry you are not eligible.", Toast.LENGTH_SHORT).show();

                } else {
                    Intent intent = new Intent(MainActivity.this, ChorshindduActivity.class);
                    intent.putExtra("plazaName", "Charsindur");
                    startActivity(intent);
                }
            }
        });

        imgChittagong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChittagongActivity.class);
                intent.putExtra("plazaName", "Chittagong");
                startActivity(intent);
            }
        });

        imgManikganj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ManikGong_Axel.class);
                intent.putExtra("plazaName", "Manikganj");
                startActivity(intent);
            }
        });

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
