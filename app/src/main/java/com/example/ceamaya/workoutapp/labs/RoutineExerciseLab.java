package com.example.ceamaya.workoutapp.labs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.ceamaya.workoutapp.Exercise;
import com.example.ceamaya.workoutapp.database.DatabaseHelper;
import com.example.ceamaya.workoutapp.database.DbSchema.RoutineExerciseTable;
import com.example.ceamaya.workoutapp.database.DbSchema.RoutineExerciseTable.Cols;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class RoutineExerciseLab {

  private static RoutineExerciseLab routineExerciseLab;
  private final SQLiteDatabase database;

  private RoutineExerciseLab(Context context) {
    database = new DatabaseHelper(context.getApplicationContext()).getWritableDatabase();
  }

  public static RoutineExerciseLab get(Context context) {
    if (routineExerciseLab == null) {
      routineExerciseLab = new RoutineExerciseLab(context);
    }
    return routineExerciseLab;
  }

  public ArrayList<Exercise> getExercises(int routineId) {
    String whereClause = RoutineExerciseTable.Cols.ROUTINE_ID + "=?";
    String[] whereArgs = new String[]{String.valueOf(routineId)};
    Cursor cursor = queryRoutine(whereClause, whereArgs);

    ArrayList<Exercise> exercises = new ArrayList<>();
    if (cursor.moveToNext()) {
      String exerciseString = cursor.getString(cursor.getColumnIndex(Cols.EXERCISE_ID));
      Type type = new TypeToken<ArrayList<Exercise>>() {
      }.getType();
      exercises = new Gson().fromJson(exerciseString, type);
    }
    cursor.close();

    return exercises;
  }

  private Cursor queryRoutine(String whereClause, String[] whereArgs) {
    return database.query(
        RoutineExerciseTable.NAME,
        null,
        whereClause,
        whereArgs,
        null,
        null,
        null
    );
  }

  public void updateRoutineExercises(int routineId, ArrayList<Exercise> exercises) {
    deleteRoutineExercise(routineId);
    String exercisesString = new Gson().toJson(exercises);
    ContentValues values = new ContentValues();
    values.put(RoutineExerciseTable.Cols.ROUTINE_ID, routineId);
    values.put(Cols.EXERCISE_ID, exercisesString);
    database.insert(RoutineExerciseTable.NAME, null, values);
  }

  void deleteRoutineExercise(int routineId) {
    String whereClause = RoutineExerciseTable.Cols.ROUTINE_ID + "=?";
    String[] whereArgs = new String[]{String.valueOf(routineId)};
    database.delete(RoutineExerciseTable.NAME, whereClause, whereArgs);
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
}
