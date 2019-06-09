package com.devcesar.workoutapp.exerciseActivity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.databinding.ActivityExerciseBinding;
import com.devcesar.workoutapp.labs.WorkoutLab;
import com.devcesar.workoutapp.utils.ExerciseSet;
import com.devcesar.workoutapp.utils.Workout;
import java.util.ArrayList;
import java.util.Date;

public class ExerciseActivity extends AppCompatActivity implements SaveSets {

  private static final String EXTRA_EXERCISE_NAME = "EXTRA_EXERCISE_NAME";
  private static final String EXTRA_EXERCISE_ID = "EXTRA_EXERCISE_ID";
  private ViewPager mViewPager;
  private int exerciseId;
  private String exerciseName;
  private WorkoutLab workoutLab;
  private ActivityExerciseBinding binding;

  public static Intent newIntent(Context packageContext, String exerciseName, int exerciseId) {
    Intent intent = new Intent(packageContext, ExerciseActivity.class);
    intent.putExtra(EXTRA_EXERCISE_NAME, exerciseName);
    intent.putExtra(EXTRA_EXERCISE_ID, exerciseId);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_exercise);

    workoutLab = WorkoutLab.get(this);
    int INVALID_ID = -1;
    exerciseId = getIntent().getIntExtra(EXTRA_EXERCISE_ID, INVALID_ID);
    exerciseName = getIntent().getStringExtra(EXTRA_EXERCISE_NAME);

    if (exerciseId == INVALID_ID) {
      finish();
    }

    setSupportActionBar(binding.toolbar);
    binding.toolbar.setTitle(exerciseName);

    SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(
        getSupportFragmentManager());

    mViewPager = binding.container;
    mViewPager.setAdapter(mSectionsPagerAdapter);

    TabLayout tabLayout = binding.tabs;

    mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    tabLayout.setupWithViewPager(mViewPager);
    tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
  }

  @Override
  public void saveSets(ArrayList<ExerciseSet> exerciseSets) {
    long timeStamp = new Date().getTime();
    Workout workout = new Workout(exerciseId, exerciseSets, timeStamp);
    workoutLab.insertWorkout(workout);
    finish();
  }

  @Override
  public void onBackPressed() {
    Fragment fragment = getSupportFragmentManager().findFragmentByTag(
        "android:switcher:" + R.id.container + ":" + mViewPager.getCurrentItem());
    if (fragment instanceof ExerciseFragment) {
      ((ExerciseFragment) fragment).onBackPressed();
    } else {
      super.onBackPressed();
    }
  }

  class SectionsPagerAdapter extends FragmentPagerAdapter {

    SectionsPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      Fragment fragment = null;
      switch (position) {
        case 0:
          fragment = ExerciseFragment.newInstance(exerciseId);
          break;
        case 1:
          fragment = WorkoutHistoryFragment.newInstance(exerciseId, exerciseName);
          break;
      }
      return fragment;
    }

    @Override
    public int getCount() {
      return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
      switch (position) {
        case 0:
          return getString(R.string.exercise);
        case 1:
          return getString(R.string.history);
      }
      return null;
    }
  }
}
