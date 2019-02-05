package com.sample.lib;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelizedData {
    private Parcel parcel = Parcel.obtain();

    ParcelizedData(Parcelable parcelable) {
        parcelable.writeToParcel(parcel, 0);
    }

    public ParcelizedData(byte[] data) {
        parcel.unmarshall(data, 0, data.length);
        parcel.setDataPosition(0);
    }

    byte[] toBytes() {
        return parcel.marshall();
    }

    Parcel getParcel() {
        parcel.setDataPosition(0);
        return parcel;
    }

    void recycle() {
        parcel.recycle();
    }
}
