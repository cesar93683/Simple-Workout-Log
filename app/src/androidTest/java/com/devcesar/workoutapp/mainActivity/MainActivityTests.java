package com.devcesar.workoutapp.mainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.childAtPosition;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.sleepFor2Seconds;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_AlternatingDumbbellCurl;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.content.pm.ActivityInfo;
import android.view.ViewGroup;
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

    ViewInteraction textView2 = onView(withText(alternatingDumbbellCurl2));
    textView2.check(matches(withText(alternatingDumbbellCurl2)));
    textView2.perform(longClick());

    onView(withId(R.id.edit_linear_layout)).perform(click());

    onView(withId(R.id.text_input_edit_text))
        .perform(replaceText(str_AlternatingDumbbellCurl), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).check(matches(withText(
        str_AlternatingDumbbellCurl)));
  }

  @Test
  public void shouldBeAbleToFilterExercises() {
    onView(allOf(withId(R.id.filter_edit_text), isDisplayed()))
        .perform(replaceText("barbell"), closeSoftKeyboard());

    ViewInteraction textView = onView(
        allOf(withText("Barbell Back Squat"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    textView.check(matches(withText("Barbell Back Squat")));
  }

  @Test
  public void shouldRenderErrorWhenTryingToCreateExerciseWithNoName() {
    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    ViewInteraction linearLayout = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.text_input_layout),
                childAtPosition(
                    IsInstanceOf.instanceOf(android.widget.FrameLayout.class),
                    0)),
            1),
            isDisplayed()));
    linearLayout.check(matches(isDisplayed()));
  }

  @Test
  public void shouldRenderErrorWhenTryingToCreateExerciseWithExistingName() {
    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text))
        .perform(replaceText("Alternating Dumbbell Curl"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    ViewInteraction linearLayout = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.text_input_layout),
                childAtPosition(
                    IsInstanceOf.instanceOf(android.widget.FrameLayout.class),
                    0)),
            1),
            isDisplayed()));
    linearLayout.check(matches(isDisplayed()));
  }

  @Test
  public void shouldRenderErrorWhenRenamingExerciseWithSameName() {
    onView(withText(str_AlternatingDumbbellCurl)).perform(longClick());

    onView(withId(R.id.edit_linear_layout)).perform(click());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    ViewInteraction linearLayout1 = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.text_input_layout),
                childAtPosition(
                    IsInstanceOf.instanceOf(android.widget.FrameLayout.class),
                    0)),
            1),
            isDisplayed()));
    linearLayout1.check(matches(isDisplayed()));
  }

  @Test
  public void shouldKeepFilterAfterRotating() {
    onView(allOf(withId(R.id.filter_edit_text), isDisplayed()))
        .perform(replaceText("sq"), closeSoftKeyboard());

    mActivityTestRule.getActivity()
        .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    sleepFor2Seconds();

    onView(allOf(withId(R.id.filter_edit_text), isDisplayed())).check(matches(withText("sq")));

    ViewInteraction textView = onView(
        allOf(withText("Barbell Back Squat"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(ViewGroup.class),
                        1)),
                0),
            isDisplayed()));
    textView.check(matches(withText("Barbell Back Squat")));
  }
}