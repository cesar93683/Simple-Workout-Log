package com.devcesar.workoutapp.labs;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import com.devcesar.workoutapp.database.DatabaseHelper;
import com.devcesar.workoutapp.database.DbSchema.WorkoutTable;
import com.devcesar.workoutapp.database.WorkoutCursorWrapper;
import com.devcesar.workoutapp.utils.ExerciseSet;
import com.devcesar.workoutapp.utils.Workout;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collections;

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
    WorkoutCursorWrapper cursor = queryWorkout(whereClause, whereArgs);

    ArrayList<Workout> workouts = new ArrayList<>();
    while (cursor.moveToNext()) {
      workouts.add(cursor.getWorkout());
    }
    cursor.close();
    Collections.sort(workouts);

    return workouts;
  }

  private WorkoutCursorWrapper queryWorkout(String whereClause, String[] whereArgs) {
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
    WorkoutCursorWrapper cursor = queryWorkout(whereClause, whereArgs);

    if (!cursor.moveToNext()) {
      return new Workout(exerciseId, new ArrayList<>(), timeStamp);
    }
    Workout workout = cursor.getWorkout();
    cursor.close();

    return workout;
  }

  public void updateWorkout(Workout workout) {
    deleteWorkout(workout.getExerciseId(), workout.getTimeStamp());
    insertWorkout(workout);
  }

  public void deleteWorkout(int exerciseId, long timeStamp) {
    String whereClause = String
        .format("%s=? AND %s=?", WorkoutTable.Cols.EXERCISE_ID, WorkoutTable.Cols.TIME_STAMP);
    String[] whereArgs = new String[]{String.valueOf(exerciseId), String.valueOf(timeStamp)};
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
    values.put(WorkoutTable.Cols.EXERCISE_ID, workout.getExerciseId());
    values.put(WorkoutTable.Cols.TIME_STAMP, workout.getTimeStamp());
    return values;
  }

  void deleteWorkouts(int exerciseId) {
    String whereClause = WorkoutTable.Cols.EXERCISE_ID + "=?";
    String[] whereArgs = new String[]{String.valueOf(exerciseId)};
    database.delete(WorkoutTable.NAME, whereClause, whereArgs);
  }

}

