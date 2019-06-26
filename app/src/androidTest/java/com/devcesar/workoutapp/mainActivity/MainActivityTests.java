package com.devcesar.workoutapp.mainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.childAtPosition;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.sleepFor2Seconds;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_AlternatingDumbbellCurl;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_BarbellBackSquat;
import static org.hamcrest.Matchers.allOf;

import android.content.pm.ActivityInfo;
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
public class MainActivityTests {

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(
      MainActivity.class);

  @Test
  public void shouldBeAbleToRenameExercise() {
    onView(withText(str_AlternatingDumbbellCurl)).perform(longClick());

    onView(withId(R.id.edit_linear_layout)).perform(click());

    String alternatingDumbbellCurl2 = "Alternating Dumbbell Curl2";

    onView(withId(R.id.text_input_edit_text))
        .perform(replaceText(alternatingDumbbellCurl2), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    onView(childAtPosition(withId(R.id.recycler_view), 0))
        .check(matches(withText(alternatingDumbbellCurl2)));

    onView(withText(alternatingDumbbellCurl2)).perform(longClick());

    onView(withId(R.id.edit_linear_layout)).perform(click());

    onView(withId(R.id.text_input_edit_text))
        .perform(replaceText(str_AlternatingDumbbellCurl), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());
  }

  @Test
  public void shouldBeAbleToFilterExercises() {
    onView(allOf(withId(R.id.filter_edit_text), isDisplayed()))
        .perform(replaceText("barbell"), closeSoftKeyboard());

    onView(childAtPosition(withId(R.id.recycler_view), 0))
        .check(matches(withText(str_BarbellBackSquat)));
  }

  @Test
  public void shouldRenderErrorWhenTryingToCreateExerciseWithNoName() {
    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    onView(withId(R.id.textinput_error)).check(matches(withText("Please enter a name.")));
  }

  @Test
  public void shouldRenderErrorWhenTryingToCreateExerciseWithExistingName() {
    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text))
        .perform(replaceText(str_AlternatingDumbbellCurl), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    onView(withId(R.id.textinput_error))
        .check(matches(withText(str_AlternatingDumbbellCurl + " already exists.")));
  }

  @Test
  public void shouldRenderErrorWhenRenamingExerciseWithSameName() {
    onView(withText(str_AlternatingDumbbellCurl)).perform(longClick());

    onView(withId(R.id.edit_linear_layout)).perform(click());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    onView(withId(R.id.textinput_error)).check(matches(withText("Same name.")));
  }

  @Test
  public void shouldKeepFilterAfterRotating() {
    onView(allOf(withId(R.id.filter_edit_text), isDisplayed()))
        .perform(replaceText("sq"), closeSoftKeyboard());

    mActivityTestRule.getActivity()
        .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    sleepFor2Seconds();

    onView(allOf(withId(R.id.filter_edit_text), isDisplayed())).check(matches(withText("sq")));

    onView(childAtPosition(withId(R.id.recycler_view), 0))
        .check(matches(withText(str_BarbellBackSquat)));
  }
}