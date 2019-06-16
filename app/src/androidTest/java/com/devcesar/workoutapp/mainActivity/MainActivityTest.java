package com.devcesar.workoutapp.mainActivity;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
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
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

  // should_render_error_when_trying_to_create_exercise_with_existing_name
  // should_be_able_to_delete_exercise
  // should_be_able_to_rename_exercise
  // should_be_able_to_create_and_delete_exercise
  // should_be_able_to_filter_exercises
  //

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(
      MainActivity.class);

  @Test
  public void should_default_to_exercise() {
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

  @Test
  public void should_be_able_to_switch_to_category() {
    ViewInteraction bottomNavigationItemView = onView(
        allOf(withId(R.id.nav_category),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                1),
            isDisplayed()));
    bottomNavigationItemView.perform(click());

    ViewInteraction textView = onView(
        allOf(withText("Category"),
            childAtPosition(
                allOf(withId(R.id.action_bar),
                    childAtPosition(
                        withId(R.id.action_bar_container),
                        0)),
                0),
            isDisplayed()));
    textView.check(matches(withText("Category")));
  }

  @Test
  public void should_be_able_to_switch_to_routine_category() {
    ViewInteraction bottomNavigationItemView = onView(
        allOf(withId(R.id.nav_routine),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                2),
            isDisplayed()));
    bottomNavigationItemView.perform(click());

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

  @Test
  public void should_start_with_at_least_2_exercises() {
    ViewInteraction textView = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                        1)),
                0),
            isDisplayed()));
    textView.check(matches(isDisplayed()));

    ViewInteraction textView2 = onView(
        allOf(withText("Barbell Back Squat"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                        1)),
                1),
            isDisplayed()));
    textView2.check(matches(isDisplayed()));
  }

  @Test
  public void should_render_error_when_no_name_entered_in_dialog_box() {
    ViewInteraction floatingActionButton = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragment_container),
                    0),
                2),
            isDisplayed()));
    floatingActionButton.perform(click());

    ViewInteraction appCompatButton = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton.perform(scrollTo(), click());

    ViewInteraction linearLayout = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.text_input_layout),
                childAtPosition(
                    IsInstanceOf.instanceOf(android.widget.RelativeLayout.class),
                    0)),
            1),
            isDisplayed()));
    linearLayout.check(matches(isDisplayed()));
  }

  @Test
  public void should_have_at_least_1_category() {
    ViewInteraction bottomNavigationItemView = onView(
        allOf(withId(R.id.nav_category),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                1),
            isDisplayed()));
    bottomNavigationItemView.perform(click());

    ViewInteraction textView = onView(
        allOf(withText("Back"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                        1)),
                0),
            isDisplayed()));
    textView.check(matches(isDisplayed()));
  }
}
