package com.devcesar.workoutapp.labs;

import com.devcesar.workoutapp.utils.Exercise;
import java.util.ArrayList;

public interface ContainsExercisesLab {

  void deleteExercise(int id, int exerciseId);

  ArrayList<Exercise> getExercises(int id);

  void updateExercises(int id, ArrayList<Exercise> exercises);

}
