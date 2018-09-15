package com.iago.networkingconference;

import android.app.Application;
import android.content.Intent;
import android.provider.Settings;

import com.iago.networkingconference.database.DatabaseModule;
import com.iago.networkingconference.di.DaggerNetworkingConferenceComponent;
import com.iago.networkingconference.di.NetworkingConferenceComponent;
import com.iago.networkingconference.network.NetworkModule;
import com.iago.networkingconference.services.UpdateAttendeeListService;
import com.orhanobut.hawk.Hawk;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import timber.log.Timber;

public class NetworkingConferenceApplication extends Application {

    private static NetworkingConferenceComponent conferenceComponent;

    public static NetworkingConferenceComponent getComponent() {
        return conferenceComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

        Hawk.init(this).build();

        Hawk.put(NetworkModule.DEVICE_ID, Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));

        conferenceComponent = DaggerNetworkingConferenceComponent.builder()
                                        .utilModule(new UtilModule(this))
                                        .networkModule(new NetworkModule())
                                        .databaseModule(new DatabaseModule(this))
                                .build();

        startService(new Intent(this, UpdateAttendeeListService.class));
    }
}
