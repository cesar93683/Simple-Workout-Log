package com.example.ceamaya.workoutapp.ExerciseActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ceamaya.workoutapp.Database.ExerciseBaseHelper;
import com.example.ceamaya.workoutapp.Database.ExerciseDbSchema;
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
        database = new ExerciseBaseHelper(context.getApplicationContext())
                .getWritableDatabase();
    }

    static WorkoutLab get(Context context) {
        if (workoutLab == null) {
            workoutLab = new WorkoutLab(context);
        }
        return workoutLab;
    }

    public ArrayList<Workout> getWorkouts(int exerciseId) {
        Cursor cursor = database.query(ExerciseDbSchema.ExerciseSetTable.NAME, null,
                ExerciseDbSchema.ExerciseSetTable.Cols.EXERCISE_ID + "=?",
                new String[]{String.valueOf
                        (exerciseId)},
                null, null, null);

        HashMap<Long, ArrayList<ExerciseSet>> exerciseSetsMap = new HashMap<>();
        while (cursor.moveToNext()) {
            int reps =
                    cursor.getInt(cursor.getColumnIndex(ExerciseDbSchema.ExerciseSetTable.Cols.REPS));
            int weight =
                    cursor.getInt(cursor.getColumnIndex(ExerciseDbSchema.ExerciseSetTable.Cols.WEIGHT));
            int setNumber =
                    cursor.getInt(cursor.getColumnIndex(ExerciseDbSchema.ExerciseSetTable.Cols.SET_NUMBER));
            long timestamp =
                    cursor.getLong(cursor.getColumnIndex(ExerciseDbSchema.ExerciseSetTable.Cols.DATE));
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

    public ArrayList<ExerciseSet> getExerciseSets(int exerciseId, long time) {
        String[] whereArgs = new String[]{String.valueOf(exerciseId), String.valueOf(time)};

        Cursor cursor = database.query(ExerciseDbSchema.ExerciseSetTable.NAME, null,
                ExerciseDbSchema.ExerciseSetTable.Cols.EXERCISE_ID + "=? AND " + ExerciseDbSchema.ExerciseSetTable.Cols.DATE
                        + "=?", whereArgs, null, null, null);

        ArrayList<ExerciseSet> exerciseSets = new ArrayList<>();
        while (cursor.moveToNext()) {
            int reps =
                    cursor.getInt(cursor.getColumnIndex(ExerciseDbSchema.ExerciseSetTable.Cols.REPS));
            int weight =
                    cursor.getInt(cursor.getColumnIndex(ExerciseDbSchema.ExerciseSetTable.Cols.WEIGHT));
            int setNumber =
                    cursor.getInt(cursor.getColumnIndex(ExerciseDbSchema.ExerciseSetTable.Cols.SET_NUMBER));
            ExerciseSet exerciseSet = new ExerciseSet(reps, weight, exerciseId, setNumber);
            exerciseSets.add(exerciseSet);
        }
        cursor.close();

        return exerciseSets;
    }

    public void deleteWorkout(long time) {
        String[] whereArgs = new String[]{String.valueOf(time)};
        database.delete(ExerciseDbSchema.ExerciseSetTable.NAME,
                ExerciseDbSchema.ExerciseSetTable.Cols.DATE + "=?", whereArgs);
    }

    public void insertWorkout(Workout workout) {
        for (ExerciseSet exerciseSet : workout.getExerciseSets()) {
            workoutLab.insertExerciseSet(exerciseSet, workout.getDate().getTime());
        }
    }

    private void insertExerciseSet(ExerciseSet exerciseSet, long timeStamp) {
        int weight = exerciseSet.getWeight();
        int reps = exerciseSet.getReps();
        int exerciseId = exerciseSet.getExerciseId();
        int setNumber = exerciseSet.getSetNumber();
        ContentValues cv = new ContentValues();
        cv.put(ExerciseDbSchema.ExerciseSetTable.Cols.SET_NUMBER, setNumber);
        cv.put(ExerciseDbSchema.ExerciseSetTable.Cols.REPS, reps);
        cv.put(ExerciseDbSchema.ExerciseSetTable.Cols.WEIGHT, weight);
        cv.put(ExerciseDbSchema.ExerciseSetTable.Cols.EXERCISE_ID, exerciseId);
        cv.put(ExerciseDbSchema.ExerciseSetTable.Cols.DATE, timeStamp);
        database.insert(ExerciseDbSchema.ExerciseSetTable.NAME, null, cv);
    }
}
