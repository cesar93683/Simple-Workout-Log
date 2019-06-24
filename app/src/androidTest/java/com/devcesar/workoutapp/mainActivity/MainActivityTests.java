package com.devcesar.workoutapp.mainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.AlternatingDumbbellCurl;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.getAlternatingDumbbellCurlFromExerciseTabInMainActivity;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.getEditFromEditOrDeleteDialog;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.getFabFromExerciseTabInMainActivity;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.getSaveFromDialog;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.getTextInputEditTextFromDialogInput;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.content.pm.ActivityInfo;
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
public class MainActivityTests {

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(
      MainActivity.class);

  @Test
  public void shouldBeAbleToRenameExercise() {
    getAlternatingDumbbellCurlFromExerciseTabInMainActivity().perform(longClick());

    getEditFromEditOrDeleteDialog().perform(click());

    String alternatingDumbbellCurl2 = "Alternating Dumbbell Curl2";

    getTextInputEditTextFromDialogInput()
        .perform(replaceText(alternatingDumbbellCurl2), closeSoftKeyboard());

    getSaveFromDialog().perform(scrollTo(), click());

    ViewInteraction textView2 = onView(
        allOf(withText(alternatingDumbbellCurl2),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    textView2.check(matches(withText(alternatingDumbbellCurl2)));

    textView2.perform(longClick());

    getEditFromEditOrDeleteDialog().perform(click());

    getTextInputEditTextFromDialogInput()
        .perform(replaceText(AlternatingDumbbellCurl), closeSoftKeyboard());

    getSaveFromDialog().perform(scrollTo(), click());

    getAlternatingDumbbellCurlFromExerciseTabInMainActivity().check(matches(withText(
        AlternatingDumbbellCurl)));
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
  public void shouldBeAbleToFilterExercises() {
    ViewInteraction appCompatEditText = onView(
        allOf(withId(R.id.filter_edit_text),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
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
    getFabFromExerciseTabInMainActivity().perform(click());

    getSaveFromDialog().perform(scrollTo(), click());

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
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        0)),
                1),
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

    getSaveFromDialog().perform(scrollTo(), click());

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
    getAlternatingDumbbellCurlFromExerciseTabInMainActivity().perform(longClick());

    getEditFromEditOrDeleteDialog().perform(click());

    getSaveFromDialog().perform(scrollTo(), click());

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
    ViewInteraction appCompatEditText = onView(
        allOf(withId(R.id.filter_edit_text),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    0),
                0),
            isDisplayed()));
    appCompatEditText.perform(replaceText("sq"), closeSoftKeyboard());

    mActivityTestRule.getActivity()
        .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction editText = onView(
        allOf(withId(R.id.filter_edit_text), withText("sq"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    0),
                0),
            isDisplayed()));
    editText.check(matches(withText("sq")));

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
