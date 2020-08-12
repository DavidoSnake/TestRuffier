package com.example.ruffier;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.Date;

import static com.example.common.Constants.DEVICES_SYNC;
import static com.example.common.Constants.DEVICES_SYNC_PATH;
import static com.example.common.Constants.HEART_MEASURE_1;
import static com.example.common.Constants.HEART_MEASURE_2;
import static com.example.common.Constants.HEART_MEASURE_3;
import static com.example.common.Constants.HEART_RATE_COUNT_PATH;

public class SyncAsyncTaskWearRunningTest extends AsyncTask<Integer, Integer, Integer> {

    private DataClient mDataClient;
    int measure_nb;

    SyncAsyncTaskWearRunningTest(Context mContext, int measure_nb) {
        super();
        mDataClient = Wearable.getDataClient(mContext);
        this.measure_nb = measure_nb;
    }

    @Override
    protected Integer doInBackground(Integer... params) {
        int flags = params[0];

        PutDataMapRequest dataMapRate = PutDataMapRequest.create(HEART_RATE_COUNT_PATH);
        PutDataMapRequest dataMapSync = PutDataMapRequest.create(DEVICES_SYNC_PATH);

        PutDataRequest putDataReq;

        if (measure_nb == 0) {
            // send wear start of test signal
            dataMapSync.getDataMap().putInt(DEVICES_SYNC, flags);
            dataMapSync.getDataMap().putLong("time", new Date().getTime()); // forces the onDataChanged to be caught
            putDataReq = dataMapSync.asPutDataRequest();
        } else {
            // send heart measures
            if (measure_nb == 1) {
                dataMapRate.getDataMap().putInt(HEART_MEASURE_1, flags);
            } else if (measure_nb == 2) {
                dataMapRate.getDataMap().putInt(HEART_MEASURE_2, flags);
            } else if (measure_nb == 3) {
                dataMapRate.getDataMap().putInt(HEART_MEASURE_3, flags);
            }
            dataMapRate.getDataMap().putLong("time", new Date().getTime()); // forces the onDataChanged to be caught
            putDataReq = dataMapRate.asPutDataRequest();
        }
        putDataReq.setUrgent();
        try {
            mDataClient.putDataItem(putDataReq);
        } catch (Exception e) {
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
