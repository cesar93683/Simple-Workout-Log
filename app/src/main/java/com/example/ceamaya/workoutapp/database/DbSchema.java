package com.example.ceamaya.workoutapp.database;

import android.provider.BaseColumns;

public class DbSchema {
    public static final class ExerciseTable implements BaseColumns {
        public static final String NAME = "exercises";

        public static final class Cols {
            public static final String NAME = "name";
        }
    }

    public static final class ExerciseSetTable implements BaseColumns {
        public static final String NAME = "exercise_sets";

        public static final class Cols {
            public static final String EXERCISE_ID = "exercise_id";
            public static final String SET_NUMBER = "set_number";
            public static final String REPS = "reps";
            public static final String WEIGHT = "weight";
            public static final String TIME_STAMP = "time_stamp";
        }
    }

    public static final class RoutineTable implements BaseColumns {
        public static final String NAME = "routines";

        public static final class Cols {
            public static final String NAME = "name";
        }
    }

    public static final class RoutineExerciseTable implements BaseColumns {
        public static final String NAME = "routine_exercise";

        public static final class Cols {
            public static final String ROUTINE_ID = "routine_id";
            public static final String EXERCISE_ID = "exercise_id";
        }
    }
}
