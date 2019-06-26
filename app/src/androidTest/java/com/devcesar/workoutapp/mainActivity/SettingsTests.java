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
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_AlternatingDumbbellCurl;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_Back;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_BarbellBackSquat;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_Biceps;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_Strong5x5WorkoutA;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_Strong5x5WorkoutB;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import com.devcesar.workoutapp.R;
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

    onView(allOf(childAtPosition(withId(R.id.recycler_view), 0), isDisplayed()))
        .check(matches(withText(str_AlternatingDumbbellCurl)));

    onView(allOf(childAtPosition(withId(R.id.recycler_view), 1), isDisplayed()))
        .check(matches(withText(str_BarbellBackSquat)));

    onView(withId(R.id.nav_category)).perform(click());

    onView(allOf(childAtPosition(withId(R.id.recycler_view), 0), isDisplayed()))
        .check(matches(withText(str_Back)));

    onView(allOf(childAtPosition(withId(R.id.recycler_view), 1), isDisplayed()))
        .check(matches(withText(str_Biceps)));

    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(childAtPosition(withId(R.id.recycler_view), 0), isDisplayed()))
        .check(matches(withText(str_Strong5x5WorkoutA)));

    onView(allOf(childAtPosition(withId(R.id.recycler_view), 1), isDisplayed()))
        .check(matches(withText(str_Strong5x5WorkoutB)));

  }

  @Test
  public void canDeleteAndImportAllExercisesCategoriesRoutines() {
    onView(withId(R.id.nav_settings)).perform(click());

    onView(withId(R.id.delete_all_items)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());

    onView(withId(R.id.nav_exercise)).perform(click());

    onView(withText(ViewHelper.str_AlternatingDumbbellCurl)).check(doesNotExist());

    onView(withId(R.id.nav_category)).perform(click());

    onView(withText(str_Back)).check(doesNotExist());

    onView(withId(R.id.nav_routine)).perform(click());

    onView(withText(str_Strong5x5WorkoutA)).check(doesNotExist());

    onView(withId(R.id.nav_settings)).perform(click());

    onView(withId(R.id.import_default_items)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());

    onView(withId(R.id.nav_exercise)).perform(click());

    onView(allOf(childAtPosition(withId(R.id.recycler_view), 0), isDisplayed()))
        .check(matches(withText(str_AlternatingDumbbellCurl)));

    onView(withId(R.id.nav_category)).perform(click());

    onView(allOf(childAtPosition(withId(R.id.recycler_view), 0), isDisplayed()))
        .check(matches(withText(str_Back)));

    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(childAtPosition(withId(R.id.recycler_view), 0), isDisplayed()))
        .check(matches(withText(str_Strong5x5WorkoutA)));
  }

  @Test
  public void canVisitSettingsTab() {
    onView(withId(R.id.nav_settings)).perform(click());

    onView(childAtPosition(withId(R.id.action_bar), 0)).check(matches(withText("Settings")));
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

    onView(withContentDescription("History")).perform(click());

    onView(childAtPosition(withId(R.id.recycler_view), 0)).check(doesNotExist());
  }

}
