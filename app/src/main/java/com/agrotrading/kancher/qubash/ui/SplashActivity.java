package com.agrotrading.kancher.qubash.ui;

import android.support.v7.app.AppCompatActivity;

import com.agrotrading.kancher.qubash.ui.MainActivity_;
import com.agrotrading.kancher.qubash.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.api.BackgroundExecutor;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends AppCompatActivity {

    @AfterViews
    void ready() {
        doInBackground();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BackgroundExecutor.cancelAll("start", true);
    }

    @Background(id="start",delay=3000)
    void doInBackground() {
        MainActivity_.intent(this).start();
        finish();
    }
}
