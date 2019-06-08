package com.devcesar.workoutapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.devcesar.workoutapp.Utils.Exercise;
import com.devcesar.workoutapp.database.DbSchema.ExerciseTable;


public class ExerciseCursorWrapper extends CursorWrapper {

  public ExerciseCursorWrapper(Cursor cursor) {
    super(cursor);
  }

  public Exercise getExercise() {
    String exerciseName = getString(getColumnIndex(ExerciseTable.Cols.NAME));
    int exerciseId = getInt(getColumnIndex(ExerciseTable._ID));

    return new Exercise(exerciseName, exerciseId);
  }
}
