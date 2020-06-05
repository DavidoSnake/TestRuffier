package com.example.ruffier;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.common.SQLiteDBHandler;
import com.example.testruffier.R;

import java.util.Objects;

public class AddPatientActivity extends AppCompatActivity implements View.OnClickListener {

    EditText fname;
    EditText lname;
    Button addB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_patient_activity);

        addB = findViewById(R.id.addbutton);
        fname = findViewById(R.id.firstname);

        lname = findViewById(R.id.lastname);
        lname.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE){
                    performAction();
                }
                return false;
            }
        });

        addB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performAction();
            }
        });

        // back arrow
        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    // manages both the button and keyboard actions
    public void performAction() {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(this);

        String ftext = fname.getText().toString();
        String ltext = lname.getText().toString();

        if (ftext.equals("") || ltext.equals("")) {
            Toast.makeText(this, "Veuillez remplir les champs", Toast.LENGTH_SHORT).show();
        } else {
            if (sqLiteDBHandler.isAlreadyRecorded(ftext, ltext)) {
                Toast.makeText(this, "Ce patient existe déjà", Toast.LENGTH_LONG).show();
                hideSoftKeyBoard();
            } else {
                sqLiteDBHandler.addPatient(fname.getText().toString(), lname.getText().toString());
                Toast.makeText(this, "Patient enregistré !", Toast.LENGTH_LONG).show();
                hideSoftKeyBoard();
                finish();
            }
        }
    }

    // hide the keyboard
    private void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        assert imm != null;
        if(imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View view) {

    }

    // back arrow
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
