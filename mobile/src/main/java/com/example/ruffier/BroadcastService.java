package com.example.ruffier;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Timer;

import static com.example.common.Constants.changeValue;
import static com.example.common.Constants.START_MEASURE;
import static com.example.common.Constants.START_MEASURE_MSG;

public class BroadcastService extends Service {

    public final String TAG = "BroadcastService";
    private static Timer mTimer =null;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        System.out.println("onstartcommand");
     /*   if (mTimer ==null) {
            mTimer = new Timer();
            long delay =  1000;
            long period = 1000*20;
            mTimer.scheduleAtFixedRate(new RandomValueTimerTask(getApplicationContext()), delay, period);
        }*/
        startIntent();
        return super.onStartCommand(intent, flags, startId);
    }

    public void startIntent() {
        if (changeValue) {
            System.out.println("val: "+changeValue);
            Intent intent = new Intent();
            intent.setAction(START_MEASURE_MSG);
            intent.putExtra(START_MEASURE, 0);
            getApplicationContext().sendBroadcast(intent);
            changeValue = false;
        } else {
            System.out.println("val: "+changeValue);
            Intent intent = new Intent();
            intent.setAction(START_MEASURE_MSG);
            intent.putExtra(START_MEASURE, 1);
            getApplicationContext().sendBroadcast(intent);
            changeValue = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
