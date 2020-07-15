package com.sepon.regnumtollplaza.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.sepon.regnumtollplaza.BaseActivity;
import com.sepon.regnumtollplaza.R;

import static com.sepon.regnumtollplaza.BaseActivity.isValidEmail;

public class AddUserActivity extends BaseActivity {

    EditText emailet, passwordet, confirmPasset;

    String email,password,userID;
    Button adduserbtn;

    FirebaseAuth mAuth;
    FirebaseUser currentUser ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useradd);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        emailet = findViewById(R.id.useremail);
        passwordet = findViewById(R.id.userpassword);
        confirmPasset = findViewById(R.id.confirmpassword);

        adduserbtn = findViewById(R.id.adduserbtn);
        adduserbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideUserKeyboard();
                email = emailet.getText().toString();
                if (isValidEmail(email)==false){
                    Toast.makeText(AddUserActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }else {
                    if (passwordet.getText().toString().length()<6){

                        Toast.makeText(AddUserActivity.this, "At least 6 characters", Toast.LENGTH_SHORT).show();

                    }else if(passwordet.getText().toString().equals( confirmPasset.getText().toString())){
                        //do things if these 2 are correct.
                        //Toast.makeText(RegistationActivity.this, "correct", Toast.LENGTH_SHORT).show();
                        if (isOnline()==false){
                            Toast.makeText(AddUserActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }else{

                            registation();
                        }
                    }else {

                        Toast.makeText(AddUserActivity.this, "Password mismatch", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void registation() {

        showprogressdialog("Registration a new User");
        password =confirmPasset.getText().toString();

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            setedittextnull();

                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            userID = user.getUid();

                            storeuserinfirestore(user);
                            hiddenProgressDialog();

                            //updateUI(user);
                        } else {
                            setedittextnull();
                            hiddenProgressDialog();
                            // If sign in fails, display a message to the user.
                            // Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(),
                                        "This email already exist.", Toast.LENGTH_SHORT).show();
                            }else{
                                hiddenProgressDialog();
                                Toast.makeText(getApplicationContext(), "Reg failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void storeuserinfirestore(FirebaseUser user) {
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName("user").build();

        user.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    hiddenProgressDialog();
                    Toast.makeText(getApplicationContext(), "Registation success", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void setedittextnull() {

        emailet.setText("");
        passwordet.setText("");
        confirmPasset.setText("");
    }
}
