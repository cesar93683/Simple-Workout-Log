package com.devcesar.workoutapp.labs;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.devcesar.workoutapp.database.DatabaseHelper;
import com.devcesar.workoutapp.database.DbSchema.RoutineTable;
import com.devcesar.workoutapp.database.RoutineCursorWrapper;
import com.devcesar.workoutapp.utils.Exercise;
import com.devcesar.workoutapp.utils.NamedEntity;
import com.devcesar.workoutapp.utils.Routine;
import com.google.gson.Gson;
import java.util.ArrayList;

public class RoutineLab implements NamedEntityLab, NamedEntityExerciseLab {

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

  @Override
  public void insert(String name) {
    ContentValues values = new ContentValues();
    values.put(RoutineTable.Cols.NAME, name);
    values.put(RoutineTable.Cols.EXERCISES, new Gson().toJson(new ArrayList<Exercise>()));
    database.insert(RoutineTable.NAME, null, values);
    updateRoutines();
  }

  @Override
  public void delete(int id) {
    String whereClause = RoutineTable._ID + "=?";
    String[] whereArgs = new String[]{String.valueOf(id)};
    database.delete(RoutineTable.NAME, whereClause, whereArgs);
    updateRoutines();
  }

  @Override
  public void updateName(int id, String newName) {
    ContentValues values = new ContentValues();
    values.put(RoutineTable.Cols.NAME, newName);
    String whereClause = RoutineTable._ID + "=?";
    String[] whereArgs = new String[]{String.valueOf(id)};
    database.update(RoutineTable.NAME, values, whereClause, whereArgs);
    updateRoutines();
  }

  @Override
  public boolean contains(String routineName) {
    for (Routine routine : routines) {
      if (routine.getName().equals(routineName)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public ArrayList<NamedEntity> getFiltered(String filter) {
    ArrayList<NamedEntity> filteredRoutines = new ArrayList<>();
    for (Routine routine : routines) {
      if (routine.getName().contains(filter)) {
        filteredRoutines.add(routine);
      }
    }
    return filteredRoutines;
  }

  @Override
  public void deleteExercise(int id, int exerciseId) {
    ArrayList<Exercise> exercises = getExercises(id);
    for (int i = 0; i < exercises.size(); i++) {
      if (exercises.get(i).getId() == exerciseId) {
        exercises.remove(i);
        break;
      }
    }
    updateExercises(id, exercises);
  }

  @Override
  public ArrayList<Exercise> getExercises(int id) {
    String whereClause = RoutineTable._ID + "=?";
    String[] whereArgs = new String[]{String.valueOf(id)};
    RoutineCursorWrapper cursor = queryRoutines(whereClause, whereArgs);

    cursor.moveToNext();
    ArrayList<Exercise> exercises = cursor.getExercises();
    cursor.close();

    return exercises;
  }

  @Override
  public void updateExercises(int id, ArrayList<Exercise> exercises) {
    ContentValues values = new ContentValues();
    values.put(RoutineTable.Cols.EXERCISES, new Gson().toJson(exercises));
    String whereClause = RoutineTable._ID + "=?";
    String[] whereArgs = new String[]{String.valueOf(id)};
    database.update(RoutineTable.NAME, values, whereClause, whereArgs);
  }

}
