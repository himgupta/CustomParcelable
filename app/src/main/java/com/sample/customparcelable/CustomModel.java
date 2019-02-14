package com.sample.customparcelable;

import android.os.Parcel;
import android.os.Parcelable;

public class CustomModel implements Parcelable {

    private int id;
    private String name;

    public CustomModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    protected CustomModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<CustomModel> CREATOR = new Creator<CustomModel>() {
        @Override
        public CustomModel createFromParcel(Parcel in) {
            return new CustomModel(in);
        }

        @Override
        public CustomModel[] newArray(int size) {
            return new CustomModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
