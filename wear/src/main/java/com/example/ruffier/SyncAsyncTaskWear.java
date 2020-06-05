package com.example.ruffier;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.common.Constants;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.Date;

import static com.example.common.Constants.HEART_MEASURE_1;
import static com.example.common.Constants.HEART_MEASURE_2;
import static com.example.common.Constants.HEART_MEASURE_3;
import static com.example.common.Constants.HEART_RATE_COUNT_PATH;
import static com.example.common.Constants.HEART_RATE_VALUE;

public class SyncAsyncTaskWear extends AsyncTask<Integer, Integer, Integer> {

    private DataClient mDataClient;
    int measure_nb;

    SyncAsyncTaskWear(Context mContext, int measure_nb) {
        super();
        mDataClient = Wearable.getDataClient(mContext);
        this.measure_nb = measure_nb;
    }

    @Override
    protected Integer doInBackground(Integer... params) {
        int heartRateCount = params[0];
       /* int measure1 = params[1];
        int measure2 = params[2];
        int measure3 = params[3];*/

        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(HEART_RATE_COUNT_PATH);
        if (measure_nb == 1) {
            putDataMapRequest.getDataMap().putInt(HEART_MEASURE_1, heartRateCount);
        } else if (measure_nb == 2) {
            putDataMapRequest.getDataMap().putInt(HEART_MEASURE_2, heartRateCount);
        } else if (measure_nb == 3) {
            putDataMapRequest.getDataMap().putInt(HEART_MEASURE_3, heartRateCount);
        }
        putDataMapRequest.getDataMap().putLong("time", new Date().getTime()); // forces the onDataChanged to be caught
        PutDataRequest putDataReq = putDataMapRequest.asPutDataRequest();
        putDataReq.setUrgent();
        try {
            mDataClient.putDataItem(putDataReq);
        }
        catch (Exception e) {
            Log.e("Sync", "Error uploading data to Mobile", e);
        }
        return -1;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        Log.d("PostExecute", "---------------OK---------------");
    }
}
