package com.iago.networkingconference.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iago.networkingconference.models.Attendee;
import com.iago.networkingconference.models.AttendeeAdapter;
import com.orhanobut.hawk.Hawk;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    public static final String DEVICE_ID = "device_id";

    @Singleton
    @Provides
    FestivalityAPI provideFestivalityAPI(GsonConverterFactory gsonConverterFactory) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                original.newBuilder().addHeader("x-header-request", "{\"deviceId\": " + Hawk.get(DEVICE_ID) + "\"}");
                return chain.proceed(original);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("http://api.festivality.co/v2/")
        .addConverterFactory(gsonConverterFactory)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(clientBuilder.build())
        .build();

        return retrofit.create(FestivalityAPI.class);
    }

    @Singleton
    @Provides
    public GsonConverterFactory provideGsonConverterFactory() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        AttendeeAdapter attendeeAdapter = new AttendeeAdapter();
        gsonBuilder.registerTypeAdapter(Attendee.class, attendeeAdapter);
        Gson gson = gsonBuilder.create();
        attendeeAdapter.setGson(gson);
        return GsonConverterFactory.create(gson);
    }
}
