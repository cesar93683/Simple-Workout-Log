package com.devcesar.workoutapp.utils;

import android.content.Context;
import com.devcesar.workoutapp.labs.ExerciseLab;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class ExerciseUtils {
  private ExerciseUtils() {
    throw new AssertionError();
  }

  public static ArrayList<Integer> getExerciseIds(Collection<Exercise> exercises) {
    ArrayList<Integer> exerciseIds = new ArrayList<>(exercises.size());
    for (Exercise exercise : exercises) {
      exerciseIds.add(exercise.getId());
    }
    return exerciseIds;
  }

  public static List<Exercise> getExercises(ArrayList<Integer> exerciseIds, Context context) {
    List<Exercise> exercises = new LinkedList<>();
    for (int exerciseId : exerciseIds) {
      exercises.add(ExerciseLab.get(context).getExerciseById(exerciseId));
    }
    return exercises;
  }

}
