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
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import com.devcesar.workoutapp.R;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CategoryTests {

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(
      MainActivity.class);

  // shouldDeleteExerciseFromCategoryIfExerciseDeletedInExerciseTab

  @Before
  public void createCategory() {
    clickCategoryButtonInMainActivity();

    ViewInteraction floatingActionButton = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragment_container),
                    2),
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
  }

  private void clickCategoryButtonInMainActivity() {
    ViewInteraction bottomNavigationItemView = onView(
        allOf(withId(R.id.nav_category), withContentDescription("Category"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                1),
            isDisplayed()));
    bottomNavigationItemView.perform(click());
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

  @After
  public void deleteCategory() {
    clickCategoryButtonInMainActivity();

    ViewInteraction appCompatTextView = onView(
        allOf(withText("A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("android.widget.RelativeLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView.perform(longClick());

    ViewInteraction linearLayout = onView(
        allOf(withId(R.id.delete_linear_layout),
            childAtPosition(
                childAtPosition(
                    withId(R.id.custom),
                    0),
                1),
            isDisplayed()));
    linearLayout.perform(click());

    ViewInteraction appCompatButton = onView(
        allOf(withId(android.R.id.button1), withText("Yes"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton.perform(scrollTo(), click());
  }

  @Test
  public void onlyExercisesThatAreNotInCategoryCanBeAdded() {
    clickCategoryButtonInMainActivity();

    clickButtonNamedA();

    clickFabInViewExercisesActivity();

    clickFirstItemInListInAddExerciseActivity();

    clickFabInAddExerciseActivity();

    // next twos lines shouldn't be needed but espresso buggy
    pressBack();
    clickButtonNamedA();

    clickFabInViewExercisesActivity();

    ViewInteraction textView = onView(
        allOf(withId(R.id.text_view), withText("Alternating Dumbbell Curl"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.recycler_view),
                    0),
                1),
            isDisplayed()));
    textView.check(doesNotExist());

    pressBack();

    pressBack();
  }

  private void clickButtonNamedA() {
    ViewInteraction appCompatTextView = onView(
        allOf(withText("A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("android.widget.RelativeLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView.perform(click());
  }

  private void clickFabInViewExercisesActivity() {
    ViewInteraction floatingActionButton2 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragment_container),
                    0),
                1),
            isDisplayed()));
    floatingActionButton2.perform(click());
  }

  private void clickFirstItemInListInAddExerciseActivity() {
    ViewInteraction linearLayout = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withClassName(is("android.widget.RelativeLayout")),
                    1)),
            0),
            isDisplayed()));
    linearLayout.perform(click());
  }

  private void clickFabInAddExerciseActivity() {
    ViewInteraction floatingActionButton3 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragment_container),
                    0),
                2),
            isDisplayed()));
    floatingActionButton3.perform(click());
  }

  @Test
  public void canVisitExerciseFromCategory() {
    clickCategoryButtonInMainActivity();

    clickButtonNamedA();

    clickFabInViewExercisesActivity();

    clickFirstItemInListInAddExerciseActivity();

    clickFabInAddExerciseActivity();

    ViewInteraction appCompatTextView2 = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("android.widget.RelativeLayout")),
                        0)),
                0),
            isDisplayed()));
    appCompatTextView2.perform(click());

    ViewInteraction textView = onView(
        allOf(withId(R.id.title), withText("Alternating Dumbbell Curl"),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                    0),
                0),
            isDisplayed()));
    textView.check(matches(withText("Alternating Dumbbell Curl")));

    pressBack();

    pressBack();
  }

  @Test
  public void canDeleteExercisesFromCategory() {
    clickCategoryButtonInMainActivity();

    clickButtonNamedA();

    clickFabInViewExercisesActivity();

    clickFirstItemInListInAddExerciseActivity();

    ViewInteraction linearLayout2 = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withClassName(is("android.widget.RelativeLayout")),
                    1)),
            1),
            isDisplayed()));
    linearLayout2.perform(click());

    clickFabInAddExerciseActivity();

    ViewInteraction appCompatTextView2 = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("android.widget.RelativeLayout")),
                        0)),
                0),
            isDisplayed()));
    appCompatTextView2.perform(longClick());

    ViewInteraction appCompatButton2 = onView(
        allOf(withId(android.R.id.button1), withText("Yes"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton2.perform(scrollTo(), click());

    ViewInteraction textView = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.RelativeLayout.class),
                        0)),
                0),
            isDisplayed()));
    textView.check(doesNotExist());

    pressBack();
  }

  @Test
  public void canCheckCheckBoxByClickingCheckBoxOrText() {
    clickCategoryButtonInMainActivity();

    clickButtonNamedA();

    clickFabInViewExercisesActivity();

    ViewInteraction appCompatCheckBox = onView(
        allOf(withId(R.id.check_box),
            childAtPosition(
                childAtPosition(
                    withId(R.id.recycler_view),
                    0),
                0),
            isDisplayed()));
    appCompatCheckBox.perform(click());

    ViewInteraction linearLayout = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withClassName(is("android.widget.RelativeLayout")),
                    1)),
            1),
            isDisplayed()));
    linearLayout.perform(click());

    clickFabInAddExerciseActivity();

    ViewInteraction textView = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.RelativeLayout.class),
                        0)),
                0),
            isDisplayed()));
    textView.check(matches(isDisplayed()));

    ViewInteraction textView2 = onView(
        allOf(withText("Barbell Back Squat"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.RelativeLayout.class),
                        0)),
                1),
            isDisplayed()));
    textView2.check(matches(isDisplayed()));

    pressBack();
  }

  @Test
  public void canAddMultipleCategoriesAndAreAlphabetized() {
    clickCategoryButtonInMainActivity();

    clickButtonNamedA();

    clickFabInViewExercisesActivity();

    ViewInteraction linearLayout = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withClassName(is("android.widget.RelativeLayout")),
                    1)),
            1),
            isDisplayed()));
    linearLayout.perform(click());

    ViewInteraction linearLayout2 = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withClassName(is("android.widget.RelativeLayout")),
                    1)),
            2),
            isDisplayed()));
    linearLayout2.perform(click());

    clickFirstItemInListInAddExerciseActivity();

    clickFabInAddExerciseActivity();

    ViewInteraction textView = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.RelativeLayout.class),
                        0)),
                0),
            isDisplayed()));
    textView.check(matches(withText("Alternating Dumbbell Curl")));

    ViewInteraction textView2 = onView(
        allOf(withText("Barbell Back Squat"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.RelativeLayout.class),
                        0)),
                1),
            isDisplayed()));
    textView2.check(matches(withText("Barbell Back Squat")));

    ViewInteraction textView3 = onView(
        allOf(withText("Barbell Bench Press"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.RelativeLayout.class),
                        0)),
                2),
            isDisplayed()));
    textView3.check(matches(withText("Barbell Bench Press")));

    pressBack();
  }

  @Test
  public void canAddExerciseToCategory() {
    clickCategoryButtonInMainActivity();

    clickButtonNamedA();

    clickFabInViewExercisesActivity();

    clickFirstItemInListInAddExerciseActivity();

    clickFabInAddExerciseActivity();

    ViewInteraction textView = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.RelativeLayout.class),
                        0)),
                0),
            isDisplayed()));
    textView.check(matches(withText("Alternating Dumbbell Curl")));

    pressBack();
  }

  @Test
  public void canAddExerciseGoBackToMainAndGoBackToCategoryAndIsStillThere() {
    clickCategoryButtonInMainActivity();

    clickButtonNamedA();

    clickFabInViewExercisesActivity();

    clickFirstItemInListInAddExerciseActivity();

    clickFabInAddExerciseActivity();

    pressBack();

    clickButtonNamedA();

    ViewInteraction textView = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.RelativeLayout.class),
                        0)),
                0),
            isDisplayed()));
    textView.check(matches(isDisplayed()));

    pressBack();
  }
}
