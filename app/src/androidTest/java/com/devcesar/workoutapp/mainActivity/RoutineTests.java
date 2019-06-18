package com.devcesar.workoutapp.mainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

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
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RoutineTests {

  // shouldBeAbleToCreateAndDeleteRoutine
  // shouldBeAbleToAddExerciseToRoutine
  // shouldNotBeAbleToAddRoutinesThatAreAlreadyInRoutine
  // shouldBeAbleToSortRoutine
  // shouldBeAbleToVisitExerciseFromRoutine
  // exerciseShouldBeDeletedFromRoutineIfExerciseDeletedInExerciseList
  // shouldBeAbleToDeleteExerciseFromRoutineByLongClicking
  // shouldDisplayDiscardChangesToUserIfAddsExerciseButDoesntSave
  // shouldAddExercisesToRoutineFromClickingSaveInDiscardDialog
  // shouldNotAddExercisesToRoutineFromClickingCancelInDiscardDialog
  // canAddMultipleRoutinesAtOnce

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(
      MainActivity.class);

  @Test
  public void shouldBeAbleToSwitchToRoutineCategory() {
    clickRoutineButtonInBottomNavigation();
    ViewInteraction textView = onView(
        allOf(withText("Routine"),
            childAtPosition(
                allOf(withId(R.id.action_bar),
                    childAtPosition(
                        withId(R.id.action_bar_container),
                        0)),
                0),
            isDisplayed()));
    textView.check(matches(withText("Routine")));
  }

  private void clickRoutineButtonInBottomNavigation() {
    ViewInteraction bottomNavigationItemView = onView(
        allOf(withId(R.id.nav_routine),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                2),
            isDisplayed()));
    bottomNavigationItemView.perform(click());
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
