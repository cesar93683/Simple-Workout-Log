package com.example.ceamaya.workoutapp.ExerciseActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.ceamaya.workoutapp.ExerciseSet;
import com.example.ceamaya.workoutapp.R;
import com.example.ceamaya.workoutapp.Workout;

import java.util.ArrayList;

public class EditExerciseActivity extends AppCompatActivity implements SaveSets {
    private static final String TAG = "EditExerciseActivity";
    private static final String EXTRA_EXERCISE_ID = "EXTRA_EXERCISE_ID";
    private static final String EXTRA_TIME = "EXTRA_TIME";
    private static final String EXTRA_EXERCISE_NAME = "EXTRA_EXERCISE_NAME";
    private long timeStamp;
    private WorkoutLab workoutLab;

    public static Intent newIntent(Context packageContext, String exerciseName, int exerciseId,
                                   long time) {
        Intent intent = new Intent(packageContext, EditExerciseActivity.class);
        intent.putExtra(EXTRA_EXERCISE_NAME, exerciseName);
        intent.putExtra(EXTRA_EXERCISE_ID, exerciseId);
        intent.putExtra(EXTRA_TIME, time);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workoutLab = WorkoutLab.get(this);

        String exerciseName = getIntent().getStringExtra(EXTRA_EXERCISE_NAME);
        int exerciseId = getIntent().getIntExtra(EXTRA_EXERCISE_ID, -1);
        timeStamp = getIntent().getLongExtra(EXTRA_TIME, -1);

        if (exerciseId == -1 || timeStamp == -1) {
            finish();
        }
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
        ArrayList<ExerciseSet> exerciseSets =
                workoutLab.getWorkout(exerciseId, timeStamp).getExerciseSets();
        ((ExerciseFragment) fragment).addExerciseSets(exerciseSets);
    }

    @Override
    public void saveSets(ArrayList<ExerciseSet> exerciseSets) {
        workoutLab.deleteWorkout(timeStamp);
        Workout workout = new Workout(timeStamp, exerciseSets);
        workoutLab.insertWorkout(workout);
        setResult(RESULT_OK);
        finish();
    }
}