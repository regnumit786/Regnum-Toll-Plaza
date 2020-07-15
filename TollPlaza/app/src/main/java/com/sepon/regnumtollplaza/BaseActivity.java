package com.sepon.regnumtollplaza;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


/**
 * Created by Sepon on 11/2/2019.
 * Regnum IT Limited
 * ismailhossainsepon@gmail.com
 */
public class BaseActivity extends AppCompatActivity {
    private ProgressDialog progDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progDailog = new ProgressDialog(this);
        isNetworkConnected();
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public void showprogressdialog(String title) {
        if (progDailog != null) {
            progDailog.setTitle(title);
            progDailog.setMessage("Please wait...");
            progDailog.setCancelable(false);
            progDailog.show();
        }
    }

    public void hiddenProgressDialog() {
        if (progDailog != null) {
            progDailog.dismiss();
        }
    }

    public void hideUserKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showAlert(String title_str, String message, final Activity destContext) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setTitle(title_str);
        alertDialogBuilder.setMessage(message).setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                if (destContext != null) {
                    finish();
                    Intent intent = new Intent(BaseActivity.this, destContext.getClass());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    BaseActivity.this.startActivity(intent);

                }
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void showAlertWithOKClick(String title_str, String message) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setTitle(title_str);
        alertDialogBuilder.setMessage(message).setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progDailog != null && progDailog.isShowing()) {
            progDailog.cancel();
        }
    }

    public void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
            }
        }
    }



    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
           return false;

        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public void string_sharedpreferene(String key, String value){
        SharedPreferences sharedPreferences = getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value).commit();
    }

    public void boolean_sharedpreferene(String key, boolean value){
        SharedPreferences sharedPreferences = getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value).commit();
    }

    public SharedPreferences getSharedPreferences(String key){
        SharedPreferences sharedPreferences = getSharedPreferences(key, Context.MODE_PRIVATE);
        return sharedPreferences;
    }


}
