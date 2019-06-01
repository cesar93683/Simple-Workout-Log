package com.example.ceamaya.workoutapp.labs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ceamaya.workoutapp.Exercise;
import com.example.ceamaya.workoutapp.database.DatabaseHelper;
import com.example.ceamaya.workoutapp.database.DbSchema.RoutineExerciseTable;

import java.util.ArrayList;

public class RoutineExerciseLab {

    private static RoutineExerciseLab routineExerciseLab;
    private final SQLiteDatabase database;
    private ExerciseLab exerciseLab;
    private static final String TAG = "RoutineExerciseLab";

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
            exercises.add(exerciseLab.getExerciseById(exerciseId));
        }
        cursor.close();
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

    public void insertRoutineExercise(int routineId, int exerciseId) {
        ContentValues values = new ContentValues();
        values.put(RoutineExerciseTable.Cols.ROUTINE_ID, routineId);
        values.put(RoutineExerciseTable.Cols.EXERCISE_ID, exerciseId);
        database.insert(RoutineExerciseTable.NAME, null, values);
    }

}
