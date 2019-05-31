package com.example.ceamaya.workoutapp.MainActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.ceamaya.workoutapp.Database.ExerciseBaseHelper;
import com.example.ceamaya.workoutapp.R;

public class MainActivity extends AppCompatActivity {

    public static ExerciseBaseHelper exerciseDB;
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.nav_exercise_select:
                            selectedFragment = ExerciseSelectFragment.newInstance();
                            break;
                        case R.id.nav_settings:
                            selectedFragment = SettingsFragment.newInstance();
                            break;
                        case R.id.nav_routine:
                            selectedFragment = RoutineSelectFragment.newInstance();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        exerciseDB = new ExerciseBaseHelper(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ExerciseSelectFragment()).commit();
        }
    }
}
