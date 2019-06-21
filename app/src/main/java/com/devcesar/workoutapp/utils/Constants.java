package com.devcesar.workoutapp.utils;

public class Constants {

  public static final String START_TIME = "START_TIME";
  public static final int DEFAULT_START_TIME = 120;

  private Constants() {
    throw new AssertionError();
  }
  public static final int TYPE_CATEGORY = 1;
  public static final int TYPE_ROUTINE = 2;
  public static final int TYPE_EXERCISE = 3;
  public static final String TYPE = "TYPE";
  public static final String SHOULD_AUTO_START_TIMER = "SHOULD_AUTO_START_TIMER";
}
