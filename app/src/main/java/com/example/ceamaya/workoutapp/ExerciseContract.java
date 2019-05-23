package com.example.ceamaya.workoutapp;

import android.provider.BaseColumns;

class ExerciseContract {
    private ExerciseContract() {
    }

    static final class ExerciseEntry implements BaseColumns {
        static final String EXERCISE_TABLE_NAME = "exerciseList";
        static final String COLUMN_NAME = "name";
    }

    static final class RoutineEntry implements BaseColumns {
        static final String ROUTINE_TABLE_NAME = "routineList";
        static final String COLUMN_NAME = "name";
        static final String COLUMN_EXERCISE_IDS = "exercise_ids";
    }
}
