package com.devcesar.workoutapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.devcesar.workoutapp.database.DbSchema.CategoryTable;
import com.devcesar.workoutapp.utils.Category;
import com.devcesar.workoutapp.utils.Exercise;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class CategoryCursorWrapper extends CursorWrapper {

  public CategoryCursorWrapper(Cursor cursor) {
    super(cursor);
  }

  public Category getCategory() {
    String categoryName = getString(getColumnIndexOrThrow(CategoryTable.Cols.NAME));
    int categoryId = getInt(getColumnIndexOrThrow(CategoryTable._ID));
    return new Category(categoryName, categoryId);
  }

  public ArrayList<Exercise> getExercises() {
    String exerciseString = getString(getColumnIndexOrThrow(CategoryTable.Cols.EXERCISES));
    Type type = new TypeToken<ArrayList<Exercise>>() {
    }.getType();
    return new Gson().fromJson(exerciseString, type);
  }

}
