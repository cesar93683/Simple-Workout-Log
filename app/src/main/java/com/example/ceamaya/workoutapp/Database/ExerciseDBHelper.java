package com.example.ceamaya.workoutapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ceamaya.workoutapp.ExerciseSet;
import com.example.ceamaya.workoutapp.Workout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import static com.example.ceamaya.workoutapp.Database.ExerciseDbSchema.ExerciseSetTable;
import static com.example.ceamaya.workoutapp.Database.ExerciseDbSchema.ExerciseTable;


public class ExerciseDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "exerciseList.db";
    private static final int VERSION = 1;
    private static final String TAG = "ExerciseDBHelper";

    public ExerciseDBHelper(Context context) {
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
                        .Cols.WEIGHT,
                ExerciseSetTable.Cols.DATE);
        db.execSQL(SQL_CREATE_EXERCISE_LIST_TABLE);
        db.execSQL(SQL_CREATE_SET_LIST_TABLE);
    }

    public void insertExercise(String exercise) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ExerciseTable.Cols.NAME, exercise);
        db.insert(ExerciseTable.NAME, null, cv);
    }

    public HashMap<String, Integer> getExercises() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(ExerciseTable.NAME,
                null, null, null, null, null, null);
        HashMap<String, Integer> exercises = new HashMap<>();
        while (cursor.moveToNext()) {
            String exercise = cursor.getString(cursor.getColumnIndex(ExerciseTable.Cols.NAME));
            int id = cursor.getInt(cursor.getColumnIndex(ExerciseTable._ID));
            exercises.put(exercise, id);
        }
        cursor.close();
        return exercises;
    }

    public void deleteExercise(long id) {
        SQLiteDatabase db = getWritableDatabase();
        String[] whereArgs = new String[]{String.valueOf(id)};
        db.delete(ExerciseTable.NAME, ExerciseTable._ID + "=?", whereArgs);
        db.delete(ExerciseSetTable.NAME, ExerciseSetTable.Cols.EXERCISE_ID + "=?", whereArgs);
    }

    public void updateExercise(long id, String newExercise) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ExerciseTable.Cols.NAME, newExercise);
        String[] whereArgs = new String[]{String.valueOf(id)};
        db.update(ExerciseTable.NAME, cv, ExerciseTable._ID + "=?",whereArgs);
    }

    public void insertSet(ExerciseSet exerciseSet, long timeStamp) {
        int weight = exerciseSet.getWeight();
        int reps = exerciseSet.getReps();
        int exerciseId = exerciseSet.getExerciseId();
        int setNumber = exerciseSet.getSetNumber();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ExerciseSetTable.Cols.SET_NUMBER, setNumber);
        cv.put(ExerciseSetTable.Cols.REPS, reps);
        cv.put(ExerciseSetTable.Cols.WEIGHT, weight);
        cv.put(ExerciseSetTable.Cols.EXERCISE_ID, exerciseId);
        cv.put(ExerciseSetTable.Cols.DATE, timeStamp);
        db.insert(ExerciseSetTable.NAME, null, cv);
    }

    public ArrayList<Workout> getWorkouts(int exerciseId) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(ExerciseSetTable.NAME, null,
                ExerciseSetTable.Cols.EXERCISE_ID + "=?", new String[]{String.valueOf
                        (exerciseId)},
                null, null, null);

        HashMap<Long, ArrayList<ExerciseSet>> exerciseSetsMap = new HashMap<>();
        while (cursor.moveToNext()) {
            int reps = cursor.getInt(cursor.getColumnIndex(ExerciseSetTable.Cols.REPS));
            int weight = cursor.getInt(cursor.getColumnIndex(ExerciseSetTable.Cols.WEIGHT));
            int setNumber = cursor.getInt(cursor.getColumnIndex(ExerciseSetTable.Cols.SET_NUMBER));
            long timestamp = cursor.getLong(cursor.getColumnIndex(ExerciseSetTable.Cols
                    .DATE));
            ExerciseSet exerciseSet = new ExerciseSet(reps, weight, exerciseId, setNumber);

            ArrayList<ExerciseSet> exerciseSets;
            if (exerciseSetsMap.containsKey(timestamp)) {
                exerciseSets = exerciseSetsMap.get(timestamp);
            } else {
                exerciseSets = new ArrayList<>();
            }
            exerciseSets.add(exerciseSet);
            exerciseSetsMap.put(timestamp, exerciseSets);
        }
        cursor.close();

        ArrayList<Workout> workouts = new ArrayList<>();

        for (long timestamp : exerciseSetsMap.keySet()) {
            workouts.add(new Workout(new Date(timestamp), exerciseSetsMap.get(timestamp)));
        }

        Collections.sort(workouts, new Comparator<Workout>() {
            @Override
            public int compare(Workout o1, Workout o2) {
                return (int) (o2.getDate().getTime() - o1.getDate().getTime());
            }
        });

        return workouts;
    }

    public void deleteWorkout(long time) {
        SQLiteDatabase db = getWritableDatabase();
        String[] whereArgs = new String[]{String.valueOf(time)};
        db.delete(ExerciseSetTable.NAME, ExerciseSetTable.Cols.DATE + "=?", whereArgs);
    }
}
