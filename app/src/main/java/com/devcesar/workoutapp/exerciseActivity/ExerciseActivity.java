package com.devcesar.workoutapp.exerciseActivity;

import static com.devcesar.workoutapp.utils.Constants.DEFAULT_START_TIME;
import static com.devcesar.workoutapp.utils.Constants.START_TIME;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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
  private static final String CHANNEL_ID = "Workout App";
  private NamedEntity exercise;
  private Fragment exerciseFragment;
  private int startTime;
  private long timeLeftInMillis;
  private TextView timerDisplay;
  private ImageView startPauseButton;
  private boolean isTimerRunning;
  private CountDownTimer mCountDownTimer;
  private NotificationCompat.Builder builder;
  private boolean isShowingNotification;
  private NotificationManagerCompat notificationManagerCompat;
  private int asdsf = 1;

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
    binding.title.setText(exerciseName);

    exercise = new NamedEntity(exerciseName, exerciseId);

    SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(
        getSupportFragmentManager());
    binding.viewPager.setAdapter(sectionsPagerAdapter);
    binding.tabs.setupWithViewPager(binding.viewPager);

    timerDisplay = binding.timerDisplay;
    timerDisplay.setOnClickListener(view -> showSetTimeDialog());

    binding.timerReset.setOnClickListener(view -> resetTimer());

    startPauseButton = binding.timerPlay;
    startPauseButton.setOnClickListener(view -> {
      if (isTimerRunning) {
        pauseTimer();
      } else {
        startTimer();
      }
    });

    startTime = PreferenceManager.getDefaultSharedPreferences(this)
        .getInt(START_TIME, DEFAULT_START_TIME);
    timeLeftInMillis = startTime * 1000;
    isTimerRunning = false;
    updateTimeDisplay();
    isShowingNotification = false;
    notificationManagerCompat = NotificationManagerCompat
        .from(getApplicationContext());
  }

  private void resetTimer() {
    if (mCountDownTimer == null) {
      return;
    }
    pauseTimer();
    timeLeftInMillis = startTime * 1000;
    updateTimeDisplay();
  }

  private void pauseTimer() {
    mCountDownTimer.cancel();
    isTimerRunning = false;
    setIconToPlay();
  }

  private void setIconToPlay() {
    startPauseButton.setImageResource(R.drawable.ic_play_arrow_white_24dp);
    startPauseButton.setContentDescription(getString(R.string.play));
  }

  private void startTimer() {
    mCountDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
      @Override
      public void onTick(long l) {
        timeLeftInMillis = l;
        updateTimeDisplay();
        if(isShowingNotification) {
          builder.setContentText(getTimeString());
          notificationManagerCompat.notify(asdsf, builder.build());
        }
      }

      @Override
      public void onFinish() {
        isTimerRunning = false;
        setIconToPlay();
        timeLeftInMillis = startTime * 1000;
        updateTimeDisplay();
      }
    }.start();
    isTimerRunning = true;
    setIconToStop();
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (isTimerRunning) {
      showNotification();
    }
  }

  private void showNotification() {
    createNotificationChannel();

    Intent intent = new Intent(this, ExerciseActivity.class);
    intent.setAction(Intent.ACTION_MAIN);
    intent.addCategory(Intent.CATEGORY_LAUNCHER);

    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
        intent, 0);
    isShowingNotification = true;
    builder = new NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_add_black_24dp)
        .setContentTitle(getString(R.string.time_left))
        .setContentText(getTimeString())
        .setContentIntent(pendingIntent)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(false);

    notificationManagerCompat.notify(asdsf, builder.build());
  }

  private void createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      CharSequence name = getString(R.string.channel_name);
      String description = getString(R.string.channel_description);
      int importance = NotificationManager.IMPORTANCE_DEFAULT;
      NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
      channel.setDescription(description);
      // Register the channel with the system; you can't change the importance
      // or other notification behaviors after this
      NotificationManager notificationManager = getSystemService(NotificationManager.class);
      notificationManager.createNotificationChannel(channel);
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    clearNotifications();
  }

  private void clearNotifications() {
    NotificationManager notificationManager = (NotificationManager) getSystemService(
        Context.NOTIFICATION_SERVICE);
    notificationManager.cancel(1);
  }

  @Override
  protected void onResume() {
    super.onResume();
    clearNotifications();
  }

  private void setIconToStop() {
    startPauseButton.setImageResource(R.drawable.ic_stop_white_24dp);
    startPauseButton.setContentDescription(getString(R.string.pause));
  }

  private void showSetTimeDialog() {
    if (isTimerRunning) {
      return;
    }

    final DialogSetTimerBinding dialogBinding = DataBindingUtil
        .inflate(LayoutInflater.from(this), R.layout.dialog_set_timer, null, false);

    dialogBinding.secondsNumberPicker.setMinValue(0);
    dialogBinding.minutesNumberPicker.setMinValue(0);
    dialogBinding.secondsNumberPicker.setMaxValue(59);
    dialogBinding.minutesNumberPicker.setMaxValue(59);

    dialogBinding.secondsNumberPicker.setValue(startTime % 60);
    dialogBinding.minutesNumberPicker.setValue(startTime / 60);

    new Builder(this)
        .setTitle(getString(R.string.set_timer))
        .setNegativeButton(R.string.cancel, null)
        .setPositiveButton(R.string.save, (dialogInterface, i) -> {
          int seconds = dialogBinding.secondsNumberPicker.getValue();
          int minutes = dialogBinding.minutesNumberPicker.getValue();
          startTime = minutes * 60 + seconds;

          PreferenceManager.getDefaultSharedPreferences(this)
              .edit()
              .putInt(START_TIME, startTime)
              .apply();

          timeLeftInMillis = startTime * 1000;
          updateTimeDisplay();
        })
        .setView(dialogBinding.getRoot())
        .show();
  }

  private void updateTimeDisplay() {
    String newTime = getTimeString();
    timerDisplay.setText(newTime);
  }

  private String getTimeString() {
    int minutes = (int) (timeLeftInMillis / 1000) / 60;
    int seconds = (int) (timeLeftInMillis / 1000) % 60;
    return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds);
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