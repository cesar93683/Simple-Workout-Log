package com.devcesar.workoutapp.labs;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.devcesar.workoutapp.database.DatabaseHelper;
import com.devcesar.workoutapp.database.DbSchema.ExerciseTable;
import com.devcesar.workoutapp.database.NamedEntityCursorWrapper;
import com.devcesar.workoutapp.utils.NamedEntity;
import java.util.ArrayList;

public class ExerciseLab implements NamedEntityLab {

  private static ExerciseLab exerciseLab;
  private final SQLiteDatabase database;
  private final WorkoutLab workoutLab;
  private ArrayList<NamedEntity> exercises;

  private ExerciseLab(Context context) {
    database = new DatabaseHelper(context.getApplicationContext()).getWritableDatabase();
    exercises = new ArrayList<>();
    workoutLab = WorkoutLab.get(context);
    updateExercises();
  }

  private void updateExercises() {
    exercises.clear();
    NamedEntityCursorWrapper cursor = queryExercises();
    exercises = new ArrayList<>();
    while (cursor.moveToNext()) {
      exercises.add(cursor.getNamedEntity());
    }
    cursor.close();
  }

  private NamedEntityCursorWrapper queryExercises() {
    @SuppressLint("Recycle") Cursor cursor = database
        .query(ExerciseTable.NAME, null, null, null, null, null, null);
    return new NamedEntityCursorWrapper(cursor, ExerciseTable.Cols.NAME, ExerciseTable._ID);
  }

  public static ExerciseLab get(Context context) {
    if (exerciseLab == null) {
      exerciseLab = new ExerciseLab(context);
    }
    return exerciseLab;
  }

  public NamedEntity getExerciseById(int id) {
    for (NamedEntity exercise : exercises) {
      if (exercise.getId() == id) {
        return exercise;
      }
    }
    throw new RuntimeException("ERROR: exercise id does not exist");
  }

  @Override
  public void insert(String name) {
    ContentValues values = getContentValues(name);
    database.insert(ExerciseTable.NAME, null, values);
    updateExercises();
  }

  private ContentValues getContentValues(String name) {
    ContentValues values = new ContentValues();
    values.put(ExerciseTable.Cols.NAME, name);
    return values;
  }

  @Override
  public void delete(int id) {
    String whereClause = ExerciseTable._ID + "=?";
    String[] whereArgs = new String[]{String.valueOf(id)};
    database.delete(ExerciseTable.NAME, whereClause, whereArgs);
    workoutLab.deleteWorkouts(id);
    updateExercises();
  }

  @Override
  public void updateName(int id, String newName) {
    ContentValues values = getContentValues(newName);
    String whereClause = ExerciseTable._ID + "=?";
    String[] whereArgs = new String[]{String.valueOf(id)};
    database.update(ExerciseTable.NAME, values, whereClause, whereArgs);
    updateExercises();
  }

  @Override
  public boolean contains(String name) {
    for (NamedEntity exercise : exercises) {
      if (exercise.getName().equals(name)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public ArrayList<NamedEntity> getFiltered(String filter) {
    return new ArrayList<>(getFilteredExercises(filter));
  }

  public ArrayList<NamedEntity> getFilteredExercises(String filter) {
    ArrayList<NamedEntity> filteredExercises = new ArrayList<>();
    for (NamedEntity exercise : exercises) {
      if (exercise.getName().contains(filter)) {
        filteredExercises.add(exercise);
      }
    }
    return filteredExercises;
  }

}
