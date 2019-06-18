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
public class MainActivityTest {

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(
      MainActivity.class);

  @Test
  public void should_be_able_to_create_and_delete_exercise() {
    clickFabInExerciseFragment();
    writeTextIntoInputInDialog("Aa");
    clickSaveInDialog();

    ViewInteraction textView = onView(
        allOf(withText("Aa"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    textView.check(matches(isDisplayed()));

    ViewInteraction textView2 = onView(
        allOf(withText("Aa"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    textView2.perform(longClick());

    ViewInteraction linearLayout = onView(
        allOf(withId(R.id.delete_linear_layout),
            childAtPosition(
                childAtPosition(
                    withId(R.id.custom),
                    0),
                1),
            isDisplayed()));
    linearLayout.perform(click());

    ViewInteraction appCompatButton2 = onView(
        allOf(withId(android.R.id.button1), withText("Yes"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton2.perform(scrollTo(), click());

    ViewInteraction textView3 = onView(
        allOf(withText("Aa"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    textView3.check(doesNotExist());
  }

  private void clickFabInExerciseFragment() {
    ViewInteraction floatingActionButton = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragment_container),
                    0),
                2),
            isDisplayed()));
    floatingActionButton.perform(click());
  }

  @Test
  public void should_be_able_to_rename_exercise() {
    ViewInteraction textView = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    textView.perform(longClick());

    clickRenameInDialog();

    ViewInteraction textInputEditText = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.text_input_layout),
                    0),
                0),
            isDisplayed()));
    textInputEditText.perform(replaceText("Alternating Dumbbell Curl2"));

    ViewInteraction textInputEditText2 = onView(
        allOf(withText("Alternating Dumbbell Curl2"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.text_input_layout),
                    0),
                0),
            isDisplayed()));
    textInputEditText2.perform(closeSoftKeyboard());

    clickSaveInDialog();

    ViewInteraction textView2 = onView(
        allOf(withText("Alternating Dumbbell Curl2"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    textView2.check(matches(withText("Alternating Dumbbell Curl2")));

    textView2.perform(longClick());

    clickRenameInDialog();

    ViewInteraction textInputEditText3 = onView(
        allOf(withText("Alternating Dumbbell Curl2"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.text_input_layout),
                    0),
                0),
            isDisplayed()));
    textInputEditText3.perform(replaceText("Alternating Dumbbell Curl"));

    ViewInteraction textInputEditText4 = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.text_input_layout),
                    0),
                0),
            isDisplayed()));
    textInputEditText4.perform(closeSoftKeyboard());

    clickSaveInDialog();

    ViewInteraction textView3 = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    textView3.check(matches(withText("Alternating Dumbbell Curl")));
  }

  @Test
  public void should_render_error_when_renaming_exercise_with_same_name() {
    ViewInteraction textView = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    textView.perform(longClick());

    clickRenameInDialog();

    clickSaveInDialog();
    checkIfErrorIsRenderedInDialog();
  }

  private void clickRenameInDialog() {
    ViewInteraction linearLayout = onView(
        allOf(withId(R.id.edit_linear_layout),
            childAtPosition(
                childAtPosition(
                    withId(R.id.custom),
                    0),
                0),
            isDisplayed()));
    linearLayout.perform(click());
  }

  @Test
  public void should_be_able_to_filter_exercises() {
    ViewInteraction appCompatEditText = onView(
        allOf(withId(R.id.filter_edit_text),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragment_container),
                    0),
                0),
            isDisplayed()));
    appCompatEditText.perform(replaceText("barbell"), closeSoftKeyboard());

    ViewInteraction textView = onView(
        allOf(withText("Barbell Back Squat"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    textView.check(matches(isDisplayed()));
  }

  @Test
  public void should_render_error_when_trying_to_create_exercise_with_existing_name() {
    clickFabInExerciseFragment();

    writeTextIntoInputInDialog("Alternating Dumbbell Curl");

    clickSaveInDialog();
    checkIfErrorIsRenderedInDialog();
  }

  private void writeTextIntoInputInDialog(String s) {
    ViewInteraction textInputEditText = onView(
        allOf(childAtPosition(
            childAtPosition(
                withId(R.id.text_input_layout),
                0),
            0),
            isDisplayed()));
    textInputEditText.perform(replaceText(s), closeSoftKeyboard());
  }

  @Test
  public void should_default_to_exercise() {
    checkIfActionBarIsNamed("Exercise");
  }

  private void checkIfActionBarIsNamed(String exercise) {
    ViewInteraction textView = onView(
        allOf(withText(exercise),
            childAtPosition(
                allOf(withId(R.id.action_bar),
                    childAtPosition(
                        withId(R.id.action_bar_container),
                        0)),
                0),
            isDisplayed()));
    textView.check(matches(withText(exercise)));
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
    clickCategoryButtonInBottomNavigation();

    checkIfActionBarIsNamed("Category");
  }

  private void clickCategoryButtonInBottomNavigation() {
    ViewInteraction bottomNavigationItemView = onView(
        allOf(withId(R.id.nav_category),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                1),
            isDisplayed()));
    bottomNavigationItemView.perform(click());
  }

  @Test
  public void should_be_able_to_switch_to_routine_category() {
    clickRoutineButtonInBottomNavigation();
    checkIfActionBarIsNamed("Routine");
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

  @Test
  public void should_start_with_at_least_2_exercises() {
    ViewInteraction textView = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    textView.check(matches(isDisplayed()));

    ViewInteraction textView2 = onView(
        allOf(withText("Barbell Back Squat"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                1),
            isDisplayed()));
    textView2.check(matches(isDisplayed()));
  }

  @Test
  public void should_render_error_when_no_name_entered_in_dialog_box() {
    clickFabInExerciseFragment();

    clickSaveInDialog();

    checkIfErrorIsRenderedInDialog();
  }

  private void checkIfErrorIsRenderedInDialog() {
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

  private void clickSaveInDialog() {
    ViewInteraction appCompatButton = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton.perform(scrollTo(), click());
  }

  @Test
  public void should_have_at_least_1_category() {
    clickCategoryButtonInBottomNavigation();

    ViewInteraction textView = onView(
        allOf(withText("Back"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    textView.check(matches(isDisplayed()));
  }
}