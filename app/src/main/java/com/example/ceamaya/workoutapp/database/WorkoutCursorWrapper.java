package com.example.ceamaya.workoutapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.example.ceamaya.workoutapp.ExerciseSet;
import com.example.ceamaya.workoutapp.database.DbSchema.WorkoutTable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class WorkoutCursorWrapper extends CursorWrapper {

  public WorkoutCursorWrapper(Cursor cursor) {
    super(cursor);
  }

  public ArrayList<ExerciseSet> getExerciseSets() {
    String exerciseSetsString = getString(getColumnIndex(WorkoutTable.Cols.EXERCISE_SETS));
    Type type = new TypeToken<ArrayList<ExerciseSet>>() {
    }.getType();
    return new Gson().fromJson(exerciseSetsString, type);
  }
}
