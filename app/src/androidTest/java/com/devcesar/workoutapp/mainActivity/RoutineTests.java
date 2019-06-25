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
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.childAtPosition;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.sleepFor2Seconds;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;
import com.devcesar.workoutapp.R;
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

  private Activity getCurrentActivity() {
    getInstrumentation().waitForIdleSync();
    final Activity[] activity = new Activity[1];
    try {
      mActivityTestRule.runOnUiThread(() -> {
        java.util.Collection<Activity> activities = ActivityLifecycleMonitorRegistry.getInstance()
            .getActivitiesInStage(
                Stage.RESUMED);
        activity[0] = Iterables.getOnlyElement(activities);
      });
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }
    return activity[0];
  }

  @Test
  public void shouldKeepNewExercisesAddedAfterAddingThemThenRotating() {
    onView(withId(R.id.nav_routine)).perform(click());

    ViewInteraction appCompatTextView = onView(
        allOf(withText("Strong 5x5 - Workout A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView.perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    ViewInteraction linearLayout = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                    1)),
            0),
            isDisplayed()));
    linearLayout.perform(click());

    getCurrentActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    sleepFor2Seconds();

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    ViewInteraction textView = onView(
        allOf(withId(R.id.text_view), withText("Alternating Dumbbell Curl"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.recycler_view),
                    3),
                1),
            isDisplayed()));
    textView.check(matches(withText("Alternating Dumbbell Curl")));
  }

  @Test
  public void shouldSaveNewOrderIfChangedOrderThenRotated() {
    onView(withId(R.id.nav_routine)).perform(click());

    ViewInteraction appCompatTextView = onView(
        allOf(withText("Strong 5x5 - Workout A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView.perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

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

    getCurrentActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    sleepFor2Seconds();

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action2)).perform(click());

    ViewInteraction textView = onView(
        allOf(withText("Barbell Row"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withId(R.id.coordinator_layout),
                        0)),
                0),
            isDisplayed()));
    textView.check(matches(withText("Barbell Row")));

    ViewInteraction textView2 = onView(
        allOf(withText("Barbell Back Squat"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withId(R.id.coordinator_layout),
                        0)),
                1),
            isDisplayed()));
    textView2.check(matches(withText("Barbell Back Squat")));

    ViewInteraction textView3 = onView(
        allOf(withText("Barbell Bench Press"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withId(R.id.coordinator_layout),
                        0)),
                2),
            isDisplayed()));
    textView3.check(matches(withText("Barbell Bench Press")));

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    ViewInteraction imageView2 = onView(
        allOf(withId(R.id.drag_image_view), withContentDescription("Drag Icon"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.recycler_view),
                    0),
                0),
            isDisplayed()));

    imageView2.perform(new GeneralSwipeAction(Swipe.FAST, GeneralLocation.BOTTOM_CENTER,
        view -> {
          float[] coordinates = GeneralLocation.CENTER.calculateCoordinates(view);
          coordinates[1] = 1000;
          return coordinates;
        }, Press.FINGER));

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action2)).perform(click());
  }

  @Test
  public void shouldShowSaveChangesDialogIfModifiedThenRotatedThenPressBack() {
    onView(withId(R.id.nav_routine)).perform(click());

    ViewInteraction appCompatTextView = onView(
        allOf(withText("Strong 5x5 - Workout A"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                0),
            isDisplayed()));
    appCompatTextView.perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    ViewInteraction linearLayout = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    0)),
            0),
            isDisplayed()));
    linearLayout.perform(longClick());

    onView(withText(ViewHelper.str_Yes)).perform(click());

    getCurrentActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    sleepFor2Seconds();

    pressBack();

    ViewInteraction textView = onView(
        allOf(withId(R.id.alertTitle), withText("Save changes?"),
            isDisplayed()));
    textView.check(matches(withText("Save changes?")));
  }

  @Test
  public void shouldNotShowSaveChangesDialogIfNoChangesMade() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(scrollTo(), click());

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

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    pressBack();

    onView(ViewMatchers.withText(ViewHelper.str_Save)).check(doesNotExist());

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

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void shouldShowSaveChangesDialogIfDeleteExercise() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(scrollTo(), click());

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

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    ViewInteraction linearLayout = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                    1)),
            0),
            isDisplayed()));
    linearLayout.perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.fab_action2)).perform(click());

    sleepFor2Seconds();

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    ViewInteraction linearLayout2 = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    0)),
            0),
            isDisplayed()));
    linearLayout2.perform(longClick());

    onView(withText(ViewHelper.str_Yes)).perform(click());

    pressBack();

    onView(ViewMatchers.withText(ViewHelper.str_Save)).check(matches(isDisplayed()));

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(scrollTo(), click());

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

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void shouldShowSaveChangesDialogIfRearrangeOrder() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(scrollTo(), click());

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

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

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

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.fab_action2)).perform(click());

    sleepFor2Seconds();

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

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

    sleepFor2Seconds();

    onView(ViewMatchers.withText(ViewHelper.str_Save)).check(matches(isDisplayed()));

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(scrollTo(), click());

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

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void routineShouldGetNewNameAfterExerciseInRoutineIsRenamed() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(scrollTo(), click());

    sleepFor2Seconds();

    onView(withId(R.id.nav_exercise)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(scrollTo(), click());

    sleepFor2Seconds();

    onView(withId(R.id.nav_routine)).perform(click());

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

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    ViewInteraction linearLayout = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                    1)),
            0),
            isDisplayed()));
    linearLayout.perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.fab_action2)).perform(click());

    pressBack();

    onView(withId(R.id.nav_exercise)).perform(click());

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

    onView(withId(R.id.edit_linear_layout)).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("Aa"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(scrollTo(), click());

    sleepFor2Seconds();

    onView(withId(R.id.nav_routine)).perform(click());

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

    onView(withId(R.id.nav_exercise)).perform(click());

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

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.nav_routine)).perform(click());

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

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void shouldAddExercisesToRoutineFromClickingSaveInDiscardDialog() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(scrollTo(), click());

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

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    ViewInteraction linearLayout = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                    1)),
            0),
            isDisplayed()));
    linearLayout.perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    pressBack();

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(scrollTo(), click());

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

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void shouldAddMultipleExercisesToRoutineAlphabetically() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(scrollTo(), click());

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

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

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

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

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

    sleepFor2Seconds();

    onView(withId(R.id.fab_action2)).perform(click());

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

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void shouldBeAbleToAddExerciseToRoutine() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(scrollTo(), click());

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

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    ViewInteraction linearLayout = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                    1)),
            0),
            isDisplayed()));
    linearLayout.perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.fab_action2)).perform(click());

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

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void shouldBeAbleToChangeExerciseOrderInRoutine() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(scrollTo(), click());

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

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

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

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    sleepFor2Seconds();

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

    onView(withId(R.id.fab_action2)).perform(click());

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

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void shouldBeAbleToCreateAndDeleteRoutine() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(scrollTo(), click());

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

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void shouldBeAbleToDeleteExerciseFromRoutineInEditRoutine() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(scrollTo(), click());

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

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

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

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    ViewInteraction linearLayout3 = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    0)),
            0),
            isDisplayed()));
    linearLayout3.perform(longClick());

    onView(withText(ViewHelper.str_Yes)).perform(click());

    ViewInteraction textView = onView(
        allOf(withId(R.id.text_view), withText("Alternating Dumbbell Curl"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.recycler_view),
                    0),
                1),
            isDisplayed()));
    textView.check(doesNotExist());

    onView(withId(R.id.fab_action2)).perform(click());

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

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void shouldBeAbleToDeleteExerciseFromRoutineInViewExercises() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(scrollTo(), click());

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

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

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

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.fab_action2)).perform(click());

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

    onView(withText(ViewHelper.str_Yes)).perform(click());

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

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
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
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(scrollTo(), click());

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

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    ViewInteraction linearLayout = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                    1)),
            0),
            isDisplayed()));
    linearLayout.perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.fab_action2)).perform(click());

    sleepFor2Seconds();

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

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void shouldDeleteExerciseFromRoutineIfExerciseDeletedInExerciseTab() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(scrollTo(), click());

    sleepFor2Seconds();

    onView(withId(R.id.nav_exercise)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(scrollTo(), click());

    sleepFor2Seconds();

    onView(withId(R.id.nav_routine)).perform(click());

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

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    ViewInteraction linearLayout = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                    1)),
            0),
            isDisplayed()));
    linearLayout.perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.fab_action2)).perform(click());

    pressBack();

    onView(withId(R.id.nav_exercise)).perform(click());

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

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.nav_routine)).perform(click());

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

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void shouldKeepExercisesAfterRenameRoutine() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(scrollTo(), click());

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

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    ViewInteraction linearLayout = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                    1)),
            0),
            isDisplayed()));
    linearLayout.perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.fab_action2)).perform(click());

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

    onView(withId(R.id.edit_linear_layout)).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("Aa"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(scrollTo(), click());

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

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void shouldNotAddExercisesToRoutineFromClickingDiscardInDiscardDialog() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(scrollTo(), click());

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

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    ViewInteraction linearLayout = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                    1)),
            0),
            isDisplayed()));
    linearLayout.perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

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

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void shouldNotBeAbleToAddExerciseToRoutineIfAlreadyInRoutine() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(scrollTo(), click());

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

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    ViewInteraction linearLayout = onView(
        allOf(childAtPosition(
            allOf(withId(R.id.recycler_view),
                childAtPosition(
                    withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                    1)),
            0),
            isDisplayed()));
    linearLayout.perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.fab_action2)).perform(click());

    sleepFor2Seconds();

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

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

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

}