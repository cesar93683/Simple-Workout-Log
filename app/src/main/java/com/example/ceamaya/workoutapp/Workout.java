package com.example.ceamaya.workoutapp;

import android.support.annotation.NonNull;
import java.util.ArrayList;

public class Workout implements Comparable<Workout> {

  private final ArrayList<ExerciseSet> exerciseSets;
  private final long timeStamp;
  private final int exerciseId;

  public Workout(int exerciseId, ArrayList<ExerciseSet> exerciseSets, long timeStamp) {
    this.timeStamp = timeStamp;
    this.exerciseSets = exerciseSets;
    this.exerciseId = exerciseId;
  }

  public ArrayList<ExerciseSet> getExerciseSets() {
    return exerciseSets;
  }

  @Override
  public int compareTo(@NonNull Workout o) {
    return (int) (o.getTimeStamp() - getTimeStamp());
  }

  public long getTimeStamp() {
    return timeStamp;
  }

  public int getExerciseId() {
    return exerciseId;
  }
}
