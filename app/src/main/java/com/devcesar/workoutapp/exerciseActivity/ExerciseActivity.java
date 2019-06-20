package com.devcesar.workoutapp.exerciseActivity;

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
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.databinding.ActivityExerciseBinding;
import com.devcesar.workoutapp.databinding.DialogSetTimerBinding;
import com.devcesar.workoutapp.labs.WorkoutLab;
import com.devcesar.workoutapp.utils.ExerciseSet;
import com.devcesar.workoutapp.utils.NamedEntity;
import com.devcesar.workoutapp.utils.Workout;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExerciseActivity extends AppCompatActivity implements SaveSets {

  private static final String EXTRA_EXERCISE_NAME = "EXTRA_EXERCISE_NAME";
  private static final String EXTRA_EXERCISE_ID = "EXTRA_EXERCISE_ID";
  private static final String TAG = "ExerciseActivity";
  private NamedEntity exercise;
  private Fragment exerciseFragment;

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

    binding.timerDecrement.setOnClickListener(view -> decrement(binding.timerDisplay));
    binding.timerIncrement.setOnClickListener(view -> increment(binding.timerDisplay));
    binding.timerDisplay.setOnClickListener(view -> showSetTimeDialog(binding.timerDisplay));
  }

  private void showSetTimeDialog(Button timerDisplay) {
    final DialogSetTimerBinding dialogBinding = DataBindingUtil
        .inflate(LayoutInflater.from(this), R.layout.dialog_set_timer, null, false);

    dialogBinding.secondsNumberPicker.setMinValue(0);
    dialogBinding.minutesNumberPicker.setMinValue(0);
    dialogBinding.secondsNumberPicker.setMaxValue(59);
    dialogBinding.minutesNumberPicker.setMaxValue(59);

    dialogBinding.secondsNumberPicker.setValue(getSeconds(timerDisplay));
    dialogBinding.minutesNumberPicker.setValue(getMinutes(timerDisplay));

    new AlertDialog.Builder(this)
        .setTitle(getString(R.string.title))
        .setNegativeButton(R.string.cancel, null)
        .setPositiveButton(R.string.save, null)
        .setView(dialogBinding.getRoot())
        .show();
  }

  public int getMinutes(Button timerDisplay) {
    Matcher matcher = getMatcher(timerDisplay);
    if (matcher.matches()) {
      return Integer.parseInt(matcher.group(1));
    }
    return 0;
  }

  public int getSeconds(Button timerDisplay) {
    Matcher matcher = getMatcher(timerDisplay);
    if (matcher.matches()) {
      return Integer.parseInt(matcher.group(2));
    }
    return 0;
  }

  private void decrement(Button timerDisplay) {
    int minutes = getMinutes(timerDisplay);
    int seconds = getSeconds(timerDisplay);
    seconds--;
    if (seconds == -1) {
      seconds = 59;
      minutes--;
    }
    if (minutes == -1) {
      minutes = 0;
      seconds = 0;
    }
    setTime(timerDisplay, minutes, seconds);
  }

  private void increment(Button timerDisplay) {
    int minutes = getMinutes(timerDisplay);
    int seconds = getSeconds(timerDisplay);
    seconds++;
    if (seconds == 60) {
      seconds = 0;
      minutes++;
    }
    if (minutes > 59) {
      minutes = 59;
      seconds = 59;
    }
    setTime(timerDisplay, minutes, seconds);
  }

  private Matcher getMatcher(Button timerDisplay) {
    String time = timerDisplay.getText().toString();
    Pattern pattern = Pattern.compile("(\\d{1,2}):(\\d{2})");
    return pattern.matcher(time);
  }

  private void setTime(Button timerDisplay, int minutes, int seconds) {
    String newTime = minutes + ":" + (seconds < 10 ? "0" + seconds : seconds);
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