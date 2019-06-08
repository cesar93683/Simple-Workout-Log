package com.devcesar.workoutapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.devcesar.workoutapp.database.DbSchema.RoutineTable;
import com.devcesar.workoutapp.utils.Exercise;
import com.devcesar.workoutapp.utils.Routine;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class RoutineCursorWrapper extends CursorWrapper {

  public RoutineCursorWrapper(Cursor cursor) {
    super(cursor);
  }

  public Routine getRoutine() {
    String routineName = getString(getColumnIndex(RoutineTable.Cols.NAME));
    int routineId = getInt(getColumnIndex(RoutineTable._ID));
    return new Routine(routineName, routineId);
  }

  public ArrayList<Exercise> getExercises() {
    String exerciseString = getString(getColumnIndex(RoutineTable.Cols.EXERCISES));
    Type type = new TypeToken<ArrayList<Exercise>>() {
    }.getType();
    return new Gson().fromJson(exerciseString, type);
  }

}
