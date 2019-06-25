package com.devcesar.workoutapp.mainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
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
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;
import com.devcesar.workoutapp.R;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class WorkoutTests {

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(
      MainActivity.class);


  @Test
  public void shouldShowDiscardChangesDialogIfAddSetThenRotatedThenPressBack() {
    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.increase_rep_button)).perform(click());

    onView(withId(R.id.add_set_button)).perform(click());

    rotateLandscape();

    sleepFor2Seconds();

    pressBack();

    onView(withId(R.id.alertTitle)).check(matches(withText("Discard changes?")));
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
  public void shouldKeepSetsAndSaveAfterRotating() {
    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.increase_rep_button)).perform(click());

    onView(withId(R.id.add_set_button)).perform(click());

    rotateLandscape();

    sleepFor2Seconds();

    ViewInteraction textView = onView(
        allOf(withText("Set 1 - 1 Rep @ - LB"),
            childAtPosition(
                allOf(withId(R.id.exercise_sets_recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                        3)),
                0),
            isDisplayed()));
    textView.check(matches(withText("Set 1 - 1 Rep @ - LB")));
  }

  @Test
  public void shouldKeepRepsAndWeightAfterRotating() {
    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.increase_rep_button)).perform(click());

    onView(withId(R.id.increase_weight_button)).perform(click());

    rotateLandscape();

    sleepFor2Seconds();

    onView(withId(R.id.reps_text_input_edit_text)).check(matches(withText("1")));

    onView(withId(R.id.weight_text_input_edit_text)).check(matches(withText("1")));
  }

  @Test
  public void canAddWorkoutByClickingSaveInDiscardChangesDialog() {
    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.increase_rep_button)).perform(click());

    onView(withId(R.id.add_set_button)).perform(click());
    pressBack();

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    sleepFor2Seconds();

    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    ViewInteraction tabView = onView(
        allOf(withContentDescription("History"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.tabs),
                    0),
                1),
            isDisplayed()));
    tabView.perform(click());

    ViewInteraction linearLayout = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    0)),
            0),
            isDisplayed()));
    linearLayout.check(matches(isDisplayed()));

    ViewInteraction linearLayout2 = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    0)),
            0),
            isDisplayed()));
    linearLayout2.perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void canAddWorkoutAndDeleteWorkout() {
    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.increase_rep_button)).perform(click());

    onView(withId(R.id.add_set_button)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.finish_exercise_fab)).perform(click());

    sleepFor2Seconds();

    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    ViewInteraction tabView = onView(
        allOf(withContentDescription("History"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.tabs),
                    0),
                1),
            isDisplayed()));
    tabView.perform(click());

    ViewInteraction linearLayout = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    0)),
            0),
            isDisplayed()));
    linearLayout.check(matches(isDisplayed()));

    ViewInteraction linearLayout2 = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    0)),
            0),
            isDisplayed()));
    linearLayout2.perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());

    ViewInteraction linearLayout4 = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    0)),
            0),
            isDisplayed()));
    linearLayout4.check(doesNotExist());
  }

  @Test
  public void canEditWorkoutFromHistoryTab() {
    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.increase_rep_button)).perform(click());

    onView(withId(R.id.add_set_button)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.finish_exercise_fab)).perform(click());

    sleepFor2Seconds();

    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    ViewInteraction tabView = onView(
        allOf(withContentDescription("History"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.tabs),
                    0),
                1),
            isDisplayed()));
    tabView.perform(click());

    ViewInteraction linearLayout = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    0)),
            0),
            isDisplayed()));
    linearLayout.perform(longClick());

    onView(withId(R.id.edit_linear_layout)).perform(click());

    onView(withId(R.id.increase_rep_button)).perform(click());

    onView(withId(R.id.add_set_button)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.finish_exercise_fab)).perform(click());

    ViewInteraction textView = onView(
        allOf(withText("Set 1 - 1 Rep @ - LB"),
            childAtPosition(
                allOf(withId(R.id.exercise_sets_container),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                        1)),
                0),
            isDisplayed()));
    textView.check(matches(withText("Set 1 - 1 Rep @ - LB")));

    ViewInteraction textView2 = onView(
        allOf(withText("Set 2 - 1 Rep @ - LB"),
            childAtPosition(
                allOf(withId(R.id.exercise_sets_container),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                        1)),
                1),
            isDisplayed()));
    textView2.check(matches(withText("Set 2 - 1 Rep @ - LB")));

    ViewInteraction linearLayout3 = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    0)),
            0),
            isDisplayed()));
    linearLayout3.perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void canSaveWorkoutFromHistoryTabByClickingSaveInDiscardChangesDialog() {
    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.increase_rep_button)).perform(click());

    onView(withId(R.id.add_set_button)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.finish_exercise_fab)).perform(click());

    sleepFor2Seconds();

    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    ViewInteraction tabView = onView(
        allOf(withContentDescription("History"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.tabs),
                    0),
                1),
            isDisplayed()));
    tabView.perform(click());

    ViewInteraction linearLayout = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    0)),
            0),
            isDisplayed()));
    linearLayout.perform(longClick());

    onView(withId(R.id.edit_linear_layout)).perform(click());

    onView(withId(R.id.increase_rep_button)).perform(click());

    onView(withId(R.id.add_set_button)).perform(click());

    pressBack();

    sleepFor2Seconds();

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    ViewInteraction textView = onView(
        allOf(withText("Set 1 - 1 Rep @ - LB"),
            childAtPosition(
                allOf(withId(R.id.exercise_sets_container),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                        1)),
                0),
            isDisplayed()));
    textView.check(matches(withText("Set 1 - 1 Rep @ - LB")));

    ViewInteraction textView2 = onView(
        allOf(withText("Set 2 - 1 Rep @ - LB"),
            childAtPosition(
                allOf(withId(R.id.exercise_sets_container),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                        1)),
                1),
            isDisplayed()));
    textView2.check(matches(withText("Set 2 - 1 Rep @ - LB")));

    ViewInteraction linearLayout3 = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    0)),
            0),
            isDisplayed()));
    linearLayout3.perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void canAddSet() {
    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.increase_rep_button)).perform(click());

    onView(withId(R.id.increase_weight_button)).perform(click());

    onView(withId(R.id.add_set_button)).perform(click());

    ViewInteraction textView = onView(
        allOf(withText("Set 1 - 1 Rep @ 1 LB"),
            childAtPosition(
                allOf(withId(R.id.exercise_sets_recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                        3)),
                0),
            isDisplayed()));
    textView.check(matches(withText("Set 1 - 1 Rep @ 1 LB")));
  }

  @Test
  public void canAddSetWithNoWeight() {
    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.increase_rep_button)).perform(click());

    onView(withId(R.id.add_set_button)).perform(click());

    ViewInteraction textView = onView(
        allOf(withText("Set 1 - 1 Rep @ - LB"),
            childAtPosition(
                allOf(withId(R.id.exercise_sets_recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                        3)),
                0),
            isDisplayed()));
    textView.check(matches(withText("Set 1 - 1 Rep @ - LB")));
  }

  @Test
  public void canDeleteSet() {
    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.increase_rep_button)).perform(click());

    onView(withId(R.id.add_set_button)).perform(click());

    ViewInteraction appCompatTextView2 = onView(
        allOf(withText("Set 1 - 1 Rep @ - LB"),
            childAtPosition(
                allOf(withId(R.id.exercise_sets_recycler_view),
                    childAtPosition(
                        withClassName(is("android.widget.LinearLayout")),
                        3)),
                0),
            isDisplayed()));
    appCompatTextView2.perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());

    ViewInteraction textView = onView(
        allOf(withText("Set 1 - 1 Rep @ - LB"),
            childAtPosition(
                allOf(withId(R.id.exercise_sets_recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                        3)),
                0),
            isDisplayed()));
    textView.check(doesNotExist());
  }

  @Test
  public void canModifySet() {
    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.increase_rep_button)).perform(click());

    onView(withId(R.id.add_set_button)).perform(click());

    ViewInteraction appCompatTextView2 = onView(
        allOf(withText("Set 1 - 1 Rep @ - LB"),
            childAtPosition(
                allOf(withId(R.id.exercise_sets_recycler_view),
                    childAtPosition(
                        withClassName(is("android.widget.LinearLayout")),
                        3)),
                0),
            isDisplayed()));
    appCompatTextView2.perform(longClick());

    onView(withId(R.id.edit_linear_layout)).perform(click());

    onView(withId(R.id.increase_rep_button)).perform(click());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    ViewInteraction textView = onView(
        allOf(withText("Set 1 - 2 Reps @ - LB"),
            childAtPosition(
                allOf(withId(R.id.exercise_sets_recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                        3)),
                0),
            isDisplayed()));
    textView.check(matches(withText("Set 1 - 2 Reps @ - LB")));
  }

  @Test
  public void givesErrorIfTryToAddSetWithRepsInputEmptyOrAtZero() {
    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.add_set_button)).perform(click());

    onView(withId(R.id.textinput_error)).check(matches(isDisplayed()));

    onView(withId(R.id.decrease_rep_button)).perform(click());

    onView(withId(R.id.add_set_button)).perform(click());

    onView(withId(R.id.textinput_error)).check(matches(isDisplayed()));
  }

  @Test
  public void setAndWeightInputIncrementAndDecrementProperly() {
    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.decrease_rep_button)).perform(click());

    ViewInteraction editText = onView(
        allOf(withText("0"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.reps_text_input_layout),
                    0),
                0),
            isDisplayed()));
    editText.check(matches(withText("0")));

    onView(withId(R.id.increase_weight_button)).perform(click());

    ViewInteraction editText2 = onView(
        allOf(withText("1"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.weight_text_input_layout),
                    0),
                0),
            isDisplayed()));
    editText2.check(matches(withText("1")));

    onView(withId(R.id.increase_rep_button)).perform(click());

    ViewInteraction editText3 = onView(
        allOf(withText("1"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.reps_text_input_layout),
                    0),
                0),
            isDisplayed()));
    editText3.check(matches(withText("1")));

    onView(withId(R.id.decrease_weight_button)).perform(click());

    ViewInteraction editText4 = onView(
        allOf(withText("0"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.weight_text_input_layout),
                    0),
                0),
            isDisplayed()));
    editText4.check(matches(withText("0")));
  }
}