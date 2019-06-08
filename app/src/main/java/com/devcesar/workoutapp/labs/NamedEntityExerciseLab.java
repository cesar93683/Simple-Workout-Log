package com.devcesar.workoutapp.labs;

import com.devcesar.workoutapp.Utils.Exercise;
import com.devcesar.workoutapp.Utils.NamedEntity;
import java.util.ArrayList;

public interface NamedEntityExerciseLab {

  void deleteExercise(int id, int exerciseId);

  ArrayList<Exercise> getExercises(int id);

  void updateExercises(int id, ArrayList<Exercise> exercises);

}
