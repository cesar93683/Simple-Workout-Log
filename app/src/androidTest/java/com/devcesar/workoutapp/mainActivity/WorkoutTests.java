package com.devcesar.workoutapp.mainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
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
public class WorkoutTests {

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(
      MainActivity.class);

  @Test
  public void canAddWorkoutByClickingSaveInDiscardChangesDialog() {
    ViewInteraction appCompatTextView = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView.perform(click());

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

    pressBack();

    ViewInteraction appCompatButton3 = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton3.perform(scrollTo(), click());

    ViewInteraction appCompatTextView2 = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView2.perform(click());

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
    linearLayout.check(matches(isDisplayed()));

    ViewInteraction linearLayout2 = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withClassName(is("android.widget.LinearLayout")),
                    0)),
            0),
            isDisplayed()));
    linearLayout2.perform(longClick());

    ViewInteraction linearLayout3 = onView(
        allOf(withId(R.id.delete_linear_layout),
            childAtPosition(
                childAtPosition(
                    withId(R.id.custom),
                    0),
                1),
            isDisplayed()));
    linearLayout3.perform(click());

    ViewInteraction appCompatButton4 = onView(
        allOf(withId(android.R.id.button1), withText("Yes"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton4.perform(scrollTo(), click());
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
  public void canAddWorkoutAndDeleteWorkout() {
    ViewInteraction appCompatTextView = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView.perform(click());

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

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction floatingActionButton = onView(
        allOf(withId(R.id.finish_exercise_fab),
            childAtPosition(
                withParent(withId(R.id.view_pager)),
                1),
            isDisplayed()));
    floatingActionButton.perform(click());

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction appCompatTextView2 = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView2.perform(click());

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
    linearLayout.check(matches(isDisplayed()));

    ViewInteraction linearLayout2 = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withClassName(is("android.widget.LinearLayout")),
                    0)),
            0),
            isDisplayed()));
    linearLayout2.perform(longClick());

    ViewInteraction linearLayout3 = onView(
        allOf(withId(R.id.delete_linear_layout),
            childAtPosition(
                childAtPosition(
                    withId(R.id.custom),
                    0),
                1),
            isDisplayed()));
    linearLayout3.perform(click());

    ViewInteraction appCompatButton3 = onView(
        allOf(withId(android.R.id.button1), withText("Yes"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton3.perform(scrollTo(), click());

    ViewInteraction linearLayout4 = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                    0)),
            0),
            isDisplayed()));
    linearLayout4.check(doesNotExist());
  }

  @Test
  public void canEditWorkoutFromHistoryTab() {
    ViewInteraction appCompatTextView = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView.perform(click());

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

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction floatingActionButton = onView(
        allOf(withId(R.id.finish_exercise_fab),
            childAtPosition(
                withParent(withId(R.id.view_pager)),
                1),
            isDisplayed()));
    floatingActionButton.perform(click());

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction appCompatTextView2 = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView2.perform(click());

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
                    withClassName(is("android.widget.LinearLayout")),
                    0)),
            0),
            isDisplayed()));
    linearLayout.perform(longClick());

    ViewInteraction linearLayout2 = onView(
        allOf(withId(R.id.edit_linear_layout),
            childAtPosition(
                childAtPosition(
                    withId(R.id.custom),
                    0),
                0),
            isDisplayed()));
    linearLayout2.perform(click());

    ViewInteraction appCompatButton3 = onView(
        allOf(withId(R.id.increase_rep_button),
            childAtPosition(
                childAtPosition(
                    withId(R.id.exercise_set_editor),
                    0),
                2),
            isDisplayed()));
    appCompatButton3.perform(click());

    ViewInteraction appCompatButton4 = onView(
        allOf(withId(R.id.add_set_button),
            childAtPosition(
                childAtPosition(
                    withClassName(is("android.widget.LinearLayout")),
                    0),
                1),
            isDisplayed()));
    appCompatButton4.perform(click());

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction floatingActionButton2 = onView(
        allOf(withId(R.id.finish_exercise_fab),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragment_container),
                    0),
                1),
            isDisplayed()));
    floatingActionButton2.perform(click());

    ViewInteraction textView = onView(
        allOf(withText("Set 1 - 1 Rep @ - LB"),
            childAtPosition(
                allOf(withId(R.id.exercise_sets_container),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                        1)),
                0),
            isDisplayed()));
    textView.check(matches(withText("Set 1 - 1 Rep @ - LB")));

    ViewInteraction textView2 = onView(
        allOf(withText("Set 2 - 1 Rep @ - LB"),
            childAtPosition(
                allOf(withId(R.id.exercise_sets_container),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                        1)),
                1),
            isDisplayed()));
    textView2.check(matches(withText("Set 2 - 1 Rep @ - LB")));

    ViewInteraction linearLayout3 = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withClassName(is("android.widget.LinearLayout")),
                    0)),
            0),
            isDisplayed()));
    linearLayout3.perform(longClick());

    ViewInteraction linearLayout4 = onView(
        allOf(withId(R.id.delete_linear_layout),
            childAtPosition(
                childAtPosition(
                    withId(R.id.custom),
                    0),
                1),
            isDisplayed()));
    linearLayout4.perform(click());

    ViewInteraction appCompatButton5 = onView(
        allOf(withId(android.R.id.button1), withText("Yes"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton5.perform(scrollTo(), click());
  }

  @Test
  public void canSaveWorkoutFromHistoryTabByClickingSaveInDiscardChangesDialog() {
    ViewInteraction appCompatTextView = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView.perform(click());

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

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction floatingActionButton = onView(
        allOf(withId(R.id.finish_exercise_fab),
            childAtPosition(
                withParent(withId(R.id.view_pager)),
                1),
            isDisplayed()));
    floatingActionButton.perform(click());

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction appCompatTextView2 = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView2.perform(click());

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
                    withClassName(is("android.widget.LinearLayout")),
                    0)),
            0),
            isDisplayed()));
    linearLayout.perform(longClick());

    ViewInteraction linearLayout2 = onView(
        allOf(withId(R.id.edit_linear_layout),
            childAtPosition(
                childAtPosition(
                    withId(R.id.custom),
                    0),
                0),
            isDisplayed()));
    linearLayout2.perform(click());

    ViewInteraction appCompatButton3 = onView(
        allOf(withId(R.id.increase_rep_button),
            childAtPosition(
                childAtPosition(
                    withId(R.id.exercise_set_editor),
                    0),
                2),
            isDisplayed()));
    appCompatButton3.perform(click());

    ViewInteraction appCompatButton4 = onView(
        allOf(withId(R.id.add_set_button),
            childAtPosition(
                childAtPosition(
                    withClassName(is("android.widget.LinearLayout")),
                    0),
                1),
            isDisplayed()));
    appCompatButton4.perform(click());

    pressBack();

    ViewInteraction appCompatButton5 = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton5.perform(scrollTo(), click());

    ViewInteraction textView = onView(
        allOf(withText("Set 1 - 1 Rep @ - LB"),
            childAtPosition(
                allOf(withId(R.id.exercise_sets_container),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                        1)),
                0),
            isDisplayed()));
    textView.check(matches(withText("Set 1 - 1 Rep @ - LB")));

    ViewInteraction textView2 = onView(
        allOf(withText("Set 2 - 1 Rep @ - LB"),
            childAtPosition(
                allOf(withId(R.id.exercise_sets_container),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                        1)),
                1),
            isDisplayed()));
    textView2.check(matches(withText("Set 2 - 1 Rep @ - LB")));

    ViewInteraction linearLayout3 = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withClassName(is("android.widget.LinearLayout")),
                    0)),
            0),
            isDisplayed()));
    linearLayout3.perform(longClick());

    ViewInteraction linearLayout4 = onView(
        allOf(withId(R.id.delete_linear_layout),
            childAtPosition(
                childAtPosition(
                    withId(R.id.custom),
                    0),
                1),
            isDisplayed()));
    linearLayout4.perform(click());

    ViewInteraction appCompatButton6 = onView(
        allOf(withId(android.R.id.button1), withText("Yes"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton6.perform(scrollTo(), click());
  }

  @Test
  public void canAddSet() {
    ViewInteraction appCompatTextView = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView.perform(click());

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
        allOf(withId(R.id.increase_weight_button),
            childAtPosition(
                childAtPosition(
                    withId(R.id.exercise_set_editor),
                    1),
                2),
            isDisplayed()));
    appCompatButton2.perform(click());

    ViewInteraction appCompatButton3 = onView(
        allOf(withId(R.id.add_set_button),
            childAtPosition(
                childAtPosition(
                    withClassName(is("android.widget.LinearLayout")),
                    0),
                1),
            isDisplayed()));
    appCompatButton3.perform(click());

    ViewInteraction textView = onView(
        allOf(withText("Set 1 - 1 Rep @ 1 LB"),
            childAtPosition(
                allOf(withId(R.id.exercise_sets_recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                        2)),
                0),
            isDisplayed()));
    textView.check(matches(isDisplayed()));
  }

  @Test
  public void canAddSetWithNoWeight() {
    ViewInteraction appCompatTextView = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView.perform(click());

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

    ViewInteraction textView = onView(
        allOf(withText("Set 1 - 1 Rep @ - LB"),
            childAtPosition(
                allOf(withId(R.id.exercise_sets_recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                        2)),
                0),
            isDisplayed()));
    textView.check(matches(isDisplayed()));
  }

  @Test
  public void canDeleteSet() {
    ViewInteraction appCompatTextView = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView.perform(click());

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

    ViewInteraction appCompatTextView2 = onView(
        allOf(withText("Set 1 - 1 Rep @ - LB"),
            childAtPosition(
                allOf(withId(R.id.exercise_sets_recycler_view),
                    childAtPosition(
                        withClassName(is("android.widget.LinearLayout")),
                        2)),
                0),
            isDisplayed()));
    appCompatTextView2.perform(longClick());

    ViewInteraction linearLayout = onView(
        allOf(withId(R.id.delete_linear_layout),
            childAtPosition(
                childAtPosition(
                    withId(R.id.custom),
                    0),
                1),
            isDisplayed()));
    linearLayout.perform(click());

    ViewInteraction appCompatButton3 = onView(
        allOf(withId(android.R.id.button1), withText("Yes"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton3.perform(scrollTo(), click());

    ViewInteraction textView = onView(
        allOf(withText("Set 1 - 1 Rep @ - LB"),
            childAtPosition(
                allOf(withId(R.id.exercise_sets_recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                        2)),
                0),
            isDisplayed()));
    textView.check(doesNotExist());
  }

  @Test
  public void canModifySet() {
    ViewInteraction appCompatTextView = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView.perform(click());

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

    ViewInteraction appCompatTextView2 = onView(
        allOf(withText("Set 1 - 1 Rep @ - LB"),
            childAtPosition(
                allOf(withId(R.id.exercise_sets_recycler_view),
                    childAtPosition(
                        withClassName(is("android.widget.LinearLayout")),
                        2)),
                0),
            isDisplayed()));
    appCompatTextView2.perform(longClick());

    ViewInteraction linearLayout = onView(
        allOf(withId(R.id.edit_linear_layout),
            childAtPosition(
                childAtPosition(
                    withId(R.id.custom),
                    0),
                0),
            isDisplayed()));
    linearLayout.perform(click());

    ViewInteraction appCompatButton3 = onView(
        allOf(withId(R.id.increase_rep_button),
            childAtPosition(
                childAtPosition(
                    withClassName(is("android.widget.LinearLayout")),
                    0),
                2),
            isDisplayed()));
    appCompatButton3.perform(click());

    ViewInteraction appCompatButton4 = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton4.perform(scrollTo(), click());

    ViewInteraction textView = onView(
        allOf(withText("Set 1 - 2 Reps @ - LB"),
            childAtPosition(
                allOf(withId(R.id.exercise_sets_recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                        2)),
                0),
            isDisplayed()));
    textView.check(matches(withText("Set 1 - 2 Reps @ - LB")));
  }

  @Test
  public void givesErrorIfTryToAddSetWithRepsInputEmptyOrAtZero() {
    ViewInteraction appCompatTextView = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView.perform(click());

    ViewInteraction appCompatButton = onView(
        allOf(withId(R.id.add_set_button),
            childAtPosition(
                childAtPosition(
                    withClassName(is("android.widget.LinearLayout")),
                    0),
                1),
            isDisplayed()));
    appCompatButton.perform(click());

    ViewInteraction textView = onView(
        allOf(withId(R.id.textinput_error), withText("Please enter at least 1 rep"),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                    0),
                0),
            isDisplayed()));
    textView.check(matches(isDisplayed()));

    ViewInteraction appCompatButton2 = onView(
        allOf(withId(R.id.decrease_rep_button),
            childAtPosition(
                childAtPosition(
                    withId(R.id.exercise_set_editor),
                    0),
                0),
            isDisplayed()));
    appCompatButton2.perform(click());

    ViewInteraction appCompatButton3 = onView(
        allOf(withId(R.id.add_set_button),
            childAtPosition(
                childAtPosition(
                    withClassName(is("android.widget.LinearLayout")),
                    0),
                1),
            isDisplayed()));
    appCompatButton3.perform(click());

    ViewInteraction textView2 = onView(
        allOf(withId(R.id.textinput_error), withText("Please enter at least 1 rep"),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                    0),
                0),
            isDisplayed()));
    textView2.check(matches(isDisplayed()));
  }

  @Test
  public void setAndWeightInputIncrementAndDecrementProperly() {
    ViewInteraction appCompatTextView = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView.perform(click());

    ViewInteraction appCompatButton = onView(
        allOf(withId(R.id.decrease_rep_button),
            childAtPosition(
                childAtPosition(
                    withId(R.id.exercise_set_editor),
                    0),
                0),
            isDisplayed()));
    appCompatButton.perform(click());

    ViewInteraction editText = onView(
        allOf(withText("0"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.reps_text_input_layout),
                    0),
                0),
            isDisplayed()));
    editText.check(matches(withText("0")));

    ViewInteraction appCompatButton2 = onView(
        allOf(withId(R.id.increase_weight_button),
            childAtPosition(
                childAtPosition(
                    withId(R.id.exercise_set_editor),
                    1),
                2),
            isDisplayed()));
    appCompatButton2.perform(click());

    ViewInteraction editText2 = onView(
        allOf(withText("1"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.weight_text_input_layout),
                    0),
                0),
            isDisplayed()));
    editText2.check(matches(withText("1")));

    ViewInteraction appCompatButton3 = onView(
        allOf(withId(R.id.increase_rep_button),
            childAtPosition(
                childAtPosition(
                    withId(R.id.exercise_set_editor),
                    0),
                2),
            isDisplayed()));
    appCompatButton3.perform(click());

    ViewInteraction editText3 = onView(
        allOf(withText("1"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.reps_text_input_layout),
                    0),
                0),
            isDisplayed()));
    editText3.check(matches(withText("1")));

    ViewInteraction appCompatButton4 = onView(
        allOf(withId(R.id.decrease_weight_button),
            childAtPosition(
                childAtPosition(
                    withId(R.id.exercise_set_editor),
                    1),
                0),
            isDisplayed()));
    appCompatButton4.perform(click());

    ViewInteraction editText4 = onView(
        allOf(withText("0"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.weight_text_input_layout),
                    0),
                0),
            isDisplayed()));
    editText4.check(matches(withText("0")));
  }
}
