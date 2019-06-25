package com.devcesar.workoutapp.mainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.childAtPosition;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.sleepFor2Seconds;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.widget.NumberPicker;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
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
    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

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
    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.timer_start_pause)).perform(click());

    sleepFor2Seconds();

    rotateLandscape();

    sleepFor2Seconds();

    onView(withId(R.id.timer_display)).check(matches(withText("1:56")));
  }

  @Test
  public void afterTimerIsDoneShouldReset() {
    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.timer_display)).perform(click());

    ViewInteraction numberPicker = onView(
        allOf(childAtPosition(
            childAtPosition(
                withId(R.id.custom),
                0),
            0),
            isDisplayed()));
    numberPicker.perform(new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        NumberPicker tp = (NumberPicker) view;
        tp.setValue(0);
      }

      @Override
      public String getDescription() {
        return "Set the passed value into the NumberPicker";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(NumberPicker.class);
      }
    });

    ViewInteraction numberPicker2 = onView(
        allOf(childAtPosition(
            childAtPosition(
                withId(R.id.custom),
                0),
            2),
            isDisplayed()));
    numberPicker2.perform(new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        NumberPicker tp = (NumberPicker) view;
        tp.setValue(2);
      }

      @Override
      public String getDescription() {
        return "Set the passed value into the NumberPicker";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(NumberPicker.class);
      }
    });

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    onView(withId(R.id.timer_start_pause)).perform(click());

    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    onView(withId(R.id.timer_display)).check(matches(withText("0:02")));

    onView(withId(R.id.timer_display)).perform(click());

    ViewInteraction numberPicker3 = onView(
        allOf(childAtPosition(
            childAtPosition(
                withId(R.id.custom),
                0),
            0),
            isDisplayed()));
    numberPicker3.perform(new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        NumberPicker tp = (NumberPicker) view;
        tp.setValue(2);
      }

      @Override
      public String getDescription() {
        return "Set the passed value into the NumberPicker";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(NumberPicker.class);
      }
    });

    ViewInteraction numberPicker4 = onView(
        allOf(childAtPosition(
            childAtPosition(
                withId(R.id.custom),
                0),
            2),
            isDisplayed()));
    numberPicker4.perform(new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        NumberPicker tp = (NumberPicker) view;
        tp.setValue(0);
      }

      @Override
      public String getDescription() {
        return "Set the passed value into the NumberPicker";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(NumberPicker.class);
      }
    });

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());
  }

  @Test
  public void ifTimerChangedShouldUpdateTimerForAllExercises() {
    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.timer_display)).perform(click());

    ViewInteraction numberPicker = onView(
        allOf(childAtPosition(
            childAtPosition(
                withId(R.id.custom),
                0),
            0),
            isDisplayed()));
    numberPicker.perform(new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        NumberPicker tp = (NumberPicker) view;
        tp.setValue(1);
      }

      @Override
      public String getDescription() {
        return "Set the passed value into the NumberPicker";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(NumberPicker.class);
      }
    });

    ViewInteraction numberPicker2 = onView(
        allOf(childAtPosition(
            childAtPosition(
                withId(R.id.custom),
                0),
            2),
            isDisplayed()));
    numberPicker2.perform(new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        NumberPicker tp = (NumberPicker) view;
        tp.setValue(0);
      }

      @Override
      public String getDescription() {
        return "Set the passed value into the NumberPicker";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(NumberPicker.class);
      }
    });

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    pressBack();

    ViewInteraction appCompatTextView3 = onView(
        allOf(withText("Barbell Back Squat"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                1),
            isDisplayed()));
    appCompatTextView3.perform(click());

    onView(withId(R.id.timer_display)).check(matches(withText("1:00")));

    onView(withId(R.id.timer_display)).perform(click());

    ViewInteraction numberPicker3 = onView(
        allOf(childAtPosition(
            childAtPosition(
                withId(R.id.custom),
                0),
            0),
            isDisplayed()));
    numberPicker3.perform(new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        NumberPicker tp = (NumberPicker) view;
        tp.setValue(2);
      }

      @Override
      public String getDescription() {
        return "Set the passed value into the NumberPicker";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(NumberPicker.class);
      }
    });

    ViewInteraction numberPicker4 = onView(
        allOf(childAtPosition(
            childAtPosition(
                withId(R.id.custom),
                0),
            2),
            isDisplayed()));
    numberPicker4.perform(new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        NumberPicker tp = (NumberPicker) view;
        tp.setValue(0);
      }

      @Override
      public String getDescription() {
        return "Set the passed value into the NumberPicker";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(NumberPicker.class);
      }
    });

    onView(withText("Save")).perform(click());
  }

  @Test
  public void canResetTimer() {
    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.timer_start_pause)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.timer_reset)).perform(click());

    onView(withId(R.id.timer_display)).check(matches(withText("2:00")));

    sleepFor2Seconds();

    onView(withId(R.id.timer_display)).check(matches(withText("2:00")));
  }

  @Test
  public void canNotSetTimerWhileRunning() {
    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.timer_start_pause)).perform(click());

    try {
      Thread.sleep(900);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    onView(withId(R.id.timer_display)).perform(click());

    ViewInteraction numberPicker = onView(
        allOf(childAtPosition(
            childAtPosition(
                withId(R.id.custom),
                0),
            0),
            isDisplayed()));
    numberPicker.check(doesNotExist());
  }

  @Test
  public void canPauseTimer() {
    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.timer_start_pause)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.timer_start_pause)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.timer_display)).check(matches(withText("1:57")));
  }

  @Test
  public void timerIconChangesWhenStartedAndStopped() {
    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.timer_start_pause)).perform(click());

    onView(withId(R.id.timer_start_pause)).check(matches(withContentDescription("Pause")));

    onView(withId(R.id.timer_start_pause)).perform(click());

    onView(withId(R.id.timer_start_pause)).check(matches(withContentDescription("Play")));
  }

  @Test
  public void canStartTimer() {
    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.timer_start_pause)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.timer_display)).check(matches(withText("1:57")));
  }

  @Test
  public void canSetTimeInDialog() {
    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.timer_display)).perform(click());

    ViewInteraction numberPicker = onView(
        allOf(childAtPosition(
            childAtPosition(
                withId(R.id.custom),
                0),
            0),
            isDisplayed()));
    numberPicker.perform(new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        NumberPicker tp = (NumberPicker) view;
        tp.setValue(3);
      }

      @Override
      public String getDescription() {
        return "Set the passed value into the NumberPicker";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(NumberPicker.class);
      }
    });

    ViewInteraction numberPicker2 = onView(
        allOf(childAtPosition(
            childAtPosition(
                withId(R.id.custom),
                0),
            2),
            isDisplayed()));
    numberPicker2.perform(new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        NumberPicker tp = (NumberPicker) view;
        tp.setValue(59);
      }

      @Override
      public String getDescription() {
        return "Set the passed value into the NumberPicker";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(NumberPicker.class);
      }
    });

    onView(withText("Save")).perform(click());

    onView(withId(R.id.timer_display)).check(matches(withText("3:59")));

    onView(withId(R.id.timer_display)).perform(click());

    ViewInteraction numberPicker3 = onView(
        allOf(childAtPosition(
            childAtPosition(
                withId(R.id.custom),
                0),
            0),
            isDisplayed()));
    numberPicker3.perform(new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        NumberPicker tp = (NumberPicker) view;
        tp.setValue(2);
      }

      @Override
      public String getDescription() {
        return "Set the passed value into the NumberPicker";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(NumberPicker.class);
      }
    });

    ViewInteraction numberPicker4 = onView(
        allOf(childAtPosition(
            childAtPosition(
                withId(R.id.custom),
                0),
            2),
            isDisplayed()));
    numberPicker4.perform(new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        NumberPicker tp = (NumberPicker) view;
        tp.setValue(0);
      }

      @Override
      public String getDescription() {
        return "Set the passed value into the NumberPicker";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(NumberPicker.class);
      }
    });

    onView(withText("Save")).perform(click());
  }

  @Test
  public void shouldSetTimeCorrectlyInDialog() {
    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.timer_display)).perform(click());

    onView(withText("2")).check(matches(withText("2")));

    onView(withText("0")).check(matches(withText("0")));
  }

  @Test
  public void shouldNotAutoStartTimer() {
    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

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

    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.increase_rep_button)).perform(click());

    onView(withId(R.id.add_set_button)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.timer_display)).check(matches(withText("1:57")));

    pressBack();

    onView(withText("Discard")).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.nav_settings)).perform(click());

    onView(withId(R.id.auto_start_timer)).perform(click());

    onView(withId(R.id.nav_exercise)).perform(click());

    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.increase_rep_button)).perform(click());

    onView(withId(R.id.add_set_button)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.timer_display)).check(matches(withText("2:00")));
  }

}