package com.iago.networkingconference;

import android.content.Context;

import com.iago.networkingconference.network.FestivalityAPI;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class UtilModule {

    private Context context;

    public UtilModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    EventBus provideEventBus() {
        return EventBus.getDefault();
    }

}
