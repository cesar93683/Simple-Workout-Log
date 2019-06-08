package com.devcesar.workoutapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.devcesar.workoutapp.database.DbSchema.ExerciseTable;
import com.devcesar.workoutapp.utils.Exercise;


public class ExerciseCursorWrapper extends CursorWrapper {

  public ExerciseCursorWrapper(Cursor cursor) {
    super(cursor);
  }

  public Exercise getExercise() {
    String exerciseName = getString(getColumnIndexOrThrow(ExerciseTable.Cols.NAME));
    int exerciseId = getInt(getColumnIndexOrThrow(ExerciseTable._ID));

    return new Exercise(exerciseName, exerciseId);
  }
}
