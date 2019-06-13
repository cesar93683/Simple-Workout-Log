package com.devcesar.workoutapp.mainActivity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.databinding.ActivityMainBinding;
import com.devcesar.workoutapp.utils.Constants;

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
