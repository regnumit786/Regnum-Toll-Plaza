package com.sepon.regnumtollplaza;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sepon.regnumtollplaza.admin.ExcelReadActivity;
import com.sepon.regnumtollplaza.admin.ExcelReadSecondActivity;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    EditText email_et,password_et;
    Button login_btn;
    String email,password;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            String profile = currentUser.getDisplayName();
           // Log.e("Profile ", profile);
            if (profile.equals("")){
                //admin
                goforadmin();
            }else {
                //user
                goForUser();
            }
        }else {
            email_et = findViewById(R.id.email);
            password_et = findViewById(R.id.password);
            login_btn = findViewById(R.id.login);
            login_btn.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
            email = email_et.getText().toString();
            password = password_et.getText().toString();

            if (email.isEmpty()){
                email_et.setError("Invalid Input");
            }else if (password.isEmpty()){
                password_et.setError("Give Password");
            }else {
                showprogressdialog("Checking your Information");
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    setedittextnull();
                                    currentUser = mAuth.getCurrentUser();
                                    Toast.makeText(LoginActivity.this, "success", Toast.LENGTH_SHORT).show();

                                    String profile = currentUser.getDisplayName();
                                    if (profile==null){
                                        //admin
                                        goforadmin() ;
                                    }else {
                                        //user
                                        goForUser();
                                    }

                                    hiddenProgressDialog();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    hiddenProgressDialog();
                                }

                                // ...
                            }
                        });
            }
    }

    private void goForUser() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void goforadmin() {
        Intent intent = new Intent(this, ExcelReadSecondActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void backPreviousActivity() {
        Intent intent = new Intent(this, Splash_screen_activity.class);
        startActivity(intent);
        this.finish();

    }

    private void setedittextnull() {
        email_et.setText("");
        password_et.setText("");
    }
}
