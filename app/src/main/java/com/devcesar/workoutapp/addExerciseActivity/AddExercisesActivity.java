package com.devcesar.workoutapp.addExerciseActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.utils.Exercise;
import com.devcesar.workoutapp.utils.ExerciseUtils;
import java.util.ArrayList;
import java.util.List;

public class AddExercisesActivity extends AppCompatActivity {

  private static final String EXTRA_EXERCISE_IDS = "EXTRA_EXERCISE_IDS";
  private static final String EXTRA_NAME = "EXTRA_NAME";

  public static Intent newIntent(Context packageContext, List<Exercise> exercises,
      String name) {
    Intent intent = new Intent(packageContext, AddExercisesActivity.class);
    intent.putExtra(EXTRA_EXERCISE_IDS, ExerciseUtils.getExerciseIds(exercises));
    intent.putExtra(EXTRA_NAME, name);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fragment);
    String name = getIntent().getStringExtra(EXTRA_NAME);
    setTitle(name);

    ArrayList<Integer> exerciseIds = getIntent().getIntegerArrayListExtra(EXTRA_EXERCISE_IDS);

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
