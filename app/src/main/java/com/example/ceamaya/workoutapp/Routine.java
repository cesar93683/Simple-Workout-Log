package com.example.ceamaya.workoutapp;

import android.support.annotation.NonNull;

public class Routine implements Comparable<Routine> {

  private final String routineName;
  private final int routineId;

  public Routine(String routineName, int routineId) {
    this.routineName = routineName;
    this.routineId = routineId;
  }

  public int getRoutineId() {
    return routineId;
  }

  @Override
  public int compareTo(@NonNull Routine o) {
    return getRoutineName().compareTo(o.getRoutineName());
  }

  public String getRoutineName() {
    return routineName;
  }
}
