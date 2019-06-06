package com.example.ceamaya.workoutapp.labs;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import com.example.ceamaya.workoutapp.ExerciseSet;
import com.example.ceamaya.workoutapp.Workout;
import com.example.ceamaya.workoutapp.database.DatabaseHelper;
import com.example.ceamaya.workoutapp.database.DbSchema.WorkoutTable;
import com.example.ceamaya.workoutapp.database.WorkoutCursorWrapper;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class WorkoutLab {

  private static WorkoutLab workoutLab;
  private final SQLiteDatabase database;

  private WorkoutLab(Context context) {
    database = new DatabaseHelper(context.getApplicationContext()).getWritableDatabase();
  }

  public static WorkoutLab get(Context context) {
    if (workoutLab == null) {
      workoutLab = new WorkoutLab(context);
    }
    return workoutLab;
  }

  public ArrayList<Workout> getWorkouts(int exerciseId) {
    String whereClause = WorkoutTable.Cols.EXERCISE_ID + "=?";
    String[] whereArgs = new String[]{String.valueOf(exerciseId)};
    WorkoutCursorWrapper cursor = queryExerciseSets(whereClause, whereArgs);

    HashSet<Long> uniqueTimeStamps = new HashSet<>();
    while (cursor.moveToNext()) {
      long timeStamp = cursor.getLong(cursor.getColumnIndex(WorkoutTable.Cols.TIME_STAMP));
      uniqueTimeStamps.add(timeStamp);
    }
    cursor.close();

    ArrayList<Workout> workouts = new ArrayList<>();
    for (long timestamp : uniqueTimeStamps) {
      workouts.add(getWorkout(exerciseId, timestamp));
    }
    Collections.sort(workouts);

    return workouts;
  }

  private WorkoutCursorWrapper queryExerciseSets(String whereClause, String[] whereArgs) {
    @SuppressLint("Recycle") Cursor cursor = database.query(
        WorkoutTable.NAME,
        null,
        whereClause,
        whereArgs,
        null,
        null,
        null
    );
    return new WorkoutCursorWrapper(cursor);
  }

  public Workout getWorkout(int exerciseId, long timeStamp) {
    String whereClause = String
        .format("%s=? AND %s=?", WorkoutTable.Cols.EXERCISE_ID, WorkoutTable.Cols.TIME_STAMP);
    String[] whereArgs = new String[]{String.valueOf(exerciseId), String.valueOf(timeStamp)};
    WorkoutCursorWrapper cursor = queryExerciseSets(whereClause, whereArgs);

    ArrayList<ExerciseSet> exerciseSets = new ArrayList<>();
    if (cursor.moveToNext()) {
      exerciseSets = cursor.getExerciseSets();
    }
    cursor.close();

    return new Workout(timeStamp, exerciseSets, exerciseId);
  }

  public void deleteExercise(int exerciseId) {
    String whereClause = WorkoutTable.Cols.EXERCISE_ID + "=?";
    String[] whereArgs = new String[]{String.valueOf(exerciseId)};
    database.delete(WorkoutTable.NAME, whereClause, whereArgs);
  }

  public void updateWorkout(Workout workout) {
    deleteWorkout(workout.getTimeStamp());
    insertWorkout(workout);
  }

  public void deleteWorkout(long time) {
    String whereClause = WorkoutTable.Cols.TIME_STAMP + "=?";
    String[] whereArgs = new String[]{String.valueOf(time)};
    database.delete(WorkoutTable.NAME, whereClause, whereArgs);
  }

  public void insertWorkout(Workout workout) {
    ArrayList<ExerciseSet> exerciseSets = workout.getExerciseSets();
    if (exerciseSets.size() == 0) {
      return;
    }
    ContentValues values = getContentValues(workout, exerciseSets);
    database.insert(WorkoutTable.NAME, null, values);
  }

  @NonNull
  private ContentValues getContentValues(Workout workout, ArrayList<ExerciseSet> exerciseSets) {
    ContentValues values = new ContentValues();
    String exerciseSetsString = new Gson().toJson(exerciseSets);
    values.put(WorkoutTable.Cols.EXERCISE_SETS, exerciseSetsString);
    values.put(WorkoutTable.Cols.EXERCISE_ID, exerciseSets.get(0).getExerciseId());
    values.put(WorkoutTable.Cols.TIME_STAMP, workout.getTimeStamp());
    return values;
  }

}

