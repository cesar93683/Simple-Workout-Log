package com.devcesar.workoutapp.labs;

import android.content.Context;
import com.devcesar.workoutapp.utils.NamedEntity;
import java.util.ArrayList;

public interface NamedEntityLab {

  void insert(String name);

  void delete(int id, Context context);

  void updateName(int id, String newName);

  boolean contains(String name);

  ArrayList<NamedEntity> getFiltered(String filter);

}
