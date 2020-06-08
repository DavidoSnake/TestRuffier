package com.example.ruffier;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.common.Constants;
import com.example.testruffier.R;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.Wearable;

import java.util.Objects;

import static com.example.common.Constants.MEASURE_DURATION;
import static com.example.common.Constants.measureNb;
import static com.example.common.Constants.measuresList1;
import static com.example.common.Constants.MEASURE_1_MSG;
import static com.example.common.Constants.MEASURE_2_MSG;
import static com.example.common.Constants.MEASURE_3_MSG;
import static com.example.common.Constants.measuresList2;
import static com.example.common.Constants.measuresList3;

public class HeartRateActivity extends WearableActivity {

    final String TAG = "HeartRateActivity";
    TextView title;
    TextView description;
    BroadcastReceiver broadcastReceiver;

    Intent rateServiceIntent;
    DataClient mDataClient;
    private static final int BOYDY_SENSOR_PERMISSION_CODE = 0;

    // 1 min counter before first measure
    CountDownTimer timer1 = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long l) {
            Log.d(TAG, "onTick1");
            String txt = "" + l / 1000;
            description.setText(txt);
        }

        @Override
        public void onFinish() {
            String txt = "Mesure en cours";
            title.setText(txt);
            description.setText("");
            startService(rateServiceIntent);
            //timer2.start();
        }
    };

    // timer on measure process
    CountDownTimer timer2 = new CountDownTimer(MEASURE_DURATION, 1000) {

        @Override
        public void onTick(long l) {
            Log.d(TAG, "onTick2");
        }

        @Override
        public void onFinish() {
            stopService(rateServiceIntent);
            isTimerRunning = false;
            description.setText("");
            title.setText("");
            if (measureNb == 1) {
                setButtonClickListener(2);
                done.setText("Continuer");
                done.setVisibility(View.VISIBLE);
                description.setText("A chaque fois que vous sentirez une vibration, faites une flexion");
            } else if (measureNb == 2) {
                title.setText("Allongez vous");
            } else if (measureNb == 3) {
                title.setText("Test terminé");
                description.setText("Résultats envoyés sur le téléphone");
                new CountDownTimer(10000, 1000) {
                    @Override
                    public void onTick(long l) {
                        Log.d(TAG, "tickBeforeFinish");
                    }

                    @Override
                    public void onFinish() {
                        HeartRateActivity.this.finish();
                    }
                }.start();
            }
        }
    };

    CountDownTimer timer3 = new CountDownTimer(45000, 1500) {
        @Override
        public void onTick(long l) {
            Log.d(TAG, "onTick3");
            // vibrate
            vibrator.vibrate(100);
            title.setText("" + l/1500);
        }

        @Override
        public void onFinish() {
            //timer2.start();
            timer1.start();
            startService(rateServiceIntent);
        }
    };


    static Button done;
    int mRate = 0;
    Vibrator vibrator;
    private boolean isTimerRunning = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heartrate);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);

        mDataClient = Wearable.getDataClient(this);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // filter received intent to send different messages

                switch (Objects.requireNonNull(intent.getAction())) {
                    case MEASURE_1_MSG:
                        Log.d(TAG, "received msg 1");
                        mRate = intent.getIntExtra(Constants.HEART_MEASURE_1, 0);
                        description.setText("" + mRate + " bpm");

                        //  measuresList1.add(mRate);
                        break;
                    case MEASURE_2_MSG:
                        Log.d(TAG, "received msg 2");
                        mRate = intent.getIntExtra(Constants.HEART_MEASURE_2, 0);
                        title.setText("" + mRate + " bpm");

                        // measuresList2.add(mRate);
                        break;
                    case MEASURE_3_MSG:
                        Log.d(TAG, "received msg 3");
                        mRate = intent.getIntExtra(Constants.HEART_MEASURE_3, 0);
                        description.setText("" + mRate + " bpm");

                        // measuresList3.add(mRate);
                        break;
                    default:
                        Log.d(TAG, "unknow message");
                }
                if (!isTimerRunning) {
                    timer2.start();
                    isTimerRunning = true;
                }
            }
        };

        checkBodySensorPermissionisRequired();
        // enable always-on
        setAmbientEnabled();

        done = findViewById(R.id.action);
        setButtonClickListener(1);

        rateServiceIntent = new Intent(getApplicationContext(), WearHeartRateService.class);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    public void setButtonClickListener(int step) {
        if (step == 1) {
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick");
                    done.setVisibility(View.GONE);
                    title.setText("Début de la mesure dans :");
                    timer1.start();
                }
            });
        } else if (step == 2) {
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick");
                    done.setVisibility(View.GONE);
                    description.setText("");
                    new CountDownTimer(5000, 1000) {
                        @Override
                        public void onTick(long l) {
                            Log.d(TAG, "beforeFlex");
                            title.setText("Début dans " + l/1000);
                        }

                        @Override
                        public void onFinish() {
                            timer3.start();
                        }
                    }.start();
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter1 = new IntentFilter(MEASURE_1_MSG);
        IntentFilter filter2 = new IntentFilter(MEASURE_2_MSG);
        IntentFilter filter3 = new IntentFilter(MEASURE_3_MSG);

        this.registerReceiver(broadcastReceiver, filter1);
        this.registerReceiver(broadcastReceiver, filter2);
        this.registerReceiver(broadcastReceiver, filter3);

        // reset the measures number
        measureNb = 1;
        measuresList1.clear();
        measuresList2.clear();
        measuresList3.clear();
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onpause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.stopService(rateServiceIntent);
        this.unregisterReceiver(broadcastReceiver);
    }

    public void checkBodySensorPermissionisRequired() {
        // Check if the BODY_SENSOR permission is already available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS)
                != PackageManager.PERMISSION_GRANTED) {
            // body sensor has not been granted.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BODY_SENSORS}, BOYDY_SENSOR_PERMISSION_CODE);
        } else {
            // Body Sensor permissions is already available.
            Log.i("RateFragment", "BODY_SENSOR permission has already been granted.");
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == BOYDY_SENSOR_PERMISSION_CODE) {
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for camera permission.
            Log.i("RateFragment", "Received response for BODY_SENSOR permission request.");

            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted, preview can be displayed
                Log.i("RateFragment", "BODY_SENSOR permission has now been granted. Showing preview.");

            } else {
                Log.i("RateFragment", "BODY_SENSOR permission was NOT granted.");
            }
            // END_INCLUDE(permission_result)
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
