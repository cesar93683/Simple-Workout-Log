package com.devcesar.workoutapp.Utils;

import android.annotation.SuppressLint;

public class ExerciseSet {

  private int reps;
  private int weight;
  private int setNumber;

  public ExerciseSet(int reps, int weight, int setNumber) {
    this.reps = reps;
    this.weight = weight;
    this.setNumber = setNumber;
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
