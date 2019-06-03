package com.example.ceamaya.workoutapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.ceamaya.workoutapp.database.DbSchema.ExerciseSetTable;
import static com.example.ceamaya.workoutapp.database.DbSchema.ExerciseTable;
import static com.example.ceamaya.workoutapp.database.DbSchema.RoutineTable;
import static com.example.ceamaya.workoutapp.database.DbSchema.RoutineExerciseTable;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "exerciseList.db";
    private static final int VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", ExerciseTable.NAME));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", ExerciseSetTable.NAME));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", RoutineTable.NAME));
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
        final String SQL_CREATE_ROUTINE_LIST_TABLE = String.format(
                "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL);",
                RoutineTable.NAME, RoutineTable._ID, RoutineTable.Cols.NAME);
        final String SQL_CREATE_ROUTINE_EXERCISE_TABLE = String.format(
                "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER NOT NULL," +
                        " %s STRING NOT NULL);",
                RoutineExerciseTable.NAME, RoutineExerciseTable._ID,
                RoutineExerciseTable.Cols.ROUTINE_ID, RoutineExerciseTable.Cols.EXERCISE_ID);
        db.execSQL(SQL_CREATE_EXERCISE_LIST_TABLE);
        db.execSQL(SQL_CREATE_SET_LIST_TABLE);
        db.execSQL(SQL_CREATE_ROUTINE_LIST_TABLE);
        db.execSQL(SQL_CREATE_ROUTINE_EXERCISE_TABLE);
    }
}