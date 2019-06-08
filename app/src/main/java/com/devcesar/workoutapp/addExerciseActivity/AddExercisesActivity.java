package com.devcesar.workoutapp.addExerciseActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.utils.Exercise;
import java.util.ArrayList;

public class AddExercisesActivity extends AppCompatActivity {

  private static final String EXTRA_EXERCISE_IDS = "EXTRA_EXERCISE_IDS";
  private static final String EXTRA_NAME = "EXTRA_NAME";

  public static Intent newIntent(Context packageContext, ArrayList<Exercise> exercises,
      String name) {
    int[] exerciseIds = new int[exercises.size()];
    int i = 0;
    for (Exercise exercise : exercises) {
      exerciseIds[i++] = exercise.getId();
    }
    Intent intent = new Intent(packageContext, AddExercisesActivity.class);
    intent.putExtra(EXTRA_EXERCISE_IDS, exerciseIds);
    intent.putExtra(EXTRA_NAME, name);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fragment);
    String name = getIntent().getStringExtra(EXTRA_NAME);
    setTitle(name);

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
