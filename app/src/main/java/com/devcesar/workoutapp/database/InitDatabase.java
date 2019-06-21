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

  private InitDatabase(Activity activity) {
    ExerciseLab exerciseLab = ExerciseLab.get(activity);

    barbellBenchPress = activity.getString(R.string.barbell_bench_press);
    exerciseLab.insert(barbellBenchPress);
    barbellBenchPressIncline = activity.getString(R.string.barbell_bench_press_incline);
    exerciseLab.insert(barbellBenchPressIncline);
    dumbbellPress = activity.getString(R.string.dumbbell_press);
    exerciseLab.insert(dumbbellPress);
    dumbbellPressIncline = activity.getString(R.string.dumbbell_press_incline);
    exerciseLab.insert(dumbbellPressIncline);
    dumbbellChestFly = activity.getString(R.string.dumbbell_chest_fly);
    exerciseLab.insert(dumbbellChestFly);

    barbellBackSquat = activity.getString(R.string.barbell_back_squat);
    exerciseLab.insert(barbellBackSquat);
    barbellFrontSquat = activity.getString(R.string.barbell_front_squat);
    exerciseLab.insert(barbellFrontSquat);
    barbellLunge = activity.getString(R.string.barbell_lunge);
    exerciseLab.insert(barbellLunge);
    romanianDeadlift = activity.getString(R.string.romanian_deadlift);
    exerciseLab.insert(romanianDeadlift);
    legPressMachine = activity.getString(R.string.leg_press_machine);
    exerciseLab.insert(legPressMachine);

    barbellCurl = activity.getString(R.string.barbell_curl);
    exerciseLab.insert(barbellCurl);
    eZBarBicepsCurl = activity.getString(R.string.ez_bar_biceps_curl);
    exerciseLab.insert(eZBarBicepsCurl);
    alternatingDumbbellCurl = activity.getString(R.string.alternating_dumbbell_curl);
    exerciseLab.insert(alternatingDumbbellCurl);
    hammerDumbbellCurl = activity.getString(R.string.hammer_dumbbell_curl);
    exerciseLab.insert(hammerDumbbellCurl);
    standingCableCurl = activity.getString(R.string.standing_cable_curl);
    exerciseLab.insert(standingCableCurl);

    benchPressCloseGrip = activity.getString(R.string.bench_press_close_grip);
    exerciseLab.insert(benchPressCloseGrip);
    dumbbellOverheadTricepsPress = activity.getString(R.string.dumbbell_overhead_triceps_press);
    exerciseLab.insert(dumbbellOverheadTricepsPress);
    ezBarSkullCrushers = activity.getString(R.string.ez_bar_skull_crushers);
    exerciseLab.insert(ezBarSkullCrushers);
    dip = activity.getString(R.string.dip);
    exerciseLab.insert(dip);
    tricepsPushdown = activity.getString(R.string.triceps_pushdown);
    exerciseLab.insert(tricepsPushdown);

    deadlift = activity.getString(R.string.deadlift);
    exerciseLab.insert(deadlift);
    barbellRow = activity.getString(R.string.barbell_row);
    exerciseLab.insert(barbellRow);
    dumbbellRow = activity.getString(R.string.dumbbellRow);
    exerciseLab.insert(dumbbellRow);
    tBarRow = activity.getString(R.string.t_bar_row);
    exerciseLab.insert(tBarRow);
    latPulldown = activity.getString(R.string.lat_pulldown);
    exerciseLab.insert(latPulldown);

    overheadPress = activity.getString(R.string.overhead_press);
    exerciseLab.insert(overheadPress);
    dumbbellFrontRaise = activity.getString(R.string.dumbbell_front_raise);
    exerciseLab.insert(dumbbellFrontRaise);
    dumbbellSideLateralRaise = activity.getString(R.string.dumbbell_side_lateral_raise);
    exerciseLab.insert(dumbbellSideLateralRaise);
    seatedDumbbellPress = activity.getString(R.string.seated_dumbbell_press);
    exerciseLab.insert(seatedDumbbellPress);
    dumbbellInclineRow = activity.getString(R.string.dumbbell_incline_row);
    exerciseLab.insert(dumbbellInclineRow);

    initCategories(activity);
  }

  private void initCategories(Activity activity) {
    ArrayList<String> chestExercises = new ArrayList<>(Arrays.asList(
        barbellBenchPress, barbellBenchPressIncline, dumbbellPress, dumbbellPressIncline,
        dumbbellChestFly));
    initCategory(activity, activity.getString(R.string.chest), chestExercises);

    ArrayList<String> legExercises = new ArrayList<>(Arrays.asList(
        barbellBackSquat, barbellFrontSquat, barbellLunge, romanianDeadlift, legPressMachine));
    initCategory(activity, activity.getString(R.string.legs), legExercises);

    ArrayList<String> bicepExercises = new ArrayList<>(Arrays.asList(
        barbellCurl, eZBarBicepsCurl, alternatingDumbbellCurl, hammerDumbbellCurl, standingCableCurl
    ));
    initCategory(activity, activity.getString(R.string.biceps), bicepExercises);

    ArrayList<String> tricepExercises = new ArrayList<>(Arrays.asList(
        benchPressCloseGrip, dumbbellOverheadTricepsPress, ezBarSkullCrushers, tricepsPushdown,
        dip));
    initCategory(activity, activity.getString(R.string.triceps), tricepExercises);

    ArrayList<String> backExercises = new ArrayList<>(Arrays.asList(
        deadlift, barbellRow, dumbbellRow, tBarRow, latPulldown));
    initCategory(activity, activity.getString(R.string.back), backExercises);

    ArrayList<String> shoulderExercises = new ArrayList<>(Arrays.asList(
        overheadPress, dumbbellFrontRaise, dumbbellSideLateralRaise, seatedDumbbellPress,
        dumbbellInclineRow));
    initCategory(activity, activity.getString(R.string.shoulders), shoulderExercises);
  }

  private void initCategory(Activity activity, String categoryName,
      ArrayList<String> exerciseNames) {
    List<NamedEntity> exercises = ExerciseLab.get(activity).getExercises(exerciseNames);
    CategoryOrRoutineLab.getCategoryLab(activity).insert(categoryName, exercises);
  }

  public static void run(Activity activity) {
    new InitDatabase(activity);
  }
}
