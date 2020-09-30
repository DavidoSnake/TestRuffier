package com.example.ruffier;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.Date;

import static com.example.common.Constants.MOBILE_QUIT_APP_PATH;
import static com.example.common.Constants.START_MEASURE_PATH;
import static com.example.common.Constants.STOP_MEASURE_PATH;

public class SyncAsyncTasksMobileRunningTest extends AsyncTask<Integer, Integer, Integer> {

    private DataClient mDataClient;

    SyncAsyncTasksMobileRunningTest(Context mContext) {
        super();
        mDataClient = Wearable.getDataClient(mContext);
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        int flags = integers[0];

        PutDataMapRequest startDataMapRequest = PutDataMapRequest.create(START_MEASURE_PATH);
        PutDataMapRequest stopDataMapRequest = PutDataMapRequest.create(STOP_MEASURE_PATH);
        PutDataMapRequest quitDataMapRequest = PutDataMapRequest.create(MOBILE_QUIT_APP_PATH);

        PutDataRequest putDataRequest = null;

        if (flags == 0) {
            startDataMapRequest.getDataMap().putLong("time", new Date().getTime()); // forces the onDataChanged to be caught
            putDataRequest = startDataMapRequest.asPutDataRequest();
        } else if (flags == 1) {
            stopDataMapRequest.getDataMap().putLong("time", new Date().getTime()); // forces the onDataChanged to be caught
            putDataRequest = stopDataMapRequest.asPutDataRequest();
        } else {
            quitDataMapRequest.getDataMap().putLong("time", new Date().getTime()); // forces the onDataChanged to be caught
            putDataRequest = quitDataMapRequest.asPutDataRequest();
        }

        assert putDataRequest != null;
        putDataRequest.setUrgent();

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
