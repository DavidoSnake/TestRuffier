package com.example.ruffier;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.Date;

import static com.example.common.Constants.START_MEASURE;
import static com.example.common.Constants.START_MEASURE_PATH;

public class SyncAsyncTasksMobile extends AsyncTask<Integer, Integer, Integer> {

    private DataClient mDataClient;

    SyncAsyncTasksMobile(Context mContext) {
        super();
        mDataClient = Wearable.getDataClient(mContext);
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        int startMeasure = integers[0];

        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(START_MEASURE_PATH);
        putDataMapRequest.getDataMap().putInt(START_MEASURE, startMeasure);
        putDataMapRequest.getDataMap().putLong("time", new Date().getTime()); // forces the onDataChanged to be caught
        PutDataRequest putDataRequest = putDataMapRequest.asPutDataRequest();
        putDataMapRequest.setUrgent();

        try {
            mDataClient.putDataItem(putDataRequest);
        } catch (Exception e) {
            Log.e("Sync", "Error uploading data to Wear", e);
            System.out.println("err sync");
        }
        return -1;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        Log.d("OnPostExecute", "---------ok---------");
    }
}
