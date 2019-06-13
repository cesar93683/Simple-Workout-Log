package com.devcesar.workoutapp.viewExercisesActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.utils.NamedEntity;

public class ViewExercisesActivity extends AppCompatActivity {

  private static final String EXTRA_NAME = "EXTRA_NAME";
  private static final String EXTRA_ID = "EXTRA_ID";
  private static final String EXTRA_TYPE = "EXTRA_TYPE";

  public static Intent newIntent(Context packageContext, NamedEntity namedEntity, int type) {
    Intent intent = new Intent(packageContext, ViewExercisesActivity.class);
    intent.putExtra(EXTRA_NAME, namedEntity.getName());
    intent.putExtra(EXTRA_ID, namedEntity.getId());
    intent.putExtra(EXTRA_TYPE, type);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fragment);
    int invalid = -1;

    int id = getIntent().getIntExtra(EXTRA_ID, invalid);
    int type = getIntent().getIntExtra(EXTRA_TYPE, invalid);
    if (id == invalid || type == invalid) {
      finish();
    }
    String name = getIntent().getStringExtra(EXTRA_NAME);
    setTitle(name);

    FragmentManager fm = getSupportFragmentManager();
    Fragment fragment = fm.findFragmentById(R.id.fragment_container);
    if (fragment == null) {
      fragment = ViewExercisesFragment.newInstance(new NamedEntity(name, id), type);
      fm.beginTransaction()
          .add(R.id.fragment_container, fragment)
          .commit();
    }
  }
}
