package com.example.ceamaya.workoutapp;

import android.provider.BaseColumns;

class ExerciseContract {
    private ExerciseContract() {
    }

    static final class ExerciseEntry implements BaseColumns {
        static final String EXERCISE_TABLE_NAME = "exerciseList";
        static final String COLUMN_NAME = "name";
    }

    static final class SetEntry implements BaseColumns {
        static final String SET_TABLE_NAME = "setList";
        static final String COLUMN_EXERCISEID = "exerciseId";
        static final String COLUMN_REPS = "reps";
        static final String COLUMN_WEIGHT = "weight";
        static final String COLUMN_TIMESTAMP = "timestamp";
    }

    static final class RoutineEntry implements BaseColumns {
        static final String ROUTINE_TABLE_NAME = "routineList";
        static final String COLUMN_NAME = "name";
    }

    static final class RoutineExercisePairEntry implements BaseColumns {
        static final String ROUTINEEXERCISEPAIR_TABLE_NAME = "routineExercisePair";
        static final String COLUMN_ROUTINEID = "routineId";
        static final String COLUMN_EXERCISEID = "exerciseId";
    }
}
