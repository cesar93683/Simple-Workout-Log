package com.example.ceamaya.workoutapp.MainActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ceamaya.workoutapp.Database.ExerciseBaseHelper;
import com.example.ceamaya.workoutapp.Database.ExerciseDbSchema.ExerciseTable;
import com.example.ceamaya.workoutapp.Database.ExerciseDbSchema.ExerciseSetTable;
import com.example.ceamaya.workoutapp.Exercise;

import java.util.ArrayList;

class ExerciseLab {

    private static ExerciseLab exerciseLab;
    private SQLiteDatabase database;
    private ArrayList<Exercise> exercises;

    private ExerciseLab(Context context) {
        database = new ExerciseBaseHelper(context.getApplicationContext()).getWritableDatabase();
        exercises = new ArrayList<>();
        updateExercises();
    }

    static ExerciseLab get(Context context) {
        if (exerciseLab == null) {
            exerciseLab = new ExerciseLab(context);
        }
        exerciseLab.updateExercises();
        return exerciseLab;
    }

    private void updateExercises() {
        exercises.clear();
        Cursor cursor = database.query(ExerciseTable.NAME,null, null,
                null, null, null, null);
        exercises = new ArrayList<>();
        while (cursor.moveToNext()) {
            String exercise = cursor.getString(cursor.getColumnIndex(ExerciseTable.Cols.NAME));
            int exerciseId = cursor.getInt(cursor.getColumnIndex(ExerciseTable._ID));
            exercises.add(new Exercise(exercise, exerciseId));
        }
        cursor.close();
    }

    void insertExercise(String exercise) {
        ContentValues cv = new ContentValues();
        cv.put(ExerciseTable.Cols.NAME, exercise);
        database.insert(ExerciseTable.NAME, null, cv);
        updateExercises();
    }

    void deleteExercise(long id) {
        String[] whereArgs = new String[]{String.valueOf(id)};
        database.delete(ExerciseTable.NAME,ExerciseTable._ID + "=?", whereArgs);
        database.delete(ExerciseSetTable.NAME, ExerciseSetTable.Cols.EXERCISE_ID + "=?",
                whereArgs);
        updateExercises();
    }

    void updateExercise(long id, String newExercise) {
        ContentValues cv = new ContentValues();
        cv.put(ExerciseTable.Cols.NAME, newExercise);
        String[] whereArgs = new String[]{String.valueOf(id)};
        database.update(ExerciseTable.NAME, cv,ExerciseTable._ID + "=?", whereArgs);
        updateExercises();
    }

    boolean contains(String newExercise) {
        for (Exercise exercise : exercises) {
            if (exercise.getExercise().equals(newExercise)) {
                return true;
            }
        }
        return false;
    }

    ArrayList<Exercise> getFilteredExercise(String filter) {
        ArrayList<Exercise> filteredExercises = new ArrayList<>();
        for (Exercise exercise : exercises) {
            if (exercise.getExercise().contains(filter)) {
                filteredExercises.add(exercise);
            }
        }
        return filteredExercises;
    }
}
