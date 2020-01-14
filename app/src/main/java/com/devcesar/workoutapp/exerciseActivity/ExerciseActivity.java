package com.devcesar.workoutapp.exerciseActivity;

import static com.devcesar.workoutapp.utils.Constants.DEFAULT_START_TIME;
import static com.devcesar.workoutapp.utils.Constants.START_TIME;

import android.app.Notification;
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
import com.devcesar.workoutapp.exerciseActivity.ExerciseFragment.OnSaveSetsListener;
import com.devcesar.workoutapp.labs.WorkoutLab;
import com.devcesar.workoutapp.utils.ExerciseSet;
import com.devcesar.workoutapp.utils.NamedEntity;
import com.devcesar.workoutapp.utils.Workout;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ExerciseActivity extends AppCompatActivity implements OnSaveSetsListener {

  private static final String EXTRA_EXERCISE_NAME = "EXTRA_EXERCISE_NAME";
  private static final String EXTRA_EXERCISE_ID = "EXTRA_EXERCISE_ID";
  private static final String CHANNEL_ID = "Workout App";
  private static final String EXTRA_IS_TIMER_RUNNING = "EXTRA_IS_TIMER_RUNNING";
  private static final String EXTRA_TIME_LEFT = "EXTRA_TIME_LEFT";
  private final int NOTIFICATION_TIMER_ID = 1;
  private final int NOTIFICATION_TIMER_FINISHED_ID = 2;
  private final int EXERCISE_FRAGMENT_POSITION = 0;
  private TimerHelper timerHelper;
  private boolean isShowingNotification;
  private ImageView timerStartPause;
  private NamedEntity exercise;
  private NotificationCompat.Builder builder;
  private NotificationManagerCompat notificationManagerCompat;
  private TextView timerDisplay;

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
      throw new RuntimeException("No id given");
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

    timerStartPause = binding.timerStartPause;
    timerStartPause.setOnClickListener(view -> {
      if (timerHelper.isRunning()) {
        pauseTimer();
      } else {
        startTimer();
      }
    });

    int startTime = PreferenceManager.getDefaultSharedPreferences(this)
        .getInt(START_TIME, DEFAULT_START_TIME);
    isShowingNotification = false;
    notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());

    timerHelper = new TimerHelper(startTime) {

      @Override
      public void onTick() {
        updateTimeDisplay();
        if (isShowingNotification) {
          builder.setContentText(getTimeString());
          notificationManagerCompat.notify(NOTIFICATION_TIMER_ID, builder.build());
        }
      }

      @Override
      public void onFinish() {
        setIconToPlay();
        updateTimeDisplay();
        if (isShowingNotification) {
          cancelAllNotifications();
          showTimerFinishedNotification();
        }
      }
    };

    if (savedInstanceState != null) {
      boolean isTimerRunning = savedInstanceState.getBoolean(EXTRA_IS_TIMER_RUNNING);
      int timeLeft = savedInstanceState.getInt(EXTRA_TIME_LEFT);
      timerHelper.setTimeLeft(timeLeft);
      if (isTimerRunning) {
        startTimer();
      }
    }
    updateTimeDisplay();
  }

  private void pauseTimer() {
    timerHelper.pause();
    setIconToPlay();
  }

  private void showSetTimeDialog() {
    if (timerHelper.isTimerRunning) {
      return;
    }

    final DialogSetTimerBinding dialogBinding = DataBindingUtil
        .inflate(LayoutInflater.from(this), R.layout.dialog_set_timer, null, false);

    dialogBinding.secondsNumberPicker.setMinValue(0);
    dialogBinding.minutesNumberPicker.setMinValue(0);
    dialogBinding.secondsNumberPicker.setMaxValue(59);
    dialogBinding.minutesNumberPicker.setMaxValue(59);

    dialogBinding.secondsNumberPicker.setValue(timerHelper.startTime % 60);
    dialogBinding.minutesNumberPicker.setValue(timerHelper.startTime / 60);

    new Builder(this)
        .setTitle(getString(R.string.set_timer))
        .setNegativeButton(R.string.cancel, null)
        .setPositiveButton(R.string.save, (dialogInterface, i) -> {
          int seconds = dialogBinding.secondsNumberPicker.getValue();
          int minutes = dialogBinding.minutesNumberPicker.getValue();
          int startTime = minutes * 60 + seconds;
          timerHelper.setStartTime(startTime);

          PreferenceManager.getDefaultSharedPreferences(this)
              .edit()
              .putInt(START_TIME, startTime)
              .apply();

          updateTimeDisplay();
        })
        .setView(dialogBinding.getRoot())
        .show();
  }

  public void resetTimer() {
    timerHelper.resetTimer();
    setIconToPlay();
    updateTimeDisplay();
  }

  private void setIconToPlay() {
    timerStartPause.setImageResource(R.drawable.ic_play_arrow_white_24dp);
    timerStartPause.setContentDescription(getString(R.string.play));
  }

  public void startTimer() {
    timerHelper.startTimer();
    setIconToStop();
  }

  private void setIconToStop() {
    timerStartPause.setImageResource(R.drawable.ic_stop_white_24dp);
    timerStartPause.setContentDescription(getString(R.string.pause));
  }

  private void updateTimeDisplay() {
    timerDisplay.setText(timerHelper.getTimeString());
  }

  private void cancelAllNotifications() {
    NotificationManager notificationManager = (NotificationManager) getSystemService(
        Context.NOTIFICATION_SERVICE);
    notificationManager.cancelAll();
  }

  private void showTimerFinishedNotification() {
    createNotificationChannel();

    Intent intent = new Intent(this, ExerciseActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

    Notification timerFinishedNotification = new NotificationCompat.Builder(this,
        CHANNEL_ID)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setSmallIcon(R.drawable.ic_fitness_center_black_24dp)
        .setContentTitle(getString(R.string.timer_finished))
        .setContentIntent(pendingIntent)
        .build();

    notificationManagerCompat.notify(NOTIFICATION_TIMER_FINISHED_ID, timerFinishedNotification);
  }

  private void createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      CharSequence name = getString(R.string.channel_name);
      String description = getString(R.string.channel_description);
      int importance = NotificationManager.IMPORTANCE_LOW;
      NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
      channel.setDescription(description);

      // next 2 lines disable vibration
      channel.setVibrationPattern(new long[]{0});
      channel.enableVibration(true);

      NotificationManager notificationManager = getSystemService(NotificationManager.class);
      notificationManager.createNotificationChannel(channel);
    }
  }

  @Override
  protected void onStop() {
    super.onStop();
    if (timerHelper.isRunning()) {
      showTimerNotification();
    }
  }

  private void showTimerNotification() {
    createNotificationChannel();

    Intent intent = new Intent(this, ExerciseActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

    builder = new NotificationCompat.Builder(this, CHANNEL_ID)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setSmallIcon(R.drawable.ic_fitness_center_black_24dp)
        .setContentTitle(getString(R.string.time_left))
        .setContentText(timerHelper.getTimeString())
        .setContentIntent(pendingIntent);

    notificationManagerCompat.notify(NOTIFICATION_TIMER_ID, builder.build());
    isShowingNotification = true;
  }

  @Override
  protected void onResume() {
    super.onResume();
    updateTimeDisplay();
    cancelAllNotifications();
    isShowingNotification = false;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    timerHelper.pause();
    cancelAllNotifications();
  }

  @Override
  public void onBackPressed() {
    Fragment exerciseFragment = getSupportFragmentManager().findFragmentByTag(
        "android:switcher:" + R.id.view_pager + ":" + EXERCISE_FRAGMENT_POSITION);
    ((ExerciseFragment) exerciseFragment).onBackPressed();
  }

  @Override
  public void onSaveSets(ArrayList<ExerciseSet> exerciseSets) {
    long timeStamp = new Date().getTime();
    Workout workout = new Workout(exercise.getId(), exerciseSets, timeStamp);
    WorkoutLab.get(this).insertWorkout(workout);
    finish();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putBoolean(EXTRA_IS_TIMER_RUNNING, timerHelper.isRunning());
    outState.putInt(EXTRA_TIME_LEFT, timerHelper.getTimeLeft());
  }

  private abstract static class TimerHelper {

    private boolean isTimerRunning = false;
    private int startTime;
    private int timeLeft;
    private CountDownTimer countDownTimer;

    TimerHelper(int startTime) {
      this.startTime = startTime;
      this.timeLeft = startTime;
    }

    String getTimeString() {
      int minutes = timeLeft / 60;
      int seconds = timeLeft % 60;
      return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds);
    }

    boolean isRunning() {
      return isTimerRunning;
    }

    void startTimer() {
      countDownTimer = new CountDownTimer(timeLeft * 1000, 1000) {
        @Override
        public void onTick(long l) {
          timeLeft = (int) (l / 1000);
          TimerHelper.this.onTick();
        }

        @Override
        public void onFinish() {
          timeLeft = startTime;
          isTimerRunning = false;
          TimerHelper.this.onFinish();
        }
      }.start();
      isTimerRunning = true;
    }

    abstract public void onTick();

    abstract public void onFinish();

    void resetTimer() {
      pause();
      timeLeft = startTime;
    }

    void pause() {
      if (countDownTimer != null) {
        countDownTimer.cancel();
        isTimerRunning = false;
      }
    }

    int getTimeLeft() {
      return timeLeft;
    }

    void setTimeLeft(int timeLeft) {
      this.timeLeft = timeLeft;
    }

    void setStartTime(int startTime) {
      this.startTime = startTime;
      timeLeft = startTime;
    }
  }

  class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private final int[] TAB_TITLES = new int[]{R.string.exercise, R.string.history};

    SectionsPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public int getCount() {
      return 2;
    }

    @Override
    public Fragment getItem(int position) {
      Fragment fragment = null;
      switch (position) {
        case EXERCISE_FRAGMENT_POSITION:
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
  }
}