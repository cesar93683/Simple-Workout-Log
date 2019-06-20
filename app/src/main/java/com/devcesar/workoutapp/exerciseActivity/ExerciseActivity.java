package com.devcesar.workoutapp.exerciseActivity;

import static com.devcesar.workoutapp.utils.Constants.DEFAULT_TIMER_TIME;
import static com.devcesar.workoutapp.utils.Constants.TIMER_TIME;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.preference.PreferenceManager;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.databinding.ActivityExerciseBinding;
import com.devcesar.workoutapp.databinding.DialogSetTimerBinding;
import com.devcesar.workoutapp.labs.WorkoutLab;
import com.devcesar.workoutapp.utils.ExerciseSet;
import com.devcesar.workoutapp.utils.NamedEntity;
import com.devcesar.workoutapp.utils.Workout;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ExerciseActivity extends AppCompatActivity implements SaveSets {

  private static final String EXTRA_EXERCISE_NAME = "EXTRA_EXERCISE_NAME";
  private static final String EXTRA_EXERCISE_ID = "EXTRA_EXERCISE_ID";
  private NamedEntity exercise;
  private Fragment exerciseFragment;
  private int time;
  private Button timerDisplay;

  public static Intent newIntent(Context packageContext, NamedEntity exercise) {
    Intent intent = new Intent(packageContext, ExerciseActivity.class);
    intent.putExtra(EXTRA_EXERCISE_NAME, exercise.getName());
    intent.putExtra(EXTRA_EXERCISE_ID, exercise.getId());
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityExerciseBinding binding = DataBindingUtil
        .setContentView(this, R.layout.activity_exercise);

    int invalid = -1;
    int exerciseId = getIntent().getIntExtra(EXTRA_EXERCISE_ID, invalid);
    if (exerciseId == invalid) {
      finish();
    }

    String exerciseName = getIntent().getStringExtra(EXTRA_EXERCISE_NAME);

    exercise = new NamedEntity(exerciseName, exerciseId);
    binding.title.setText(exerciseName);

    SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(
        getSupportFragmentManager());
    binding.viewPager.setAdapter(sectionsPagerAdapter);
    binding.tabs.setupWithViewPager(binding.viewPager);

    binding.timerDecrement.setOnClickListener(view -> decrement());
    binding.timerIncrement.setOnClickListener(view -> increment());

    timerDisplay = binding.timerDisplay;
    timerDisplay.setOnClickListener(view -> showSetTimeDialog());

    time = PreferenceManager.getDefaultSharedPreferences(this)
        .getInt(TIMER_TIME, DEFAULT_TIMER_TIME);

    updateTime();
  }


  private void showSetTimeDialog() {
    final DialogSetTimerBinding dialogBinding = DataBindingUtil
        .inflate(LayoutInflater.from(this), R.layout.dialog_set_timer, null, false);

    dialogBinding.secondsNumberPicker.setMinValue(0);
    dialogBinding.minutesNumberPicker.setMinValue(0);
    dialogBinding.secondsNumberPicker.setMaxValue(59);
    dialogBinding.minutesNumberPicker.setMaxValue(59);

    dialogBinding.secondsNumberPicker.setValue(time % 60);
    dialogBinding.minutesNumberPicker.setValue(time / 60);

    new AlertDialog.Builder(this)
        .setTitle(getString(R.string.title))
        .setNegativeButton(R.string.cancel, null)
        .setPositiveButton(R.string.save, (dialogInterface, i) -> {
          int seconds = dialogBinding.secondsNumberPicker.getValue();
          int minutes = dialogBinding.minutesNumberPicker.getValue();
          time = minutes * 60 + seconds;
          timeChanged();
        })
        .setView(dialogBinding.getRoot())
        .show();
  }

  private void decrement() {
    if (time == 0) {
      return;
    }
    time--;
    timeChanged();
  }

  private void increment() {
    if (time == 59 * 60 + 59) {
      return;
    }
    time++;
    timeChanged();
  }

  private void timeChanged() {
    PreferenceManager.getDefaultSharedPreferences(this)
        .edit()
        .putInt(TIMER_TIME, time)
        .apply();
    updateTime();
  }

  private void updateTime() {
    String newTime = String.format(Locale.getDefault(), "%d:%02d", time / 60, time % 60);
    timerDisplay.setText(newTime);
  }

  @Override
  public void onBackPressed() {
    ((ExerciseFragment) exerciseFragment).onBackPressed();
  }

  @Override
  public void saveSets(ArrayList<ExerciseSet> exerciseSets) {
    long timeStamp = new Date().getTime();
    Workout workout = new Workout(exercise.getId(), exerciseSets, timeStamp);
    WorkoutLab.get(this).insertWorkout(workout);
    finish();
  }

  /**
   * A [FragmentPagerAdapter] that returns a fragment corresponding to one of the
   * sections/tabs/pages.
   */
  class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private final int[] TAB_TITLES = new int[]{R.string.exercise, R.string.history};

    SectionsPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public int getCount() {
      // Show 2 total pages.
      return 2;
    }

    @Override
    public Fragment getItem(int position) {
      // getItem is called to instantiate the fragment for the given page.
      // Return a PlaceholderFragment (defined as a static inner class below).
      Fragment fragment = null;
      switch (position) {
        case 0:
          exerciseFragment = ExerciseFragment.newInstance(exercise.getId());
          fragment = exerciseFragment;
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
  }
}