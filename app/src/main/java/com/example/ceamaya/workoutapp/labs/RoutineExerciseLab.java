package com.example.ceamaya.workoutapp.labs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.example.ceamaya.workoutapp.Exercise;
import com.example.ceamaya.workoutapp.database.DatabaseHelper;
import com.example.ceamaya.workoutapp.database.DbSchema.RoutineExerciseTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RoutineExerciseLab {

    private static final String TAG = "RoutineExerciseLab";
    private static RoutineExerciseLab routineExerciseLab;
    private final SQLiteDatabase database;
    private final ExerciseLab exerciseLab;

    private RoutineExerciseLab(Context context) {
        database = new DatabaseHelper(context.getApplicationContext()).getWritableDatabase();
        exerciseLab = ExerciseLab.get(context);
    }

    public static RoutineExerciseLab get(Context context) {
        if (routineExerciseLab == null) {
            routineExerciseLab = new RoutineExerciseLab(context);
        }
        return routineExerciseLab;
    }

    public ArrayList<Exercise> getExercises(int routineId) {
        String whereClause = RoutineExerciseTable.Cols.ROUTINE_ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(routineId)};
        Cursor cursor = queryRoutine(whereClause, whereArgs);

        ArrayList<Exercise> exercises = new ArrayList<>();
        while (cursor.moveToNext()) {
            int exerciseId = cursor.getInt(cursor.getColumnIndex(
                    RoutineExerciseTable.Cols.EXERCISE_ID));
            int position = cursor.getInt(cursor.getColumnIndex(
                    RoutineExerciseTable.Cols.EXERCISE_POSITION));
            Exercise exercise = exerciseLab.getExerciseById(exerciseId);
            exercise.setPosition(position);
            exercises.add(exercise);
        }
        cursor.close();
        Collections.sort(exercises, sortByPosition());
        return exercises;
    }

    private Cursor queryRoutine(String whereClause, String[] whereArgs) {
        return database.query(
                RoutineExerciseTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
    }

    @NonNull
    private Comparator<Exercise> sortByPosition() {
        return new Comparator<Exercise>() {
            @Override
            public int compare(Exercise o1, Exercise o2) {
                return o1.getPosition() - o2.getPosition();
            }
        };
    }

    public void updateRoutineExercises(int routineId, ArrayList<Exercise> exercises) {
        deleteRoutineExercise(routineId);
        for(int i = 0; i < exercises.size(); i++) {
            insertRoutineExercise(routineId, exercises.get(i).getExerciseId(), i);
        }
    }

    public void deleteRoutineExercise(int routineId) {
        String whereClause = RoutineExerciseTable.Cols.ROUTINE_ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(routineId)};
        database.delete(RoutineExerciseTable.NAME, whereClause, whereArgs);
    }

    private void insertRoutineExercise(int routineId, int exerciseId, int position) {
        ContentValues values = new ContentValues();
        values.put(RoutineExerciseTable.Cols.ROUTINE_ID, routineId);
        values.put(RoutineExerciseTable.Cols.EXERCISE_ID, exerciseId);
        values.put(RoutineExerciseTable.Cols.EXERCISE_POSITION, position);
        database.insert(RoutineExerciseTable.NAME, null, values);
    }
}
