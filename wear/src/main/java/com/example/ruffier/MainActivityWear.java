package com.example.ruffier;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;

import com.example.common.Person;
import com.example.common.SQLiteDBHandler;
import com.example.testruffier.R;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import java.util.Objects;
import java.util.Timer;

import static android.app.PendingIntent.getActivity;
import static com.example.common.Constants.HEART_MEASURE_1;
import static com.example.common.Constants.HEART_MEASURE_2;
import static com.example.common.Constants.HEART_MEASURE_3;
import static com.example.common.Constants.HEART_RATE_VALUE;
import static com.example.common.Constants.START_MEASURE;
import static com.example.common.Constants.START_MEASURE_PATH;

public class MainActivityWear extends WearableActivity implements DataClient.OnDataChangedListener {

    TextView mTextView;

    // help :
    // https://members.femto-st.fr/jf-couchot/sites/femto-st.fr.jf-couchot/files/content/mainwear.pdf
    // https://stackoverflow.com/questions/36760344/how-to-read-heart-rate-from-android-wear
    // https://github.com/alejandrocq/HeartRateTest
    private static final String TAG = "MainActivityWear";
    TextView mTextViewHeart;
    SensorManager mSensorManager;
    Sensor mHeartRateSensor;


    TextView isStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
       // setContentView(R.layout.activity_main);

        //mTextView = findViewById(R.id.text);
        //mTextViewHeart = findViewById(R.id.rate);


        // hearthrate sensor
       // mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
       // assert mSensorManager != null;
       // mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        // Enables Always-on
        setAmbientEnabled();

        //Button rateView = findViewById(R.id.rateView);
      /*  rateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HeartRateActivity.class);
                startActivity(intent);
            }
        });*/

        Wearable.getDataClient(this).addListener(this);
        //isStarted = findViewById(R.id.isTestStarted);
    }

/*

    public void startTimer(View view) {
        timer.start();
        // Log.d("msg", Constants.HEART_RATE_MSG);
        // Log.d("value", Constants.HEART_RATE_VALUE);
    }
*/

/*
    public void startMeasure() {
        boolean sensorRegistered = mSensorManager.registerListener(this, mHeartRateSensor, SensorManager.SENSOR_DELAY_FASTEST);
        Log.d("Sensor Status:", " Sensor registered: " + (sensorRegistered ? "yes" : "no"));
    }
*/

/*
    public void stopMeasure() {
        mSensorManager.unregisterListener(this);
    }
*/

/*
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            String msg = "" + (int)event.values[0];
            mTextViewHeart.setText(msg);
            Log.d(TAG, msg);
        }
        else
            Log.d(TAG, "Unknown sensor type");
    }
*/

/*    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "onAccuracyChanged - accuracy: " + accuracy);
    }*/

    @Override
    public void onDataChanged(@NonNull DataEventBuffer dataEventBuffer) {
        Log.d(TAG, "OnDataChanged"+ dataEventBuffer);
        System.out.println("DataCahnged");

        for (DataEvent event : dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem changed
                DataItem item = event.getDataItem();
                if (Objects.requireNonNull(item.getUri().getPath()).compareTo(START_MEASURE_PATH) == 0) {
                    Intent intent = new Intent(getApplicationContext(), HeartRateActivity.class);
                    startActivity(intent);
                }
            }
        }

    }

/*
    private void startTestTimer() {
        CountDownTimer t = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {
                String txt = ""+l / 1000;
                mTextView.setText(txt);
            }

            @Override
            public void onFinish() {
                String txt = "Done !";
                mTextView.setText(txt);

            }
        };
        t.start();
    }
*/


/*    @Override
    protected void onResume() {
        super.onResume();
        Wearable.getDataClient(this).addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.getDataClient(this).removeListener(this);
    }*/
}

