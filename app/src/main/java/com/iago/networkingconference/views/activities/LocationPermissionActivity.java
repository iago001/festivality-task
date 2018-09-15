package com.iago.networkingconference.views.activities;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;

import com.iago.networkingconference.R;
import com.iago.networkingconference.SplashActivity;

import timber.log.Timber;

public class LocationPermissionActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_permission);

        Button locationPermission = findViewById(R.id.enable_location_button);
        locationPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] permissions = new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                };
                ActivityCompat.requestPermissions(LocationPermissionActivity.this,
                        permissions,
                        100);
            }
        });

        Button skipButton = findViewById(R.id.skip_button);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextActivity();
            }
        });
    }

    private void startNextActivity() {
        startActivity(new Intent(this, StartUsingAppActivity.class));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Timber.d("permissions granted: %d", grantResults[0]);
        startNextActivity();
    }
}
