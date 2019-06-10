package com.devcesar.workoutapp.mainActivity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.databinding.ActivityMainBinding;
import com.devcesar.workoutapp.utils.Constants;

public class MainActivity extends AppCompatActivity {

  private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
      new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
          Fragment selectedFragment = null;
          switch (item.getItemId()) {
            case R.id.nav_exercise_select:
              setTitle(R.string.exercise);
              selectedFragment = SelectFragment.newInstance(Constants.TYPE_EXERCISE);
              break;
            case R.id.nav_category:
              setTitle(R.string.category);
              selectedFragment = SelectFragment.newInstance(Constants.TYPE_CATEGORY);
              break;
            case R.id.nav_routine:
              setTitle(R.string.routine);
              selectedFragment = SelectFragment.newInstance(Constants.TYPE_ROUTINE);
              break;
          }
          getSupportFragmentManager().beginTransaction()
              .replace(R.id.fragment_container, selectedFragment)
              .commit();
          return true;
        }
      };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    binding.bottomNavigation.setOnNavigationItemSelectedListener(navListener);

    if (savedInstanceState == null) {
      setTitle(R.string.exercise);
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.fragment_container,
              SelectFragment.newInstance(Constants.TYPE_EXERCISE))
          .commit();
    }
  }
}
