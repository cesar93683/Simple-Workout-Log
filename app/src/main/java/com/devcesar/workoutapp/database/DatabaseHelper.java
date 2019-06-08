package com.devcesar.workoutapp.database;

import static com.devcesar.workoutapp.database.DbSchema.ExerciseTable;
import static com.devcesar.workoutapp.database.DbSchema.RoutineTable;
import static com.devcesar.workoutapp.database.DbSchema.WorkoutTable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.devcesar.workoutapp.database.DbSchema.CategoryTable;


public class DatabaseHelper extends SQLiteOpenHelper {

  private static final String DATABASE_NAME = "exerciseList.db";
  private static final int VERSION = 1;

  public DatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, VERSION);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL(String.format("DROP TABLE IF EXISTS %s", ExerciseTable.NAME));
    db.execSQL(String.format("DROP TABLE IF EXISTS %s", WorkoutTable.NAME));
    db.execSQL(String.format("DROP TABLE IF EXISTS %s", RoutineTable.NAME));
    db.execSQL(String.format("DROP TABLE IF EXISTS %s", CategoryTable.NAME));
    onCreate(db);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    final String SQL_CREATE_EXERCISE_LIST_TABLE = String.format(
        "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL);",
        ExerciseTable.NAME, ExerciseTable._ID, ExerciseTable.Cols.NAME);
    final String SQL_CREATE_WORKOUT_TABLE = String.format(
        "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER NOT NULL, %s STRING "
            + "NOT NULL, %s LONG NOT NULL);",
        WorkoutTable.NAME, WorkoutTable._ID, WorkoutTable.Cols.EXERCISE_ID,
        WorkoutTable.Cols.EXERCISE_SETS, WorkoutTable.Cols.TIME_STAMP);
    final String SQL_CREATE_ROUTINE_LIST_TABLE = String.format(
        "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL, %s TEXT NOT "
            + "NULL);", RoutineTable.NAME, RoutineTable._ID, RoutineTable.Cols.NAME,
        RoutineTable.Cols.EXERCISES);
    final String SQL_CREATE_CATEGORY_LIST_TABLE = String.format(
        "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL, %s TEXT NOT "
            + "NULL);", CategoryTable.NAME, CategoryTable._ID, CategoryTable.Cols.NAME,
        CategoryTable.Cols.EXERCISES);
    db.execSQL(SQL_CREATE_EXERCISE_LIST_TABLE);
    db.execSQL(SQL_CREATE_WORKOUT_TABLE);
    db.execSQL(SQL_CREATE_ROUTINE_LIST_TABLE);
    db.execSQL(SQL_CREATE_CATEGORY_LIST_TABLE);
  }
}
