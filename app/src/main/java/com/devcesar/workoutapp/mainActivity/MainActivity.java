package com.devcesar.workoutapp.mainActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

  private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
      item -> {
        switch (item.getItemId()) {
          case R.id.nav_exercise:
            setTitle(R.string.exercise);
            setTabStateFragment(item.getItemId());
            return true;
          case R.id.nav_category:
            setTitle(R.string.category);
            setTabStateFragment(item.getItemId());
            return true;
          case R.id.nav_routine:
            setTitle(R.string.routine);
            setTabStateFragment(item.getItemId());
            return true;
          case R.id.nav_settings:
            setTitle(R.string.settings);
            setTabStateFragment(item.getItemId());
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
        .add(R.id.fragment_container, routineFragment)
        .add(R.id.fragment_container, categoryFragment)
        .hide(exerciseFragment)
        .hide(categoryFragment)
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

    setTitle(R.string.exercise);

    exerciseFragment = SelectFragment.newInstance(Constants.TYPE_EXERCISE);
    categoryFragment = SelectFragment.newInstance(Constants.TYPE_CATEGORY);
    routineFragment = SelectFragment.newInstance(Constants.TYPE_ROUTINE);
    settingsFragment = SettingsFragment.newInstance();

    getSupportFragmentManager()
        .beginTransaction()
        .add(R.id.fragment_container, exerciseFragment)
        .add(R.id.fragment_container, routineFragment)
        .add(R.id.fragment_container, categoryFragment)
        .add(R.id.fragment_container, settingsFragment)
        .commit();
    setTabStateFragment(R.id.nav_exercise);
  }

  private void setTabStateFragment(int type) {
    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    switch (type) {
      case R.id.nav_category:
        transaction.show(categoryFragment);
        transaction.hide(exerciseFragment);
        transaction.hide(routineFragment);
        transaction.hide(settingsFragment);
        break;
      case R.id.nav_exercise:
        transaction.hide(categoryFragment);
        transaction.show(exerciseFragment);
        transaction.hide(routineFragment);
        transaction.hide(settingsFragment);
        break;
      case R.id.nav_routine:
        transaction.hide(categoryFragment);
        transaction.hide(exerciseFragment);
        transaction.show(routineFragment);
        transaction.hide(settingsFragment);
        break;
      case R.id.nav_settings:
        transaction.hide(categoryFragment);
        transaction.hide(exerciseFragment);
        transaction.hide(routineFragment);
        transaction.show(settingsFragment);
        break;
    }
    transaction.commit();
  }

}
