package com.example.ceamaya.workoutapp.Database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.ceamaya.workoutapp.ExerciseSet;

public class ExerciseSetCursorWrapper extends CursorWrapper {

    public ExerciseSetCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public ExerciseSet getExerciseSet() {
        int reps = getInt(getColumnIndex(ExerciseDbSchema.ExerciseSetTable.Cols.REPS));
        int weight = getInt(getColumnIndex(ExerciseDbSchema.ExerciseSetTable.Cols.WEIGHT));
        int exerciseId = getInt(getColumnIndex(ExerciseDbSchema.ExerciseSetTable.Cols.EXERCISE_ID));
        int setNumber = getInt(getColumnIndex(ExerciseDbSchema.ExerciseSetTable.Cols.SET_NUMBER));
        long timestamp = getLong(getColumnIndex(ExerciseDbSchema.ExerciseSetTable.Cols.TIME_STAMP));

        ExerciseSet exerciseSet = new ExerciseSet(reps, weight, exerciseId, setNumber);
        exerciseSet.setTimeStamp(timestamp);
        return exerciseSet;
    }
}
