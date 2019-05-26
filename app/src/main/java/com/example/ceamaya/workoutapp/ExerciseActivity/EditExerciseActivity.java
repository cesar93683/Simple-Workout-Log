package com.example.ceamaya.workoutapp.ExerciseActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.ceamaya.workoutapp.R;

public class EditExerciseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int exerciseId = getIntent().getIntExtra(WorkoutHistoryFragment.EXTRA_EXERCISE_ID,0);
        String exerciseName = getIntent().getStringExtra(WorkoutHistoryFragment.EXTRA_EXERCISE_NAME);
        setTitle(exerciseName);
        setContentView(R.layout.activity_edit_exercise);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = ExerciseFragment.newInstance(exerciseId);
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}