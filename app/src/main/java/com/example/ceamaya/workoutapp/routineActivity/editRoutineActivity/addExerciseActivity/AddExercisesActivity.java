package com.example.ceamaya.workoutapp.routineActivity.editRoutineActivity.addExerciseActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.ceamaya.workoutapp.R;

public class AddExercisesActivity extends AppCompatActivity {

    private static final String EXTRA_EXERCISE_IDS = "EXTRA_EXERCISE_IDS";
    private static final String EXTRA_ROUTINE_NAME = "EXTRA_ROUTINE_NAME";

    public static Intent newIntent(Context packageContext, int[] exerciseIds, String routineName) {
        Intent intent = new Intent(packageContext, AddExercisesActivity.class);
        intent.putExtra(EXTRA_EXERCISE_IDS, exerciseIds);
        intent.putExtra(EXTRA_ROUTINE_NAME, routineName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        String routineName = getIntent().getStringExtra(EXTRA_ROUTINE_NAME);
        setTitle(routineName);

        int[] exerciseIds = getIntent().getIntArrayExtra(EXTRA_EXERCISE_IDS);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = AddExerciseFragment.newInstance(exerciseIds);
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
