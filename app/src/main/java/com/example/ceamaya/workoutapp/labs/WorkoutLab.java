package com.example.ceamaya.workoutapp.labs;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ceamaya.workoutapp.ExerciseSet;
import com.example.ceamaya.workoutapp.Workout;
import com.example.ceamaya.workoutapp.database.DatabaseHelper;
import com.example.ceamaya.workoutapp.database.DbSchema.ExerciseSetTable;
import com.example.ceamaya.workoutapp.database.ExerciseSetCursorWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class WorkoutLab {

    private static WorkoutLab workoutLab;
    private final SQLiteDatabase database;

    private WorkoutLab(Context context) {
        database = new DatabaseHelper(context.getApplicationContext()).getWritableDatabase();
    }

    public static WorkoutLab get(Context context) {
        if (workoutLab == null) {
            workoutLab = new WorkoutLab(context);
        }
        return workoutLab;
    }

    public ArrayList<Workout> getWorkouts(int exerciseId) {
        String whereClause = ExerciseSetTable.Cols.EXERCISE_ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(exerciseId)};
        ExerciseSetCursorWrapper cursor = queryExerciseSets(whereClause, whereArgs);

        HashSet<Long> uniqueTimeStamps = new HashSet<>();
        while (cursor.moveToNext()) {
            uniqueTimeStamps.add(cursor.getExerciseSet().getTimeStamp());
        }
        cursor.close();

        ArrayList<Workout> workouts = new ArrayList<>();
        for (long timestamp : uniqueTimeStamps) {
            workouts.add(getWorkout(exerciseId, timestamp));
        }
        Collections.sort(workouts);

        return workouts;
    }

    private ExerciseSetCursorWrapper queryExerciseSets(String whereClause, String[] whereArgs) {
        @SuppressLint("Recycle") Cursor cursor = database.query(
                ExerciseSetTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new ExerciseSetCursorWrapper(cursor);
    }

    public Workout getWorkout(int exerciseId, long timeStamp) {
        String whereClause = String.format("%s=? AND %s=?", ExerciseSetTable.Cols.EXERCISE_ID,
                ExerciseSetTable.Cols.TIME_STAMP);
        String[] whereArgs = new String[]{String.valueOf(exerciseId), String.valueOf(timeStamp)};
        ExerciseSetCursorWrapper cursor = queryExerciseSets(whereClause, whereArgs);

        ArrayList<ExerciseSet> exerciseSets = new ArrayList<>();
        while (cursor.moveToNext()) {
            exerciseSets.add(cursor.getExerciseSet());
        }
        cursor.close();

        return new Workout(timeStamp, exerciseSets);
    }

    public void deleteExercise(int exerciseId) {
        String whereClause = ExerciseSetTable.Cols.EXERCISE_ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(exerciseId)};
        database.delete(ExerciseSetTable.NAME, whereClause, whereArgs);
    }

    public void updateWorkout(long timeStamp, ArrayList<ExerciseSet> exerciseSets) {
        deleteWorkout(timeStamp);
        Workout workout = new Workout(timeStamp, exerciseSets);
        insertWorkout(workout);
    }

    public void deleteWorkout(long time) {
        String whereClause = ExerciseSetTable.Cols.TIME_STAMP + "=?";
        String[] whereArgs = new String[]{String.valueOf(time)};
        database.delete(ExerciseSetTable.NAME, whereClause, whereArgs);
    }

    public void insertWorkout(Workout workout) {
        for (ExerciseSet exerciseSet : workout.getExerciseSets()) {
            workoutLab.insertExerciseSet(exerciseSet, workout.getTimeStamp());
        }
    }

    private void insertExerciseSet(ExerciseSet exerciseSet, long timeStamp) {
        ContentValues values = getContentValues(exerciseSet, timeStamp);
        database.insert(ExerciseSetTable.NAME, null, values);
    }

    private ContentValues getContentValues(ExerciseSet exerciseSet, long timeStamp) {
        ContentValues values = new ContentValues();
        values.put(ExerciseSetTable.Cols.REPS, exerciseSet.getReps());
        values.put(ExerciseSetTable.Cols.WEIGHT, exerciseSet.getWeight());
        values.put(ExerciseSetTable.Cols.SET_NUMBER, exerciseSet.getSetNumber());
        values.put(ExerciseSetTable.Cols.EXERCISE_ID, exerciseSet.getExerciseId());
        values.put(ExerciseSetTable.Cols.TIME_STAMP, timeStamp);
        return values;
    }
}

