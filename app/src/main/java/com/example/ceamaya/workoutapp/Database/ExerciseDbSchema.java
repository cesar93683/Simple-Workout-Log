package com.example.ceamaya.workoutapp.Database;

import android.provider.BaseColumns;

class ExerciseDbSchema {
    static final class ExerciseTable implements BaseColumns {
        static final String NAME = "exercises";
        static final class Cols {
            static final String NAME = "name";
        }
    }

    static final class ExerciseSetTable implements BaseColumns {
        static final String NAME = "exercise_sets";
        static final class Cols {
            static final String EXERCISE_ID = "exercise_id";
            static final String SET_NUMBER = "set_number";
            static final String REPS = "reps";
            static final String WEIGHT = "weight";
            static final String DATE = "date";
        }
    }
}
