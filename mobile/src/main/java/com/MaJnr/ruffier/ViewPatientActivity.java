package com.MaJnr.ruffier;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.MaJnr.common.Patient;
import com.MaJnr.common.SQLiteDBHandler;
import com.MaJnr.testruffier.R;


public class ViewPatientActivity extends AppCompatActivity {

    private String TAG = "ViewPatientActivity";

    // layout entities
    TextView pat_name;
    TextView dateTest;
    TextView show_m1;
    TextView show_m2;
    TextView show_m3;
    TextView index_ir;
    TextView index_id;
    Button startMeasure;

    int patientId;

    // database access
    SQLiteDBHandler sqlDb;
    Context mContext;
    Patient p;


    // test proceeding fragment
    WaitFragment waitFragment;
    private boolean doNeedRefresh = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_patient_activity);
        mContext = this;

        // get patient id
        patientId = getIntent().getIntExtra("PATIENT_ID", 0);

        refreshFields();

        // back button
        Toolbar tb = findViewById(R.id.toolbar2);
        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.menu_view_profie);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        startMeasure = findViewById(R.id.startMeasureButton);
        startMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMeasure.setEnabled(false);
                waitFragment = WaitFragment.newInstance(patientId);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_layout, waitFragment);
                fragmentTransaction.commit();
            }
        });

        sqlDb = new SQLiteDBHandler(this);
    }

    /**
     * Get data from the selected profile and fill the view with all recorded informations.
     * Also calculate the indexes and gives an appreciation message (with a color)
     */
    public void refreshFields() {
        Log.d(TAG, "refreshing fields");

        doNeedRefresh = false;

        SQLiteDBHandler db = new SQLiteDBHandler(this);
        p = db.getPatientById(patientId);
        pat_name = findViewById(R.id.patient_fullname);
        String names = p.getFirstname() + "    " + p.getLastname();
        pat_name.setText(names);

        dateTest = findViewById(R.id.dateTest);
        dateTest.setText(p.getDateTest());

        int m1 = p.getMeasure_1();
        int m2 = p.getMeasure_2();
        int m3 = p.getMeasure_3();

        show_m1 = findViewById(R.id.show_m1);
        String sm1 = "" + m1;
        show_m1.setText(sm1);

        show_m2 = findViewById(R.id.show_m2);
        String sm2 = "" + m2;
        show_m2.setText(sm2);

        show_m3 = findViewById(R.id.show_m3);
        String sm3 = "" + m3;
        show_m3.setText(sm3);

        // calcul of indexes
        index_ir = findViewById(R.id.index_ir);
        index_id = findViewById(R.id.index_id);

        TextView color_ir = findViewById(R.id.color_ir);
        TextView color_id = findViewById(R.id.color_id);

        if (m1 != 0 && m2 != 0 && m3 != 0) {
            double ir = (m1 + m2 + m3 - 200) / 10.;
            double roundIr = Math.floor(ir * 10) / 10;
            double id = ((m2 - 70) + 2 * (m3 - m1)) / 10.;
            double roundId = Math.floor(id * 10) / 10;
            String sIr = "" + roundIr;
            index_ir.setText(sIr);
            String sId = "" + roundId;
            index_id.setText(sId);

            // colors
            if (ir <= 0) {
                color_ir.setBackgroundColor(Color.argb(100, 0, 255, 0));
                color_ir.setText(R.string.result_really_good);
            } else if (ir <= 5) {
                color_ir.setBackgroundColor(Color.argb(100, 128, 255, 0));
                color_ir.setText(R.string.result_good);
            } else if (ir <= 10) {
                color_ir.setBackgroundColor(Color.argb(100, 255, 255, 0));
                color_ir.setText(R.string.result_medium);
            } else if (ir <= 15) {
                color_ir.setBackgroundColor(Color.argb(100, 255, 128, 0));
                color_ir.setText(R.string.result_insufficient);
            } else if (ir > 15) {
                color_ir.setBackgroundColor(Color.argb(100, 255, 0, 0));
                color_ir.setText(R.string.result_bad);
            }

            if (id <= 0) {
                color_id.setBackgroundColor(Color.argb(100, 0, 255, 0));
                color_id.setText(R.string.result_exellent);
            } else if (id <= 2) {
                color_id.setBackgroundColor(Color.argb(100, 64, 255, 0));
                color_id.setText(R.string.result_really_good);
            } else if (id <= 4) {
                color_id.setBackgroundColor(Color.argb(100, 128, 255, 0));
                color_id.setText(R.string.result_good);
            } else if (id <= 6) {
                color_id.setBackgroundColor(Color.argb(100, 255, 255, 0));
                color_id.setText(R.string.result_medium);
            } else if (id <= 8) {
                color_id.setBackgroundColor(Color.argb(100, 255, 194, 0));
                color_id.setText(R.string.result_weak);
            } else if (id <= 10) {
                color_id.setBackgroundColor(Color.argb(100, 255, 64, 0));
                color_id.setText(R.string.result_really_weak);
            } else if (id > 10) {
                color_id.setBackgroundColor(Color.argb(100, 204, 0, 0));
                color_id.setText(R.string.result_bad);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // handle the menu bar items (delete and edit)
        if (item.getItemId() == R.id.delete) {
            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            alert.setTitle(R.string.suppression)
                    .setMessage(R.string.suppression_alert)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            sqlDb.deletePatient(patientId);
                            Log.d(TAG, "patient deleted");
                            Toast.makeText(ViewPatientActivity.this, R.string.toast_profile_deleted, Toast.LENGTH_LONG).show();
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .setIcon(R.drawable.outline_warning_24)
                    .show();
            // sqlDb.deletePatient(patientId);
        } else if (item.getItemId() == R.id.edit) {
            Log.d(TAG, "edit");
            Intent editIntent = new Intent(this, EditPatientActivity.class);
            editIntent.putExtra("ID_PATIENT", patientId);
            startActivity(editIntent);
        } else {
            Log.d(TAG, "back arrow");
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.patient_view, menu);
        return true;
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        if (doNeedRefresh) {
            Log.d(TAG, "need refresh");
            refreshFields();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        doNeedRefresh = true;
        super.onPause();
    }
}
