package com.devcesar.workoutapp.database;

import android.content.Context;
import android.database.Cursor;
import com.devcesar.workoutapp.labs.ExerciseLab;
import com.devcesar.workoutapp.utils.NamedEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CategoryOrRoutineCursorWrapper extends NamedEntityCursorWrapper {

  private final String colExerciseIds;

  public CategoryOrRoutineCursorWrapper(Cursor cursor, String colName, String colId,
      String colExerciseIds) {
    super(cursor, colName, colId);
    this.colExerciseIds = colExerciseIds;
  }

  public List<NamedEntity> getExercises(Context context) {
    String exerciseIdsString = getString(getColumnIndexOrThrow(colExerciseIds));
    Type type = new TypeToken<ArrayList<Integer>>() {
    }.getType();
    ArrayList<Integer> exerciseIds = new Gson().fromJson(exerciseIdsString, type);
    return ExerciseLab.get(context).findExercises(exerciseIds);
  }

}
