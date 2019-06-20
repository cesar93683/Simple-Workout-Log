package com.devcesar.workoutapp.mainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.NumberPicker;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
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
public class ExerciseTests {

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(
      MainActivity.class);

  @Test
  public void ifTimerChangedShouldUpdateTimerForAllExercises() {
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

    ViewInteraction appCompatImageButton = onView(
        allOf(withId(R.id.timer_increment), withContentDescription("Increment"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                3),
            isDisplayed()));
    appCompatImageButton.perform(click());

    pressBack();

    ViewInteraction appCompatTextView2 = onView(
        allOf(withText("Barbell Back Squat"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                1),
            isDisplayed()));
    appCompatTextView2.perform(click());

    ViewInteraction button = onView(
        allOf(withId(R.id.timer_display),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                    2),
                2),
            isDisplayed()));
    button.check(matches(withText("2:01")));

    ViewInteraction appCompatImageButton2 = onView(
        allOf(withId(R.id.timer_decrement), withContentDescription("Decrement"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                1),
            isDisplayed()));
    appCompatImageButton2.perform(click());
  }

  @Test
  public void canSetTimeInDialog() {
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
        allOf(withId(R.id.timer_display), withText("2:00"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                2),
            isDisplayed()));
    appCompatButton.perform(click());

    ViewInteraction numberPicker = onView(
        allOf(childAtPosition(
            childAtPosition(
                withId(R.id.custom),
                0),
            0),
            isDisplayed()));
    numberPicker.perform(new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        NumberPicker tp = (NumberPicker) view;
        tp.setValue(3);
      }

      @Override
      public String getDescription() {
        return "Set the passed value into the NumberPicker";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(NumberPicker.class);
      }
    });

    ViewInteraction numberPicker2 = onView(
        allOf(childAtPosition(
            childAtPosition(
                withId(R.id.custom),
                0),
            2),
            isDisplayed()));
    numberPicker2.perform(new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        NumberPicker tp = (NumberPicker) view;
        tp.setValue(59);
      }

      @Override
      public String getDescription() {
        return "Set the passed value into the NumberPicker";
      }

      @Override
      public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(NumberPicker.class);
      }
    });

    ViewInteraction appCompatButton2 = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton2.perform(scrollTo(), click());

    ViewInteraction button = onView(
        allOf(withId(R.id.timer_display),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                    2),
                2),
            isDisplayed()));
    button.check(matches(withText("3:59")));
  }

  @Test
  public void shouldSetTimeCorrectlyInDialog() {
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
        allOf(withId(R.id.timer_display), withText("2:00"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                2),
            isDisplayed()));
    appCompatButton.perform(click());

    ViewInteraction editText = onView(
        allOf(IsInstanceOf.instanceOf(android.widget.EditText.class), withText("2"),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                    0),
                1),
            isDisplayed()));
    editText.check(matches(withText("2")));

    ViewInteraction editText2 = onView(
        allOf(IsInstanceOf.instanceOf(android.widget.EditText.class), withText("0"),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                    2),
                1),
            isDisplayed()));
    editText2.check(matches(withText("0")));
  }

  @Test
  public void canDecrementAndIncrementTimerProperly() {
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

    ViewInteraction appCompatImageButton = onView(
        allOf(withId(R.id.timer_decrement), withContentDescription("Decrement"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                1),
            isDisplayed()));
    appCompatImageButton.perform(click());

    ViewInteraction button = onView(
        allOf(withId(R.id.timer_display),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                    2),
                2),
            isDisplayed()));
    button.check(matches(withText("1:59")));

    ViewInteraction appCompatImageButton2 = onView(
        allOf(withId(R.id.timer_decrement), withContentDescription("Decrement"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                1),
            isDisplayed()));
    appCompatImageButton2.perform(click());

    ViewInteraction button2 = onView(
        allOf(withId(R.id.timer_display),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                    2),
                2),
            isDisplayed()));
    button2.check(matches(withText("1:58")));

    ViewInteraction appCompatImageButton3 = onView(
        allOf(withId(R.id.timer_increment), withContentDescription("Increment"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                3),
            isDisplayed()));
    appCompatImageButton3.perform(click());

    ViewInteraction appCompatImageButton4 = onView(
        allOf(withId(R.id.timer_increment), withContentDescription("Increment"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                3),
            isDisplayed()));
    appCompatImageButton4.perform(click());

    ViewInteraction button3 = onView(
        allOf(withId(R.id.timer_display),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                    2),
                2),
            isDisplayed()));
    button3.check(matches(withText("2:00")));

    ViewInteraction appCompatImageButton5 = onView(
        allOf(withId(R.id.timer_increment), withContentDescription("Increment"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                3),
            isDisplayed()));
    appCompatImageButton5.perform(click());

    ViewInteraction appCompatImageButton6 = onView(
        allOf(withId(R.id.timer_increment), withContentDescription("Increment"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                3),
            isDisplayed()));
    appCompatImageButton6.perform(click());

    ViewInteraction appCompatImageButton7 = onView(
        allOf(withId(R.id.timer_increment), withContentDescription("Increment"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                3),
            isDisplayed()));
    appCompatImageButton7.perform(click());

    ViewInteraction appCompatImageButton8 = onView(
        allOf(withId(R.id.timer_increment), withContentDescription("Increment"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                3),
            isDisplayed()));
    appCompatImageButton8.perform(click());

    ViewInteraction appCompatImageButton9 = onView(
        allOf(withId(R.id.timer_increment), withContentDescription("Increment"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                3),
            isDisplayed()));
    appCompatImageButton9.perform(click());

    ViewInteraction appCompatImageButton10 = onView(
        allOf(withId(R.id.timer_increment), withContentDescription("Increment"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                3),
            isDisplayed()));
    appCompatImageButton10.perform(click());

    ViewInteraction appCompatImageButton11 = onView(
        allOf(withId(R.id.timer_increment), withContentDescription("Increment"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                3),
            isDisplayed()));
    appCompatImageButton11.perform(click());

    ViewInteraction appCompatImageButton12 = onView(
        allOf(withId(R.id.timer_increment), withContentDescription("Increment"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                3),
            isDisplayed()));
    appCompatImageButton12.perform(click());

    ViewInteraction appCompatImageButton13 = onView(
        allOf(withId(R.id.timer_increment), withContentDescription("Increment"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                3),
            isDisplayed()));
    appCompatImageButton13.perform(click());

    ViewInteraction appCompatImageButton14 = onView(
        allOf(withId(R.id.timer_increment), withContentDescription("Increment"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                3),
            isDisplayed()));
    appCompatImageButton14.perform(click());

    ViewInteraction button4 = onView(
        allOf(withId(R.id.timer_display),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                    2),
                2),
            isDisplayed()));
    button4.check(matches(withText("2:10")));
  }

  @Test
  public void shouldGoToExerciseWhenClickingExercise() {
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

    ViewInteraction textView = onView(
        allOf(withId(R.id.title), withText("Alternating Dumbbell Curl"),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                    0),
                0),
            isDisplayed()));
    textView.check(matches(withText("Alternating Dumbbell Curl")));
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
    ViewInteraction floatingActionButton = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragment_container),
                    0),
                2),
            isDisplayed()));
    floatingActionButton.perform(click());
    ViewInteraction textInputEditText = onView(
        allOf(childAtPosition(
            childAtPosition(
                withId(R.id.text_input_layout),
                0),
            0),
            isDisplayed()));
    textInputEditText.perform(replaceText("A"), closeSoftKeyboard());
    ViewInteraction appCompatButton = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton.perform(scrollTo(), click());

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

  @Test
  public void shouldBeAbleToRenameExercise() {
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

    ViewInteraction linearLayout1 = onView(
        allOf(withId(R.id.edit_linear_layout),
            childAtPosition(
                childAtPosition(
                    withId(R.id.custom),
                    0),
                0),
            isDisplayed()));
    linearLayout1.perform(click());

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

    ViewInteraction appCompatButton1 = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton1.perform(scrollTo(), click());

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

    ViewInteraction linearLayout = onView(
        allOf(withId(R.id.edit_linear_layout),
            childAtPosition(
                childAtPosition(
                    withId(R.id.custom),
                    0),
                0),
            isDisplayed()));
    linearLayout.perform(click());

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

    ViewInteraction appCompatButton = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton.perform(scrollTo(), click());

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
  public void shouldBeAbleToFilterExercises() {
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
  public void shouldRenderErrorWhenTryingToCreateExerciseWithNoName() {
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
                    IsInstanceOf.instanceOf(android.widget.FrameLayout.class),
                    0)),
            1),
            isDisplayed()));
    linearLayout.check(matches(isDisplayed()));
  }

  @Test
  public void shouldRenderErrorWhenTryingToCreateExerciseWithExistingName() {
    ViewInteraction floatingActionButton = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragment_container),
                    0),
                2),
            isDisplayed()));
    floatingActionButton.perform(click());

    ViewInteraction textInputEditText = onView(
        allOf(childAtPosition(
            childAtPosition(
                withId(R.id.text_input_layout),
                0),
            0),
            isDisplayed()));
    textInputEditText.perform(replaceText("Alternating Dumbbell Curl"), closeSoftKeyboard());

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
                    IsInstanceOf.instanceOf(android.widget.FrameLayout.class),
                    0)),
            1),
            isDisplayed()));
    linearLayout.check(matches(isDisplayed()));
  }

  @Test
  public void shouldRenderErrorWhenRenamingExerciseWithSameName() {
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

    ViewInteraction linearLayout = onView(
        allOf(withId(R.id.edit_linear_layout),
            childAtPosition(
                childAtPosition(
                    withId(R.id.custom),
                    0),
                0),
            isDisplayed()));
    linearLayout.perform(click());

    ViewInteraction appCompatButton = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton.perform(scrollTo(), click());
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
}
