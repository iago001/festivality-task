package com.iago.networkingconference.views.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.iago.networkingconference.R;
import com.iago.networkingconference.SplashActivity;
import com.orhanobut.hawk.Hawk;

import static com.iago.networkingconference.SplashActivity.FLOW_COMPLETED;

public class StartUsingAppActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_app);

        Button locationPermission = findViewById(R.id.start_app);
        locationPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hawk.put(FLOW_COMPLETED, true);
                startActivity(new Intent(StartUsingAppActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
