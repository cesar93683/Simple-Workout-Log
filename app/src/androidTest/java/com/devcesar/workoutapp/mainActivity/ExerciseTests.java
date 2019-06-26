package com.devcesar.workoutapp.mainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.childAtPosition;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_AlternatingDumbbellCurl;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_Exercise;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_Save;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_Yes;
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
public class ExerciseTests {

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(
      MainActivity.class);

  @Test
  public void shouldGoToExerciseWhenClickingExercise() {
    onView(ViewMatchers.withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.title)).check(matches(withText(str_AlternatingDumbbellCurl)));
  }

  @Test
  public void shouldDefaultToExercise() {
    onView(childAtPosition(withId(R.id.action_bar), 0)).check(matches(withText(str_Exercise)));
  }

  @Test
  public void shouldBeAbleToCreateAndDeleteExercise() {
    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(str_Save)).perform(click());

    onView(childAtPosition(withId(R.id.recycler_view), 0)).check(matches(withText("A")));

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(str_Yes)).perform(click());

    onView(withText("A")).check(doesNotExist());
  }

}