package com.devcesar.workoutapp.mainActivity;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.databinding.ActivityMainBinding;
import com.devcesar.workoutapp.labs.CategoryOrRoutineLab;
import com.devcesar.workoutapp.labs.ExerciseLab;
import com.devcesar.workoutapp.utils.Constants;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

  private final Fragment routineFragment = SelectFragment.newInstance(Constants.TYPE_ROUTINE);
  private final Fragment exerciseFragment = SelectFragment.newInstance(Constants.TYPE_EXERCISE);
  private final Fragment categoryFragment = SelectFragment.newInstance(Constants.TYPE_CATEGORY);
  private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
      item -> {
        switch (item.getItemId()) {
          case R.id.nav_exercise:
            setTitle(R.string.exercise);
            setTabStateFragment(item.getItemId()).commit();
            return true;
          case R.id.nav_category:
            setTitle(R.string.category);
            setTabStateFragment(item.getItemId()).commit();
            return true;
          case R.id.nav_routine:
            setTitle(R.string.routine);
            setTabStateFragment(item.getItemId()).commit();
            return true;
        }
        return false;
      };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    boolean isFirstRun = prefs.getBoolean("FIRST_RUN", true);
    if (isFirstRun) {
      PreferenceManager.getDefaultSharedPreferences(this)
          .edit()
          .putBoolean("FIRST_RUN", false)
          .apply();
      HashMap<String, ArrayList<String>> categoryAndExerciseNames = getCategoryAndExerciseNames();
      ExerciseLab.get(this).importExercises(categoryAndExerciseNames);
      CategoryOrRoutineLab.getCategoryLab(this).importNamedEntitiesAndExercises(categoryAndExerciseNames, this);
    }

    ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    binding.bottomNavigation.setOnNavigationItemSelectedListener(navListener);

    setTitle(R.string.exercise);
    getSupportFragmentManager()
        .beginTransaction()
        .add(R.id.fragment_container, exerciseFragment)
        .add(R.id.fragment_container, routineFragment)
        .add(R.id.fragment_container, categoryFragment)
        .commit();
    setTabStateFragment(R.id.nav_exercise).commit();
  }

  @NonNull
  private HashMap<String, ArrayList<String>> getCategoryAndExerciseNames() {
    HashMap<String, ArrayList<String>> exerciseNames = new HashMap<>();

    ArrayList<String> chestExercises = new ArrayList<>();
    String barbellBenchPress = getString(R.string.barbellBenchPress);
    chestExercises.add(barbellBenchPress);
    String barbellBenchPressIncline = getString(R.string.barbellBenchPressIncline);
    chestExercises.add(barbellBenchPressIncline);
    String dumbbellPress = getString(R.string.dumbbellPress);
    chestExercises.add(dumbbellPress);
    String dumbbellPressIncline = getString(R.string.dumbbellPressIncline);
    chestExercises.add(dumbbellPressIncline);
    String dumbbellChestFly = getString(R.string.dumbbellChestFly);
    chestExercises.add(dumbbellChestFly);
    exerciseNames.put(getString(R.string.chest), chestExercises);

    ArrayList<String> legExercises = new ArrayList<>();
    String barbellBackSquat = getString(R.string.barbellBackSquat);
    legExercises.add(barbellBackSquat);
    String barbellFrontSquat = getString(R.string.barbellFrontSquat);
    legExercises.add(barbellFrontSquat);
    String barbellLunge = getString(R.string.barbellLunge);
    legExercises.add(barbellLunge);
    String romanianDeadlift = getString(R.string.romanianDeadlift);
    legExercises.add(romanianDeadlift);
    String legPressMachine = getString(R.string.legPressMachine);
    legExercises.add(legPressMachine);
    exerciseNames.put(getString(R.string.legs), legExercises);

    ArrayList<String> bicepExercises = new ArrayList<>();
    String barbellCurl = getString(R.string.barbellCurl);
    bicepExercises.add(barbellCurl);
    String eZBarBicepsCurl = getString(R.string.eZBarBicepsCurl);
    bicepExercises.add(eZBarBicepsCurl);
    String alternatingDumbbellCurl = getString(R.string.alternatingDumbbellCurl);
    bicepExercises.add(alternatingDumbbellCurl);
    String hammerDumbbellCurl = getString(R.string.hammerDumbbellCurl);
    bicepExercises.add(hammerDumbbellCurl);
    String standingCableCurl = getString(R.string.standingCableCurl);
    bicepExercises.add(standingCableCurl);
    exerciseNames.put(getString(R.string.biceps), bicepExercises);

    ArrayList<String> tricepExercises = new ArrayList<>();
    String benchPressCloseGrip = getString(R.string.benchPressCloseGrip);
    tricepExercises.add(benchPressCloseGrip);
    String dumbbellOverheadTricepsPress = getString(R.string.dumbbellOverheadTricepsPress);
    tricepExercises.add(dumbbellOverheadTricepsPress);
    String ezBarSkullCrushers = getString(R.string.ezBarSkullCrushers);
    tricepExercises.add(ezBarSkullCrushers);
    String tricepsPushdown = getString(R.string.tricepsPushdown);
    tricepExercises.add(tricepsPushdown);
    String dip = getString(R.string.dip);
    tricepExercises.add(dip);
    exerciseNames.put(getString(R.string.triceps), tricepExercises);

    ArrayList<String> backExercises = new ArrayList<>();
    String deadlift = getString(R.string.deadlift);
    backExercises.add(deadlift);
    String barbellRow = getString(R.string.barbellRow);
    backExercises.add(barbellRow);
    String dumbbellRow = getString(R.string.dumbbellRow);
    backExercises.add(dumbbellRow);
    String tBarRow = getString(R.string.tBarRow);
    backExercises.add(tBarRow);
    String latPulldown = getString(R.string.latPulldown);
    backExercises.add(latPulldown);
    exerciseNames.put(getString(R.string.back), backExercises);

    ArrayList<String> shoulderExercises = new ArrayList<>();
    String overheadPress = getString(R.string.overheadPress);
    shoulderExercises.add(overheadPress);
    String dumbbellFrontRaise = getString(R.string.dumbbellFrontRaise);
    shoulderExercises.add(dumbbellFrontRaise);
    String dumbbellSideLateralRaise = getString(R.string.dumbbellSideLateralRaise);
    shoulderExercises.add(dumbbellSideLateralRaise);
    String seatedDumbbellPress = getString(R.string.seatedDumbbellPress);
    shoulderExercises.add(seatedDumbbellPress);
    String dumbbellInclineRow = getString(R.string.dumbbellInclineRow);
    shoulderExercises.add(dumbbellInclineRow);
    exerciseNames.put(getString(R.string.shoulders), shoulderExercises);

    return exerciseNames;
  }

  private FragmentTransaction setTabStateFragment(int type) {
    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    switch (type) {
      case R.id.nav_category:
        transaction.show(categoryFragment);
        transaction.hide(exerciseFragment);
        transaction.hide(routineFragment);
        break;
      case R.id.nav_exercise:
        transaction.hide(categoryFragment);
        transaction.show(exerciseFragment);
        transaction.hide(routineFragment);
        break;
      case R.id.nav_routine:
        transaction.hide(categoryFragment);
        transaction.hide(exerciseFragment);
        transaction.show(routineFragment);
        break;
    }

    return transaction;
  }

}
