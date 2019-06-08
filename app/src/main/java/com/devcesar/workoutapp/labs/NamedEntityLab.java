package com.devcesar.workoutapp.labs;

import com.devcesar.workoutapp.utils.NamedEntity;
import java.util.ArrayList;

public interface NamedEntityLab {

  void insert(String name);

  void delete(int id);

  void updateName(int id, String newName);

  boolean contains(String name);

  ArrayList<NamedEntity> getFiltered(String filter);


}
