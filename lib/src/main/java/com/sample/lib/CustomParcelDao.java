package com.sample.lib;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;

@Dao
public interface CustomParcelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(CustomParcel customParcel);

    @Query("SELECT * FROM parcel WHERE id = :id")
    CustomParcel getData(String id);

    @Query("DELETE FROM parcel")
    void deleteAll();

}
