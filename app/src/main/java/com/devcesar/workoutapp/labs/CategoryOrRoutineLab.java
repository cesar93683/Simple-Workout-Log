package com.devcesar.workoutapp.labs;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.devcesar.workoutapp.database.CategoryOrRoutineCursorWrapper;
import com.devcesar.workoutapp.database.DatabaseHelper;
import com.devcesar.workoutapp.database.DbSchema.CategoryTable;
import com.devcesar.workoutapp.database.DbSchema.RoutineTable;
import com.devcesar.workoutapp.utils.Exercise;
import com.devcesar.workoutapp.utils.NamedEntity;
import com.google.gson.Gson;
import java.util.ArrayList;

public class CategoryOrRoutineLab implements NamedEntityLab {

  private static CategoryOrRoutineLab categoryLab;
  private static CategoryOrRoutineLab routineLab;
  private final SQLiteDatabase database;
  private ArrayList<NamedEntity> namedEntities;
  private final String tableName;
  private final String colName;
  private final String colId;
  private final String colExercises;

  private CategoryOrRoutineLab(Context context, String tableName, String colName, String colId,
      String colExercises) {
    this.tableName = tableName;
    this.colName = colName;
    this.colId = colId;
    this.colExercises = colExercises;

    database = new DatabaseHelper(context.getApplicationContext()).getWritableDatabase();
    namedEntities = new ArrayList<>();
    updateNamedEntities();
  }

  private void updateNamedEntities() {
    namedEntities.clear();
    CategoryOrRoutineCursorWrapper cursor = queryNamedEntities(null, null);
    namedEntities = new ArrayList<>();
    while (cursor.moveToNext()) {
      namedEntities.add(cursor.getNamedEntity());
    }
    cursor.close();
  }

  private CategoryOrRoutineCursorWrapper queryNamedEntities(String whereClause,
      String[] whereArgs) {
    @SuppressLint("Recycle") Cursor cursor = database.query(
        tableName,
        null,
        whereClause,
        whereArgs,
        null,
        null,
        null
    );
    return new CategoryOrRoutineCursorWrapper(cursor, colName, colId, colExercises);
  }

  public static CategoryOrRoutineLab getCategoryLab(Context context) {
    if (categoryLab == null) {
      categoryLab = new CategoryOrRoutineLab(context, CategoryTable.NAME, CategoryTable.Cols.NAME,
          CategoryTable._ID, CategoryTable.Cols.EXERCISES);
    }
    return categoryLab;
  }

  public static CategoryOrRoutineLab getRoutineLab(Context context) {
    if (routineLab == null) {
      routineLab = new CategoryOrRoutineLab(context, RoutineTable.NAME, RoutineTable.Cols.NAME,
          RoutineTable._ID, RoutineTable.Cols.EXERCISES);
    }
    return routineLab;
  }

  @Override
  public void insert(String name) {
    ContentValues values = new ContentValues();
    values.put(colName, name);
    values.put(colExercises, new Gson().toJson(new ArrayList<Exercise>()));
    database.insert(tableName, null, values);
    updateNamedEntities();
  }

  @Override
  public void delete(int id) {
    String whereClause = colId + "=?";
    String[] whereArgs = new String[]{String.valueOf(id)};
    database.delete(tableName, whereClause, whereArgs);
    updateNamedEntities();
  }

  @Override
  public void updateName(int id, String newCategoryName) {
    ContentValues values = new ContentValues();
    values.put(colName, newCategoryName);
    String whereClause = colId + "=?";
    String[] whereArgs = new String[]{String.valueOf(id)};
    database.update(tableName, values, whereClause, whereArgs);
    updateNamedEntities();
  }

  @Override
  public boolean contains(String name) {
    for (NamedEntity category : namedEntities) {
      if (category.getName().equals(name)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public ArrayList<NamedEntity> getFiltered(String filter) {
    ArrayList<NamedEntity> filtered = new ArrayList<>();
    for (NamedEntity namedEntity : namedEntities) {
      if (namedEntity.getName().contains(filter)) {
        filtered.add(namedEntity);
      }
    }
    return filtered;
  }

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

  public ArrayList<Exercise> getExercises(int id) {
    String whereClause = colId + "=?";
    String[] whereArgs = new String[]{String.valueOf(id)};
    CategoryOrRoutineCursorWrapper cursor = queryNamedEntities(whereClause, whereArgs);

    cursor.moveToNext();
    ArrayList<Exercise> exercises = cursor.getExercises();
    cursor.close();

    return exercises;
  }

  public void updateExercises(int id, ArrayList<Exercise> exercises) {
    ContentValues values = new ContentValues();
    values.put(colExercises, new Gson().toJson(exercises));
    String whereClause = colId + "=?";
    String[] whereArgs = new String[]{String.valueOf(id)};
    database.update(tableName, values, whereClause, whereArgs);
  }

}
