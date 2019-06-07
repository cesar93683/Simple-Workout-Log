package com.devcesar.workoutapp;

import android.support.annotation.NonNull;

public class Exercise implements Comparable<Exercise> {

  private final String exerciseName;
  private final int exerciseId;

  public Exercise(String exerciseName, int exerciseId) {
    this.exerciseName = exerciseName;
    this.exerciseId = exerciseId;
  }

  public int getExerciseId() {
    return exerciseId;
  }

  @Override
  public int compareTo(@NonNull Exercise o) {
    return getExerciseName().compareTo(o.getExerciseName());
  }

  public String getExerciseName() {
    return exerciseName;
  }
}
