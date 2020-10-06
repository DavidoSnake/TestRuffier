package com.MaJnr.ruffier;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import static com.MaJnr.common.Constants.HEART_MEASURE_1;
import static com.MaJnr.common.Constants.HEART_MEASURE_2;
import static com.MaJnr.common.Constants.HEART_MEASURE_3;
import static com.MaJnr.common.Constants.MEASURE_1_MSG;
import static com.MaJnr.common.Constants.MEASURE_2_MSG;
import static com.MaJnr.common.Constants.MEASURE_3_MSG;
import static com.MaJnr.common.Constants.measureNb;
import static com.MaJnr.common.Constants.measuresList1;
import static com.MaJnr.common.Constants.measuresList2;
import static com.MaJnr.common.Constants.measuresList3;

public class WearHeartRateService extends Service implements SensorEventListener {

    SyncAsyncTaskWearRunningTest mSyncAsyncTaskWearRunningTest = null;

    public final String TAG = "WearHeartRateService";
    private SensorManager sensorManager;
    private Sensor heartRateSensor;
    private int heartRate = 0;
    int res = 0;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        sensorManager.registerListener(this, heartRateSensor, SensorManager.SENSOR_DELAY_FASTEST);

        // delete or comment to remove emulator values
        //------------------------
            if (measureNb == 1)
                stepRate = 65;
            if (measureNb == 2)
                stepRate = 90;
            if (measureNb == 3)
                stepRate = 75;
            heartEmulator.start();
        //-------------------------

        return super.onStartCommand(intent, flags, startId);
    }

    int stepRate;

    // delete or comment to remove heart rate emulation (used to debug)
    //-------------------------------------------
    CountDownTimer heartEmulator = new CountDownTimer(10000, 9000) {
        @Override
        public void onTick(long l) {
            heartRate = (int) (Math.random()*5 + stepRate);
            updateMeasuresList();
        }

        @Override
        public void onFinish() {

        }
    };
    //-------------------------------------------

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(TAG, "onSensorChanged : " + event.sensor.getType());
        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            heartRate = (int) event.values[0];
            updateMeasuresList();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.d(TAG, "onAccuracyChanged");
        updateMeasuresList();
    }

    private void updateMeasuresList() {
        Log.d(TAG, "updateMeasuresList");

        // the rate numbers could be zero
        if (heartRate != 0) {
            if (measureNb == 1) {
                measuresList1.add(heartRate);
            } else if (measureNb == 2) {
                measuresList2.add(heartRate);
            } else if (measureNb == 3) {
                measuresList3.add(heartRate);
            }
            sendHeartRateUpdate();
        } else Log.d(TAG, "value equals to 0: skipping sending");
    }

    private void sendHeartRateUpdate() {
        Log.d(TAG, "sendHeartRateUpdate");
        Intent intent = new Intent();
        if (measureNb == 1) {
            intent.setAction(MEASURE_1_MSG);
            intent.putExtra(HEART_MEASURE_1, heartRate);
        } else if (measureNb == 2) {
            intent.setAction(MEASURE_2_MSG);
            intent.putExtra(HEART_MEASURE_2, heartRate);
        } else if (measureNb == 3) {
            intent.setAction(MEASURE_3_MSG);
            intent.putExtra(HEART_MEASURE_3, heartRate);
        }
        sendBroadcast(intent);
    }

    public void finishService() {
        if (measureNb == 1) {
            // calcul of average rate
            int sum = 0;
            for (int i = 0; i < measuresList1.size(); i++) {
                sum += measuresList1.get(i);
            }
            if (measuresList1.size() != 0) {
                res = sum / measuresList1.size();
            }
            Log.d(TAG, "1st average : " + res);

        } else {
            if (measureNb == 2) {
                // calcul of average rate
                int sum = 0;
                for (int i = 0; i < measuresList2.size(); i++) {
                    sum += measuresList2.get(i);
                }
                if (measuresList2.size() != 0) {
                    res = sum / measuresList2.size();
                }
                Log.d(TAG, "2nd average : " + res);
                // result send to asynctask
            } else if (measureNb == 3) {
                // calcul of average rate
                int sum = 0;
                for (int i = 0; i < measuresList3.size(); i++) {
                    sum += measuresList3.get(i);
                }
                if (measuresList3.size() != 0) {
                    res = sum / measuresList3.size();
                }
                Log.d(TAG, "3rd average : " + res);
            }
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        finishService();
        // result send to asynctask
        mSyncAsyncTaskWearRunningTest = new SyncAsyncTaskWearRunningTest(getApplicationContext(), measureNb);
        mSyncAsyncTaskWearRunningTest.execute(res);
        measureNb++;
        sensorManager.unregisterListener(this);
        //super.onDestroy();
    }


}