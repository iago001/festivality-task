package com.iago.networkingconference.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.iago.networkingconference.NetworkingConferenceApplication;
import com.iago.networkingconference.database.DataStoreHelper;
import com.iago.networkingconference.network.APIResponse;
import com.iago.networkingconference.network.DataFetched;
import com.iago.networkingconference.network.FestivalityAPI;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class UpdateAttendeeListService extends Service {

    public static final String DATA_FETCHED = "data_fetched";

    @Inject
    EventBus eventBus;

    @Inject
    FestivalityAPI festivalityAPI;

    @Inject
    DataStoreHelper dataStoreHelper;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        NetworkingConferenceApplication.getComponent().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (Hawk.contains(DATA_FETCHED)) {
            stopSelf();
        } else {
            festivalityAPI.fetchUserList()
                    .map(new Function<APIResponse, Boolean>() {
                        @Override
                        public Boolean apply(APIResponse apiResponse) throws Exception {
                            Timber.d("saving response to database, size: %d",
                                    apiResponse.getResponseSize());
                            return dataStoreHelper.saveAPIResponseToDB(apiResponse);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Boolean success) {
                            Timber.d("data save success: %s", success);
                            Hawk.put(DATA_FETCHED, true);
                            eventBus.post(new DataFetched());
                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.e(e, "error fetching user list");
                        }

                        @Override
                        public void onComplete() {
                            UpdateAttendeeListService.this.stopSelf();
                        }
                    });
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.d("service stopped");
    }
}
