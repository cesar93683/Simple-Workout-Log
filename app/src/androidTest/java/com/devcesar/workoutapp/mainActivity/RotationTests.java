package com.devcesar.workoutapp.mainActivity;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.content.pm.ActivityInfo;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import com.devcesar.workoutapp.R;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RotationTests {

  // ifTimerRunningShouldContinueAfterRotate
  // ifTimerStartedThenPausedThenRotatedTimerShouldKeepSameTimeAsBefore

  // AddExercises
  // exercisesShouldRemainedCheckedAndCanBeAddedAfterRotating
  // shouldKeepFilterAfterRotating

  // EditRoutine
  // shouldShowDiscardChangesDialogIfModifiedThenRotatedThenPressBack
  // shouldSaveNewOrderIfChangedOrderThenRotated
  // shouldKeepNewExercisesAddedAfterAddingThenRotating

  // Workout
  // shouldKeepRepsAndWeightAfterRotating
  // shouldKeepSetsAndSaveAfterRotating
  // shouldShowDiscardChangesDialogIfAddSetThenRotatedThenPressBack

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(
      MainActivity.class);

  @Test
  public void shouldKeepFilterAfterRotating() {
    ViewInteraction appCompatEditText = onView(
        allOf(withId(R.id.filter_edit_text),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    0),
                0),
            isDisplayed()));
    appCompatEditText.perform(replaceText("sq"), closeSoftKeyboard());

    mActivityTestRule.getActivity()
        .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    ViewInteraction editText = onView(
        allOf(withId(R.id.filter_edit_text), withText("sq"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    0),
                0),
            isDisplayed()));
    editText.check(matches(withText("sq")));

    ViewInteraction textView = onView(
        allOf(withText("Barbell Back Squat"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                        1)),
                0),
            isDisplayed()));
    textView.check(matches(withText("Barbell Back Squat")));
  }

  private static Matcher<View> childAtPosition(
      final Matcher<View> parentMatcher, final int position) {

    return new TypeSafeMatcher<View>() {
      @Override
      public void describeTo(Description description) {
        description.appendText("Child at position " + position + " in parent ");
        parentMatcher.describeTo(description);
      }

      @Override
      public boolean matchesSafely(View view) {
        ViewParent parent = view.getParent();
        return parent instanceof ViewGroup && parentMatcher.matches(parent)
            && view.equals(((ViewGroup) parent).getChildAt(position));
      }
    };
  }
}
