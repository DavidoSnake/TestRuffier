package com.MaJnr.ruffier;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.MaJnr.testruffier.R;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.Wearable;

import java.util.Objects;

import static com.MaJnr.common.Constants.START_MEASURE_PATH;

public class MainActivityWear extends WearableActivity implements DataClient.OnDataChangedListener {


    private final String TAG = "MainActivityWear";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Enables Always-on
        setAmbientEnabled();
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
                    Intent intent = new Intent(getApplicationContext(), HeartRateActivity.class);
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Wearable.getDataClient(this).addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPauseMain");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStopMain");
        Wearable.getDataClient(this).removeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

