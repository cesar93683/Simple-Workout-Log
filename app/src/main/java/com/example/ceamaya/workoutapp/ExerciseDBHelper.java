package com.example.ceamaya.workoutapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.ceamaya.workoutapp.ExerciseContract.ExerciseEntry.*;


public class ExerciseDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "exerciseList.db";
    private static final int DATABASE_VERSION = 1;

    public ExerciseDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_EXERCISELIST_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL" +
                ");";
        db.execSQL(SQL_CREATE_EXERCISELIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ExerciseContract.ExerciseEntry.TABLE_NAME);
        onCreate(db);
    }

    public void insert(String exercise) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, exercise);
        db.insert(TABLE_NAME, null, cv);
    }

    public HashMap<String, Long> getExercises() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + _ID + ", " + COLUMN_NAME +
                " FROM " + TABLE_NAME + ";", null);
        HashMap<String, Long> exercises = new HashMap<>();
        while (c.moveToNext()) {
            String exercise = c.getString(c.getColumnIndex(COLUMN_NAME));
            long id = c.getLong(c.getColumnIndex(_ID));
            exercises.put(exercise, id);
        }
        return exercises;
    }

    public int delete(long id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NAME, _ID + "=?",
                new String[]{String.valueOf(id)});
    }
}
