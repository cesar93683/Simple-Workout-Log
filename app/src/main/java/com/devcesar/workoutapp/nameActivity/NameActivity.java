package com.devcesar.workoutapp.nameActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.utils.Constants;

public class NameActivity extends AppCompatActivity {

  private static final String EXTRA_NAME = "EXTRA_NAME";
  private static final String EXTRA_ID = "EXTRA_ID";
  private static final String EXTRA_TYPE = "EXTRA_TYPE";

  public static Intent newIntent(Context packageContext, String name, int id, int type) {
    Intent intent = new Intent(packageContext, NameActivity.class);
    intent.putExtra(EXTRA_NAME, name);
    intent.putExtra(EXTRA_ID, id);
    intent.putExtra(EXTRA_TYPE, type);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fragment);

    String name = getIntent().getStringExtra(EXTRA_NAME);
    int id = getIntent().getIntExtra(EXTRA_ID, -1);
    if (id == -1) {
      finish();
    }
    int type = getIntent().getIntExtra(EXTRA_TYPE, Constants.TYPE_CATEGORY);
    setTitle(name);

    FragmentManager fm = getSupportFragmentManager();
    Fragment fragment = fm.findFragmentById(R.id.fragment_container);
    if (fragment == null) {
      fragment = NameFragment.newInstance(id, name, type);
      fm.beginTransaction()
          .add(R.id.fragment_container, fragment)
          .commit();
    }
  }
}
