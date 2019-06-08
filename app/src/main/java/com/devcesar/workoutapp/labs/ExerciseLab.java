package com.devcesar.workoutapp.labs;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.devcesar.workoutapp.Utils.Exercise;
import com.devcesar.workoutapp.database.DatabaseHelper;
import com.devcesar.workoutapp.database.DbSchema.ExerciseTable;
import com.devcesar.workoutapp.database.ExerciseCursorWrapper;
import java.util.ArrayList;

public class ExerciseLab {

  private static ExerciseLab exerciseLab;
  private final SQLiteDatabase database;
  private final WorkoutLab workoutLab;
  private ArrayList<Exercise> exercises;

  private ExerciseLab(Context context) {
    database = new DatabaseHelper(context.getApplicationContext()).getWritableDatabase();
    exercises = new ArrayList<>();
    workoutLab = WorkoutLab.get(context);
    updateExercises();
  }

  private void updateExercises() {
    exercises.clear();
    ExerciseCursorWrapper cursor = queryExercises(null, null);
    exercises = new ArrayList<>();
    while (cursor.moveToNext()) {
      exercises.add(cursor.getExercise());
    }
    cursor.close();
  }

  private ExerciseCursorWrapper queryExercises(String whereClause, String[] whereArgs) {
    @SuppressLint("Recycle") Cursor cursor = database.query(
        ExerciseTable.NAME,
        null,
        whereClause,
        whereArgs,
        null,
        null,
        null
    );
    return new ExerciseCursorWrapper(cursor);
  }

  public static ExerciseLab get(Context context) {
    if (exerciseLab == null) {
      exerciseLab = new ExerciseLab(context);
    }
    exerciseLab.updateExercises();
    return exerciseLab;
  }

  public Exercise getExerciseById(int exerciseId) {
    for (Exercise exercise : exercises) {
      if (exercise.getId() == exerciseId) {
        return exercise;
      }
    }
    throw new RuntimeException("ERROR: exercise id does not exist");
  }

  public void insertExercise(String exerciseName) {
    ContentValues values = getContentValues(exerciseName);
    database.insert(ExerciseTable.NAME, null, values);
    updateExercises();
  }

  private ContentValues getContentValues(String exerciseName) {
    ContentValues values = new ContentValues();
    values.put(ExerciseTable.Cols.NAME, exerciseName);
    return values;
  }

  public void deleteExercise(int exerciseId) {
    String whereClause = ExerciseTable._ID + "=?";
    String[] whereArgs = new String[]{String.valueOf(exerciseId)};
    database.delete(ExerciseTable.NAME, whereClause, whereArgs);
    workoutLab.deleteWorkouts(exerciseId);
    updateExercises();
  }

  public void updateExercise(long exerciseId, String newExerciseName) {
    ContentValues values = getContentValues(newExerciseName);
    String whereClause = ExerciseTable._ID + "=?";
    String[] whereArgs = new String[]{String.valueOf(exerciseId)};
    database.update(ExerciseTable.NAME, values, whereClause, whereArgs);
    updateExercises();
  }

  public boolean contains(String exerciseName) {
    for (Exercise exercise : exercises) {
      if (exercise.getName().equals(exerciseName)) {
        return true;
      }
    }
    return false;
  }

  public ArrayList<Exercise> getFilteredExercise(String filter) {
    ArrayList<Exercise> filteredExercises = new ArrayList<>();
    for (Exercise exercise : exercises) {
      if (exercise.getName().contains(filter)) {
        filteredExercises.add(exercise);
      }
    }
    return filteredExercises;
  }
}
