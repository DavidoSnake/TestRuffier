package com.MaJnr.ruffier;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;

import com.MaJnr.testruffier.R;

public class WearAlertInfoActivity extends WearableActivity implements View.OnClickListener {

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