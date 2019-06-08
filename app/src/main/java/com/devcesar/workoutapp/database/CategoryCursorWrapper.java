package com.devcesar.workoutapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.devcesar.workoutapp.Utils.Category;
import com.devcesar.workoutapp.Utils.Exercise;
import com.devcesar.workoutapp.database.DbSchema.CategoryTable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class CategoryCursorWrapper extends CursorWrapper {

  public CategoryCursorWrapper(Cursor cursor) {
    super(cursor);
  }

  public Category getCategory() {
    String categoryName = getString(getColumnIndex(CategoryTable.Cols.NAME));
    int categoryId = getInt(getColumnIndex(CategoryTable._ID));
    return new Category(categoryName, categoryId);
  }

  public ArrayList<Exercise> getExercises() {
    String exerciseString = getString(getColumnIndex(CategoryTable.Cols.EXERCISES));
    Type type = new TypeToken<ArrayList<Exercise>>() {
    }.getType();
    return new Gson().fromJson(exerciseString, type);
  }

}
