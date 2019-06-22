package com.devcesar.workoutapp.mainActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.database.InitDatabase;
import com.devcesar.workoutapp.databinding.ActivityMainBinding;
import com.devcesar.workoutapp.utils.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

  private Fragment settingsFragment;
  private Fragment exerciseFragment;
  private Fragment categoryFragment;
  private Fragment routineFragment;
  private Fragment activeFragment;
  private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
      item -> {
        switch (item.getItemId()) {
          case R.id.nav_exercise:
            setTitle(R.string.exercise);
            getSupportFragmentManager().beginTransaction().hide(activeFragment)
                .show(exerciseFragment).commit();
            activeFragment = exerciseFragment;
            return true;
          case R.id.nav_category:
            setTitle(R.string.category);
            getSupportFragmentManager().beginTransaction().hide(activeFragment)
                .show(categoryFragment).commit();
            activeFragment = categoryFragment;
            return true;
          case R.id.nav_routine:
            setTitle(R.string.routine);
            getSupportFragmentManager().beginTransaction().hide(activeFragment)
                .show(routineFragment).commit();
            activeFragment = routineFragment;
            return true;
          case R.id.nav_settings:
            setTitle(R.string.settings);
            getSupportFragmentManager().beginTransaction().hide(activeFragment)
                .show(settingsFragment).commit();
            activeFragment = settingsFragment;
            return true;
        }
        return false;
      };

  public void refreshFragments() {
    getSupportFragmentManager().beginTransaction()
        .remove(exerciseFragment)
        .remove(categoryFragment)
        .remove(routineFragment)
        .commit();

    exerciseFragment = SelectFragment.newInstance(Constants.TYPE_EXERCISE);
    categoryFragment = SelectFragment.newInstance(Constants.TYPE_CATEGORY);
    routineFragment = SelectFragment.newInstance(Constants.TYPE_ROUTINE);

    getSupportFragmentManager().beginTransaction()
        .add(R.id.fragment_container, exerciseFragment)
        .hide(exerciseFragment)
        .add(R.id.fragment_container, categoryFragment)
        .hide(categoryFragment)
        .add(R.id.fragment_container, routineFragment)
        .hide(routineFragment)
        .commit();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    String IS_FIRST_RUN = "IS_FIRST_RUN";
    boolean isFirstRun = prefs.getBoolean(IS_FIRST_RUN, true);
    if (isFirstRun) {
      InitDatabase.run(this);
      prefs.edit().putBoolean(IS_FIRST_RUN, false).apply();
      prefs.edit().putInt(Constants.START_TIME, Constants.DEFAULT_START_TIME).apply();
    }

    ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    binding.bottomNavigation.setOnNavigationItemSelectedListener(navListener);

    exerciseFragment = SelectFragment.newInstance(Constants.TYPE_EXERCISE);
    categoryFragment = SelectFragment.newInstance(Constants.TYPE_CATEGORY);
    routineFragment = SelectFragment.newInstance(Constants.TYPE_ROUTINE);
    settingsFragment = SettingsFragment.newInstance();

    activeFragment = exerciseFragment;

    getSupportFragmentManager()
        .beginTransaction()
        .add(R.id.fragment_container, exerciseFragment)
        .add(R.id.fragment_container, categoryFragment)
        .hide(categoryFragment)
        .add(R.id.fragment_container, routineFragment)
        .hide(routineFragment)
        .add(R.id.fragment_container, settingsFragment)
        .hide(settingsFragment)
        .commit();

    setTitle(R.string.exercise);
  }

}
