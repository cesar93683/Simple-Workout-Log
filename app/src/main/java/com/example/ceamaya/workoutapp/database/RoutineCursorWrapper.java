package com.example.ceamaya.workoutapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.example.ceamaya.workoutapp.Routine;
import com.example.ceamaya.workoutapp.database.DbSchema.RoutineTable;

public class RoutineCursorWrapper extends CursorWrapper {

  public RoutineCursorWrapper(Cursor cursor) {
    super(cursor);
  }

  public Routine getRoutine() {
    String routineName = getString(getColumnIndex(RoutineTable.Cols.NAME));
    int routineId = getInt(getColumnIndex(RoutineTable._ID));

    return new Routine(routineName, routineId);
  }
}
