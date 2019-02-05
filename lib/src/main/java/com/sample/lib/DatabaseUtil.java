package com.sample.lib;

public class DatabaseUtil implements Runnable {

    private final CustomParcel customParcel;

    public DatabaseUtil(CustomParcel customParcel) {
        this.customParcel = customParcel;
    }

    interface CustomParcelHandler{




    }

    @Override
    public void run() {
        //Run in background
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
    }
}
