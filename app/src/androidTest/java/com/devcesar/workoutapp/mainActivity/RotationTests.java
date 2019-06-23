package com.devcesar.workoutapp.mainActivity;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
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
