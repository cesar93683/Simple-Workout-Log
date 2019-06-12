package com.devcesar.workoutapp.utils;

import android.content.Context;
import com.devcesar.workoutapp.labs.ExerciseLab;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class NamedEntitiesUtils {

  private NamedEntitiesUtils() {
    throw new AssertionError();
  }

  public static ArrayList<Integer> getIds(Collection<NamedEntity> namedEntities) {
    ArrayList<Integer> ids = new ArrayList<>(namedEntities.size());
    for (NamedEntity namedEntity : namedEntities) {
      ids.add(namedEntity.getId());
    }
    return ids;
  }

  public static List<NamedEntity> getNamedEntities(ArrayList<Integer> ids, Context context) {
    List<NamedEntity> namedEntities = new LinkedList<>();
    for (int exerciseId : ids) {
      namedEntities.add(ExerciseLab.get(context).getExerciseById(exerciseId));
    }
    return namedEntities;
  }

}
