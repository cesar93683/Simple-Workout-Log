package com.example.ceamaya.workoutapp;

import android.annotation.SuppressLint;

public class ExerciseSet {

  private final int exerciseId;
  private int reps;
  private int weight;
  private int setNumber;
  private long timeStamp;

  public ExerciseSet(int reps, int weight, int exerciseId, int setNumber) {
    this.reps = reps;
    this.weight = weight;
    this.exerciseId = exerciseId;
    this.setNumber = setNumber;
  }

  public long getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(long timeStamp) {
    this.timeStamp = timeStamp;
  }

  public int getReps() {
    return reps;
  }

  public void setReps(int reps) {
    this.reps = reps;
  }

  public int getWeight() {
    return weight;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }

  public int getExerciseId() {
    return exerciseId;
  }

  public int getSetNumber() {
    return setNumber;
  }

  public void setSetNumber(int setNumber) {
    this.setNumber = setNumber;
  }

  @SuppressLint("DefaultLocale")
  @Override
  public String toString() {
    return String.format("Set %d - %d Reps @ %s LB", setNumber, reps, weight > 0 ? weight : "-");
  }
}
