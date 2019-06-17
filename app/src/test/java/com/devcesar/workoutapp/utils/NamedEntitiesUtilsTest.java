package com.devcesar.workoutapp.utils;

import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import org.junit.Test;

public class NamedEntitiesUtilsTest {

  @Test
  public void shouldReturnEmptyArrayWhenListEmpty() {
    ArrayList<NamedEntity> namedEntities = new ArrayList<>();
    assertArrayEquals(NamedEntitiesUtils.getIds(namedEntities).toArray(), new Integer[]{});
  }

  @Test
  public void shouldReturnIdsCorrectly() {
    Integer[] vals = new Integer[]{1, 2, 3, 4, 5};
    ArrayList<NamedEntity> namedEntities = new ArrayList<>();
    for (Integer val : vals) {
      namedEntities.add(new NamedEntity("asdf", val));
    }
    assertArrayEquals(NamedEntitiesUtils.getIds(namedEntities).toArray(), vals);
  }

}