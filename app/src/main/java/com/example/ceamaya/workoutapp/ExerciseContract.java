package com.example.ceamaya.workoutapp;

import android.provider.BaseColumns;

class ExerciseContract {
    private ExerciseContract() {}

    public static final class ExerciseEntry implements BaseColumns {
        public static final String TABLE_NAME = "exerciseList";
        public static final String COLUMN_NAME = "name";
    }
}
