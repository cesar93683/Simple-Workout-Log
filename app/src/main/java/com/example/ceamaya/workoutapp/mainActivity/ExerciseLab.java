package com.example.ceamaya.workoutapp.mainActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ceamaya.workoutapp.Exercise;
import com.example.ceamaya.workoutapp.database.ExerciseBaseHelper;
import com.example.ceamaya.workoutapp.database.ExerciseCursorWrapper;
import com.example.ceamaya.workoutapp.database.ExerciseDbSchema.ExerciseSetTable;
import com.example.ceamaya.workoutapp.database.ExerciseDbSchema.ExerciseTable;

import java.util.ArrayList;

class ExerciseLab {

    private static ExerciseLab exerciseLab;
    private final SQLiteDatabase database;
    private ArrayList<Exercise> exercises;

    private ExerciseLab(Context context) {
        database = new ExerciseBaseHelper(context.getApplicationContext()).getWritableDatabase();
        exercises = new ArrayList<>();
        updateExercises();
    }

    private void updateExercises() {
        exercises.clear();
        ExerciseCursorWrapper cursor = queryExercises(null, null);
        exercises = new ArrayList<>();
        while (cursor.moveToNext()) {
            exercises.add(cursor.getExercise());
        }
        cursor.close();
    }

    private ExerciseCursorWrapper queryExercises(String whereClause, String[] whereArgs) {
        Cursor cursor = database.query(
                ExerciseTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new ExerciseCursorWrapper(cursor);
    }

    static ExerciseLab get(Context context) {
        if (exerciseLab == null) {
            exerciseLab = new ExerciseLab(context);
        }
        exerciseLab.updateExercises();
        return exerciseLab;
    }

    void insertExercise(String exerciseName) {
        ContentValues values = getContentValues(exerciseName);
        database.insert(ExerciseTable.NAME, null, values);
        updateExercises();
    }

    private static ContentValues getContentValues(String exerciseName) {
        ContentValues values = new ContentValues();
        values.put(ExerciseTable.Cols.NAME, exerciseName);
        return values;
    }

    void deleteExercise(long id) {
        String[] whereArgs = new String[]{String.valueOf(id)};
        database.delete(ExerciseTable.NAME, ExerciseTable._ID + "=?", whereArgs);
        database.delete(ExerciseSetTable.NAME, ExerciseSetTable.Cols.EXERCISE_ID + "=?",
                whereArgs);
        updateExercises();
    }

    void updateExercise(long id, String newExerciseName) {
        ContentValues values = getContentValues(newExerciseName);
        String whereClause = ExerciseTable._ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        database.update(ExerciseTable.NAME, values, whereClause, whereArgs);
        updateExercises();
    }

    boolean contains(String newExercise) {
        for (Exercise exercise : exercises) {
            if (exercise.getExerciseName().equals(newExercise)) {
                return true;
            }
        }
        return false;
    }

    ArrayList<Exercise> getFilteredExercise(String filter) {
        ArrayList<Exercise> filteredExercises = new ArrayList<>();
        for (Exercise exercise : exercises) {
            if (exercise.getExerciseName().contains(filter)) {
                filteredExercises.add(exercise);
            }
        }
        return filteredExercises;
    }
}
