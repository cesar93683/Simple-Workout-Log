package com.devcesar.workoutapp.mainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.childAtPosition;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

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
public class ExerciseTests {

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(
      MainActivity.class);

  @Test
  public void shouldGoToExerciseWhenClickingExercise() {
    onView(ViewMatchers.withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.title)).check(matches(withText(ViewHelper.str_AlternatingDumbbellCurl)));
  }

  @Test
  public void shouldDefaultToExercise() {
    ViewInteraction textView = onView(
        allOf(withText("Exercise"),
            childAtPosition(
                allOf(withId(R.id.action_bar),
                    childAtPosition(
                        withId(R.id.action_bar_container),
                        0)),
                0),
            isDisplayed()));
    textView.check(matches(withText("Exercise")));
  }

  @Test
  public void shouldBeAbleToCreateAndDeleteExercise() {
    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(scrollTo(), click());

    ViewInteraction textView = onView(
        allOf(withText("A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    textView.check(matches(isDisplayed()));

    ViewInteraction textView2 = onView(
        allOf(withText("A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    textView2.perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());

    ViewInteraction textView3 = onView(
        allOf(withText("A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    textView3.check(doesNotExist());
  }

}