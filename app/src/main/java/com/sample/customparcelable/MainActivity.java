package com.sample.customparcelable;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sample.lib.ParcelController;

public class MainActivity extends AppCompatActivity {

    private CustomModel customModel;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.name);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customModel = new CustomModel(1,editText.getText().toString());
                ParcelController parcelController = ParcelController.getInstance(MainActivity.this);
                Intent intent = new Intent(MainActivity.this,SecondaryActivity.class);
                intent.putExtra("1",parcelController.createParcel(customModel));
                startActivity(intent);
                finish();
            }
        });

    }
}
