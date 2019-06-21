package com.devcesar.workoutapp.labs;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;
import com.devcesar.workoutapp.database.DatabaseHelper;
import com.devcesar.workoutapp.database.DbSchema.ExerciseTable;
import com.devcesar.workoutapp.database.NamedEntityCursorWrapper;
import com.devcesar.workoutapp.utils.NamedEntity;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ExerciseLab implements NamedEntityLab {

  private static ExerciseLab exerciseLab;
  private final SQLiteDatabase database;
  private ArrayList<NamedEntity> exercises;

  private ExerciseLab(Context context) {
    database = new DatabaseHelper(context.getApplicationContext()).getWritableDatabase();
    exercises = new ArrayList<>();
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

  public List<NamedEntity> findExercises(ArrayList<Integer> ids) {
    List<NamedEntity> namedEntities = new LinkedList<>();
    for (int exerciseId : ids) {
      namedEntities.add(findExercise(exerciseId));
    }
    return namedEntities;
  }

  private NamedEntity findExercise(int id) {
    for (NamedEntity exercise : exercises) {
      if (exercise.getId() == id) {
        return exercise;
      }
    }
    throw new RuntimeException(String.format("ERROR: exercise id:%d does not exist", id));
  }

  @NonNull
  public List<NamedEntity> getExercises(ArrayList<String> exerciseNames) {
    List<NamedEntity> exercises = new ArrayList<>();
    for (String exerciseName : exerciseNames) {
      exercises.add(findExercise(exerciseName));
    }
    return exercises;
  }

  @Override
  public void insert(String name) {
    insertNoUpdate(name);
    updateExercises();
  }

  private void insertNoUpdate(String name) {
    ContentValues values = getContentValues(name);
    database.insert(ExerciseTable.NAME, null, values);
  }

  private ContentValues getContentValues(String name) {
    ContentValues values = new ContentValues();
    values.put(ExerciseTable.Cols.NAME, name);
    return values;
  }

  @Override
  public void delete(int id, Context context) {
    String whereClause = ExerciseTable._ID + "=?";
    String[] whereArgs = new String[]{String.valueOf(id)};
    database.delete(ExerciseTable.NAME, whereClause, whereArgs);
    WorkoutLab.get(context).deleteWorkouts(id);
    CategoryOrRoutineLab.getCategoryLab(context).deleteExerciseFromAll(id, context);
    CategoryOrRoutineLab.getRoutineLab(context).deleteExerciseFromAll(id, context);
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
    ArrayList<NamedEntity> filtered = new ArrayList<>();
    for (NamedEntity exercise : exercises) {
      if (exercise.getName().toLowerCase().contains(filter.toLowerCase())) {
        filtered.add(exercise);
      }
    }
    return filtered;
  }

  public void insertMultiple(ArrayList<String> exerciseNames) {
    for (String exerciseName : exerciseNames) {
      insertNoUpdate(exerciseName);
    }
    updateExercises();
  }

  private NamedEntity findExercise(String name) {
    for (NamedEntity exercise : exercises) {
      if (exercise.getName().equals(name)) {
        return exercise;
      }
    }
    throw new RuntimeException(String.format("ERROR: exercise name:%s does not exist", name));
  }

}
