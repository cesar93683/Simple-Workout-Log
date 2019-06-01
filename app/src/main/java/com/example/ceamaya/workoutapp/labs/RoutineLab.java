package com.example.ceamaya.workoutapp.labs;

import android.annotation.SuppressLint;
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
    private final RoutineExerciseLab routineExerciseLab;
    private ArrayList<Routine> routines;

    private RoutineLab(Context context) {
        database = new DatabaseHelper(context.getApplicationContext()).getWritableDatabase();
        routines = new ArrayList<>();
        routineExerciseLab = RoutineExerciseLab.get(context);
        updateRoutines();
    }

    private void updateRoutines() {
        routines.clear();
        RoutineCursorWrapper cursor = queryRoutines(null, null);
        routines = new ArrayList<>();
        while (cursor.moveToNext()) {
            routines.add(cursor.getRoutine());
        }
        cursor.close();
    }

    private RoutineCursorWrapper queryRoutines(String whereClause, String[] whereArgs) {
        @SuppressLint("Recycle") Cursor cursor = database.query(
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

    private ContentValues getContentValues(String routineName) {
        ContentValues values = new ContentValues();
        values.put(RoutineTable.Cols.NAME, routineName);
        return values;
    }

    public void deleteRoutine(int routineId) {
        String whereClause = RoutineTable._ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(routineId)};
        database.delete(RoutineTable.NAME, whereClause, whereArgs);
        routineExerciseLab.deleteRoutineExercise(routineId);
        updateRoutines();
    }

    public void updateRoutine(int routineId, String newRoutineName) {
        ContentValues values = getContentValues(newRoutineName);
        String whereClause = RoutineTable._ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(routineId)};
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
