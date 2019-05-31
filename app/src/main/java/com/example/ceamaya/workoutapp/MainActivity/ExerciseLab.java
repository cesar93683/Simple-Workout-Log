package com.example.ceamaya.workoutapp.MainActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ceamaya.workoutapp.Database.ExerciseBaseHelper;
import com.example.ceamaya.workoutapp.Database.ExerciseDbSchema;

import java.util.ArrayList;

public class ExerciseLab {

    private static ExerciseLab exerciseLab;
    private SQLiteDatabase database;
    private ArrayList<Exercise> exercises;

    private ExerciseLab(Context context) {
        database = new ExerciseBaseHelper(context.getApplicationContext())
                .getWritableDatabase();
        exercises = new ArrayList<>();
        updateExercises();
    }

    public static ExerciseLab get(Context context) {
        if (exerciseLab == null) {
            exerciseLab = new ExerciseLab(context);
        }
        return exerciseLab;
    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    public void updateExercises() {
        exercises.clear();
        Cursor cursor = database.query(ExerciseDbSchema.ExerciseTable.NAME,
                null, null, null, null, null, null);
        exercises = new ArrayList<>();
        while (cursor.moveToNext()) {
            String exercise =
                    cursor.getString(cursor.getColumnIndex(ExerciseDbSchema.ExerciseTable.Cols.NAME));
            int exerciseId =
                    cursor.getInt(cursor.getColumnIndex(ExerciseDbSchema.ExerciseTable._ID));
            exercises.add(new Exercise(exercise, exerciseId));
        }
        cursor.close();
    }

    public void insertExercise(String exercise) {
        ContentValues cv = new ContentValues();
        cv.put(ExerciseDbSchema.ExerciseTable.Cols.NAME, exercise);
        database.insert(ExerciseDbSchema.ExerciseTable.NAME, null, cv);
        updateExercises();
    }

    public void deleteExercise(long id) {
        String[] whereArgs = new String[]{String.valueOf(id)};
        database.delete(ExerciseDbSchema.ExerciseTable.NAME,
                ExerciseDbSchema.ExerciseTable._ID + "=?", whereArgs);
        database.delete(ExerciseDbSchema.ExerciseSetTable.NAME,
                ExerciseDbSchema.ExerciseSetTable.Cols.EXERCISE_ID + "=?", whereArgs);
        updateExercises();
    }

    public void updateExercise(long id, String newExercise) {
        ContentValues cv = new ContentValues();
        cv.put(ExerciseDbSchema.ExerciseTable.Cols.NAME, newExercise);
        String[] whereArgs = new String[]{String.valueOf(id)};
        database.update(ExerciseDbSchema.ExerciseTable.NAME, cv,
                ExerciseDbSchema.ExerciseTable._ID + "=?", whereArgs);
        updateExercises();
    }

    public boolean contains(String newExercise) {
        for (Exercise exercise : exercises) {
            if (exercise.getExercise().equals(newExercise)) {
                return true;
            }
        }
        return false;
    }
}
