package com.devcesar.workoutapp.labs;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.devcesar.workoutapp.Category;
import com.devcesar.workoutapp.Exercise;
import com.devcesar.workoutapp.database.CategoryCursorWrapper;
import com.devcesar.workoutapp.database.DatabaseHelper;
import com.devcesar.workoutapp.database.DbSchema.CategoryTable;
import com.google.gson.Gson;
import java.util.ArrayList;

public class CategoryLab {

  private static CategoryLab categoryLab;
  private final SQLiteDatabase database;
  private ArrayList<Category> categories;

  private CategoryLab(Context context) {
    database = new DatabaseHelper(context.getApplicationContext()).getWritableDatabase();
    categories = new ArrayList<>();
    updateCategories();
  }

  public static CategoryLab get(Context context) {
    if (categoryLab == null) {
      categoryLab = new CategoryLab(context);
    }
    categoryLab.updateCategories();
    return categoryLab;
  }

  private void updateCategories() {
    categories.clear();
    CategoryCursorWrapper cursor = queryCategories(null, null);
    categories = new ArrayList<>();
    while (cursor.moveToNext()) {
      categories.add(cursor.getCategory());
    }
    cursor.close();
  }

  private CategoryCursorWrapper queryCategories(String whereClause, String[] whereArgs) {
    @SuppressLint("Recycle") Cursor cursor = database.query(
        CategoryTable.NAME,
        null,
        whereClause,
        whereArgs,
        null,
        null,
        null
    );
    return new CategoryCursorWrapper(cursor);
  }

  public void insertCategory(String categoryName) {
    ContentValues values = new ContentValues();
    values.put(CategoryTable.Cols.NAME, categoryName);
    values.put(CategoryTable.Cols.EXERCISES, new Gson().toJson(new ArrayList<Exercise>()));
    database.insert(CategoryTable.NAME, null, values);
    updateCategories();
  }

  public void deleteCategory(int categoryId) {
    String whereClause = CategoryTable._ID + "=?";
    String[] whereArgs = new String[]{String.valueOf(categoryId)};
    database.delete(CategoryTable.NAME, whereClause, whereArgs);
    updateCategories();
  }

  public void updateCategoryName(int categoryId, String newCategoryName) {
    ContentValues values = new ContentValues();
    values.put(CategoryTable.Cols.NAME, newCategoryName);
    String whereClause = CategoryTable._ID + "=?";
    String[] whereArgs = new String[]{String.valueOf(categoryId)};
    database.update(CategoryTable.NAME, values, whereClause, whereArgs);
    updateCategories();
  }

  public boolean contains(String categoryName) {
    for (Category category : categories) {
      if (category.getName().equals(categoryName)) {
        return true;
      }
    }
    return false;
  }

  public ArrayList<Category> getFilteredCategories(String filter) {
    ArrayList<Category> filteredCategories = new ArrayList<>();
    for (Category category : categories) {
      if (category.getName().contains(filter)) {
        filteredCategories.add(category);
      }
    }
    return filteredCategories;
  }

  public void deleteExerciseFromCategory(int categoryId, int exerciseId) {
    ArrayList<Exercise> exercises = getExercises(categoryId);
    for (int i = 0; i < exercises.size(); i++) {
      if (exercises.get(i).getId() == exerciseId) {
        exercises.remove(i);
        break;
      }
    }
    updateCategoryExercises(categoryId, exercises);
  }

  public ArrayList<Exercise> getExercises(int categoryId) {
    String whereClause = CategoryTable._ID + "=?";
    String[] whereArgs = new String[]{String.valueOf(categoryId)};
    CategoryCursorWrapper cursor = queryCategories(whereClause, whereArgs);

    cursor.moveToNext();
    ArrayList<Exercise> exercises = cursor.getExercises();
    cursor.close();

    return exercises;
  }

  public void updateCategoryExercises(int categoryId, ArrayList<Exercise> exercises) {
    ContentValues values = new ContentValues();
    values.put(CategoryTable.Cols.EXERCISES, new Gson().toJson(exercises));
    String whereClause = CategoryTable._ID + "=?";
    String[] whereArgs = new String[]{String.valueOf(categoryId)};
    database.update(CategoryTable.NAME, values, whereClause, whereArgs);
  }

}
