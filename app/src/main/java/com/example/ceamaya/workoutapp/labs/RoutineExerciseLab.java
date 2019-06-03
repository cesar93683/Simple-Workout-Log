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
        if (cursor.moveToNext()) {
            String exerciseIds = cursor.getString(cursor.getColumnIndex(
                    RoutineExerciseTable.Cols.EXERCISE_ID));
            String[] exerciseIdStringArr = exerciseIds.split(",");
            for (String exerciseIdString : exerciseIdStringArr) {
                int exerciseId = Integer.parseInt(exerciseIdString);
                Exercise exercise = exerciseLab.getExerciseById(exerciseId);
                exercises.add(exercise);
            }
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

    public void updateRoutineExercises(int routineId, ArrayList<Exercise> exercises) {
        deleteRoutineExercise(routineId);
        StringBuilder sb = new StringBuilder();
        String comma = "";
        for (Exercise exercise : exercises) {
            sb.append(comma);
            sb.append(exercise.getExerciseId());
            if (comma.equals("")) {
                comma = ",";
            }
        }
        if(sb.length() == 0) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put(RoutineExerciseTable.Cols.ROUTINE_ID, routineId);
        values.put(RoutineExerciseTable.Cols.EXERCISE_ID, sb.toString());
        database.insert(RoutineExerciseTable.NAME, null, values);
    }

    void deleteRoutineExercise(int routineId) {
        String whereClause = RoutineExerciseTable.Cols.ROUTINE_ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(routineId)};
        database.delete(RoutineExerciseTable.NAME, whereClause, whereArgs);
    }

    public void deleteExerciseFromRoutine(int routineId, int exerciseId) {
        ArrayList<Exercise> exercises = getExercises(routineId);
        for (int i = 0; i < exercises.size(); i++) {
            if (exercises.get(i).getExerciseId() == exerciseId) {
                exercises.remove(i);
                break;
            }
        }
        updateRoutineExercises(routineId, exercises);
    }
}
