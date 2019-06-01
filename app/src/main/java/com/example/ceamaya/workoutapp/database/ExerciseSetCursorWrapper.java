package com.example.ceamaya.workoutapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.ceamaya.workoutapp.ExerciseSet;
import com.example.ceamaya.workoutapp.database.DbSchema.ExerciseSetTable;

public class ExerciseSetCursorWrapper extends CursorWrapper {

    public ExerciseSetCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public ExerciseSet getExerciseSet() {
        int reps = getInt(getColumnIndex(ExerciseSetTable.Cols.REPS));
        int weight = getInt(getColumnIndex(ExerciseSetTable.Cols.WEIGHT));
        int exerciseId = getInt(getColumnIndex(ExerciseSetTable.Cols.EXERCISE_ID));
        int setNumber = getInt(getColumnIndex(ExerciseSetTable.Cols.SET_NUMBER));
        long timestamp = getLong(getColumnIndex(ExerciseSetTable.Cols.TIME_STAMP));

        ExerciseSet exerciseSet = new ExerciseSet(reps, weight, exerciseId, setNumber);
        exerciseSet.setTimeStamp(timestamp);
        return exerciseSet;
    }
}
