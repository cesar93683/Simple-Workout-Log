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
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;
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
public class RoutineTests {

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(
      MainActivity.class);

  @Test
  public void shouldNotShowSaveChangesDialogIfNoChangesMade() {
    ViewInteraction bottomNavigationItemView = onView(
        allOf(withId(R.id.nav_routine), withContentDescription("Routine"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                2),
            isDisplayed()));
    bottomNavigationItemView.perform(click());

    ViewInteraction floatingActionButton = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        1)),
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
    textInputEditText.perform(replaceText("A"), closeSoftKeyboard());

    ViewInteraction appCompatButton = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton.perform(scrollTo(), click());

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

    ViewInteraction floatingActionButton2 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragment_container),
                    0),
                1),
            isDisplayed()));
    floatingActionButton2.perform(click());

    pressBack();

    ViewInteraction appCompatButton2 = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton2.check(doesNotExist());

    pressBack();

    ViewInteraction appCompatTextView2 = onView(
        allOf(withText("A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
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
  }

  @Test
  public void shouldShowSaveChangesDialogIfDeleteExercise() {
    ViewInteraction bottomNavigationItemView = onView(
        allOf(withId(R.id.nav_routine), withContentDescription("Routine"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                2),
            isDisplayed()));
    bottomNavigationItemView.perform(click());

    ViewInteraction floatingActionButton = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        1)),
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
    textInputEditText.perform(replaceText("A"), closeSoftKeyboard());

    ViewInteraction appCompatButton = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton.perform(scrollTo(), click());

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

    ViewInteraction floatingActionButton2 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragment_container),
                    0),
                1),
            isDisplayed()));
    floatingActionButton2.perform(click());

    ViewInteraction viewInteraction = onView(
        allOf(withId(R.id.fab_expand_menu_button),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                2),
            isDisplayed()));
    viewInteraction.perform(click());

    ViewInteraction floatingActionButton3 = onView(
        allOf(withId(R.id.fab_action1),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                0),
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

    ViewInteraction floatingActionButton4 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        0)),
                1),
            isDisplayed()));
    floatingActionButton4.perform(click());

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction floatingActionButton5 = onView(
        allOf(withId(R.id.fab_action2),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                1),
            isDisplayed()));
    floatingActionButton5.perform(click());

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction floatingActionButton6 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragment_container),
                    0),
                1),
            isDisplayed()));
    floatingActionButton6.perform(click());

    ViewInteraction linearLayout2 = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    0)),
            0),
            isDisplayed()));
    linearLayout2.perform(longClick());

    ViewInteraction appCompatButton2 = onView(
        allOf(withId(android.R.id.button1), withText("Yes"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton2.perform(scrollTo(), click());

    pressBack();

    ViewInteraction appCompatButton3 = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton3.check(matches(isDisplayed()));

    ViewInteraction appCompatButton4 = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton4.perform(scrollTo(), click());

    pressBack();

    ViewInteraction appCompatTextView2 = onView(
        allOf(withText("A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView2.perform(longClick());

    ViewInteraction linearLayout3 = onView(
        allOf(withId(R.id.delete_linear_layout),
            childAtPosition(
                childAtPosition(
                    withId(R.id.custom),
                    0),
                1),
            isDisplayed()));
    linearLayout3.perform(click());

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
  public void shouldShowSaveChangesDialogIfRearrangeOrder() {
    ViewInteraction bottomNavigationItemView = onView(
        allOf(withId(R.id.nav_routine), withContentDescription("Routine"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                2),
            isDisplayed()));
    bottomNavigationItemView.perform(click());

    ViewInteraction floatingActionButton = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        1)),
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
    textInputEditText.perform(replaceText("A"), closeSoftKeyboard());

    ViewInteraction appCompatButton = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton.perform(scrollTo(), click());

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

    ViewInteraction floatingActionButton2 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragment_container),
                    0),
                1),
            isDisplayed()));
    floatingActionButton2.perform(click());

    ViewInteraction viewInteraction = onView(
        allOf(withId(R.id.fab_expand_menu_button),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                2),
            isDisplayed()));
    viewInteraction.perform(click());

    ViewInteraction floatingActionButton3 = onView(
        allOf(withId(R.id.fab_action1),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                0),
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
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        0)),
                1),
            isDisplayed()));
    floatingActionButton4.perform(click());

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction floatingActionButton5 = onView(
        allOf(withId(R.id.fab_action2),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                1),
            isDisplayed()));
    floatingActionButton5.perform(click());

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction floatingActionButton6 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragment_container),
                    0),
                1),
            isDisplayed()));
    floatingActionButton6.perform(click());

    ViewInteraction imageView = onView(
        allOf(withId(R.id.drag_image_view), withContentDescription("Drag Icon"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.recycler_view),
                    1),
                0),
            isDisplayed()));
    imageView.perform(new GeneralSwipeAction(Swipe.FAST, GeneralLocation.BOTTOM_CENTER,
        view -> {
          float[] coordinates = GeneralLocation.CENTER.calculateCoordinates(view);
          coordinates[1] = 0;
          return coordinates;
        }, Press.FINGER));

    pressBack();

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction appCompatButton2 = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton2.check(matches(isDisplayed()));

    ViewInteraction appCompatButton3 = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton3.perform(scrollTo(), click());

    pressBack();

    ViewInteraction appCompatTextView2 = onView(
        allOf(withText("A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView2.perform(longClick());

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

  @Test
  public void routineShouldGetNewNameAfterExerciseInRoutineIsRenamed() {
    ViewInteraction bottomNavigationItemView = onView(
        allOf(withId(R.id.nav_routine), withContentDescription("Routine"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                2),
            isDisplayed()));
    bottomNavigationItemView.perform(click());

    ViewInteraction floatingActionButton = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        1)),
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
    textInputEditText.perform(replaceText("A"), closeSoftKeyboard());

    ViewInteraction appCompatButton = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton.perform(scrollTo(), click());

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
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        0)),
                1),
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
        allOf(withId(R.id.nav_routine), withContentDescription("Routine"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                2),
            isDisplayed()));
    bottomNavigationItemView3.perform(click());

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

    ViewInteraction floatingActionButton3 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragment_container),
                    0),
                1),
            isDisplayed()));
    floatingActionButton3.perform(click());

    ViewInteraction viewInteraction = onView(
        allOf(withId(R.id.fab_expand_menu_button),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                2),
            isDisplayed()));
    viewInteraction.perform(click());

    ViewInteraction floatingActionButton4 = onView(
        allOf(withId(R.id.fab_action1),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                0),
            isDisplayed()));
    floatingActionButton4.perform(click());

    ViewInteraction linearLayout = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                    1)),
            0),
            isDisplayed()));
    linearLayout.perform(click());

    ViewInteraction floatingActionButton5 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        0)),
                1),
            isDisplayed()));
    floatingActionButton5.perform(click());

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction floatingActionButton6 = onView(
        allOf(withId(R.id.fab_action2),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                1),
            isDisplayed()));
    floatingActionButton6.perform(click());

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

    ViewInteraction appCompatTextView2 = onView(
        allOf(withText("A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView2.perform(longClick());

    ViewInteraction linearLayout2 = onView(
        allOf(withId(R.id.edit_linear_layout),
            childAtPosition(
                childAtPosition(
                    withId(R.id.custom),
                    0),
                0),
            isDisplayed()));
    linearLayout2.perform(click());

    ViewInteraction textInputEditText3 = onView(
        allOf(withText("A"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.text_input_layout),
                    0),
                0),
            isDisplayed()));
    textInputEditText3.perform(replaceText("Aa"));

    ViewInteraction textInputEditText4 = onView(
        allOf(withText("Aa"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.text_input_layout),
                    0),
                0),
            isDisplayed()));
    textInputEditText4.perform(closeSoftKeyboard());

    ViewInteraction appCompatButton3 = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
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
        allOf(withId(R.id.nav_routine), withContentDescription("Routine"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                2),
            isDisplayed()));
    bottomNavigationItemView5.perform(click());

    ViewInteraction appCompatTextView3 = onView(
        allOf(withText("A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView3.perform(click());

    ViewInteraction textView = onView(
        allOf(withText("Aa"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                        0)),
                0),
            isDisplayed()));
    textView.check(matches(withText("Aa")));

    pressBack();

    ViewInteraction bottomNavigationItemView6 = onView(
        allOf(withId(R.id.nav_exercise), withContentDescription("Exercise"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                0),
            isDisplayed()));
    bottomNavigationItemView6.perform(click());

    ViewInteraction appCompatTextView4 = onView(
        allOf(withText("Aa"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView4.perform(longClick());

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

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction bottomNavigationItemView7 = onView(
        allOf(withId(R.id.nav_routine), withContentDescription("Routine"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                2),
            isDisplayed()));
    bottomNavigationItemView7.perform(click());

    ViewInteraction appCompatTextView5 = onView(
        allOf(withText("A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView5.perform(longClick());

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
  public void shouldAddExercisesToRoutineFromClickingSaveInDiscardDialog() {
    ViewInteraction bottomNavigationItemView = onView(
        allOf(withId(R.id.nav_routine), withContentDescription("Routine"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                2),
            isDisplayed()));
    bottomNavigationItemView.perform(click());

    ViewInteraction floatingActionButton = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        1)),
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
    textInputEditText.perform(replaceText("A"), closeSoftKeyboard());

    ViewInteraction appCompatButton = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton.perform(scrollTo(), click());

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

    ViewInteraction floatingActionButton2 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragment_container),
                    0),
                1),
            isDisplayed()));
    floatingActionButton2.perform(click());

    ViewInteraction viewInteraction = onView(
        allOf(withId(R.id.fab_expand_menu_button),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                2),
            isDisplayed()));
    viewInteraction.perform(click());

    ViewInteraction floatingActionButton3 = onView(
        allOf(withId(R.id.fab_action1),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                0),
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

    ViewInteraction floatingActionButton4 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        0)),
                1),
            isDisplayed()));
    floatingActionButton4.perform(click());

    pressBack();

    ViewInteraction appCompatButton2 = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
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
                        IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                        0)),
                0),
            isDisplayed()));
    textView.check(matches(isDisplayed()));

    pressBack();

    ViewInteraction appCompatTextView2 = onView(
        allOf(withText("A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView2.perform(longClick());

    ViewInteraction linearLayout2 = onView(
        allOf(withId(R.id.delete_linear_layout),
            childAtPosition(
                childAtPosition(
                    withId(R.id.custom),
                    0),
                1),
            isDisplayed()));
    linearLayout2.perform(click());

    ViewInteraction appCompatButton3 = onView(
        allOf(withId(android.R.id.button1), withText("Yes"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton3.perform(scrollTo(), click());
  }

  @Test
  public void shouldAddMultipleExercisesToRoutineAlphabetically() {
    ViewInteraction bottomNavigationItemView = onView(
        allOf(withId(R.id.nav_routine), withContentDescription("Routine"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                2),
            isDisplayed()));
    bottomNavigationItemView.perform(click());

    ViewInteraction floatingActionButton = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        1)),
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
    textInputEditText.perform(replaceText("A"), closeSoftKeyboard());

    ViewInteraction appCompatButton = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton.perform(scrollTo(), click());

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

    ViewInteraction floatingActionButton2 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragment_container),
                    0),
                1),
            isDisplayed()));
    floatingActionButton2.perform(click());

    ViewInteraction viewInteraction = onView(
        allOf(withId(R.id.fab_expand_menu_button),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                2),
            isDisplayed()));
    viewInteraction.perform(click());

    ViewInteraction floatingActionButton3 = onView(
        allOf(withId(R.id.fab_action1),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                0),
            isDisplayed()));
    floatingActionButton3.perform(click());

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

    ViewInteraction linearLayout3 = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                    1)),
            0),
            isDisplayed()));
    linearLayout3.perform(click());

    ViewInteraction floatingActionButton4 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        0)),
                1),
            isDisplayed()));
    floatingActionButton4.perform(click());

    ViewInteraction textView = onView(
        allOf(withId(R.id.text_view), withText("Alternating Dumbbell Curl"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.recycler_view),
                    0),
                1),
            isDisplayed()));
    textView.check(matches(isDisplayed()));

    ViewInteraction textView2 = onView(
        allOf(withId(R.id.text_view), withText("Barbell Back Squat"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.recycler_view),
                    1),
                1),
            isDisplayed()));
    textView2.check(matches(isDisplayed()));

    ViewInteraction textView3 = onView(
        allOf(withId(R.id.text_view), withText("Barbell Bench Press"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.recycler_view),
                    2),
                1),
            isDisplayed()));
    textView3.check(matches(isDisplayed()));

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction floatingActionButton5 = onView(
        allOf(withId(R.id.fab_action2),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                1),
            isDisplayed()));
    floatingActionButton5.perform(click());

    pressBack();

    ViewInteraction appCompatTextView2 = onView(
        allOf(withText("A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView2.perform(longClick());

    ViewInteraction linearLayout4 = onView(
        allOf(withId(R.id.delete_linear_layout),
            childAtPosition(
                childAtPosition(
                    withId(R.id.custom),
                    0),
                1),
            isDisplayed()));
    linearLayout4.perform(click());

    ViewInteraction appCompatButton2 = onView(
        allOf(withId(android.R.id.button1), withText("Yes"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton2.perform(scrollTo(), click());
  }

  @Test
  public void shouldBeAbleToAddExerciseToRoutine() {
    ViewInteraction bottomNavigationItemView = onView(
        allOf(withId(R.id.nav_routine), withContentDescription("Routine"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                2),
            isDisplayed()));
    bottomNavigationItemView.perform(click());

    ViewInteraction floatingActionButton = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        1)),
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
    textInputEditText.perform(replaceText("A"), closeSoftKeyboard());

    ViewInteraction appCompatButton = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton.perform(scrollTo(), click());

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

    ViewInteraction floatingActionButton2 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragment_container),
                    0),
                1),
            isDisplayed()));
    floatingActionButton2.perform(click());

    ViewInteraction viewInteraction = onView(
        allOf(withId(R.id.fab_expand_menu_button),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                2),
            isDisplayed()));
    viewInteraction.perform(click());

    ViewInteraction floatingActionButton3 = onView(
        allOf(withId(R.id.fab_action1),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                0),
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

    ViewInteraction floatingActionButton4 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        0)),
                1),
            isDisplayed()));
    floatingActionButton4.perform(click());

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction floatingActionButton5 = onView(
        allOf(withId(R.id.fab_action2),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                1),
            isDisplayed()));
    floatingActionButton5.perform(click());

    ViewInteraction textView = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                        0)),
                0),
            isDisplayed()));
    textView.check(matches(isDisplayed()));

    pressBack();

    ViewInteraction appCompatTextView2 = onView(
        allOf(withText("A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView2.perform(longClick());

    ViewInteraction linearLayout2 = onView(
        allOf(withId(R.id.delete_linear_layout),
            childAtPosition(
                childAtPosition(
                    withId(R.id.custom),
                    0),
                1),
            isDisplayed()));
    linearLayout2.perform(click());

    ViewInteraction appCompatButton2 = onView(
        allOf(withId(android.R.id.button1), withText("Yes"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton2.perform(scrollTo(), click());
  }

  @Test
  public void shouldBeAbleToChangeExerciseOrderInRoutine() {
    ViewInteraction bottomNavigationItemView = onView(
        allOf(withId(R.id.nav_routine), withContentDescription("Routine"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                2),
            isDisplayed()));
    bottomNavigationItemView.perform(click());

    ViewInteraction floatingActionButton = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        1)),
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
    textInputEditText.perform(replaceText("A"), closeSoftKeyboard());

    ViewInteraction appCompatButton = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton.perform(scrollTo(), click());

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

    ViewInteraction floatingActionButton2 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragment_container),
                    0),
                1),
            isDisplayed()));
    floatingActionButton2.perform(click());

    ViewInteraction viewInteraction = onView(
        allOf(withId(R.id.fab_expand_menu_button),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                2),
            isDisplayed()));
    viewInteraction.perform(click());

    ViewInteraction floatingActionButton3 = onView(
        allOf(withId(R.id.fab_action1),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                0),
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

    ViewInteraction linearLayout3 = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                    1)),
            2),
            isDisplayed()));
    linearLayout3.perform(click());

    ViewInteraction floatingActionButton4 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        0)),
                1),
            isDisplayed()));
    floatingActionButton4.perform(click());

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction imageView = onView(
        allOf(withId(R.id.drag_image_view), withContentDescription("Drag Icon"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.recycler_view),
                    2),
                0),
            isDisplayed()));
    imageView.perform(new GeneralSwipeAction(Swipe.FAST, GeneralLocation.BOTTOM_CENTER,
        view -> {
          float[] coordinates = GeneralLocation.CENTER.calculateCoordinates(view);
          coordinates[1] = 0;
          return coordinates;
        }, Press.FINGER));

    ViewInteraction floatingActionButton5 = onView(
        allOf(withId(R.id.fab_action2),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                1),
            isDisplayed()));
    floatingActionButton5.perform(click());

    ViewInteraction textView = onView(
        allOf(withText("Barbell Bench Press"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                        0)),
                0),
            isDisplayed()));
    textView.check(matches(isDisplayed()));

    ViewInteraction textView2 = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                        0)),
                1),
            isDisplayed()));
    textView2.check(matches(isDisplayed()));

    ViewInteraction textView3 = onView(
        allOf(withText("Barbell Back Squat"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                        0)),
                2),
            isDisplayed()));
    textView3.check(matches(isDisplayed()));

    pressBack();

    ViewInteraction appCompatTextView2 = onView(
        allOf(withText("A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView2.perform(longClick());

    ViewInteraction linearLayout4 = onView(
        allOf(withId(R.id.delete_linear_layout),
            childAtPosition(
                childAtPosition(
                    withId(R.id.custom),
                    0),
                1),
            isDisplayed()));
    linearLayout4.perform(click());

    ViewInteraction appCompatButton2 = onView(
        allOf(withId(android.R.id.button1), withText("Yes"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton2.perform(scrollTo(), click());
  }

  @Test
  public void shouldBeAbleToCreateAndDeleteRoutine() {
    ViewInteraction bottomNavigationItemView = onView(
        allOf(withId(R.id.nav_routine), withContentDescription("Routine"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                2),
            isDisplayed()));
    bottomNavigationItemView.perform(click());

    ViewInteraction floatingActionButton = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        1)),
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
                        IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                        1)),
                0),
            isDisplayed()));
    textView.check(matches(isDisplayed()));

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

    ViewInteraction appCompatButton2 = onView(
        allOf(withId(android.R.id.button1), withText("Yes"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton2.perform(scrollTo(), click());
  }

  @Test
  public void shouldBeAbleToDeleteExerciseFromRoutineInEditRoutine() {
    ViewInteraction bottomNavigationItemView = onView(
        allOf(withId(R.id.nav_routine), withContentDescription("Routine"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                2),
            isDisplayed()));
    bottomNavigationItemView.perform(click());

    ViewInteraction floatingActionButton = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        1)),
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
    textInputEditText.perform(replaceText("A"), closeSoftKeyboard());

    ViewInteraction appCompatButton = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton.perform(scrollTo(), click());

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

    ViewInteraction floatingActionButton2 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragment_container),
                    0),
                1),
            isDisplayed()));
    floatingActionButton2.perform(click());

    ViewInteraction viewInteraction = onView(
        allOf(withId(R.id.fab_expand_menu_button),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                2),
            isDisplayed()));
    viewInteraction.perform(click());

    ViewInteraction floatingActionButton3 = onView(
        allOf(withId(R.id.fab_action1),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                0),
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
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        0)),
                1),
            isDisplayed()));
    floatingActionButton4.perform(click());

    ViewInteraction linearLayout3 = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    0)),
            0),
            isDisplayed()));
    linearLayout3.perform(longClick());

    ViewInteraction appCompatButton2 = onView(
        allOf(withId(android.R.id.button1), withText("Yes"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton2.perform(scrollTo(), click());

    ViewInteraction textView = onView(
        allOf(withId(R.id.text_view), withText("Alternating Dumbbell Curl"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.recycler_view),
                    0),
                1),
            isDisplayed()));
    textView.check(doesNotExist());

    ViewInteraction floatingActionButton5 = onView(
        allOf(withId(R.id.fab_action2),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                1),
            isDisplayed()));
    floatingActionButton5.perform(click());

    ViewInteraction textView2 = onView(
        allOf(withId(R.id.text_view), withText("Alternating Dumbbell Curl"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.recycler_view),
                    0),
                1),
            isDisplayed()));
    textView2.check(doesNotExist());

    pressBack();

    ViewInteraction appCompatTextView2 = onView(
        allOf(withText("A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView2.perform(longClick());

    ViewInteraction linearLayout4 = onView(
        allOf(withId(R.id.delete_linear_layout),
            childAtPosition(
                childAtPosition(
                    withId(R.id.custom),
                    0),
                1),
            isDisplayed()));
    linearLayout4.perform(click());

    ViewInteraction appCompatButton3 = onView(
        allOf(withId(android.R.id.button1), withText("Yes"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton3.perform(scrollTo(), click());
  }

  @Test
  public void shouldBeAbleToDeleteExerciseFromRoutineInViewExercises() {
    ViewInteraction bottomNavigationItemView = onView(
        allOf(withId(R.id.nav_routine), withContentDescription("Routine"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                2),
            isDisplayed()));
    bottomNavigationItemView.perform(click());

    ViewInteraction floatingActionButton = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        1)),
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
    textInputEditText.perform(replaceText("A"), closeSoftKeyboard());

    ViewInteraction appCompatButton = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton.perform(scrollTo(), click());

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

    ViewInteraction floatingActionButton2 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragment_container),
                    0),
                1),
            isDisplayed()));
    floatingActionButton2.perform(click());

    ViewInteraction viewInteraction = onView(
        allOf(withId(R.id.fab_expand_menu_button),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                2),
            isDisplayed()));
    viewInteraction.perform(click());

    ViewInteraction floatingActionButton3 = onView(
        allOf(withId(R.id.fab_action1),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                0),
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
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        0)),
                1),
            isDisplayed()));
    floatingActionButton4.perform(click());

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction floatingActionButton5 = onView(
        allOf(withId(R.id.fab_action2),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                1),
            isDisplayed()));
    floatingActionButton5.perform(click());

    ViewInteraction appCompatTextView2 = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withId(R.id.coordinator_layout),
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
                        IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                        0)),
                0),
            isDisplayed()));
    textView.check(doesNotExist());

    pressBack();

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
  }

  @Test
  public void shouldBeAbleToSwitchToRoutineCategory() {
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
  public void shouldBeAbleToVisitExerciseFromRoutine() {
    ViewInteraction bottomNavigationItemView = onView(
        allOf(withId(R.id.nav_routine), withContentDescription("Routine"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                2),
            isDisplayed()));
    bottomNavigationItemView.perform(click());

    ViewInteraction floatingActionButton = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        1)),
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
    textInputEditText.perform(replaceText("A"), closeSoftKeyboard());

    ViewInteraction appCompatButton = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton.perform(scrollTo(), click());

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

    ViewInteraction floatingActionButton2 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragment_container),
                    0),
                1),
            isDisplayed()));
    floatingActionButton2.perform(click());

    ViewInteraction viewInteraction = onView(
        allOf(withId(R.id.fab_expand_menu_button),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                2),
            isDisplayed()));
    viewInteraction.perform(click());

    ViewInteraction floatingActionButton3 = onView(
        allOf(withId(R.id.fab_action1),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                0),
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

    ViewInteraction floatingActionButton4 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        0)),
                1),
            isDisplayed()));
    floatingActionButton4.perform(click());

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction floatingActionButton5 = onView(
        allOf(withId(R.id.fab_action2),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                1),
            isDisplayed()));
    floatingActionButton5.perform(click());

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
                        withId(R.id.coordinator_layout),
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

    ViewInteraction linearLayout2 = onView(
        allOf(withId(R.id.delete_linear_layout),
            childAtPosition(
                childAtPosition(
                    withId(R.id.custom),
                    0),
                1),
            isDisplayed()));
    linearLayout2.perform(click());

    ViewInteraction appCompatButton2 = onView(
        allOf(withId(android.R.id.button1), withText("Yes"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton2.perform(scrollTo(), click());
  }

  @Test
  public void shouldDeleteExerciseFromRoutineIfExerciseDeletedInExerciseTab() {
    ViewInteraction bottomNavigationItemView = onView(
        allOf(withId(R.id.nav_routine), withContentDescription("Routine"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                2),
            isDisplayed()));
    bottomNavigationItemView.perform(click());

    ViewInteraction floatingActionButton = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        1)),
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
    textInputEditText.perform(replaceText("A"), closeSoftKeyboard());

    ViewInteraction appCompatButton = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton.perform(scrollTo(), click());

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
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        0)),
                1),
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
        allOf(withId(R.id.nav_routine), withContentDescription("Routine"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                2),
            isDisplayed()));
    bottomNavigationItemView3.perform(click());

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

    ViewInteraction floatingActionButton3 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragment_container),
                    0),
                1),
            isDisplayed()));
    floatingActionButton3.perform(click());

    ViewInteraction viewInteraction = onView(
        allOf(withId(R.id.fab_expand_menu_button),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                2),
            isDisplayed()));
    viewInteraction.perform(click());

    ViewInteraction floatingActionButton4 = onView(
        allOf(withId(R.id.fab_action1),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                0),
            isDisplayed()));
    floatingActionButton4.perform(click());

    ViewInteraction linearLayout = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                    1)),
            0),
            isDisplayed()));
    linearLayout.perform(click());

    ViewInteraction floatingActionButton5 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        0)),
                1),
            isDisplayed()));
    floatingActionButton5.perform(click());

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction floatingActionButton6 = onView(
        allOf(withId(R.id.fab_action2),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                1),
            isDisplayed()));
    floatingActionButton6.perform(click());

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

    ViewInteraction appCompatTextView2 = onView(
        allOf(withText("A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView2.perform(longClick());

    ViewInteraction linearLayout2 = onView(
        allOf(withId(R.id.delete_linear_layout),
            childAtPosition(
                childAtPosition(
                    withId(R.id.custom),
                    0),
                1),
            isDisplayed()));
    linearLayout2.perform(click());

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
        allOf(withId(R.id.nav_routine), withContentDescription("Routine"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                2),
            isDisplayed()));
    bottomNavigationItemView5.perform(click());

    ViewInteraction appCompatTextView3 = onView(
        allOf(withText("A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView3.perform(click());

    ViewInteraction textView = onView(
        allOf(withText("A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                        0)),
                0),
            isDisplayed()));
    textView.check(doesNotExist());

    pressBack();

    ViewInteraction appCompatTextView4 = onView(
        allOf(withText("A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView4.perform(longClick());

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

  @Test
  public void shouldKeepExercisesAfterRenameRoutine() {
    ViewInteraction bottomNavigationItemView = onView(
        allOf(withId(R.id.nav_routine), withContentDescription("Routine"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                2),
            isDisplayed()));
    bottomNavigationItemView.perform(click());

    ViewInteraction floatingActionButton = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        1)),
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
    textInputEditText.perform(replaceText("A"), closeSoftKeyboard());

    ViewInteraction appCompatButton = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton.perform(scrollTo(), click());

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

    ViewInteraction floatingActionButton2 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragment_container),
                    0),
                1),
            isDisplayed()));
    floatingActionButton2.perform(click());

    ViewInteraction viewInteraction = onView(
        allOf(withId(R.id.fab_expand_menu_button),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                2),
            isDisplayed()));
    viewInteraction.perform(click());

    ViewInteraction floatingActionButton3 = onView(
        allOf(withId(R.id.fab_action1),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                0),
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

    ViewInteraction floatingActionButton4 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        0)),
                1),
            isDisplayed()));
    floatingActionButton4.perform(click());

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction floatingActionButton5 = onView(
        allOf(withId(R.id.fab_action2),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                1),
            isDisplayed()));
    floatingActionButton5.perform(click());

    pressBack();

    ViewInteraction appCompatTextView2 = onView(
        allOf(withText("A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView2.perform(longClick());

    ViewInteraction linearLayout2 = onView(
        allOf(withId(R.id.edit_linear_layout),
            childAtPosition(
                childAtPosition(
                    withId(R.id.custom),
                    0),
                0),
            isDisplayed()));
    linearLayout2.perform(click());

    ViewInteraction textInputEditText2 = onView(
        allOf(withText("A"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.text_input_layout),
                    0),
                0),
            isDisplayed()));
    textInputEditText2.perform(replaceText("Aa"));

    ViewInteraction textInputEditText3 = onView(
        allOf(withText("Aa"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.text_input_layout),
                    0),
                0),
            isDisplayed()));
    textInputEditText3.perform(closeSoftKeyboard());

    ViewInteraction appCompatButton2 = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton2.perform(scrollTo(), click());

    ViewInteraction appCompatTextView3 = onView(
        allOf(withText("Aa"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView3.perform(click());

    ViewInteraction textView = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                        0)),
                0),
            isDisplayed()));
    textView.check(matches(withText("Alternating Dumbbell Curl")));

    pressBack();

    ViewInteraction appCompatTextView4 = onView(
        allOf(withText("Aa"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView4.perform(longClick());

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
  }

  @Test
  public void shouldNotAddExercisesToRoutineFromClickingDiscardInDiscardDialog() {
    ViewInteraction bottomNavigationItemView = onView(
        allOf(withId(R.id.nav_routine), withContentDescription("Routine"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                2),
            isDisplayed()));
    bottomNavigationItemView.perform(click());

    ViewInteraction floatingActionButton = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        1)),
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
    textInputEditText.perform(replaceText("A"), closeSoftKeyboard());

    ViewInteraction appCompatButton = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton.perform(scrollTo(), click());

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

    ViewInteraction floatingActionButton2 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragment_container),
                    0),
                1),
            isDisplayed()));
    floatingActionButton2.perform(click());

    ViewInteraction viewInteraction = onView(
        allOf(withId(R.id.fab_expand_menu_button),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                2),
            isDisplayed()));
    viewInteraction.perform(click());

    ViewInteraction floatingActionButton3 = onView(
        allOf(withId(R.id.fab_action1),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                0),
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

    ViewInteraction floatingActionButton4 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        0)),
                1),
            isDisplayed()));
    floatingActionButton4.perform(click());

    pressBack();

    ViewInteraction appCompatButton2 = onView(
        allOf(withId(android.R.id.button2), withText("Discard"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                2)));
    appCompatButton2.perform(scrollTo(), click());

    ViewInteraction textView = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                        0)),
                0),
            isDisplayed()));
    textView.check(doesNotExist());

    pressBack();

    ViewInteraction appCompatTextView2 = onView(
        allOf(withText("A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView2.perform(longClick());

    ViewInteraction linearLayout2 = onView(
        allOf(withId(R.id.delete_linear_layout),
            childAtPosition(
                childAtPosition(
                    withId(R.id.custom),
                    0),
                1),
            isDisplayed()));
    linearLayout2.perform(click());

    ViewInteraction appCompatButton3 = onView(
        allOf(withId(android.R.id.button1), withText("Yes"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton3.perform(scrollTo(), click());
  }

  @Test
  public void shouldNotBeAbleToAddExerciseToRoutineIfAlreadyInRoutine() {
    ViewInteraction bottomNavigationItemView = onView(
        allOf(withId(R.id.nav_routine), withContentDescription("Routine"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.bottom_navigation),
                    0),
                2),
            isDisplayed()));
    bottomNavigationItemView.perform(click());

    ViewInteraction floatingActionButton = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        1)),
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
    textInputEditText.perform(replaceText("A"), closeSoftKeyboard());

    ViewInteraction appCompatButton = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton.perform(scrollTo(), click());

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

    ViewInteraction floatingActionButton2 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragment_container),
                    0),
                1),
            isDisplayed()));
    floatingActionButton2.perform(click());

    ViewInteraction viewInteraction = onView(
        allOf(withId(R.id.fab_expand_menu_button),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                2),
            isDisplayed()));
    viewInteraction.perform(click());

    ViewInteraction floatingActionButton3 = onView(
        allOf(withId(R.id.fab_action1),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                0),
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

    ViewInteraction floatingActionButton4 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                allOf(withId(R.id.coordinator_layout),
                    childAtPosition(
                        withId(R.id.fragment_container),
                        0)),
                1),
            isDisplayed()));
    floatingActionButton4.perform(click());

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction floatingActionButton5 = onView(
        allOf(withId(R.id.fab_action2),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                1),
            isDisplayed()));
    floatingActionButton5.perform(click());

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction floatingActionButton6 = onView(
        allOf(withId(R.id.fab),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragment_container),
                    0),
                1),
            isDisplayed()));
    floatingActionButton6.perform(click());

    ViewInteraction viewInteraction2 = onView(
        allOf(withId(R.id.fab_expand_menu_button),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                2),
            isDisplayed()));
    viewInteraction2.perform(click());

    ViewInteraction floatingActionButton7 = onView(
        allOf(withId(R.id.fab_action1),
            childAtPosition(
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1),
                0),
            isDisplayed()));
    floatingActionButton7.perform(click());

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

    pressBack();

    ViewInteraction appCompatTextView2 = onView(
        allOf(withText("A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView2.perform(longClick());

    ViewInteraction linearLayout2 = onView(
        allOf(withId(R.id.delete_linear_layout),
            childAtPosition(
                childAtPosition(
                    withId(R.id.custom),
                    0),
                1),
            isDisplayed()));
    linearLayout2.perform(click());

    ViewInteraction appCompatButton2 = onView(
        allOf(withId(android.R.id.button1), withText("Yes"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton2.perform(scrollTo(), click());
  }

}
