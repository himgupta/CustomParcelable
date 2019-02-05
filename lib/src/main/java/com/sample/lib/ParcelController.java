package com.sample.lib;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.WorkerThread;

public class ParcelController<T extends Parcelable>{

    private static ParcelController mInstance = null;
    private Context context;

    private ParcelController(Context context) {
        this.context = context;
    }

    ;

    public static ParcelController getInstance(Context context) {
        synchronized (ParcelController.class) {
            if (null == mInstance) {
                mInstance = new ParcelController(context);
            }
            return mInstance;
        }
    }
    @WorkerThread
    public CustomParcel createParcel(T data) {
        ParcelizedData parcelizedData = new ParcelizedData(data);
        CustomParcel customParcel = new CustomParcel();
        customParcel.setData(parcelizedData.toBytes());
        parcelizedData.recycle();
        long id = CustomParcelDB.getDatabase(context).customParcelDao().insert(customParcel);
        customParcel.setId(id);
        return customParcel;
    }

    @WorkerThread
    public T decomposeParcel(CustomParcel customParcel, Parcelable.Creator<T> creator){
        T result = null;
        CustomParcel value = CustomParcelDB.getDatabase(context).customParcelDao().getData(customParcel.id+"");
        if(null!= value){
            ParcelizedData data = new ParcelizedData(value.getData());
            result = creator.createFromParcel(data.getParcel());
            data.recycle();
        }
        return result;
    }

}
