package com.devcesar.workoutapp.utils;

import java.util.ArrayList;
import java.util.Collection;

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

}
