package com.sample.customparcelable;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sample.lib.ParcelController;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CustomModel customModel = new CustomModel(1,"value");
        ParcelController parcelController = ParcelController.getInstance(this);
        Intent intent = new Intent(this,SecondaryActivity.class);
        intent.putExtra("1",parcelController.createParcel(customModel));
        startActivity(intent);
        finish();
    }
}
