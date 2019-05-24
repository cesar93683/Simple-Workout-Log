package com.example.ceamaya.workoutapp.Database;

import android.provider.BaseColumns;

class ExerciseContract {
    private ExerciseContract() {
    }

    static final class ExerciseEntry implements BaseColumns {
        static final String EXERCISE_TABLE_NAME = "exercise_list";
        static final String COLUMN_NAME = "name";
    }

    static final class SetEntry implements BaseColumns {
        static final String SET_TABLE_NAME = "set_list";
        static final String COLUMN_EXERCISE_ID = "exercise_id";
        static final String COLUMN_SET_NUMBER = "set_number";
        static final String COLUMN_REPS = "reps";
        static final String COLUMN_WEIGHT = "weight";
        static final String COLUMN_TIMESTAMP = "timestamp";
    }

    static final class RoutineEntry implements BaseColumns {
        static final String ROUTINE_TABLE_NAME = "routine_list";
        static final String COLUMN_NAME = "name";
    }

    static final class RoutineExercisePairEntry implements BaseColumns {
        static final String ROUTINE_EXERCISE_PAIR_TABLE_NAME = "routine_exercise_pair";
        static final String COLUMN_ROUTINE_ID = "routine_id";
        static final String COLUMN_EXERCISE_ID = "exercise_id";
    }
}
