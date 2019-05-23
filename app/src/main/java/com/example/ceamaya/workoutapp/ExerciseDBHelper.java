package com.example.ceamaya.workoutapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

import static com.example.ceamaya.workoutapp.ExerciseContract.ExerciseEntry;
import static com.example.ceamaya.workoutapp.ExerciseContract.SetEntry;


public class ExerciseDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "exerciseList.db";
    private static final int DATABASE_VERSION = 1;

    ExerciseDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", ExerciseEntry.EXERCISE_TABLE_NAME));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", SetEntry.SET_TABLE_NAME));
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_EXERCISELIST_TABLE = String.format(
                "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL);",
                ExerciseEntry.EXERCISE_TABLE_NAME, ExerciseEntry._ID, ExerciseEntry.COLUMN_NAME);
        final String SQL_CREATE_SETLIST_TABLE = String.format(
                "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER NOT NULL, %s " +
                        "INTEGER NOT NULL, %s INTEGER NOT NULL, %s TIMESTAMP DEFAULT " +
                        "CURRENT_TIMESTAMP);",
                SetEntry.SET_TABLE_NAME, SetEntry._ID, SetEntry.COLUMN_EXERCISEID,
                SetEntry.COLUMN_REPS, SetEntry.COLUMN_WEIGHT, SetEntry.COLUMN_TIMESTAMP);
        db.execSQL(SQL_CREATE_EXERCISELIST_TABLE);
        db.execSQL(SQL_CREATE_SETLIST_TABLE);
    }

    void insertExercise(String exercise) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ExerciseEntry.COLUMN_NAME, exercise);
        db.insert(ExerciseEntry.EXERCISE_TABLE_NAME, null, cv);
    }

    HashMap<String, Integer> getExercises() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(String.format("SELECT %s, %s FROM %s;",
                ExerciseEntry._ID, ExerciseEntry.COLUMN_NAME, ExerciseEntry.EXERCISE_TABLE_NAME),
                null);
        HashMap<String, Integer> exercises = new HashMap<>();
        while (c.moveToNext()) {
            String exercise = c.getString(c.getColumnIndex(ExerciseEntry.COLUMN_NAME));
            int id = c.getInt(c.getColumnIndex(ExerciseEntry._ID));
            exercises.put(exercise, id);
        }
        c.close();
        return exercises;
    }

    void deleteExercise(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(ExerciseEntry.EXERCISE_TABLE_NAME, ExerciseEntry._ID + "=?",
                new String[]{String.valueOf(id)});
    }

    void updateExercise(long id, String newExercise) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ExerciseEntry.COLUMN_NAME, newExercise);
        db.update(ExerciseEntry.EXERCISE_TABLE_NAME, cv, ExerciseEntry._ID + "=?",
                new String[]{String.valueOf(id)});
    }

    void insertSet(ExerciseSet exerciseSet) {
        int weight = exerciseSet.getWeight();
        int reps = exerciseSet.getReps();
        int id = exerciseSet.getId();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SetEntry.COLUMN_WEIGHT, weight);
        cv.put(SetEntry.COLUMN_REPS, reps);
        cv.put(SetEntry.COLUMN_EXERCISEID, id);
        db.insert(SetEntry.SET_TABLE_NAME, null, cv);
    }
}
