package com.devcesar.workoutapp.exerciseActivity.editExerciseActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.exerciseActivity.ExerciseFragment;
import com.devcesar.workoutapp.exerciseActivity.SaveSets;
import com.devcesar.workoutapp.labs.WorkoutLab;
import com.devcesar.workoutapp.utils.ExerciseSet;
import com.devcesar.workoutapp.utils.NamedEntity;
import com.devcesar.workoutapp.utils.Workout;
import java.util.ArrayList;

public class EditExerciseActivity extends AppCompatActivity implements SaveSets {

  private static final String EXTRA_EXERCISE_ID = "EXTRA_EXERCISE_ID";
  private static final String EXTRA_TIME_STAMP = "EXTRA_TIME_STAMP";
  private static final String EXTRA_EXERCISE_NAME = "EXTRA_EXERCISE_NAME";
  private int exerciseId;
  private long timeStamp;
  private Fragment fragment;

  public static Intent newIntent(Context packageContext, NamedEntity exercise, long timeStamp) {
    Intent intent = new Intent(packageContext, EditExerciseActivity.class);
    intent.putExtra(EXTRA_EXERCISE_NAME, exercise.getName());
    intent.putExtra(EXTRA_EXERCISE_ID, exercise.getId());
    intent.putExtra(EXTRA_TIME_STAMP, timeStamp);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fragment);

    int invalid = -1;
    exerciseId = getIntent().getIntExtra(EXTRA_EXERCISE_ID, invalid);
    timeStamp = getIntent().getLongExtra(EXTRA_TIME_STAMP, invalid);
    if (exerciseId == invalid || timeStamp == invalid) {
      finish();
    }

    String exerciseName = getIntent().getStringExtra(EXTRA_EXERCISE_NAME);
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
    WorkoutLab.get(this).updateWorkout(workout);
    setResult(RESULT_OK);
    finish();
  }

  @Override
  public void onBackPressed() {
    ((ExerciseFragment) fragment).onBackPressed();
  }
}