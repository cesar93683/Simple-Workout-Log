package com.devcesar.workoutapp.database;

import android.content.Context;
import android.database.Cursor;
import com.devcesar.workoutapp.utils.NamedEntitiesUtils;
import com.devcesar.workoutapp.utils.NamedEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CategoryOrRoutineCursorWrapper extends NamedEntityCursorWrapper {

  private final String colExercises;

  public CategoryOrRoutineCursorWrapper(Cursor cursor, String colName, String colId,
      String colExercises) {
    super(cursor, colName, colId);
    this.colExercises = colExercises;
  }

  public List<NamedEntity> getExercises(Context context) {
    String exerciseString = getString(getColumnIndexOrThrow(colExercises));
    Type type = new TypeToken<ArrayList<Integer>>() {
    }.getType();
    ArrayList<Integer> exerciseIds = new Gson().fromJson(exerciseString, type);
    return NamedEntitiesUtils.getNamedEntities(exerciseIds, context);
  }

}
