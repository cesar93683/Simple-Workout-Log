package com.devcesar.workoutapp.database;

import android.app.Activity;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.labs.CategoryOrRoutineLab;
import com.devcesar.workoutapp.labs.ExerciseLab;
import com.devcesar.workoutapp.utils.NamedEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InitDatabase {

  private final String barbellBenchPress;
  private final String barbellBenchPressIncline;
  private final String dumbbellPress;
  private final String dumbbellPressIncline;
  private final String dumbbellChestFly;
  private final String barbellBackSquat;
  private final String barbellLunge;
  private final String barbellFrontSquat;
  private final String romanianDeadlift;
  private final String legPressMachine;
  private final String barbellCurl;
  private final String eZBarBicepsCurl;
  private final String alternatingDumbbellCurl;
  private final String hammerDumbbellCurl;
  private final String standingCableCurl;
  private final String benchPressCloseGrip;
  private final String dumbbellOverheadTricepsPress;
  private final String ezBarSkullCrushers;
  private final String dip;
  private final String tricepsPushdown;
  private final String deadlift;
  private final String barbellRow;
  private final String dumbbellRow;
  private final String tBarRow;
  private final String latPulldown;
  private final String dumbbellFrontRaise;
  private final String overheadPress;
  private final String dumbbellSideLateralRaise;
  private final String seatedDumbbellPress;
  private final String dumbbellInclineRow;
  private final String chest;
  private final String legs;
  private final String biceps;
  private final String triceps;
  private final String back;
  private final String strong5x5WorkoutA;
  private final String strong5x5WorkoutB;
  private final String shoulders;
  private final ExerciseLab exerciseLab;
  private final CategoryOrRoutineLab categoryLab;
  private final CategoryOrRoutineLab routineLab;

  private InitDatabase(Activity activity) {
    exerciseLab = ExerciseLab.get(activity);
    categoryLab = CategoryOrRoutineLab.getCategoryLab(activity);
    routineLab = CategoryOrRoutineLab.getRoutineLab(activity);

    barbellBenchPress = activity.getString(R.string.barbell_bench_press);
    barbellBenchPressIncline = activity.getString(R.string.barbell_bench_press_incline);
    dumbbellPress = activity.getString(R.string.dumbbell_press);
    dumbbellPressIncline = activity.getString(R.string.dumbbell_press_incline);
    dumbbellChestFly = activity.getString(R.string.dumbbell_chest_fly);

    barbellBackSquat = activity.getString(R.string.barbell_back_squat);
    barbellFrontSquat = activity.getString(R.string.barbell_front_squat);
    barbellLunge = activity.getString(R.string.barbell_lunge);
    romanianDeadlift = activity.getString(R.string.romanian_deadlift);
    legPressMachine = activity.getString(R.string.leg_press_machine);

    barbellCurl = activity.getString(R.string.barbell_curl);
    eZBarBicepsCurl = activity.getString(R.string.ez_bar_biceps_curl);
    alternatingDumbbellCurl = activity.getString(R.string.alternating_dumbbell_curl);
    hammerDumbbellCurl = activity.getString(R.string.hammer_dumbbell_curl);
    standingCableCurl = activity.getString(R.string.standing_cable_curl);

    benchPressCloseGrip = activity.getString(R.string.bench_press_close_grip);
    dumbbellOverheadTricepsPress = activity.getString(R.string.dumbbell_overhead_triceps_press);
    ezBarSkullCrushers = activity.getString(R.string.ez_bar_skull_crushers);
    dip = activity.getString(R.string.dip);
    tricepsPushdown = activity.getString(R.string.triceps_pushdown);

    deadlift = activity.getString(R.string.deadlift);
    barbellRow = activity.getString(R.string.barbell_row);
    dumbbellRow = activity.getString(R.string.dumbbellRow);
    tBarRow = activity.getString(R.string.t_bar_row);
    latPulldown = activity.getString(R.string.lat_pulldown);

    overheadPress = activity.getString(R.string.overhead_press);
    dumbbellFrontRaise = activity.getString(R.string.dumbbell_front_raise);
    dumbbellSideLateralRaise = activity.getString(R.string.dumbbell_side_lateral_raise);
    seatedDumbbellPress = activity.getString(R.string.seated_dumbbell_press);
    dumbbellInclineRow = activity.getString(R.string.dumbbell_incline_row);

    chest = activity.getString(R.string.chest);
    legs = activity.getString(R.string.legs);
    biceps = activity.getString(R.string.biceps);
    triceps = activity.getString(R.string.triceps);
    back = activity.getString(R.string.back);
    shoulders = activity.getString(R.string.shoulders);
    initCategories();

    strong5x5WorkoutA = activity.getString(R.string.strong_5x5_workout_a);
    strong5x5WorkoutB = activity.getString(R.string.strong_5x5_workout_b);
    initRoutines();
  }

  private void initCategories() {
    ArrayList<String> chestExercises = new ArrayList<>(Arrays.asList(
        barbellBenchPress, barbellBenchPressIncline, dumbbellPress, dumbbellPressIncline,
        dumbbellChestFly));
    initExercises(chestExercises);
    initCategory(chest, chestExercises);

    ArrayList<String> legExercises = new ArrayList<>(Arrays.asList(
        barbellBackSquat, barbellFrontSquat, barbellLunge, romanianDeadlift, legPressMachine));
    initExercises(legExercises);
    initCategory(legs, legExercises);

    ArrayList<String> bicepExercises = new ArrayList<>(Arrays.asList(
        barbellCurl, eZBarBicepsCurl, alternatingDumbbellCurl, hammerDumbbellCurl, standingCableCurl
    ));
    initExercises(bicepExercises);
    initCategory(biceps, bicepExercises);

    ArrayList<String> tricepExercises = new ArrayList<>(Arrays.asList(
        benchPressCloseGrip, dumbbellOverheadTricepsPress, ezBarSkullCrushers, tricepsPushdown,
        dip));
    initExercises(tricepExercises);
    initCategory(triceps, tricepExercises);

    ArrayList<String> backExercises = new ArrayList<>(Arrays.asList(
        deadlift, barbellRow, dumbbellRow, tBarRow, latPulldown));
    initExercises(backExercises);
    initCategory(back, backExercises);

    ArrayList<String> shoulderExercises = new ArrayList<>(Arrays.asList(
        overheadPress, dumbbellFrontRaise, dumbbellSideLateralRaise, seatedDumbbellPress,
        dumbbellInclineRow));
    initExercises(shoulderExercises);
    initCategory(shoulders, shoulderExercises);
  }

  private void initRoutines() {
    ArrayList<String> strong5x5workoutAExercises = new ArrayList<>(Arrays.asList(
        barbellBackSquat, barbellBenchPress, barbellRow
    ));
    initRoutine(strong5x5WorkoutA, strong5x5workoutAExercises);

    ArrayList<String> strong5x5workoutBExercises = new ArrayList<>(Arrays.asList(
        barbellBackSquat, overheadPress, deadlift
    ));
    initRoutine(strong5x5WorkoutB, strong5x5workoutBExercises);
  }

  private void initExercises(ArrayList<String> exerciseNames) {
    for (String exerciseName : exerciseNames) {
      initExercise(exerciseName);
    }
  }

  private void initCategory(String categoryName, ArrayList<String> exerciseNames) {
    if (categoryLab.contains(categoryName)) {
      return;
    }
    List<NamedEntity> exercises = exerciseLab.getExercises(exerciseNames);
    categoryLab.insert(categoryName, exercises);
  }

  private void initRoutine(String routineName, ArrayList<String> exerciseNames) {
    if (routineLab.contains(routineName)) {
      return;
    }
    List<NamedEntity> exercises = exerciseLab.getExercises(exerciseNames);
    routineLab.insert(routineName, exercises);
  }

  private void initExercise(String name) {
    if (exerciseLab.contains(name)) {
      return;
    }
    exerciseLab.insert(name);
  }

  public static void run(Activity activity) {
    new InitDatabase(activity);
  }
}
