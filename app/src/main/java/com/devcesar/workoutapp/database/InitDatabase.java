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
  private final String shoulders;
  private final ExerciseLab exerciseLab;
  private final CategoryOrRoutineLab categoryLab;

  private InitDatabase(Activity activity) {
    exerciseLab = ExerciseLab.get(activity);
    categoryLab = CategoryOrRoutineLab.getCategoryLab(activity);

    barbellBenchPress = activity.getString(R.string.barbell_bench_press);
    tryToAddExercise(barbellBenchPress);
    barbellBenchPressIncline = activity.getString(R.string.barbell_bench_press_incline);
    tryToAddExercise(barbellBenchPressIncline);
    dumbbellPress = activity.getString(R.string.dumbbell_press);
    tryToAddExercise(dumbbellPress);
    dumbbellPressIncline = activity.getString(R.string.dumbbell_press_incline);
    tryToAddExercise(dumbbellPressIncline);
    dumbbellChestFly = activity.getString(R.string.dumbbell_chest_fly);
    tryToAddExercise(dumbbellChestFly);

    barbellBackSquat = activity.getString(R.string.barbell_back_squat);
    tryToAddExercise(barbellBackSquat);
    barbellFrontSquat = activity.getString(R.string.barbell_front_squat);
    tryToAddExercise(barbellFrontSquat);
    barbellLunge = activity.getString(R.string.barbell_lunge);
    tryToAddExercise(barbellLunge);
    romanianDeadlift = activity.getString(R.string.romanian_deadlift);
    tryToAddExercise(romanianDeadlift);
    legPressMachine = activity.getString(R.string.leg_press_machine);
    tryToAddExercise(legPressMachine);

    barbellCurl = activity.getString(R.string.barbell_curl);
    tryToAddExercise(barbellCurl);
    eZBarBicepsCurl = activity.getString(R.string.ez_bar_biceps_curl);
    tryToAddExercise(eZBarBicepsCurl);
    alternatingDumbbellCurl = activity.getString(R.string.alternating_dumbbell_curl);
    tryToAddExercise(alternatingDumbbellCurl);
    hammerDumbbellCurl = activity.getString(R.string.hammer_dumbbell_curl);
    tryToAddExercise(hammerDumbbellCurl);
    standingCableCurl = activity.getString(R.string.standing_cable_curl);
    tryToAddExercise(standingCableCurl);

    benchPressCloseGrip = activity.getString(R.string.bench_press_close_grip);
    tryToAddExercise(benchPressCloseGrip);
    dumbbellOverheadTricepsPress = activity.getString(R.string.dumbbell_overhead_triceps_press);
    tryToAddExercise(dumbbellOverheadTricepsPress);
    ezBarSkullCrushers = activity.getString(R.string.ez_bar_skull_crushers);
    tryToAddExercise(ezBarSkullCrushers);
    dip = activity.getString(R.string.dip);
    tryToAddExercise(dip);
    tricepsPushdown = activity.getString(R.string.triceps_pushdown);
    tryToAddExercise(tricepsPushdown);

    deadlift = activity.getString(R.string.deadlift);
    tryToAddExercise(deadlift);
    barbellRow = activity.getString(R.string.barbell_row);
    tryToAddExercise(barbellRow);
    dumbbellRow = activity.getString(R.string.dumbbellRow);
    tryToAddExercise(dumbbellRow);
    tBarRow = activity.getString(R.string.t_bar_row);
    tryToAddExercise(tBarRow);
    latPulldown = activity.getString(R.string.lat_pulldown);
    tryToAddExercise(latPulldown);

    overheadPress = activity.getString(R.string.overhead_press);
    tryToAddExercise(overheadPress);
    dumbbellFrontRaise = activity.getString(R.string.dumbbell_front_raise);
    tryToAddExercise(dumbbellFrontRaise);
    dumbbellSideLateralRaise = activity.getString(R.string.dumbbell_side_lateral_raise);
    tryToAddExercise(dumbbellSideLateralRaise);
    seatedDumbbellPress = activity.getString(R.string.seated_dumbbell_press);
    tryToAddExercise(seatedDumbbellPress);
    dumbbellInclineRow = activity.getString(R.string.dumbbell_incline_row);
    tryToAddExercise(dumbbellInclineRow);

    chest = activity.getString(R.string.chest);
    legs = activity.getString(R.string.legs);
    biceps = activity.getString(R.string.biceps);
    triceps = activity.getString(R.string.triceps);
    back = activity.getString(R.string.back);
    shoulders = activity.getString(R.string.shoulders);

    initCategories();
  }

  private void tryToAddExercise(String name) {
    if (exerciseLab.contains(name)) {
      return;
    }
    exerciseLab.insert(name);
  }

  private void initCategories() {
    ArrayList<String> chestExercises = new ArrayList<>(Arrays.asList(
        barbellBenchPress, barbellBenchPressIncline, dumbbellPress, dumbbellPressIncline,
        dumbbellChestFly));
    initCategory(chest, chestExercises);

    ArrayList<String> legExercises = new ArrayList<>(Arrays.asList(
        barbellBackSquat, barbellFrontSquat, barbellLunge, romanianDeadlift, legPressMachine));
    initCategory(legs, legExercises);

    ArrayList<String> bicepExercises = new ArrayList<>(Arrays.asList(
        barbellCurl, eZBarBicepsCurl, alternatingDumbbellCurl, hammerDumbbellCurl, standingCableCurl
    ));
    initCategory(biceps, bicepExercises);

    ArrayList<String> tricepExercises = new ArrayList<>(Arrays.asList(
        benchPressCloseGrip, dumbbellOverheadTricepsPress, ezBarSkullCrushers, tricepsPushdown,
        dip));
    initCategory(triceps, tricepExercises);

    ArrayList<String> backExercises = new ArrayList<>(Arrays.asList(
        deadlift, barbellRow, dumbbellRow, tBarRow, latPulldown));
    initCategory(back, backExercises);

    ArrayList<String> shoulderExercises = new ArrayList<>(Arrays.asList(
        overheadPress, dumbbellFrontRaise, dumbbellSideLateralRaise, seatedDumbbellPress,
        dumbbellInclineRow));
    initCategory(shoulders, shoulderExercises);
  }

  private void initCategory(String categoryName, ArrayList<String> exerciseNames) {
    if (categoryLab.contains(categoryName)) {
      return;
    }
    List<NamedEntity> exercises = exerciseLab.getExercises(exerciseNames);
    categoryLab.insert(categoryName, exercises);
  }

  public static void run(Activity activity) {
    new InitDatabase(activity);
  }
}
