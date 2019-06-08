package com.devcesar.workoutapp.exerciseActivity.editExerciseActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import com.devcesar.workoutapp.Utils.ExerciseSet;
import com.devcesar.workoutapp.Utils.Workout;
import com.devcesar.workoutapp.exerciseActivity.ExerciseFragment;
import com.devcesar.workoutapp.exerciseActivity.SaveSets;
import com.devcesar.workoutapp.labs.WorkoutLab;
import com.devcesar.workoutapp.R;
import java.util.ArrayList;

public class EditExerciseActivity extends AppCompatActivity implements SaveSets {

  private static final String EXTRA_EXERCISE_ID = "EXTRA_EXERCISE_ID";
  private static final String EXTRA_TIME_STAMP = "EXTRA_TIME_STAMP";
  private static final String EXTRA_EXERCISE_NAME = "EXTRA_EXERCISE_NAME";
  private long timeStamp;
  private WorkoutLab workoutLab;
  private Fragment fragment;
  private int exerciseId;

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
    exerciseId = getIntent().getIntExtra(EXTRA_EXERCISE_ID, -1);
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
    Workout workout = new Workout(exerciseId, exerciseSets, timeStamp);
    workoutLab.updateWorkout(workout);
    setResult(RESULT_OK);
    finish();
  }

  @Override
  public void onBackPressed() {
    ((ExerciseFragment) fragment).onBackPressed();
  }
}