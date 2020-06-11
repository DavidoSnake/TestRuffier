package com.example.ruffier;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.common.Patient;
import com.example.common.SQLiteDBHandler;
import com.example.testruffier.R;

public class ViewPatientActivity extends AppCompatActivity {

    TextView pat_fname;
    TextView pat_lname;
    TextView dateTest;
    TextView show_m1;
    TextView show_m2;
    TextView show_m3;
    TextView index_ir;
    TextView index_id;
    static int patientId;
    SQLiteDBHandler sqlDb;

    // start the measure on wear
    Button startMeasure;

    // proceeding fragment
    WaitFragment waitFragment;
    private TextView color_ir;
    private TextView color_id;
    private String TAG = "ViewPatientActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_patient_activity);

        // get patient id
        patientId = getIntent().getIntExtra("PATIENT_ID", 0);
        SQLiteDBHandler db = new SQLiteDBHandler(this);
        Patient p = db.getPatientById(patientId);

        // fill all fields
        pat_fname = findViewById(R.id.patient_fname);
        pat_fname.setText(p.getFirstname());

        pat_lname = findViewById(R.id.patient_lname);
        pat_lname.setText(p.getLastname());

        dateTest = findViewById(R.id.dateTest);
        dateTest.setText(p.getDateTest());

        int m1 = p.getMeasure_1();
        int m2 = p.getMeasure_2();
        int m3 = p.getMeasure_3();

        show_m1 = findViewById(R.id.show_m1);
        show_m1.setText("" + m1);

        show_m2 = findViewById(R.id.show_m2);
        show_m2.setText("" + m2);

        show_m3 = findViewById(R.id.show_m3);
        show_m3.setText("" + m3);

        // calcul of indexes
        index_ir = findViewById(R.id.index_ir);
        index_id = findViewById(R.id.index_id);

        color_ir = findViewById(R.id.color_ir);
        color_id = findViewById(R.id.color_id);

        if (m1 != 0 && m2 != 0 && m3 != 0) {
            double ir = (m1 + m2 + m3 - 200) / 10.;
            double roundIr = Math.floor(ir * 10) / 10;
            double id = ((m2 - 70) + 2 * (m3 - m1)) / 10.;
            double roundId = Math.floor(id * 10) / 10;
            index_ir.setText("" + roundIr);
            index_id.setText("" + roundId);

            // colors
            if (ir <= 0) {
                color_ir.setBackgroundColor(Color.argb(100, 0, 255, 0));
                color_ir.setText("très bon");
            } else if (ir <= 5) {
                color_ir.setBackgroundColor(Color.argb(100, 128, 255, 0));
                color_ir.setText("bon");
            } else if (ir <= 10) {
                color_ir.setBackgroundColor(Color.argb(100, 255, 255, 0));
                color_ir.setText("moyen");
            } else if (ir <= 15) {
                color_ir.setBackgroundColor(Color.argb(100, 255, 128, 0));
                color_ir.setText("insuffisant");
            } else if (ir > 15) {
                color_ir.setBackgroundColor(Color.argb(100, 255, 0, 0));
                color_ir.setText("mauvais");
            }

            if (id <= 0) {
                color_id.setBackgroundColor(Color.argb(100, 0, 255, 0));
                color_id.setText("exellent");
            } else if (id <= 2) {
                color_id.setBackgroundColor(Color.argb(100, 64, 255, 0));
                color_id.setText("très bon");
            } else if (id <= 4) {
                color_id.setBackgroundColor(Color.argb(100, 128, 255, 0));
                color_id.setText("bon");
            } else if (id <= 6) {
                color_id.setBackgroundColor(Color.argb(100, 255, 255, 0));
                color_id.setText("moyen");
            } else if (id <= 8) {
                color_id.setBackgroundColor(Color.argb(100, 255, 194, 0));
                color_id.setText("faible");
            } else if (id <= 10) {
                color_id.setBackgroundColor(Color.argb(100, 255, 64, 0));
                color_id.setText("très faible");
            } else if (id > 10) {
                color_id.setBackgroundColor(Color.argb(100, 204, 0, 0));
                color_id.setText("mauvais");
            }
        }

        Toolbar tb = findViewById(R.id.toolbar2);
        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        startMeasure = findViewById(R.id.startMeasureButton);
        startMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMeasure.setEnabled(false);
                waitFragment = new WaitFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.main_layout, waitFragment);
                fragmentTransaction.commit();
            }
        });

        sqlDb = new SQLiteDBHandler(this);
    }

    // delete icon
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            sqlDb.deletePatient(patientId);
            Log.d(TAG, "patient deleted");
        }
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.patient_view, menu);
        return true;
    }

    @Override
    protected void onResume() {
        System.out.println("onresumeviewpat");
        super.onResume();
    }
}
