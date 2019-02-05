package com.sample.lib;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

@Entity(tableName = "parcel")
public class CustomParcel implements Serializable {

    @PrimaryKey
    @ColumnInfo(name = "id")
    long id;
    @ColumnInfo(name = "data",typeAffinity = ColumnInfo.BLOB)
    byte[] data;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
