package com.devcesar.workoutapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.devcesar.workoutapp.utils.NamedEntity;

public class NamedEntityCursorWrapper extends CursorWrapper {

  private final String colName;
  private final String colId;

  public NamedEntityCursorWrapper(Cursor cursor, String colName, String colId) {
    super(cursor);
    this.colName = colName;
    this.colId = colId;
  }

  public NamedEntity getNamedEntity() {
    String name = getString(getColumnIndexOrThrow(colName));
    int id = getInt(getColumnIndexOrThrow(colId));
    return new NamedEntity(name, id);
  }
}
