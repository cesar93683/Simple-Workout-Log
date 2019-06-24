package com.devcesar.workoutapp.mainActivity;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.getAlternatingDumbbellCurlFromExerciseTabInMainActivity;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

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
public class SettingsTests {

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(
      MainActivity.class);

  @Test
  public void shouldNotImportDuplicatesIfImportMultipleTimes() {
    ViewInteraction bottomNavigationItemView = onView(
        allOf(withId(R.id.nav_settings), withContentDescription("Settings"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                3),
            isDisplayed()));
    bottomNavigationItemView.perform(click());

    ViewInteraction appCompatTextView = onView(
        allOf(withId(R.id.import_default_items),
            withText("Import Default Exercises, Categories, Routines"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    0),
                2),
            isDisplayed()));
    appCompatTextView.perform(click());

    ViewInteraction appCompatButton = onView(
        allOf(withId(android.R.id.button1), withText("Yes"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton.perform(scrollTo(), click());

    ViewInteraction bottomNavigationItemView2 = onView(
        allOf(withId(R.id.nav_exercise), withContentDescription("Exercise"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                0),
            isDisplayed()));
    bottomNavigationItemView2.perform(click());

    ViewInteraction textView = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                        1)),
                0),
            isDisplayed()));
    textView.check(matches(withText("Alternating Dumbbell Curl")));

    ViewInteraction textView2 = onView(
        allOf(withText("Barbell Back Squat"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                        1)),
                1),
            isDisplayed()));
    textView2.check(matches(withText("Barbell Back Squat")));

    ViewInteraction bottomNavigationItemView3 = onView(
        allOf(withId(R.id.nav_category), withContentDescription("Category"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                1),
            isDisplayed()));
    bottomNavigationItemView3.perform(click());

    ViewInteraction textView3 = onView(
        allOf(withText("Back"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                        1)),
                0),
            isDisplayed()));
    textView3.check(matches(withText("Back")));

    ViewInteraction textView4 = onView(
        allOf(withText("Biceps"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                        1)),
                1),
            isDisplayed()));
    textView4.check(matches(withText("Biceps")));

    ViewInteraction bottomNavigationItemView4 = onView(
        allOf(withId(R.id.nav_routine), withContentDescription("Routine"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                2),
            isDisplayed()));
    bottomNavigationItemView4.perform(click());

    ViewInteraction textView5 = onView(
        allOf(withText("Strong 5x5 - Workout A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                        1)),
                0),
            isDisplayed()));
    textView5.check(matches(withText("Strong 5x5 - Workout A")));

    ViewInteraction textView6 = onView(
        allOf(withText("Strong 5x5 - Workout B"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                        1)),
                1),
            isDisplayed()));
    textView6.check(matches(withText("Strong 5x5 - Workout B")));
  }

  @Test
  public void canDeleteAndImportAllExercisesCategoriesRoutines() {
    ViewInteraction bottomNavigationItemView3 = onView(
        allOf(withId(R.id.nav_settings), withContentDescription("Settings"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                3),
            isDisplayed()));
    bottomNavigationItemView3.perform(click());

    ViewInteraction appCompatTextView = onView(
        allOf(withId(R.id.delete_all_items),
            withText("Delete All Exercises, Categories, and Routines"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    0),
                1),
            isDisplayed()));
    appCompatTextView.perform(click());

    ViewInteraction appCompatButton = onView(
        allOf(withId(android.R.id.button1), withText("Yes"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton.perform(scrollTo(), click());

    ViewInteraction bottomNavigationItemView4 = onView(
        allOf(withId(R.id.nav_exercise), withContentDescription("Exercise"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                0),
            isDisplayed()));
    bottomNavigationItemView4.perform(click());

    ViewInteraction textView = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                        1)),
                0),
            isDisplayed()));
    textView.check(doesNotExist());

    ViewInteraction bottomNavigationItemView5 = onView(
        allOf(withId(R.id.nav_category), withContentDescription("Category"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                1),
            isDisplayed()));
    bottomNavigationItemView5.perform(click());

    ViewInteraction textView2 = onView(
        allOf(withText("Back"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                        1)),
                0),
            isDisplayed()));
    textView2.check(doesNotExist());

    ViewInteraction bottomNavigationItemView6 = onView(
        allOf(withId(R.id.nav_routine), withContentDescription("Routine"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                2),
            isDisplayed()));
    bottomNavigationItemView6.perform(click());

    ViewInteraction textView3 = onView(
        allOf(withText("Strong 5x5 - Workout A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                        1)),
                0),
            isDisplayed()));
    textView3.check(doesNotExist());

    ViewInteraction bottomNavigationItemView7 = onView(
        allOf(withId(R.id.nav_settings), withContentDescription("Settings"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                3),
            isDisplayed()));
    bottomNavigationItemView7.perform(click());

    ViewInteraction appCompatTextView2 = onView(
        allOf(withId(R.id.import_default_items),
            withText("Import Default Exercises, Categories, Routines"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    0),
                2),
            isDisplayed()));
    appCompatTextView2.perform(click());

    ViewInteraction appCompatButton2 = onView(
        allOf(withId(android.R.id.button1), withText("Yes"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton2.perform(scrollTo(), click());

    ViewInteraction bottomNavigationItemView8 = onView(
        allOf(withId(R.id.nav_exercise), withContentDescription("Exercise"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                0),
            isDisplayed()));
    bottomNavigationItemView8.perform(click());

    ViewInteraction textView4 = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                        1)),
                0),
            isDisplayed()));
    textView4.check(matches(isDisplayed()));

    ViewInteraction bottomNavigationItemView9 = onView(
        allOf(withId(R.id.nav_category), withContentDescription("Category"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                1),
            isDisplayed()));
    bottomNavigationItemView9.perform(click());

    ViewInteraction textView5 = onView(
        allOf(withText("Back"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                        1)),
                0),
            isDisplayed()));
    textView5.check(matches(isDisplayed()));

    ViewInteraction bottomNavigationItemView10 = onView(
        allOf(withId(R.id.nav_routine), withContentDescription("Routine"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                2),
            isDisplayed()));
    bottomNavigationItemView10.perform(click());

    ViewInteraction textView6 = onView(
        allOf(withText("Strong 5x5 - Workout A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                        1)),
                0),
            isDisplayed()));
    textView6.check(matches(isDisplayed()));
  }

  @Test
  public void canVisitSettingsTab() {
    ViewInteraction bottomNavigationItemView = onView(
        allOf(withId(R.id.nav_settings), withContentDescription("Settings"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                3),
            isDisplayed()));
    bottomNavigationItemView.perform(click());

    ViewInteraction textView = onView(
        allOf(withText("Settings"),
            childAtPosition(
                allOf(withId(R.id.action_bar),
                    childAtPosition(
                        withId(R.id.action_bar_container),
                        0)),
                0),
            isDisplayed()));
    textView.check(matches(withText("Settings")));
  }

  @Test
  public void shouldBeAbleToDeleteAllWorkouts() {
    getAlternatingDumbbellCurlFromExerciseTabInMainActivity().perform(click());

    ViewInteraction appCompatButton = onView(
        allOf(withId(R.id.increase_rep_button),
            childAtPosition(
                childAtPosition(
                    withId(R.id.exercise_set_editor),
                    0),
                2),
            isDisplayed()));
    appCompatButton.perform(click());

    ViewInteraction appCompatButton2 = onView(
        allOf(withId(R.id.add_set_button),
            childAtPosition(
                childAtPosition(
                    withClassName(is("android.widget.LinearLayout")),
                    0),
                1),
            isDisplayed()));
    appCompatButton2.perform(click());

    ViewInteraction floatingActionButton = onView(
        allOf(withId(R.id.finish_exercise_fab),
            childAtPosition(
                allOf(withId(R.id.coordinator_layout),
                    withParent(withId(R.id.view_pager))),
                1),
            isDisplayed()));
    floatingActionButton.perform(click());

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction bottomNavigationItemView = onView(
        allOf(withId(R.id.nav_settings), withContentDescription("Settings"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                3),
            isDisplayed()));
    bottomNavigationItemView.perform(click());

    ViewInteraction appCompatTextView3 = onView(
        allOf(withId(R.id.delete_all_workouts), withText("Delete All Workouts"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    0),
                0),
            isDisplayed()));
    appCompatTextView3.perform(click());

    ViewInteraction appCompatButton3 = onView(
        allOf(withId(android.R.id.button1), withText("Yes"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton3.perform(scrollTo(), click());

    ViewInteraction bottomNavigationItemView2 = onView(
        allOf(withId(R.id.nav_exercise), withContentDescription("Exercise"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                0),
            isDisplayed()));
    bottomNavigationItemView2.perform(click());

    getAlternatingDumbbellCurlFromExerciseTabInMainActivity().perform(click());

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
                    IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                    0)),
            0),
            isDisplayed()));
    linearLayout.check(doesNotExist());
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
