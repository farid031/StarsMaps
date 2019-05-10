package com.farid.starsmaps.splash_screen;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.farid.starsmaps.R;
import com.farid.starsmaps.main_activity.MainActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        int waktu_loading = 2000;
        //2000 = 2 detik
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //setelah loading maka akan langsung berpindah ke MainActivity activity
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                finish();
            }
        }, waktu_loading);
    }
}
