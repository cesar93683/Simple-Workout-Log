package com.devcesar.workoutapp.exerciseActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.labs.WorkoutLab;
import com.devcesar.workoutapp.utils.ExerciseSet;
import com.devcesar.workoutapp.utils.NamedEntity;
import com.devcesar.workoutapp.utils.Workout;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.Date;

public class ExerciseActivity extends AppCompatActivity implements SaveSets {

  private static final String EXTRA_EXERCISE_NAME = "EXTRA_EXERCISE_NAME";
  private static final String EXTRA_EXERCISE_ID = "EXTRA_EXERCISE_ID";
  private ViewPager mViewPager;
  private NamedEntity exercise;

  public static Intent newIntent(Context packageContext, NamedEntity exercise) {
    Intent intent = new Intent(packageContext, ExerciseActivity.class);
    intent.putExtra(EXTRA_EXERCISE_NAME, exercise.getName());
    intent.putExtra(EXTRA_EXERCISE_ID, exercise.getId());
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_exercise);

    int invalid = -1;
    int exerciseId = getIntent().getIntExtra(EXTRA_EXERCISE_ID, invalid);
    if (exerciseId == invalid) {
      finish();
    }

    String exerciseName = getIntent().getStringExtra(EXTRA_EXERCISE_NAME);

    exercise = new NamedEntity(exerciseName, exerciseId);
    TextView titleTextView = findViewById(R.id.title);
    titleTextView.setText(exerciseName);

    SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(
        getSupportFragmentManager());
    mViewPager = findViewById(R.id.view_pager);
    mViewPager.setAdapter(sectionsPagerAdapter);
    TabLayout tabs = findViewById(R.id.tabs);
    tabs.setupWithViewPager(mViewPager);
  }

  @Override
  public void saveSets(ArrayList<ExerciseSet> exerciseSets) {
    long timeStamp = new Date().getTime();
    Workout workout = new Workout(exercise.getId(), exerciseSets, timeStamp);
    WorkoutLab.get(this).insertWorkout(workout);
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

  /**
   * A [FragmentPagerAdapter] that returns a fragment corresponding to one of the
   * sections/tabs/pages.
   */
  public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private final int[] TAB_TITLES = new int[]{R.string.exercise, R.string.history};

    SectionsPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      // getItem is called to instantiate the fragment for the given page.
      // Return a PlaceholderFragment (defined as a static inner class below).
      Fragment fragment = null;
      switch (position) {
        case 0:
          fragment = ExerciseFragment.newInstance(exercise.getId());
          break;
        case 1:
          fragment = WorkoutHistoryFragment.newInstance(exercise);
          break;
      }
      return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
      return getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
      // Show 2 total pages.
      return 2;
    }
  }
}