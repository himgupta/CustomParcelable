package com.sample.customparcelable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.sample.lib.ParcelController;

public class SecondaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary);
        ParcelController parcelController = ParcelController.getInstance(this);
        CustomModel customModel = (CustomModel)parcelController.decomposeParcel(getIntent().getLongExtra("1",0L),CustomModel.CREATOR);
        TextView textView = findViewById(R.id.name);
        textView.setText(customModel.getName());
//        Log.d("abc", "onCreate: "+customModel.getName());
    }
}
