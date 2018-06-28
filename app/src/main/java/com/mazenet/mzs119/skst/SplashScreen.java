package com.mazenet.mzs119.skst;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.mazenet.mzs119.skst.Utils.Config;
import com.mazenet.mzs119.skst.Utils.ConnectionDetector;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private String android_id;
    int MY_PERMISSION_ACCESS_COURSE_LOCATION = 1;
    ConnectionDetector cd;
ShimmerFrameLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        container = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container1);
        container.startShimmerAnimation();
        cd = new ConnectionDetector(this);
        pref = getApplicationContext().getSharedPreferences(Config.preff,
                MODE_PRIVATE);
        editor = pref.edit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    ) {


                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (pref.getString("userid", "").isEmpty()) {
                            Intent it = new Intent(SplashScreen.this, MainActivity.class);
                            startActivity(it);
                            finish();
                        } else {
                            Intent it = new Intent(SplashScreen.this, MenuActivity.class);
                            startActivity(it);
                            finish();

                        }


                    }
                }, SPLASH_TIME_OUT);


            } else {
                ActivityCompat.requestPermissions(SplashScreen.this,
                        new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE, Manifest.permission.CAMERA},
                        MY_PERMISSION_ACCESS_COURSE_LOCATION);

            }


        } else {


            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    if (pref.getString("userid", "").isEmpty()) {
                        Intent it = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(it);
                        finish();
                    } else {

                        Intent it = new Intent(SplashScreen.this, MenuActivity.class);
                        startActivity(it);
                        finish();

                    }
                    if (cd.isConnectedToInternet()) {
                        Config.isconnected = true;
                    } else {
                        Config.isconnected = false;
                    }
                  /*  if (cd.isConnectedToInternet()) {

                        if (pref.getString("userid", "").isEmpty()) {
                            Intent it = new Intent(SplashScreen.this, MainActivity.class);
                            startActivity(it);
                            finish();
                        } else {

                            Intent it = new Intent(SplashScreen.this, MenuActivity.class);
                            startActivity(it);
                            finish();

                        }

                    } else {

                        Toast.makeText(SplashScreen.this, "Connect Onine To work", Toast.LENGTH_SHORT).show();

                    } */


                }
            }, SPLASH_TIME_OUT);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        Intent it = new Intent(SplashScreen.this, SplashScreen.class);
        startActivity(it);
        finish();

    }

}
