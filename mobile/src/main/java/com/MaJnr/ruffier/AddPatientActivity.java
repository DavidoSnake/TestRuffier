package com.MaJnr.ruffier;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
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

import com.MaJnr.common.SQLiteDBHandler;
import com.MaJnr.testruffier.R;

import java.util.Objects;

public class AddPatientActivity extends AppCompatActivity {

    // fields and button
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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.menu_add_profile);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    /**
     * Manages both the button and keyboard validation action
     */
    public void performAction() {
        SQLiteDBHandler sqLiteDBHandler = new SQLiteDBHandler(this);

        String ftext = fname.getText().toString();
        String ltext = lname.getText().toString();

        if (ftext.equals("") || ltext.equals("")) {
            Toast.makeText(this, R.string.toast_fill_fields, Toast.LENGTH_SHORT).show();
        } else {
            if (sqLiteDBHandler.isAlreadyRecorded(ftext, ltext)) {
                Toast.makeText(this, R.string.toast_profile_already_exixts, Toast.LENGTH_LONG).show();
                hideSoftKeyBoard();
            } else {
                sqLiteDBHandler.addPatient(fname.getText().toString(), lname.getText().toString());
                Toast.makeText(this, R.string.toast_profile_recorded, Toast.LENGTH_LONG).show();
                hideSoftKeyBoard();
                finish();
            }
        }
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // handle the menu's back arrow
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.no_item_view, menu);
        return true;
    }
}
