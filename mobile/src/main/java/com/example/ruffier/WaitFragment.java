package com.example.ruffier;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.common.SQLiteDBHandler;
import com.example.testruffier.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
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

import static com.example.common.Constants.DEVICES_SYNC_PATH;
import static com.example.common.Constants.HEART_MEASURE_1;
import static com.example.common.Constants.HEART_MEASURE_2;
import static com.example.common.Constants.HEART_MEASURE_3;
import static com.example.common.Constants.HEART_RATE_COUNT_PATH;
import static com.example.common.Constants.POST_TEST_AD_TEST;
import static com.example.common.Constants.WEAR_QUIT_APP_PATH;
import static com.example.common.Constants.isWaitFragmentRunning;

public class WaitFragment extends androidx.fragment.app.Fragment implements DataClient.OnDataChangedListener, View.OnClickListener {

    final String TAG = "WaitFragment";
    TextView m1;
    TextView m2;
    TextView m3;
    DataClient mDataClient;
    TextView ok1;
    TextView ok2;
    TextView ok3;
    TextView ok4;
    TextView endTest;

    Button btn_cancel;

    SyncAsyncTasksMobileRunningTest startTestTask;
    SyncAsyncTasksMobileRunningTest stopTestTask;
    SyncAsyncTasksMobileRunningTest mobileQuitTestTask;

    // determines if the devices are synchronized
    private boolean areDevicesSync = false;

    int meas1 = 0;
    int meas2 = 0;
    int meas3 = 0;
    private SQLiteDBHandler dbHandler;
    private int patientId;
    private boolean isProperDestruction;

    // sync signal loop
    private Thread syncThread;

    // ad pops up after this activity has been destroy
    InterstitialAd ad;

    CountDownTimer timerBeforeDestroy = new CountDownTimer(3000, 1000) {
        @Override
        public void onTick(long l) {
            Log.d(TAG, "destruction in " + l / 1000);
        }

        @Override
        public void onFinish() {
            // destroys this fragment
            isProperDestruction = true;
            System.out.println("properdestruction");
            destroyFragment();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_wait, container, false);
        m1 = rootView.findViewById(R.id.m1);
        m2 = rootView.findViewById(R.id.m2);
        m3 = rootView.findViewById(R.id.m3);
        ok1 = rootView.findViewById(R.id.ok1);
        ok2 = rootView.findViewById(R.id.ok2);
        ok3 = rootView.findViewById(R.id.ok3);
        ok4 = rootView.findViewById(R.id.ok4);
        endTest = rootView.findViewById(R.id.endTest);

        btn_cancel = rootView.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);

        mDataClient = Wearable.getDataClient(Objects.requireNonNull(getActivity()));

        stopTestTask = new SyncAsyncTasksMobileRunningTest(getActivity());

        Wearable.getDataClient(Objects.requireNonNull(getContext())).addListener(this);
        dbHandler = new SQLiteDBHandler(getContext());

        //todo: remove static access (see mainactivity -> viewpatientactivity use)
        patientId = ViewPatientActivity.patientId;

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    drawAlertBox();
                }
                return false;
            }
        });

        mobileQuitTestTask = new SyncAsyncTasksMobileRunningTest(getActivity());

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

        ad = new InterstitialAd(getContext());
        ad.setAdUnitId(POST_TEST_AD_TEST);

        // creation of a thread to send sync signal in a loop
        syncThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!areDevicesSync) {
                    if (!isWaitFragmentRunning) {
                        break;
                    }
                    startTestTask = new SyncAsyncTasksMobileRunningTest(getActivity());
                    startTestTask.execute(0);
                    System.out.println("sync attempt loop");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        syncThread.start();

        isWaitFragmentRunning = true;

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("onresume waitfrag");
        // (re)sets the proper fragment destruction to false
        isProperDestruction = false;
        System.out.println("NOT properdestruction");
        isWaitFragmentRunning = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("onPause waitfrag");
        isWaitFragmentRunning = false;
        if (!isProperDestruction) {
            System.out.println("not properly destroyed");
            mobileQuitTestTask.execute(2);
            destroyFragment();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Wearable.getDataClient(Objects.requireNonNull(getContext())).removeListener(this);
        System.out.println("waitDestroyed");
        //Objects.requireNonNull(getActivity()).finish();
        //startActivity(getActivity().getIntent());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        Log.d(TAG, "OnDataChanged" + dataEventBuffer);

        for (DataEvent event : dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem changed
                DataItem item = event.getDataItem();
                if (Objects.requireNonNull(item.getUri().getPath()).compareTo(DEVICES_SYNC_PATH) == 0) {
                        Log.d(TAG, "OndataChanged : devices sync");
                        ok1.setText("OK");
                        ok2.setText("...");
                        areDevicesSync = true;
                }

                if (Objects.requireNonNull(item.getUri().getPath()).compareTo(HEART_RATE_COUNT_PATH) == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    if (dataMap.containsKey(HEART_MEASURE_1)) {
                        Log.d(TAG, "onDataChanged : measure 1");
                        meas1 = dataMap.getInt(HEART_MEASURE_1, 0);
                        m1.setText("Mesure 1: " + meas1 + " BPM");
                        ok2.setText("OK");
                        ok3.setText("...");
                    } else if (dataMap.containsKey(HEART_MEASURE_2)) {
                        Log.d(TAG, "onDataChanged : measure 2");
                        meas2 = dataMap.getInt(HEART_MEASURE_2, 0);
                        m2.setText("Mesure 2: " + meas2 + " BPM");
                        ok3.setText("OK");
                        ok4.setText("...");
                    } else if (dataMap.containsKey(HEART_MEASURE_3)) {
                        Log.d(TAG, "onDataChanged : measure 3");
                        meas3 = dataMap.getInt(HEART_MEASURE_3, 0);
                        m3.setText("Mesure 3: " + meas3 + " BPM");
                        ok4.setText("OK");
                        endTest.setText("Fin du test");
                        timerBeforeDestroy.start();
                    }
                }

                if (item.getUri().getPath().compareTo(WEAR_QUIT_APP_PATH) == 0) {
                    Log.d(TAG, "app closed on wear");
                    System.out.println("app closed on wear");
                    drawInfoBox();
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

    private void drawInfoBox() {
        Intent mobileAlertInfoIntent = new Intent(getContext(), MobileAlertInfoActivity.class);
        startActivity(mobileAlertInfoIntent);
        destroyFragment();
    }

    @Override
    public void onClick(View view) {
       drawAlertBox();
    }

    public void drawAlertBox() {
        AlertDialog.Builder alert = new AlertDialog.Builder(Objects.requireNonNull(this.getContext()));
        alert.setTitle("Annuler")
                .setMessage("Annuler le test ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.out.println("action cancelled");
                        // send cancel signal
                        stopTestTask.execute(1);
                        //Objects.requireNonNull(getActivity()).finish();
                        isProperDestruction = true;
                        System.out.println("properdestruction");
                        destroyFragment();
                    }
                })
                .setNegativeButton("Non", null)
                .setIcon(R.drawable.outline_warning_24)
                .show();
        // sqlDb.deletePatient(patientId);
    }

    private void destroyFragment() {
        Button startMeasure = Objects.requireNonNull(getActivity()).findViewById(R.id.startMeasureButton);
        startMeasure.setEnabled(true);
        ((ViewPatientActivity) getActivity()).refreshFields();
        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(WaitFragment.this).commit();



        // shows the ad
        if (isProperDestruction) {
            if (ad.isLoaded()) {
                ad.show();
            } else {
                Log.d(TAG, "ad not shown : was not loaded");
            }
        }
    }

    // loads the ad (might freeze the screen for a second)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ad.loadAd(new AdRequest.Builder().build());
    }
}
