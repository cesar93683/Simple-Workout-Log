package com.devcesar.workoutapp.mainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.sleepFor2Seconds;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_AlternatingDumbbellCurl;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_BarbellBackSquat;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_Discard;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_Save;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.widget.NumberPicker;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;
import com.devcesar.workoutapp.R;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TimerTests {

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(
      MainActivity.class);

  @Test
  public void ifTimerStartedThenPausedThenRotatedTimerShouldKeepSameTimeAsBefore() {
    onView(ViewMatchers.withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.timer_start_pause)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.timer_start_pause)).perform(click());

    rotateLandscape();

    sleepFor2Seconds();

    onView(withId(R.id.timer_display)).check(matches(withText("1:57")));
  }

  private void rotateLandscape() {
    getInstrumentation().waitForIdleSync();
    final Activity[] activity = new Activity[1];
    try {
      mActivityTestRule.runOnUiThread(() -> {
        java.util.Collection<Activity> activities = ActivityLifecycleMonitorRegistry.getInstance()
            .getActivitiesInStage(
                Stage.RESUMED);
        activity[0] = Iterables.getOnlyElement(activities);
      });
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }
    activity[0].setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
  }

  @Test
  public void ifTimerRunningShouldContinueAfterRotate() {
    onView(ViewMatchers.withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.timer_start_pause)).perform(click());

    sleepFor2Seconds();

    rotateLandscape();

    sleepFor2Seconds();

    onView(withId(R.id.timer_display)).check(matches(withText("1:55")));
  }

  @Test
  public void afterTimerIsDoneShouldReset() {
    onView(ViewMatchers.withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.timer_display)).perform(click());

    onView(withId(R.id.minutesNumberPicker)).perform(setNumber(0));

    onView(withId(R.id.secondsNumberPicker)).perform(setNumber(2));

    onView(ViewMatchers.withText(str_Save)).perform(click());

    onView(withId(R.id.timer_start_pause)).perform(click());

    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    onView(withId(R.id.timer_display)).check(matches(withText("0:02")));

    onView(withId(R.id.timer_display)).perform(click());

    onView(withId(R.id.minutesNumberPicker)).perform(setNumber(2));

    onView(withId(R.id.secondsNumberPicker)).perform(setNumber(0));

    onView(ViewMatchers.withText(str_Save)).perform(click());
  }

  private ViewAction setNumber(int i) {
    return new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        NumberPicker tp = (NumberPicker) view;
        tp.setValue(i);
      }

      @Override
      public String getDescription() {
        return "Set the passed value into the NumberPicker";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(NumberPicker.class);
      }
    };
  }

  @Test
  public void ifTimerChangedShouldUpdateTimerForAllExercises() {
    onView(ViewMatchers.withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.timer_display)).perform(click());

    onView(withId(R.id.minutesNumberPicker)).perform(setNumber(1));

    onView(withId(R.id.secondsNumberPicker)).perform(setNumber(0));

    onView(ViewMatchers.withText(str_Save)).perform(click());

    pressBack();

    onView(withText(str_BarbellBackSquat)).perform(click());

    onView(withId(R.id.timer_display)).check(matches(withText("1:00")));

    onView(withId(R.id.timer_display)).perform(click());

    onView(withId(R.id.minutesNumberPicker)).perform(setNumber(2));

    onView(withId(R.id.secondsNumberPicker)).perform(setNumber(0));

    onView(withText(str_Save)).perform(click());
  }

  @Test
  public void canResetTimer() {
    onView(ViewMatchers.withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.timer_start_pause)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.timer_reset)).perform(click());

    onView(withId(R.id.timer_display)).check(matches(withText("2:00")));

    sleepFor2Seconds();

    onView(withId(R.id.timer_display)).check(matches(withText("2:00")));
  }

  @Test
  public void canNotSetTimerWhileRunning() {
    onView(ViewMatchers.withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.timer_start_pause)).perform(click());

    try {
      Thread.sleep(900);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    onView(withId(R.id.timer_display)).perform(click());

    onView(withId(R.id.minutesNumberPicker)).check(doesNotExist());
  }

  @Test
  public void canPauseTimer() {
    onView(ViewMatchers.withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.timer_start_pause)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.timer_start_pause)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.timer_display)).check(matches(withText("1:57")));
  }

  @Test
  public void timerIconChangesWhenStartedAndStopped() {
    onView(ViewMatchers.withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.timer_start_pause)).perform(click());

    onView(withId(R.id.timer_start_pause)).check(matches(withContentDescription("Pause")));

    onView(withId(R.id.timer_start_pause)).perform(click());

    onView(withId(R.id.timer_start_pause)).check(matches(withContentDescription("Play")));
  }

  @Test
  public void canStartTimer() {
    onView(ViewMatchers.withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.timer_start_pause)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.timer_display)).check(matches(withText("1:57")));
  }

  @Test
  public void canSetTimeInDialog() {
    onView(ViewMatchers.withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.timer_display)).perform(click());

    onView(withId(R.id.minutesNumberPicker)).perform(setNumber(3));

    onView(withId(R.id.secondsNumberPicker)).perform(setNumber(59));

    onView(withText(str_Save)).perform(click());

    onView(withId(R.id.timer_display)).check(matches(withText("3:59")));

    onView(withId(R.id.timer_display)).perform(click());

    onView(withId(R.id.minutesNumberPicker)).perform(setNumber(2));

    onView(withId(R.id.secondsNumberPicker)).perform(setNumber(0));

    onView(withText(str_Save)).perform(click());
  }

  @Test
  public void shouldSetTimeCorrectlyInDialog() {
    onView(ViewMatchers.withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.timer_display)).perform(click());

    onView(withText("2")).check(matches(withText("2")));

    onView(withText("0")).check(matches(withText("0")));
  }

  @Test
  public void shouldNotAutoStartTimer() {
    onView(ViewMatchers.withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.increase_rep_button)).perform(click());

    onView(withId(R.id.add_set_button)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.timer_display)).check(matches(withText("2:00")));
  }

  @Test
  public void canTurnOnAndOffAutoStartTimer() {
    onView(withId(R.id.nav_settings)).perform(click());

    onView(withId(R.id.auto_start_timer)).perform(click());

    onView(withId(R.id.nav_exercise)).perform(click());

    onView(ViewMatchers.withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.increase_rep_button)).perform(click());

    onView(withId(R.id.add_set_button)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.timer_display)).check(matches(withText("1:57")));

    pressBack();

    onView(withText(str_Discard)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.nav_settings)).perform(click());

    onView(withId(R.id.auto_start_timer)).perform(click());

    onView(withId(R.id.nav_exercise)).perform(click());

    onView(ViewMatchers.withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.increase_rep_button)).perform(click());

    onView(withId(R.id.add_set_button)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.timer_display)).check(matches(withText("2:00")));
  }

}