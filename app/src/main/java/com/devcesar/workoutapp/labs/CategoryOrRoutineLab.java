package com.devcesar.workoutapp.labs;

import static com.devcesar.workoutapp.utils.ExerciseUtils.getExerciseIds;

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
import java.util.List;

public class CategoryOrRoutineLab implements NamedEntityLab {

  private static CategoryOrRoutineLab categoryLab;
  private static CategoryOrRoutineLab routineLab;
  private final SQLiteDatabase database;
  private final String tableName;
  private final String colName;
  private final String colId;
  private final String colExercises;
  private ArrayList<NamedEntity> namedEntities;

  private CategoryOrRoutineLab(Context context, String tableName, String colName,
      String colExercises) {
    this.tableName = tableName;
    this.colName = colName;
    this.colId = android.provider.BaseColumns._ID;
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
          CategoryTable.Cols.EXERCISES_ID);
    }
    return categoryLab;
  }

  public static CategoryOrRoutineLab getRoutineLab(Context context) {
    if (routineLab == null) {
      routineLab = new CategoryOrRoutineLab(context, RoutineTable.NAME, RoutineTable.Cols.NAME,
          RoutineTable.Cols.EXERCISES_ID);
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

  public void deleteExercise(int id, int exerciseId, Context context) {
    List<Exercise> exercises = getExercises(id, context);
    for (int i = 0; i < exercises.size(); i++) {
      if (exercises.get(i).getId() == exerciseId) {
        exercises.remove(i);
        break;
      }
    }
    updateExercises(id, exercises);
  }

  public List<Exercise> getExercises(int id, Context context) {
    String whereClause = colId + "=?";
    String[] whereArgs = new String[]{String.valueOf(id)};
    CategoryOrRoutineCursorWrapper cursor = queryNamedEntities(whereClause, whereArgs);

    cursor.moveToNext();
    List<Exercise> exercises = cursor.getExercises(context);
    cursor.close();

    return exercises;
  }

  public void updateExercises(int id, List<Exercise> exercises) {
    ContentValues values = new ContentValues();
    values.put(colExercises, new Gson().toJson(getExerciseIds(exercises)));
    String whereClause = colId + "=?";
    String[] whereArgs = new String[]{String.valueOf(id)};
    database.update(tableName, values, whereClause, whereArgs);
  }

}
