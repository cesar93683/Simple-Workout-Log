package com.example.ceamaya.workoutapp.ExerciseActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.example.ceamaya.workoutapp.Database.ExerciseBaseHelper;
import com.example.ceamaya.workoutapp.Database.ExerciseDbSchema.ExerciseSetTable;
import com.example.ceamaya.workoutapp.ExerciseSet;
import com.example.ceamaya.workoutapp.Workout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

public class WorkoutLab {

    private static WorkoutLab workoutLab;
    private SQLiteDatabase database;

    public WorkoutLab(Context context) {
        database = new ExerciseBaseHelper(context.getApplicationContext()).getWritableDatabase();
    }

    static WorkoutLab get(Context context) {
        if (workoutLab == null) {
            workoutLab = new WorkoutLab(context);
        }
        return workoutLab;
    }

    public ArrayList<Workout> getWorkouts(int exerciseId) {
        String[] selectionArgs = new String[]{String.valueOf(exerciseId)};
        Cursor cursor = database.query(ExerciseSetTable.NAME, null,
                ExerciseSetTable.Cols.EXERCISE_ID + "=?", selectionArgs,
                null, null, null);

        HashMap<Long, ArrayList<ExerciseSet>> exerciseSetsMap = new HashMap<>();
        while (cursor.moveToNext()) {
            int reps = cursor.getInt(cursor.getColumnIndex(ExerciseSetTable.Cols.REPS));
            int weight = cursor.getInt(cursor.getColumnIndex(ExerciseSetTable.Cols.WEIGHT));
            int setNumber = cursor.getInt(cursor.getColumnIndex(ExerciseSetTable.Cols.SET_NUMBER));
            long timestamp = cursor.getLong(cursor.getColumnIndex(ExerciseSetTable.Cols.DATE));
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

    public Workout getWorkout(int exerciseId, long time) {
        String whereClause = ExerciseSetTable.Cols.EXERCISE_ID + "=? AND " +
                ExerciseSetTable.Cols.DATE + "=?";
        String[] whereArgs = new String[]{String.valueOf(exerciseId), String.valueOf(time)};
        ExerciseSetCursorWrapper cursor = queryExerciseSet(whereClause, whereArgs);

        ArrayList<ExerciseSet> exerciseSets = new ArrayList<>();
        while (cursor.moveToNext()) {
            exerciseSets.add(cursor.getExerciseSet());
        }
        cursor.close();

        Date date = new Date(time);
        return new Workout(date, exerciseSets);
    }

    public void deleteWorkout(long time) {
        String[] whereArgs = new String[]{String.valueOf(time)};
        database.delete(ExerciseSetTable.NAME, ExerciseSetTable.Cols.DATE + "=?",
                whereArgs);
    }

    public void insertWorkout(Workout workout) {
        for (ExerciseSet exerciseSet : workout.getExerciseSets()) {
            workoutLab.insertExerciseSet(exerciseSet, workout.getDate().getTime());
        }
    }

    public void insertExerciseSet(ExerciseSet exerciseSet, long timeStamp) {
        ContentValues values = getContentValues(exerciseSet, timeStamp);
        database.insert(ExerciseSetTable.NAME, null, values);
    }

    private static ContentValues getContentValues(ExerciseSet exerciseSet, long timeStamp) {
        ContentValues values = new ContentValues();
        values.put(ExerciseSetTable.Cols.REPS, exerciseSet.getReps());
        values.put(ExerciseSetTable.Cols.WEIGHT, exerciseSet.getWeight());
        values.put(ExerciseSetTable.Cols.SET_NUMBER, exerciseSet.getSetNumber());
        values.put(ExerciseSetTable.Cols.EXERCISE_ID, exerciseSet.getExerciseId());
        values.put(ExerciseSetTable.Cols.DATE, timeStamp);
        return values;
    }

    private ExerciseSetCursorWrapper queryExerciseSet(String whereClause, String[] whereArgs) {
        Cursor cursor = database.query(
                ExerciseSetTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return new ExerciseSetCursorWrapper(cursor);
    }
}

class ExerciseSetCursorWrapper extends CursorWrapper {

    public ExerciseSetCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public ExerciseSet getExerciseSet() {
        int reps = getInt(getColumnIndex(ExerciseSetTable.Cols.REPS));
        int weight = getInt(getColumnIndex(ExerciseSetTable.Cols.WEIGHT));
        int exerciseId = getInt(getColumnIndex(ExerciseSetTable.Cols.EXERCISE_ID));
        int setNumber = getInt(getColumnIndex(ExerciseSetTable.Cols.SET_NUMBER));

        return new ExerciseSet(reps, weight, exerciseId, setNumber);
    }
}