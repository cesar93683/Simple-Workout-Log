package com.devcesar.workoutapp.mainActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.utils.Constants;

public class MainActivity extends AppCompatActivity {

  private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
      new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
          Fragment selectedFragment = null;
          switch (item.getItemId()) {
            case R.id.nav_exercise_select:
              selectedFragment = SelectFragment.newInstance(Constants.TYPE_EXERCISE);
              break;
            case R.id.nav_category:
              selectedFragment = SelectFragment.newInstance(Constants.TYPE_CATEGORY);
              break;
            case R.id.nav_routine:
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
    setContentView(R.layout.activity_main);

    BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
    bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.fragment_container,
              SelectFragment.newInstance(Constants.TYPE_EXERCISE))
          .commit();
    }
  }
}
