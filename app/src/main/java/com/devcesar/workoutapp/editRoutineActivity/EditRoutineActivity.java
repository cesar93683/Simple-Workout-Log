package com.devcesar.workoutapp.editRoutineActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.utils.NamedEntity;

public class EditRoutineActivity extends AppCompatActivity {

  private static final String EXTRA_ROUTINE_ID = "EXTRA_ROUTINE_ID";
  private static final String EXTRA_ROUTINE_NAME = "EXTRA_ROUTINE_NAME";
  private Fragment fragment;

  public static Intent newIntent(Context packageContext, NamedEntity routine) {
    Intent intent = new Intent(packageContext, EditRoutineActivity.class);
    intent.putExtra(EXTRA_ROUTINE_ID, routine.getId());
    intent.putExtra(EXTRA_ROUTINE_NAME, routine.getName());
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fragment);
    int invalid = -1;
    int routineId = getIntent().getIntExtra(EXTRA_ROUTINE_ID, invalid);
    if (routineId == invalid) {
      finish();
    }
    String routineName = getIntent().getStringExtra(EXTRA_ROUTINE_NAME);
    setTitle(routineName);

    FragmentManager fm = getSupportFragmentManager();
    fragment = fm.findFragmentById(R.id.fragment_container);
    if (fragment == null) {
      fragment = EditRoutineFragment.newInstance(new NamedEntity(routineName, routineId));
      fm.beginTransaction()
          .add(R.id.fragment_container, fragment)
          .commit();
    }
  }

  @Override
  public void onBackPressed() {
    ((EditRoutineFragment) fragment).onBackPressed();

  }
}
