package com.sample.lib;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

public abstract class CustomParcelable implements Parcelable {

    private Context mContext;
    private CustomParcel customParcel;
    public CustomParcelable() {
    }

    public CustomParcelable(Parcel in, Context context) {
        this.mContext = context;
        String className = in.readString();
        Log.i("CustomParcelable","Constructor: " + this.getClass().getSimpleName() + "; In parcel: " + className);
        try {
            rehydrate(this, in);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    public static final Parcelable.Creator<CustomParcelable> CREATOR = new Parcelable.Creator<CustomParcelable>() {
        public CustomParcelable createFromParcel(Parcel in) {
            // get class from first parcelled item
            Class<?> parceledClass;
            try {
                parceledClass = Class.forName(in.readString());
                Log.i("CustomParcelable","Creator: " + parceledClass.getSimpleName());
                // create instance of that class
                CustomParcelable model = (CustomParcelable) parceledClass.newInstance();
                rehydrate(model, in);
                return model;
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        public CustomParcelable[] newArray(int size) {
            return new CustomParcelable[size];
        }
    };
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.getClass().getName());
        try {
            dehydrate(this, dest);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    // writes fields of a CustomParcelable to a parcel
    // does not include the first parcelled item -- the class name
    protected static void dehydrate(CustomParcelable model, Parcel out) throws IllegalArgumentException, IllegalAccessException {
        Log.i("CustomParcelable","dehydrating... " + model.getClass().toString());
        // get the fields
        Field[] fields = model.getClass().getDeclaredFields();
        // sort the fields so it is in deterministic order
        Arrays.sort(fields, compareMemberByName);
        // populate the fields
        for (Field field : fields){
            field.setAccessible(true);
            if (field.getType().equals(int.class)) {
                out.writeInt(field.getInt(model));
            } else if (field.getType().equals(double.class)) {
                out.writeDouble(field.getDouble(model));
            } else if (field.getType().equals(float.class)) {
                out.writeFloat(field.getFloat(model));
            } else if (field.getType().equals(long.class)) {
                out.writeLong(field.getLong(model));
            } else if (field.getType().equals(String.class)) {
                out.writeString((String) field.get(model));
            } else if (field.getType().equals(boolean.class)) {
                out.writeByte(field.getBoolean(model) ? (byte) 1 : (byte) 0);
            } else if (field.getType().equals(Date.class)) {
                Date date = (Date) field.get(model);
                if (date != null) {
                    out.writeLong(date.getTime());
                } else {
                    out.writeLong(0);
                }
            } else if (CustomParcelable.class.isAssignableFrom(field.getType())) {
                // why did this happen?
                Log.e("CustomParcelable", "CustomParcelable F*ck up: " + " (" + field.getType().toString() + ")");
                out.writeParcelable((CustomParcelable) field.get(model), 0);
            } else {
                // wtf
                Log.e("CustomParcelable", "Could not write field to parcel: " + " (" + field.getType().toString() + ")");
            }
        }
    }
    protected static void rehydrate(CustomParcelable model, Parcel in) throws IllegalArgumentException, IllegalAccessException {
        Log.i("CustomParcelable","rehydrating... " + model.getClass().toString());
        // get the fields
        Field[] fields = model.getClass().getDeclaredFields();
        // sort the fields so it is in deterministic order
        Arrays.sort(fields, compareMemberByName);
        // populate the fields
        for (Field field : fields){
            field.setAccessible(true);
            if (field.getType().equals(int.class)) {
                field.set(model, in.readInt());
            } else if (field.getType().equals(double.class)) {
                field.set(model, in.readDouble());
            } else if (field.getType().equals(float.class)) {
                field.set(model, in.readFloat());
            } else if (field.getType().equals(long.class)) {
                field.set(model, in.readLong());
            } else if (field.getType().equals(String.class)) {
                field.set(model, in.readString());
            } else if (field.getType().equals(boolean.class)) {
                field.set(model, in.readByte() == 1);
            } else if (field.getType().equals(Date.class)) {
                Date date = new Date(in.readLong());
                field.set(model, date);
            } else if (CustomParcelable.class.isAssignableFrom(field.getType())) {
                Log.e("CustomParcelable", "read CustomParcelable: " + " (" + field.getType().toString() + ")");
                field.set(model, in.readParcelable(field.getType().getClassLoader()));
            } else {
                // wtf
                Log.e("CustomParcelable", "Could not read field from parcel: " + field.getName() + " (" + field.getType().toString() + ")");
            }
        }
    }


    /*
     * Comparator object for Members, Fields, and Methods
     */
    private static Comparator<Field> compareMemberByName =
            new CompareMemberByName();

    private static class CompareMemberByName implements Comparator {
        public int compare(Object o1, Object o2) {
            String s1 = ((Member)o1).getName();
            String s2 = ((Member)o2).getName();

            if (o1 instanceof Method) {
                s1 += getSignature((Method)o1);
                s2 += getSignature((Method)o2);
            } else if (o1 instanceof Constructor) {
                s1 += getSignature((Constructor)o1);
                s2 += getSignature((Constructor)o2);
            }
            return s1.compareTo(s2);
        }
    }


    /**
     * Compute the JVM signature for the class.
     */
    private static String getSignature(Class clazz) {
        String type = null;
        if (clazz.isArray()) {
            Class cl = clazz;
            int dimensions = 0;
            while (cl.isArray()) {
                dimensions++;
                cl = cl.getComponentType();
            }
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < dimensions; i++) {
                sb.append("[");
            }
            sb.append(getSignature(cl));
            type = sb.toString();
        } else if (clazz.isPrimitive()) {
            if (clazz == Integer.TYPE) {
                type = "I";
            } else if (clazz == Byte.TYPE) {
                type = "B";
            } else if (clazz == Long.TYPE) {
                type = "J";
            } else if (clazz == Float.TYPE) {
                type = "F";
            } else if (clazz == Double.TYPE) {
                type = "D";
            } else if (clazz == Short.TYPE) {
                type = "S";
            } else if (clazz == Character.TYPE) {
                type = "C";
            } else if (clazz == Boolean.TYPE) {
                type = "Z";
            } else if (clazz == Void.TYPE) {
                type = "V";
            }
        } else {
            type = "L" + clazz.getName().replace('.', '/') + ";";
        }
        return type;
    }


    /*
     * Compute the JVM method descriptor for the method.
     */
    private static String getSignature(Method meth) {
        StringBuffer sb = new StringBuffer();

        sb.append("(");

        Class[] params = meth.getParameterTypes(); // avoid clone
        for (int j = 0; j < params.length; j++) {
            sb.append(getSignature(params[j]));
        }
        sb.append(")");
        sb.append(getSignature(meth.getReturnType()));
        return sb.toString();
    }

    /*
     * Compute the JVM constructor descriptor for the constructor.
     */
    private static String getSignature(Constructor cons) {
        StringBuffer sb = new StringBuffer();

        sb.append("(");

        Class[] params = cons.getParameterTypes(); // avoid clone
        for (int j = 0; j < params.length; j++) {
            sb.append(getSignature(params[j]));
        }
        sb.append(")V");
        return sb.toString();
    }
}
