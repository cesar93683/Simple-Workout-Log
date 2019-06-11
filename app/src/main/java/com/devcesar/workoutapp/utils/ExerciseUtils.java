package com.devcesar.workoutapp.utils;

import android.content.Context;
import com.devcesar.workoutapp.labs.ExerciseLab;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ExerciseUtils {
  private ExerciseUtils() {
    throw new AssertionError();
  }

  public static int[] getExerciseIds(Collection<Exercise> exercises) {
    int[] exerciseIds = new int[exercises.size()];
    int i = 0;
    for (Exercise exercise : exercises) {
      exerciseIds[i++] = exercise.getId();
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
