package com.example.ruffier;

import androidx.appcompat.app.AppCompatActivity;
import androidx.wear.widget.CircularProgressLayout;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.testruffier.R;

public class WearAlertInfoActivity extends WearableActivity implements View.OnClickListener {

    private static final String TAG = "WearAlertInfoActivity";
    Button okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        okButton = findViewById(R.id.okButton);
        okButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        finish();
    }

}