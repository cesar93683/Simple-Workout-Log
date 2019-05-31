package com.example.ceamaya.workoutapp.ExerciseActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.ceamaya.workoutapp.ExerciseSet;
import com.example.ceamaya.workoutapp.R;

import java.util.ArrayList;

import static com.example.ceamaya.workoutapp.MainActivity.MainActivity.exerciseDB;

public class EditExerciseActivity extends AppCompatActivity implements SaveSets {
    private static final String TAG = "EditExerciseActivity";
    private long time;
    private static final String EXTRA_EXERCISE_ID = "EXTRA_EXERCISE_ID";
    private static final String EXTRA_TIME = "EXTRA_TIME";
    private static final String EXTRA_EXERCISE_NAME = "EXTRA_EXERCISE_NAME";

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
        String exerciseName = getIntent().getStringExtra(EXTRA_EXERCISE_NAME);
        int exerciseId = getIntent().getIntExtra(EXTRA_EXERCISE_ID, -1);
        time = getIntent().getLongExtra(EXTRA_TIME, -1);

        if (exerciseId == -1 || time == -1) {
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
        ArrayList<ExerciseSet> exerciseSets = exerciseDB.getExerciseSets(exerciseId, time);
        ((ExerciseFragment)fragment).addExerciseSets(exerciseSets);
    }

    @Override
    public void saveSets(ArrayList<ExerciseSet> exerciseSets) {
        exerciseDB.deleteWorkout(time);
        for(ExerciseSet exerciseSet : exerciseSets) {
            exerciseDB.insertSet(exerciseSet, time);
        }
        setResult(RESULT_OK);
        finish();
    }
}