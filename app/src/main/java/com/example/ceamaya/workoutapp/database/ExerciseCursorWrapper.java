package com.example.ceamaya.workoutapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.ceamaya.workoutapp.Exercise;

public class ExerciseCursorWrapper extends CursorWrapper {

    public ExerciseCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Exercise getExercise() {
        String exerciseName = getString(getColumnIndex(ExerciseDbSchema.ExerciseTable.Cols.NAME));
        int exerciseId = getInt(getColumnIndex(ExerciseDbSchema.ExerciseTable._ID));

        return new Exercise(exerciseName, exerciseId);
    }
}
