package com.devcesar.workoutapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.devcesar.workoutapp.ExerciseSet;
import com.devcesar.workoutapp.Workout;
import com.devcesar.workoutapp.database.DbSchema.WorkoutTable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class WorkoutCursorWrapper extends CursorWrapper {

  public WorkoutCursorWrapper(Cursor cursor) {
    super(cursor);
  }

  public Workout getWorkout() {
    String exerciseSetsString = getString(getColumnIndex(WorkoutTable.Cols.EXERCISE_SETS));
    Type type = new TypeToken<ArrayList<ExerciseSet>>() {
    }.getType();
    ArrayList<ExerciseSet> exerciseSets = new Gson().fromJson(exerciseSetsString, type);
    long timeStamp = getLong(getColumnIndex(WorkoutTable.Cols.TIME_STAMP));
    int exerciseId = getInt(getColumnIndex(WorkoutTable.Cols.EXERCISE_ID));
    return new Workout(exerciseId, exerciseSets, timeStamp);
  }
}
