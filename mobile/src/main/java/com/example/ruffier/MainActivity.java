package com.example.ruffier;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.common.Patient;
import com.example.common.SQLiteDBHandler;
import com.example.testruffier.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private final String TAG = "MainActivity";

    Button searchB;
    EditText fname;
    EditText lname;
    FrameLayout noEntry;
    Button emptyBtn;

    // results list
    List<Patient> array = new ArrayList<>();
    ArrayAdapter<Patient> adapter;
    ListView listView;

    // used to get patients
    SQLiteDBHandler dbHandler;
    Patient p;

    // ad view
    AdView ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize banner ad and load it
        // ---------------------------------------
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        ad = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        ad.loadAd(adRequest);
        // ---------------------------------------

        Toolbar tb = findViewById(R.id.main_toolbar);
        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Rechercher un profil");
        }

        searchB = findViewById(R.id.searchB);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        noEntry = findViewById(R.id.noEntry);
        emptyBtn = new Button(this);

        emptyBtn.setText("Ajouter un nouveau patient");
        emptyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddPatientActivity.class);
                startActivity(intent);
            }
        });

        lname.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    performAction();
                }
                return false;
            }
        });

        searchB.setOnClickListener(this);

        //Database test (sucess)
        dbHandler = new SQLiteDBHandler(this);

        adapter = new ArrayAdapter<>(this, R.layout.list_cell, array);

        listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        // first use screen
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
        if (!previouslyStarted) {
            Log.d(TAG, "first app opening");
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
            edit.apply();
            showTuto();
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    protected void onResume() {
        showAll();
        super.onResume();
    }

    // shows quick start instructions and informations
    public void showTuto() {
        Intent intent = new Intent(this, FirstUseActivity.class);
        startActivity(intent);
    }

    private void showAll() {
        array.clear();
        List<Patient> listPatients = dbHandler.getAllPatients();
        noEntry.removeAllViewsInLayout();
        if (listPatients.size() != 0) {
            Log.d(TAG, "list not empty");
            array.addAll(listPatients);
        } else {
            Log.d(TAG, "list empty");
            noEntry.addView(emptyBtn);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add) {
            Intent intent = new Intent(this, AddPatientActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.infos) {
            Intent intent = new Intent(this, MoreInfoActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        performAction();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, ViewPatientActivity.class);
        p = (Patient) adapterView.getItemAtPosition(i);
        intent.putExtra("PATIENT_ID", p.getId());

        startActivity(intent);
        System.out.println(p.getFirstname());
    }

    // hide the keyboard
    private void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        assert imm != null;
        if (imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
        }
    }

    public void performAction() {
        array.clear();
        boolean resultFound = false;

        // if both fields are filled
        if (!fname.getText().toString().equals("") && !lname.getText().toString().equals("")) {
            Patient p = dbHandler.getPatient(fname.getText().toString(), lname.getText().toString());
            if (p.getId() != 0) {
                resultFound = true;
                array.add(p);
                adapter.notifyDataSetChanged();
            }
        } else if (lname.getText().toString().equals("") && !fname.getText().toString().equals("")) {

            // if firstname only is filled
            List<Patient> listPatients = dbHandler.getPatientsByFirstname(fname.getText().toString());
            if (listPatients.size() != 0) {
                resultFound = true;
                array.addAll(listPatients);
                adapter.notifyDataSetChanged();
            }
        } else if (fname.getText().toString().equals("") && !lname.getText().toString().equals("")) {

            // if lastname only is filled
            List<Patient> listPatients = dbHandler.getPatientsByLastname(lname.getText().toString());
            if (listPatients.size() != 0) {
                resultFound = true;
                array.addAll(listPatients);
                adapter.notifyDataSetChanged();
            }
        } else {
            resultFound = true;
            showAll();
            //Toast.makeText(this, "Veuillez remplir les champs", Toast.LENGTH_SHORT).show();
        }
        if (!resultFound) {
            Toast.makeText(this, "Aucun résultat trouvé", Toast.LENGTH_SHORT).show();
        }
        hideSoftKeyBoard();
    }
}
