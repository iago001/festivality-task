package com.iago.networkingconference;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.iago.networkingconference.views.activities.LocationPermissionActivity;
import com.iago.networkingconference.views.activities.MainActivity;
import com.iago.networkingconference.views.activities.StartUsingAppActivity;
import com.orhanobut.hawk.Hawk;

public class SplashActivity extends AppCompatActivity {

    public static final String FLOW_COMPLETED = "flow_completed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Hawk.contains(FLOW_COMPLETED)) {
                    startMainActivity();
                } else {
                    startFirstTimeFlowActivity();
                }
            }
        }, 2000L);
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void startFirstTimeFlowActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            startActivity(new Intent(this, LocationPermissionActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, StartUsingAppActivity.class));
            finish();
        }
    }
}
