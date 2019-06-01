package com.example.ceamaya.workoutapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.ceamaya.workoutapp.database.ExerciseDbSchema.ExerciseSetTable;
import static com.example.ceamaya.workoutapp.database.ExerciseDbSchema.ExerciseTable;


public class ExerciseBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "exerciseList.db";
    private static final int VERSION = 1;

    public ExerciseBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", ExerciseTable.NAME));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", ExerciseSetTable.NAME));
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_EXERCISE_LIST_TABLE = String.format(
                "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL);",
                ExerciseTable.NAME, ExerciseTable._ID, ExerciseTable.Cols.NAME);
        final String SQL_CREATE_SET_LIST_TABLE = String.format(
                "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER NOT NULL, %s " +
                        "INTEGER NOT NULL, %s INTEGER NOT NULL, %s INTEGER NOT NULL, %s LONG NOT " +
                        "NULL);",
                ExerciseSetTable.NAME, ExerciseSetTable._ID, ExerciseSetTable.Cols.EXERCISE_ID,
                ExerciseSetTable.Cols.SET_NUMBER, ExerciseSetTable.Cols.REPS, ExerciseSetTable
                        .Cols.WEIGHT, ExerciseSetTable.Cols.TIME_STAMP);
        db.execSQL(SQL_CREATE_EXERCISE_LIST_TABLE);
        db.execSQL(SQL_CREATE_SET_LIST_TABLE);
    }
}
