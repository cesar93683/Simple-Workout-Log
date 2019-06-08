package com.devcesar.workoutapp;

import android.support.annotation.NonNull;

public class Category implements Comparable<Category> {

  private final String categoryName;
  private final int categoryId;

  public Category(String categoryName, int categoryId) {
    this.categoryName = categoryName;
    this.categoryId = categoryId;
  }

  public int getCategoryId() {
    return categoryId;
  }

  @Override
  public int compareTo(@NonNull Category o) {
    return getCategoryName().compareTo(o.getCategoryName());
  }

  public String getCategoryName() {
    return categoryName;
  }
}
