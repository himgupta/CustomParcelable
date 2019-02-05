package com.sample.lib;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

@Database(entities = {CustomParcel.class}, version = 1)
public abstract class CustomParcelDB extends RoomDatabase {

    public abstract CustomParcelDao customParcelDao();

    private static volatile CustomParcelDB INSTANCE;

    public static CustomParcelDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CustomParcelDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CustomParcelDB.class, "custom_parcel")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}
