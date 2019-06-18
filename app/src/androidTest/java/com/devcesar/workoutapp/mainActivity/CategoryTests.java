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
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CategoryTests {

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(
      MainActivity.class);

  public void createCategoryNamedA() {
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

    onView(allOf(withId(android.R.id.button1), withText("Save"))).perform(scrollTo(), click());
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

  @Test
  public void shouldBeAbleToSwitchToCategory() {
    createCategoryNamedA();

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

    deleteCategoryNamedA();
  }

  public void deleteCategoryNamedA() {
    clickCategoryButtonInMainActivity();

    ViewInteraction appCompatTextView = onView(
        allOf(withText("A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
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
  public void shouldDeleteExerciseFromCategoryIfExerciseDeletedInExerciseTab() {
    createCategoryNamedA();

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction bottomNavigationItemView2 = onView(
        allOf(withId(R.id.nav_exercise), withContentDescription("Exercise"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                0),
            isDisplayed()));
    bottomNavigationItemView2.perform(click());

    ViewInteraction floatingActionButton2 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragment_container),
                    0),
                2),
            isDisplayed()));
    floatingActionButton2.perform(click());

    ViewInteraction textInputEditText2 = onView(
        allOf(childAtPosition(
            childAtPosition(
                withId(R.id.text_input_layout),
                0),
            0),
            isDisplayed()));
    textInputEditText2.perform(replaceText("A"), closeSoftKeyboard());

    ViewInteraction appCompatButton2 = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton2.perform(scrollTo(), click());

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction bottomNavigationItemView3 = onView(
        allOf(withId(R.id.nav_category), withContentDescription("Category"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                1),
            isDisplayed()));
    bottomNavigationItemView3.perform(click());

    ViewInteraction appCompatTextView2 = onView(
        allOf(withText("A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView2.perform(click());

    ViewInteraction floatingActionButton3 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragment_container),
                    0),
                1),
            isDisplayed()));
    floatingActionButton3.perform(click());

    ViewInteraction linearLayout = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                    1)),
            0),
            isDisplayed()));
    linearLayout.perform(click());

    ViewInteraction linearLayout2 = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                    1)),
            1),
            isDisplayed()));
    linearLayout2.perform(click());

    ViewInteraction floatingActionButton4 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragment_container),
                    0),
                2),
            isDisplayed()));
    floatingActionButton4.perform(click());

    pressBack();

    ViewInteraction bottomNavigationItemView4 = onView(
        allOf(withId(R.id.nav_exercise), withContentDescription("Exercise"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                0),
            isDisplayed()));
    bottomNavigationItemView4.perform(click());

    ViewInteraction appCompatTextView3 = onView(
        allOf(withText("A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView3.perform(longClick());

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

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction bottomNavigationItemView5 = onView(
        allOf(withId(R.id.nav_category), withContentDescription("Category"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                1),
            isDisplayed()));
    bottomNavigationItemView5.perform(click());

    ViewInteraction appCompatTextView4 = onView(
        allOf(withText("A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView4.perform(click());

    ViewInteraction textView = onView(
        allOf(withText("A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        0)),
                0),
            isDisplayed()));
    textView.check(doesNotExist());

    pressBack();

    deleteCategoryNamedA();
  }

  @Test
  public void shouldNotBeAbleToAddCategoryThatAreAlreadyInCategory() {
    createCategoryNamedA();

    clickCategoryButtonInMainActivity();

    clickButtonNamedA();

    clickFabInViewExercisesActivity();

    clickFirstItemInListInAddExerciseActivity();

    clickFabInAddExerciseActivity();

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
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

    deleteCategoryNamedA();
  }

  private void clickButtonNamedA() {
    ViewInteraction appCompatTextView = onView(
        allOf(withText("A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
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

                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
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

    ViewInteraction appCompatTextView2 = onView(
        allOf(withText("Back"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                1),
            isDisplayed()));
    appCompatTextView2.perform(click());

    ViewInteraction appCompatTextView3 = onView(
        allOf(withText("Barbell Row"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        0)),
                0),
            isDisplayed()));
    appCompatTextView3.perform(click());

    ViewInteraction textView = onView(
        allOf(withId(R.id.title), withText("Barbell Row"),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                    0),
                0),
            isDisplayed()));
    textView.check(matches(withText("Barbell Row")));
    pressBack();

    pressBack();
  }

  @Test
  public void canDeleteExercisesFromCategory() {
    createCategoryNamedA();

    clickCategoryButtonInMainActivity();

    clickButtonNamedA();

    clickFabInViewExercisesActivity();

    clickFirstItemInListInAddExerciseActivity();

    ViewInteraction linearLayout2 = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
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
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
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
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        0)),
                0),
            isDisplayed()));
    textView.check(doesNotExist());

    pressBack();

    deleteCategoryNamedA();
  }

  @Test
  public void canCheckCheckBoxByClickingCheckBoxOrText() {
    createCategoryNamedA();

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

                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
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
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        0)),
                0),
            isDisplayed()));
    textView.check(matches(isDisplayed()));

    ViewInteraction textView2 = onView(
        allOf(withText("Barbell Back Squat"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        0)),
                1),
            isDisplayed()));
    textView2.check(matches(isDisplayed()));

    pressBack();

    deleteCategoryNamedA();
  }

  @Test
  public void canAddMultipleCategoriesAndAreAlphabetized() {
    createCategoryNamedA();

    clickCategoryButtonInMainActivity();

    clickButtonNamedA();

    clickFabInViewExercisesActivity();

    ViewInteraction linearLayout = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(

                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                    1)),
            1),
            isDisplayed()));
    linearLayout.perform(click());

    ViewInteraction linearLayout2 = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(

                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
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
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        0)),
                0),
            isDisplayed()));
    textView.check(matches(withText("Alternating Dumbbell Curl")));

    ViewInteraction textView2 = onView(
        allOf(withText("Barbell Back Squat"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        0)),
                1),
            isDisplayed()));
    textView2.check(matches(withText("Barbell Back Squat")));

    ViewInteraction textView3 = onView(
        allOf(withText("Barbell Bench Press"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        0)),
                2),
            isDisplayed()));
    textView3.check(matches(withText("Barbell Bench Press")));

    pressBack();

    deleteCategoryNamedA();
  }

  @Test
  public void canAddExerciseToCategory() {
    createCategoryNamedA();

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
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        0)),
                0),
            isDisplayed()));
    textView.check(matches(withText("Alternating Dumbbell Curl")));

    pressBack();

    deleteCategoryNamedA();
  }

  @Test
  public void canAddExerciseGoBackToMainAndGoBackToCategoryAndIsStillThere() {
    createCategoryNamedA();

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
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        0)),
                0),
            isDisplayed()));
    textView.check(matches(isDisplayed()));

    pressBack();

    deleteCategoryNamedA();
  }
}
