package com.farid.starsmaps.main_activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.farid.starsmaps.R;
import com.farid.starsmaps.help_activity.HelpActivity;
import com.farid.starsmaps.list_toko.ListTokoActivity;
import com.farid.starsmaps.tentang_activity.TentangApps;
import com.farid.starsmaps.rute.RuteActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton btnCariJalur, btnListToko, btnTentangApp, btnExitApp, btnHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCariJalur = findViewById(R.id.BtnCariJalur);
        btnListToko = findViewById(R.id.BtnListToko);
        btnTentangApp = findViewById(R.id.BtnInfoApp);
        btnExitApp = findViewById(R.id.BtnExitApp);
        btnHelp = findViewById(R.id.BtnHelp);

        btnCariJalur.setOnClickListener(this);
        btnListToko.setOnClickListener(this);
        btnTentangApp.setOnClickListener(this);
        btnExitApp.setOnClickListener(this);
        btnHelp.setOnClickListener(this);
    }

    public boolean doubleTapParam = false;
    @Override
    public void onBackPressed() {
        if (doubleTapParam) {
            super.onBackPressed();
            return;
        }

        this.doubleTapParam = true;
        Toast.makeText(this, "Tap sekali lagi untuk keluar", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleTapParam = false;
            }
        }, 2000);
    }

    @Override
    public void onClick(View v) {
        if (v == btnListToko){
            startActivity(new Intent(this, ListTokoActivity.class));
        }
        if (v == btnExitApp){
            finish();
        }
        if (v == btnCariJalur){
            startActivity(new Intent(this, RuteActivity.class));
        }
        if (v == btnTentangApp){
            startActivity(new Intent(this, TentangApps.class));
        }
        if (v == btnHelp){
            startActivity(new Intent(this, HelpActivity.class));
        }
    }
}
