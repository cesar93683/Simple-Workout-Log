package com.example.ceamaya.workoutapp.labs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ceamaya.workoutapp.Routine;
import com.example.ceamaya.workoutapp.database.DatabaseHelper;
import com.example.ceamaya.workoutapp.database.DbSchema.RoutineTable;
import com.example.ceamaya.workoutapp.database.RoutineCursorWrapper;

import java.util.ArrayList;

public class RoutineLab {

    private static RoutineLab routineLab;
    private final SQLiteDatabase database;
    private ArrayList<Routine> routines;

    private RoutineLab(Context context) {
        database = new DatabaseHelper(context.getApplicationContext()).getWritableDatabase();
        routines = new ArrayList<>();
        updateRoutines();
    }

    private void updateRoutines() {
        routines.clear();
        RoutineCursorWrapper cursor = queryRoutines(null, null);
        routines = new ArrayList<>();
        while (cursor.moveToNext()) {
            routines.add(cursor.getRoutines());
        }
        cursor.close();
    }

    private RoutineCursorWrapper queryRoutines(String whereClause, String[] whereArgs) {
        Cursor cursor = database.query(
                RoutineTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new RoutineCursorWrapper(cursor);
    }

    public static RoutineLab get(Context context) {
        if (routineLab == null) {
            routineLab = new RoutineLab(context);
        }
        routineLab.updateRoutines();
        return routineLab;
    }

    public void insertRoutine(String routineName) {
        ContentValues values = getContentValues(routineName);
        database.insert(RoutineTable.NAME, null, values);
        updateRoutines();
    }

    private static ContentValues getContentValues(String routineName) {
        ContentValues values = new ContentValues();
        values.put(RoutineTable.Cols.NAME, routineName);
        return values;
    }

    public void deleteRoutine(long id) {
        String[] whereArgs = new String[]{String.valueOf(id)};
        database.delete(RoutineTable.NAME, RoutineTable._ID + "=?", whereArgs);
        updateRoutines();
    }

    public void updateRoutine(long id, String newRoutineName) {
        ContentValues values = getContentValues(newRoutineName);
        String whereClause = RoutineTable._ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        database.update(RoutineTable.NAME, values, whereClause, whereArgs);
        updateRoutines();
    }

    public boolean contains(String routineName) {
        for (Routine routine : routines) {
            if (routine.getRoutineName().equals(routineName)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Routine> getFilteredRoutines(String filter) {
        ArrayList<Routine> filteredRoutines = new ArrayList<>();
        for (Routine routine : routines) {
            if (routine.getRoutineName().contains(filter)) {
                filteredRoutines.add(routine);
            }
        }
        return filteredRoutines;
    }
}
