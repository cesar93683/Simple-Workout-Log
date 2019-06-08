package com.devcesar.workoutapp.labs;

import com.devcesar.workoutapp.Utils.Exercise;
import com.devcesar.workoutapp.Utils.NamedEntity;
import java.util.ArrayList;

public interface NamedEntityLab {

  void insert(String name);

  void delete(int id);

  void updateName(int id, String newName);

  boolean contains(String name);

  ArrayList<NamedEntity> getFiltered(String filter);

  void deleteExercise(int id, int exerciseId);

  ArrayList<Exercise> getExercises(int id);

  void updateExercises(int id, ArrayList<Exercise> exercises);

}
