package com.devcesar.workoutapp.labs;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.devcesar.workoutapp.database.CategoryCursorWrapper;
import com.devcesar.workoutapp.database.DatabaseHelper;
import com.devcesar.workoutapp.database.DbSchema.CategoryTable;
import com.devcesar.workoutapp.utils.Category;
import com.devcesar.workoutapp.utils.Exercise;
import com.devcesar.workoutapp.utils.NamedEntity;
import com.google.gson.Gson;
import java.util.ArrayList;

public class CategoryLab implements NamedEntityLab, ContainsExercisesLab {

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

  @Override
  public void insert(String name) {
    ContentValues values = new ContentValues();
    values.put(CategoryTable.Cols.NAME, name);
    values.put(CategoryTable.Cols.EXERCISES, new Gson().toJson(new ArrayList<Exercise>()));
    database.insert(CategoryTable.NAME, null, values);
    updateCategories();
  }

  @Override
  public void delete(int id) {
    String whereClause = CategoryTable._ID + "=?";
    String[] whereArgs = new String[]{String.valueOf(id)};
    database.delete(CategoryTable.NAME, whereClause, whereArgs);
    updateCategories();
  }

  @Override
  public void updateName(int id, String newCategoryName) {
    ContentValues values = new ContentValues();
    values.put(CategoryTable.Cols.NAME, newCategoryName);
    String whereClause = CategoryTable._ID + "=?";
    String[] whereArgs = new String[]{String.valueOf(id)};
    database.update(CategoryTable.NAME, values, whereClause, whereArgs);
    updateCategories();
  }

  @Override
  public boolean contains(String name) {
    for (Category category : categories) {
      if (category.getName().equals(name)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public ArrayList<NamedEntity> getFiltered(String filter) {
    ArrayList<NamedEntity> filteredCategories = new ArrayList<>();
    for (Category category : categories) {
      if (category.getName().contains(filter)) {
        filteredCategories.add(category);
      }
    }
    return filteredCategories;
  }

  @Override
  public void deleteExercise(int id, int exerciseId) {
    ArrayList<Exercise> exercises = getExercises(id);
    for (int i = 0; i < exercises.size(); i++) {
      if (exercises.get(i).getId() == exerciseId) {
        exercises.remove(i);
        break;
      }
    }
    updateExercises(id, exercises);
  }

  @Override
  public ArrayList<Exercise> getExercises(int id) {
    String whereClause = CategoryTable._ID + "=?";
    String[] whereArgs = new String[]{String.valueOf(id)};
    CategoryCursorWrapper cursor = queryCategories(whereClause, whereArgs);

    cursor.moveToNext();
    ArrayList<Exercise> exercises = cursor.getExercises();
    cursor.close();

    return exercises;
  }

  @Override
  public void updateExercises(int id, ArrayList<Exercise> exercises) {
    ContentValues values = new ContentValues();
    values.put(CategoryTable.Cols.EXERCISES, new Gson().toJson(exercises));
    String whereClause = CategoryTable._ID + "=?";
    String[] whereArgs = new String[]{String.valueOf(id)};
    database.update(CategoryTable.NAME, values, whereClause, whereArgs);
  }

}
