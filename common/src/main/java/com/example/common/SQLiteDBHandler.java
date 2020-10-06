package com.example.common;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.xml.sax.Parser;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDBHandler extends SQLiteOpenHelper {

    Context mContext;
    final String TAG = "SQLiteDBHandler";

    private static final int DATABASE_VERSION = 26;
    public static final String DATABASE_NAME = "patients_database";

    // patients
    public static final String PATIENT_TABLE_NAME = "patient";
    public static final String PATIENT_COLUMN_ID = "_id";
    public static final String PATIENT_COLUMN_LASTNAME = "lastname";
    public static final String PATIENT_COLUMN_FIRSTNAME = "firstname";
    public static final String PATIENT_MEASURE_1 = "measure_1";
    public static final String PATIENT_MEASURE_2 = "measure_2";
    public static final String PATIENT_MEASURE_3 = "measure_3";
    public static final String PATIENT_COLUMN_DATE = "test_date";

    public SQLiteDBHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + PATIENT_TABLE_NAME + " (" +
                PATIENT_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PATIENT_COLUMN_LASTNAME + " TEXT, " +
                PATIENT_COLUMN_FIRSTNAME + " TEXT, " +
                PATIENT_MEASURE_1 + " INTEGER, " +
                PATIENT_MEASURE_2 + " INTEGER, " +
                PATIENT_MEASURE_3 + " INTEGER, " +
                PATIENT_COLUMN_DATE + " TEXT);";

        Log.d(TAG, "onCreate");
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PATIENT_TABLE_NAME);
        // sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PERSON_TABLE_NAME);
        onCreate(sqLiteDatabase);
        Log.d(TAG, "onUpgrade");
    }

    public Patient getPatient(String firstname, String lastname) {
        // create a list of patients with the same name
        List<Patient> patientList = new ArrayList<>();
        String query = "SELECT * FROM " + PATIENT_TABLE_NAME + " WHERE " + PATIENT_COLUMN_FIRSTNAME + " LIKE \"" + firstname + "%\" "
                + "AND " + PATIENT_COLUMN_LASTNAME + " LIKE \"" + lastname + "%\";";
        Log.d(TAG, "getPatient");

        SQLiteDatabase sqlDB = this.getReadableDatabase();
        Cursor cursor = sqlDB.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                try {
                    Patient patient = new Patient();
                    int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(PATIENT_COLUMN_ID)));
                    String fname = cursor.getString(cursor.getColumnIndex(PATIENT_COLUMN_FIRSTNAME));
                    String lname = cursor.getString(cursor.getColumnIndex(PATIENT_COLUMN_LASTNAME));
                    String m1 = cursor.getString(cursor.getColumnIndex(PATIENT_MEASURE_1));
                    String m2 = cursor.getString(cursor.getColumnIndex(PATIENT_MEASURE_2));
                    String m3 = cursor.getString(cursor.getColumnIndex(PATIENT_MEASURE_3));
                    String sDateTest = cursor.getString(cursor.getColumnIndex(PATIENT_COLUMN_DATE));

                    patient.setId(id);
                    patient.setFirstname(fname);
                    patient.setLastname(lname);
                    patient.setDateTest(sDateTest);

                    // string to date conversion
                /*    Date dateTest = null;
                    Date finalDate = null;
                    try {
                        dateTest = inputFormat.parse(sDateTest);
                        assert dateTest != null;
                        inputFormat.applyPattern("dd/MM/yyyy hh:mm");
                        String newDateString = inputFormat.format(dateTest);
                        finalDate = inputFormat.parse(newDateString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    patient.setDateTest(finalDate);*/

                    if (m1 != null) {
                        patient.setMeasure_1(Integer.parseInt(m1));
                    }
                    if (m2 != null) {
                        patient.setMeasure_2(Integer.parseInt(m2));
                    }
                    if (m3 != null) {
                        patient.setMeasure_3(Integer.parseInt(m3));
                    }

                    patientList.add(patient);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        sqlDB.close();
        cursor.close();

        Patient res;
        if (patientList.size() != 0) {
            res = patientList.get(0);
        } else {
            res = new Patient();
        }
        return res;
    }

    public List<Patient> getPatientsByFirstname(String firstname, boolean isStartOfText) {
        // create a list of patients with the same name
        List<Patient> patientList = new ArrayList<>();
        String query;

        if (isStartOfText) {
            query = "SELECT * FROM " + PATIENT_TABLE_NAME + " WHERE " + PATIENT_COLUMN_FIRSTNAME + " LIKE \"" + firstname + "%\";";
        } else {
            query = "SELECT * FROM " + PATIENT_TABLE_NAME + " WHERE " + PATIENT_COLUMN_FIRSTNAME + " LIKE \"%" + firstname + "%\" ORDER BY "
                    + PATIENT_COLUMN_FIRSTNAME + ";";
        }

        SQLiteDatabase sqlDB = this.getReadableDatabase();
        Cursor cursor = sqlDB.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                try {
                    Patient patient = new Patient();
                    int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(PATIENT_COLUMN_ID)));
                    String fname = cursor.getString(cursor.getColumnIndex(PATIENT_COLUMN_FIRSTNAME));
                    String lname = cursor.getString(cursor.getColumnIndex(PATIENT_COLUMN_LASTNAME));
                    String m1 = cursor.getString(cursor.getColumnIndex(PATIENT_MEASURE_1));
                    String m2 = cursor.getString(cursor.getColumnIndex(PATIENT_MEASURE_2));
                    String m3 = cursor.getString(cursor.getColumnIndex(PATIENT_MEASURE_3));
                    String sDateTest = cursor.getString(cursor.getColumnIndex(PATIENT_COLUMN_DATE));

                    patient.setId(id);
                    patient.setFirstname(fname);
                    patient.setLastname(lname);
                    patient.setDateTest(sDateTest);

                    if (m1 != null) {
                        patient.setMeasure_1(Integer.parseInt(m1));
                    }
                    if (m2 != null) {
                        patient.setMeasure_2(Integer.parseInt(m2));
                    }
                    if (m3 != null) {
                        patient.setMeasure_3(Integer.parseInt(m3));
                    }

                    patientList.add(patient);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        sqlDB.close();
        cursor.close();

        return patientList;
    }

    public List<Patient> getPatientsByLastname(String lastname, boolean isStartOfText) {
        // create a list of patients with the same name
        List<Patient> patientList = new ArrayList<>();
        String query;

        if (isStartOfText) {
            query = "SELECT * FROM " + PATIENT_TABLE_NAME + " WHERE " + PATIENT_COLUMN_LASTNAME + " LIKE \"" + lastname + "%\";";
        } else {
            query = "SELECT * FROM " + PATIENT_TABLE_NAME + " WHERE " + PATIENT_COLUMN_LASTNAME + " LIKE \"%" + lastname + "%\" ORDER BY "
                    + PATIENT_COLUMN_LASTNAME + ";";

        }

        SQLiteDatabase sqlDB = this.getReadableDatabase();
        Cursor cursor = sqlDB.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                try {
                    Patient patient = new Patient();
                    int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(PATIENT_COLUMN_ID)));
                    String fname = cursor.getString(cursor.getColumnIndex(PATIENT_COLUMN_FIRSTNAME));
                    String lname = cursor.getString(cursor.getColumnIndex(PATIENT_COLUMN_LASTNAME));
                    String m1 = cursor.getString(cursor.getColumnIndex(PATIENT_MEASURE_1));
                    String m2 = cursor.getString(cursor.getColumnIndex(PATIENT_MEASURE_2));
                    String m3 = cursor.getString(cursor.getColumnIndex(PATIENT_MEASURE_3));
                    String sDateTest = cursor.getString(cursor.getColumnIndex(PATIENT_COLUMN_DATE));

                    patient.setId(id);
                    patient.setFirstname(fname);
                    patient.setLastname(lname);
                    patient.setDateTest(sDateTest);

                    patientList.add(patient);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        sqlDB.close();
        cursor.close();

        return patientList;
    }

    public List<Patient> getAllPatients() {
        // create a list of patients with the same name
        List<Patient> patientList = new ArrayList<>();
        String query = "SELECT * FROM " + PATIENT_TABLE_NAME + ";";

        SQLiteDatabase sqlDB = this.getReadableDatabase();
        Cursor cursor = sqlDB.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                try {
                    Patient patient = new Patient();
                    int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(PATIENT_COLUMN_ID)));
                    String fname = cursor.getString(cursor.getColumnIndex(PATIENT_COLUMN_FIRSTNAME));
                    String lname = cursor.getString(cursor.getColumnIndex(PATIENT_COLUMN_LASTNAME));
                    String m1 = cursor.getString(cursor.getColumnIndex(PATIENT_MEASURE_1));
                    String m2 = cursor.getString(cursor.getColumnIndex(PATIENT_MEASURE_2));
                    String m3 = cursor.getString(cursor.getColumnIndex(PATIENT_MEASURE_3));
                    String sDateTest = cursor.getString(cursor.getColumnIndex(PATIENT_COLUMN_DATE));

                    patient.setId(id);
                    patient.setFirstname(fname);
                    patient.setLastname(lname);
                    patient.setDateTest(sDateTest);

                    patientList.add(patient);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        sqlDB.close();
        cursor.close();

        return patientList;
    }

    public Patient getPatientById(int id) {
        // create a list for 1 patient only (may be simplified ?)

        List<Patient> patientList = new ArrayList<>();
        String query = "SELECT * FROM " + PATIENT_TABLE_NAME + " WHERE " + PATIENT_COLUMN_ID + " = " + id + ";";

        SQLiteDatabase sqlDB = this.getReadableDatabase();
        Cursor cursor = sqlDB.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                try {
                    Patient patient = new Patient();
                    int idPat = Integer.parseInt(cursor.getString(cursor.getColumnIndex(PATIENT_COLUMN_ID)));
                    String fname = cursor.getString(cursor.getColumnIndex(PATIENT_COLUMN_FIRSTNAME));
                    String lname = cursor.getString(cursor.getColumnIndex(PATIENT_COLUMN_LASTNAME));
                    String m1 = cursor.getString(cursor.getColumnIndex(PATIENT_MEASURE_1));
                    String m2 = cursor.getString(cursor.getColumnIndex(PATIENT_MEASURE_2));
                    String m3 = cursor.getString(cursor.getColumnIndex(PATIENT_MEASURE_3));
                    String sDateTest = cursor.getString(cursor.getColumnIndex(PATIENT_COLUMN_DATE));

                    patient.setId(id);
                    patient.setFirstname(fname);
                    patient.setLastname(lname);
                    patient.setDateTest(sDateTest);

                    if (m1 != null) {
                        patient.setMeasure_1(Integer.parseInt(m1));
                    }
                    if (m2 != null) {
                        patient.setMeasure_2(Integer.parseInt(m2));
                    }
                    if (m3 != null) {
                        patient.setMeasure_3(Integer.parseInt(m3));
                    }

                    patientList.add(patient);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        sqlDB.close();
        cursor.close();

        Patient res;
        if (patientList.size() != 0) {
            res = patientList.get(0);
        } else {
            res = new Patient();
        }
        return res;
    }

    //add patients here
    public void addPatient(String fname, String lname) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        String insert_beg = "INSERT INTO " + PATIENT_TABLE_NAME + "("
                + PATIENT_COLUMN_ID + ", "
                + PATIENT_COLUMN_FIRSTNAME + ", "
                + PATIENT_COLUMN_LASTNAME + ", "
                + PATIENT_MEASURE_1 + ", "
                + PATIENT_MEASURE_2 + ", "
                + PATIENT_MEASURE_3 + ", "
                + PATIENT_COLUMN_DATE + ") VALUES ";

        String insert_end = "(null, \"" + fname + "\", \"" + lname + "\", null, null, null, \"Aucun test réalisé\");";
        Log.d(TAG, "insert : " + insert_beg + insert_end);
        try {
            sqLiteDatabase.execSQL(insert_beg + insert_end);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // return true if the patient already exists

    public boolean isAlreadyRecorded(Patient patient) {
        Patient res = getPatient(patient.getFirstname(), patient.getLastname());
        return res.getId() == 0;
    }
    // return true if the patient already exists

    public boolean isAlreadyRecorded(String firstname, String lastname) {
        Patient res = getPatient(firstname, lastname);
        return res.getId() != 0;
    }

    public void addMeasures(int id, int m1, int m2, int m3) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "UPDATE " + PATIENT_TABLE_NAME + " SET " + PATIENT_MEASURE_1 + " = " + m1
                + ", " + PATIENT_MEASURE_2 + " = " + m2
                + ", " + PATIENT_MEASURE_3 + " = " + m3 + " WHERE " + PATIENT_COLUMN_ID + " = " + id + ";";
        sqLiteDatabase.execSQL(sql);
    }

    public void addDate(int id, String sDate) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "UPDATE " + PATIENT_TABLE_NAME + " SET " + PATIENT_COLUMN_DATE + "  = \"" + sDate + "\" WHERE " + PATIENT_COLUMN_ID + " = " + id + ";";
        Log.d(TAG, sql);
        sqLiteDatabase.execSQL(sql);
    }

    public void deletePatient(int id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "DELETE FROM " + PATIENT_TABLE_NAME + " WHERE " + PATIENT_COLUMN_ID + " = " + id + ";";
        Log.d(TAG, sql);
        sqLiteDatabase.execSQL(sql);
    }
    // edit all fields

    public void editPatient(int id, String lastName, String firstName, String m1, String m2, String m3) {
        int nb1 = Integer.parseInt(m1);
        int nb2 = Integer.parseInt(m2);
        int nb3 = Integer.parseInt(m3);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "UPDATE " + PATIENT_TABLE_NAME + " SET " + PATIENT_COLUMN_LASTNAME + " = \"" + lastName + "\", " + PATIENT_COLUMN_FIRSTNAME + " = \"" + firstName
                + "\", " + PATIENT_MEASURE_1 + " = " + nb1 + ", " + PATIENT_MEASURE_2 + " = " + nb2 + ", " + PATIENT_MEASURE_3 + " = " + nb3 + " WHERE " + PATIENT_COLUMN_ID
                + " = " + id + ";";
        Log.d(TAG, sql);
        sqLiteDatabase.execSQL(sql);
    }
}
