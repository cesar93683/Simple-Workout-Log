package com.devcesar.workoutapp.utils;

import android.content.Context;
import com.devcesar.workoutapp.labs.ExerciseLab;
import java.util.LinkedList;
import java.util.List;

public class ExerciseUtils {

  private ExerciseUtils() {
    throw new AssertionError();
  }

  public static int[] getExerciseIds(List<Exercise> exercises) {
    int[] exerciseIds = new int[exercises.size()];
    for (int i = 0; i < exercises.size(); i++) {
      exerciseIds[i] = exercises.get(i).getId();
    }
    return exerciseIds;
  }

  public static List<Exercise> getExercises(int[] exerciseIds, Context context) {
    List<Exercise> exercises = new LinkedList<>();
    for (int exerciseId : exerciseIds) {
      exercises.add(ExerciseLab.get(context).getExerciseById(exerciseId));
    }
    return exercises;
  }
}
