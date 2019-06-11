package com.devcesar.workoutapp.utils;

import android.support.annotation.NonNull;

public class NamedEntity implements Comparable<NamedEntity> {

  private final String name;
  private final int id;

  public NamedEntity(String name, int id) {
    this.name = name;
    this.id = id;
  }

  public int getId() {
    return id;
  }

  @Override
  public int compareTo(@NonNull NamedEntity o) {
    return getName().compareTo(o.getName());
  }

  public String getName() {
    return name;
  }
}
