package com.example.ceamaya.workoutapp.exerciseActivity.editExerciseActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.ceamaya.workoutapp.ExerciseSet;
import com.example.ceamaya.workoutapp.R;
import com.example.ceamaya.workoutapp.exerciseActivity.ExerciseFragment;
import com.example.ceamaya.workoutapp.exerciseActivity.SaveSets;
import com.example.ceamaya.workoutapp.labs.WorkoutLab;

import java.util.ArrayList;

public class EditExerciseActivity extends AppCompatActivity implements SaveSets {
    private static final String EXTRA_EXERCISE_ID = "EXTRA_EXERCISE_ID";
    private static final String EXTRA_TIME_STAMP = "EXTRA_TIME_STAMP";
    private static final String EXTRA_EXERCISE_NAME = "EXTRA_EXERCISE_NAME";
    private long timeStamp;
    private WorkoutLab workoutLab;
    private Fragment fragment;

    public static Intent newIntent(Context packageContext, String exerciseName, int exerciseId,
                                   long timeStamp) {
        Intent intent = new Intent(packageContext, EditExerciseActivity.class);
        intent.putExtra(EXTRA_EXERCISE_NAME, exerciseName);
        intent.putExtra(EXTRA_EXERCISE_ID, exerciseId);
        intent.putExtra(EXTRA_TIME_STAMP, timeStamp);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        workoutLab = WorkoutLab.get(this);

        String exerciseName = getIntent().getStringExtra(EXTRA_EXERCISE_NAME);
        int exerciseId = getIntent().getIntExtra(EXTRA_EXERCISE_ID, -1);
        timeStamp = getIntent().getLongExtra(EXTRA_TIME_STAMP, -1);

        if (exerciseId == -1 || timeStamp == -1) {
            finish();
        }
        setTitle(exerciseName);

        FragmentManager fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = ExerciseFragment.newInstance(exerciseId, timeStamp);
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public void saveSets(ArrayList<ExerciseSet> exerciseSets) {
        workoutLab.updateWorkout(timeStamp, exerciseSets);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        ((ExerciseFragment) fragment).onBackPressed();
    }
}