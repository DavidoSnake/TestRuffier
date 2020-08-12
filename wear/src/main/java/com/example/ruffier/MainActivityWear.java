package com.example.ruffier;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.testruffier.R;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.Wearable;

import java.util.Objects;

import static com.example.common.Constants.REDIRECT_MOBILE_PATH;
import static com.example.common.Constants.START_MEASURE_PATH;

public class MainActivityWear extends WearableActivity implements DataClient.OnDataChangedListener {


    private final String TAG = "MainActivityWear";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Enables Always-on
        setAmbientEnabled();

        Wearable.getDataClient(this).addListener(this);
    }

    @Override
    public void onDataChanged(@NonNull DataEventBuffer dataEventBuffer) {
        Log.d(TAG, "OnDataChanged " + dataEventBuffer);

        for (DataEvent event : dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem changed
                DataItem item = event.getDataItem();
                if (Objects.requireNonNull(item.getUri().getPath()).compareTo(START_MEASURE_PATH) == 0) {
                    Log.d(TAG, "OnDataChanged : start signal");
                    //todo: check if activity is no already running
                    Intent intent = new Intent(getApplicationContext(), HeartRateActivity.class);
                    startActivity(intent);
                }
            }
        }

    }
}

