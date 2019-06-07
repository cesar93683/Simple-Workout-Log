package com.devcesar.workoutapp.labs;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.devcesar.workoutapp.Exercise;
import com.devcesar.workoutapp.Routine;
import com.devcesar.workoutapp.database.DatabaseHelper;
import com.devcesar.workoutapp.database.DbSchema.RoutineTable;
import com.devcesar.workoutapp.database.DbSchema.RoutineTable.Cols;
import com.devcesar.workoutapp.database.RoutineCursorWrapper;
import com.google.gson.Gson;
import java.util.ArrayList;

public class RoutineLab {

  private static RoutineLab routineLab;
  private final SQLiteDatabase database;
  private ArrayList<Routine> routines;

  private RoutineLab(Context context) {
    database = new DatabaseHelper(context.getApplicationContext()).getWritableDatabase();
    routines = new ArrayList<>();
    updateRoutines();
  }

  public static RoutineLab get(Context context) {
    if (routineLab == null) {
      routineLab = new RoutineLab(context);
    }
    routineLab.updateRoutines();
    return routineLab;
  }

  private void updateRoutines() {
    routines.clear();
    RoutineCursorWrapper cursor = queryRoutines(null, null);
    routines = new ArrayList<>();
    while (cursor.moveToNext()) {
      routines.add(cursor.getRoutine());
    }
    cursor.close();
  }

  private RoutineCursorWrapper queryRoutines(String whereClause, String[] whereArgs) {
    @SuppressLint("Recycle") Cursor cursor = database.query(
        RoutineTable.NAME,
        null,
        whereClause,
        whereArgs,
        null,
        null,
        null
    );
    return new RoutineCursorWrapper(cursor);
  }

  public void insertRoutine(String routineName) {
    ContentValues values = new ContentValues();
    values.put(RoutineTable.Cols.NAME, routineName);
    values.put(Cols.EXERCISES, new Gson().toJson(new ArrayList<Exercise>()));
    database.insert(RoutineTable.NAME, null, values);
    updateRoutines();
  }

  public void deleteRoutine(int routineId) {
    String whereClause = RoutineTable._ID + "=?";
    String[] whereArgs = new String[]{String.valueOf(routineId)};
    database.delete(RoutineTable.NAME, whereClause, whereArgs);
    updateRoutines();
  }

  public void updateRoutineName(int routineId, String newRoutineName) {
    ContentValues values = new ContentValues();
    values.put(RoutineTable.Cols.NAME, newRoutineName);
    String whereClause = RoutineTable._ID + "=?";
    String[] whereArgs = new String[]{String.valueOf(routineId)};
    database.update(RoutineTable.NAME, values, whereClause, whereArgs);
    updateRoutines();
  }

  public boolean contains(String routineName) {
    for (Routine routine : routines) {
      if (routine.getRoutineName().equals(routineName)) {
        return true;
      }
    }
    return false;
  }

  public ArrayList<Routine> getFilteredRoutines(String filter) {
    ArrayList<Routine> filteredRoutines = new ArrayList<>();
    for (Routine routine : routines) {
      if (routine.getRoutineName().contains(filter)) {
        filteredRoutines.add(routine);
      }
    }
    return filteredRoutines;
  }

  public void deleteExerciseFromRoutine(int routineId, int exerciseId) {
    ArrayList<Exercise> exercises = getExercises(routineId);
    for (int i = 0; i < exercises.size(); i++) {
      if (exercises.get(i).getExerciseId() == exerciseId) {
        exercises.remove(i);
        break;
      }
    }
    updateRoutineExercises(routineId, exercises);
  }

  public ArrayList<Exercise> getExercises(int routineId) {
    String whereClause = RoutineTable._ID + "=?";
    String[] whereArgs = new String[]{String.valueOf(routineId)};
    RoutineCursorWrapper cursor = queryRoutines(whereClause, whereArgs);

    cursor.moveToNext();
    ArrayList<Exercise> exercises = cursor.getExercises();
    cursor.close();

    return exercises;
  }

  public void updateRoutineExercises(int routineId, ArrayList<Exercise> exercises) {
    ContentValues values = new ContentValues();
    values.put(Cols.EXERCISES, new Gson().toJson(exercises));
    String whereClause = RoutineTable._ID + "=?";
    String[] whereArgs = new String[]{String.valueOf(routineId)};
    database.update(RoutineTable.NAME, values, whereClause, whereArgs);
  }

}
