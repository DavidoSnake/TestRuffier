package com.example.ruffier;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.common.Constants;
import com.example.common.Patient;
import com.example.common.SQLiteDBHandler;
import com.example.testruffier.R;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;

import static com.example.common.Constants.HEART_MEASURE_1;
import static com.example.common.Constants.HEART_MEASURE_2;
import static com.example.common.Constants.HEART_MEASURE_3;
import static com.example.common.Constants.HEART_RATE_COUNT_PATH;
import static com.example.common.Constants.START_MEASURE_MSG;

public class WaitFragment extends androidx.fragment.app.Fragment implements DataClient.OnDataChangedListener {

    final String TAG = "WaitFragment";
    static TextView tv;
    static TextView m1;
    static TextView m2;
    static TextView m3;
    DataClient mDataClient;
    BroadcastReceiver br;
    SyncAsyncTasksMobile mSyncAsyncTasksMobile;
    Intent intent;
    IntentFilter intentFilter;
    int meas1 = 0;
    int meas2 = 0;
    int meas3 = 0;
    private SQLiteDBHandler dbHandler;
    private int patientId;

    CountDownTimer timerBeforeDestroy = new CountDownTimer(3000, 1000) {
        @Override
        public void onTick(long l) {
            Log.d(TAG, "destruction in " + l/1000);
        }

        @Override
        public void onFinish() {
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().remove(WaitFragment.this).commit();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_wait, container, false);
        tv = rootView.findViewById(R.id.wait_txt);
        m1 = rootView.findViewById(R.id.m1);
        m2 = rootView.findViewById(R.id.m2);
        m3 = rootView.findViewById(R.id.m3);

        mDataClient = Wearable.getDataClient(Objects.requireNonNull(getActivity()));
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive");
                System.out.println("oneceive");
                int startMeasure = intent.getIntExtra(Constants.START_MEASURE, 0);
                mSyncAsyncTasksMobile = new SyncAsyncTasksMobile(getActivity());
                mSyncAsyncTasksMobile.execute(startMeasure);
            }
        };
        intent = new Intent(getActivity(), BroadcastService.class);

        Wearable.getDataClient(Objects.requireNonNull(getContext())).addListener(this);
        dbHandler = new SQLiteDBHandler(getContext());
        patientId = ViewPatientActivity.patientId;

        return rootView;
    }


//todo : prevent onResume to be called when the orientation changes

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        System.out.println("onresume");
        Wearable.getDataClient(Objects.requireNonNull(getContext())).addListener(this);
        intentFilter = new IntentFilter(START_MEASURE_MSG);
        Objects.requireNonNull(getActivity()).registerReceiver(br, intentFilter);
        getActivity().startService(intent);
    }

    @Override
    public void onPause() {
        System.out.println("onPausewait");
        super.onPause();
        Wearable.getDataClient(Objects.requireNonNull(getContext())).removeListener(this);
        Objects.requireNonNull(getActivity()).stopService(intent);
        getActivity().unregisterReceiver(br);
    }

    @Override
    public void onDestroyView() {
        System.out.println("waitDestroyed");
        super.onDestroyView();
        Objects.requireNonNull(getActivity()).finish();
        startActivity(getActivity().getIntent());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        Log.d(TAG, "OnDataChanged" + dataEventBuffer);

        for (DataEvent event : dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem changed
                DataItem item = event.getDataItem();
                if (Objects.requireNonNull(item.getUri().getPath()).compareTo(HEART_RATE_COUNT_PATH) == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    if (dataMap.containsKey(HEART_MEASURE_1)) {
                        Log.d(TAG, "onDataChanged : measure 1");
                        meas1 = dataMap.getInt(HEART_MEASURE_1, 0);
                        m1.setText("Mesure 1: " + meas1);
                    } else if (dataMap.containsKey(HEART_MEASURE_2)) {
                        Log.d(TAG, "onDataChanged : measure 2");
                        meas2 = dataMap.getInt(HEART_MEASURE_2, 0);
                        m2.setText("Mesure 2: " + meas2);
                    } else if (dataMap.containsKey(HEART_MEASURE_3)) {
                        Log.d(TAG, "onDataChanged : measure 3");
                        meas3 = dataMap.getInt(HEART_MEASURE_3, 0);
                        m3.setText("Mesure 3: " + meas3);
                        timerBeforeDestroy.start();
                    }
                }
            }
        }
        if (meas1 != 0 && meas2 != 0 && meas3 != 0) {
            dbHandler.addMeasures(patientId, meas1, meas2, meas3);
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE);
            String sDate = sdf.format(cal.getTime());
            Log.d(TAG, sDate);
            dbHandler.addDate(patientId, sDate);
        }
    }
}
