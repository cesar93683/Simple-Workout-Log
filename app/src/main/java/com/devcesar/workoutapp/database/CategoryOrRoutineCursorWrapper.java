package com.devcesar.workoutapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.devcesar.workoutapp.utils.Exercise;
import com.devcesar.workoutapp.utils.NamedEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class CategoryOrRoutineCursorWrapper extends CursorWrapper {

  private final String colName;
  private final String colId;
  private final String colExercises;

  public CategoryOrRoutineCursorWrapper(Cursor cursor, String colName, String colId,
      String colExercises) {
    super(cursor);
    this.colName = colName;
    this.colId = colId;
    this.colExercises = colExercises;
  }

  public NamedEntity getNamedEntity() {
    String categoryName = getString(getColumnIndexOrThrow(colName));
    int categoryId = getInt(getColumnIndexOrThrow(colId));
    return new NamedEntity(categoryName, categoryId);
  }

  public ArrayList<Exercise> getExercises() {
    String exerciseString = getString(getColumnIndexOrThrow(colExercises));
    Type type = new TypeToken<ArrayList<Exercise>>() {
    }.getType();
    return new Gson().fromJson(exerciseString, type);
  }

}
