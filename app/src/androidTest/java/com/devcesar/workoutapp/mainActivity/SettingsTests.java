package com.devcesar.workoutapp.mainActivity;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.childAtPosition;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.sleepFor2Seconds;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import com.devcesar.workoutapp.R;
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
    onView(withId(R.id.nav_settings)).perform(click());

    onView(withId(R.id.import_default_items)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());

    onView(withId(R.id.nav_exercise)).perform(click());

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

    onView(withId(R.id.nav_category)).perform(click());

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

    onView(withId(R.id.nav_routine)).perform(click());

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
    onView(withId(R.id.nav_settings)).perform(click());

    onView(withId(R.id.delete_all_items)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());

    onView(withId(R.id.nav_exercise)).perform(click());

    onView(withText(ViewHelper.str_AlternatingDumbbellCurl)).check(doesNotExist());

    onView(withId(R.id.nav_category)).perform(click());

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

    onView(withId(R.id.nav_routine)).perform(click());

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

    onView(withId(R.id.nav_settings)).perform(click());

    onView(withId(R.id.import_default_items)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());

    onView(withId(R.id.nav_exercise)).perform(click());

    ViewInteraction textView4 = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                        1)),
                0),
            isDisplayed()));
    textView4.check(matches(withText("Alternating Dumbbell Curl")));

    onView(withId(R.id.nav_category)).perform(click());

    ViewInteraction textView5 = onView(
        allOf(withText("Back"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                        1)),
                0),
            isDisplayed()));
    textView5.check(matches(withText("Back")));

    onView(withId(R.id.nav_routine)).perform(click());

    ViewInteraction textView6 = onView(
        allOf(withText("Strong 5x5 - Workout A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                        1)),
                0),
            isDisplayed()));
    textView6.check(matches(withText("Strong 5x5 - Workout A")));
  }

  @Test
  public void canVisitSettingsTab() {
    onView(withId(R.id.nav_settings)).perform(click());

    ViewInteraction textView = onView(
        childAtPosition(
            withId(R.id.action_bar),
            0));
    textView.check(matches(withText("Settings")));
  }

  @Test
  public void shouldBeAbleToDeleteAllWorkouts() {
    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.increase_rep_button)).perform(click());

    onView(withId(R.id.add_set_button)).perform(click());

    onView(withId(R.id.finish_exercise_fab)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.nav_settings)).perform(click());

    onView(withId(R.id.delete_all_workouts)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());

    onView(withId(R.id.nav_exercise)).perform(click());

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
                    IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                    0)),
            0),
            isDisplayed()));
    linearLayout.check(doesNotExist());
  }

}
