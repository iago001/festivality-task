package com.iago.networkingconference.database;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    private final Context context;

    public DatabaseModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    DatabaseHelper provideDatabaseHelper() {
        return new DatabaseHelper(context);
    }

    @Singleton
    @Provides
    DataStoreHelper provideDataStoreHelper(DatabaseHelper databaseHelper) {
        return new DataStoreHelper(databaseHelper);
    }

    @Singleton
    @Provides
    DataFetchHelper provideDataFetchHelper(DatabaseHelper databaseHelper) {
        return new DataFetchHelper(databaseHelper);
    }
}
