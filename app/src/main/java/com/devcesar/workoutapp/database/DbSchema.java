package com.devcesar.workoutapp.database;

import android.provider.BaseColumns;

public class DbSchema {

  public static final class ExerciseTable implements BaseColumns {

    public static final String NAME = "exercises";

    public static final class Cols {

      public static final String NAME = "name";
    }
  }

  public static final class WorkoutTable implements BaseColumns {

    public static final String NAME = "workouts";

    public static final class Cols {

      public static final String EXERCISE_ID = "exercise_id";
      public static final String TIME_STAMP = "time_stamp";
      public static final String EXERCISE_SETS = "exercise_sets";
    }
  }

  public static final class RoutineTable implements BaseColumns {

    public static final String NAME = "routines";

    public static final class Cols {

      public static final String NAME = "name";
      public static final String EXERCISES = "exercises";
    }
  }

  public static final class CategoryTable implements BaseColumns {

    public static final String NAME = "category";

    public static final class Cols {

      public static final String NAME = "name";
      public static final String EXERCISES = " exercises";
    }
  }

}
