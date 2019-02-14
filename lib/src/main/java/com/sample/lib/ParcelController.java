package com.sample.lib;

import android.content.Context;
import android.os.Parcelable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParcelController<T extends Parcelable> {

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

    public long createParcel(T data) {
        ParcelizedData parcelizedData = new ParcelizedData(data);
        final CustomParcel customParcel = new CustomParcel();
        customParcel.setData(parcelizedData.toBytes());
        parcelizedData.recycle();
        Callable<Long> callable = new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return CustomParcelDB.getDatabase(context).customParcelDao().insert(customParcel);
            }
        };

        Future<Long> future = Executors.newSingleThreadExecutor().submit(callable);

        long id = 0;
        try {
            id = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return id;
    }

    public T decomposeParcel(final long id, Parcelable.Creator<T> creator) {
        T result = null;
        Callable<CustomParcel> callable = new Callable<CustomParcel>() {
            @Override
            public CustomParcel call() throws Exception {
                return CustomParcelDB.getDatabase(context).customParcelDao().getData(id + "");
            }
        };

        Future<CustomParcel> future = Executors.newSingleThreadExecutor().submit(callable);
        CustomParcel value = null;
        try {
            value = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (null != value) {
            ParcelizedData data = new ParcelizedData(value.getData());
            result = creator.createFromParcel(data.getParcel());
            data.recycle();
        }
        return result;
    }

}
