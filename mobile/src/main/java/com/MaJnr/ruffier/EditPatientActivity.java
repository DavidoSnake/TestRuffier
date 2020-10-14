package com.MaJnr.ruffier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.MaJnr.common.Patient;
import com.MaJnr.common.SQLiteDBHandler;
import com.MaJnr.testruffier.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class EditPatientActivity extends AppCompatActivity {

    private static final String TAG = "EditPatientActivity";

    // used to get patients
    int patientId;
    SQLiteDBHandler sqLiteDBHandler;
    Patient p;

    // fields of the selected patient
    EditText lname;
    EditText fname;
    EditText m1;
    EditText m2;
    EditText m3;

    // displayed text different with each translation
    private String lnameText;
    private String fnameText;
    private String m1Text;
    private String m2Text;
    private String m3Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient);

        // toolbar with back arrow
        Toolbar tb = findViewById(R.id.toolbar_edit);
        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.menu_edit);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        lname = findViewById(R.id.editLastName);
        fname = findViewById(R.id.editFirstName);
        m1 = findViewById(R.id.editMeasure1);
        m2 = findViewById(R.id.editMeasure2);
        m3 = findViewById(R.id.editMeasure3);


        patientId = getIntent().getIntExtra("ID_PATIENT", 0);
        sqLiteDBHandler = new SQLiteDBHandler(this);

        p = sqLiteDBHandler.getPatientById(patientId);
        lname.setText(p.getLastname());
        fname.setText(p.getFirstname());
        String s1 = "" + p.getMeasure_1();
        String s2 = "" + p.getMeasure_2();
        String s3 = "" + p.getMeasure_3();
        m1.setText(s1);
        m2.setText(s2);
        m3.setText(s3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_view, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // handle the items in the menu bar (confirm and back arrow)
        if (item.getItemId() == R.id.edit_confirm) {
            lnameText = lname.getText().toString();
            fnameText = fname.getText().toString();
            m1Text = m1.getText().toString();
            m2Text = m2.getText().toString();
            m3Text = m3.getText().toString();
            if (lnameText.equals("") || fnameText.equals("") || m1Text.equals("") || m2Text.equals("") || m3Text.equals("")) {
                Toast.makeText(this, R.string.toast_fill_fields, Toast.LENGTH_SHORT).show();
                hideSoftKeyBoard();
            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle(R.string.modify)
                        .setMessage(R.string.modify_alert)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d(TAG, "confirmation");
                                performConfirm();
                            }
                        })
                        .setNegativeButton(R.string.no, null)
                        .setIcon(R.drawable.outline_warning_24)
                        .show();
            }

        } else {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Check every fields to make sure they are not empty before editing patient
     */
    private void performConfirm() {
        sqLiteDBHandler.editPatient(patientId, lnameText, fnameText, m1Text, m2Text, m3Text);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE);
        String sDate = sdf.format(cal.getTime());
        Log.d(TAG, sDate);
        sqLiteDBHandler.addDate(patientId, sDate);
        Toast.makeText(this, R.string.toast_data_modified, Toast.LENGTH_LONG).show();
        hideSoftKeyBoard();
        finish();
    }

    /**
     * Hide the soft keyboard
     */
    private void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        assert imm != null;
        if (imm.isAcceptingText()) {
            // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
        }
    }
}