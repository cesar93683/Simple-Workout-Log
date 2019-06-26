package com.devcesar.workoutapp.mainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.replaceText;
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
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_AlternatingDumbbellCurl;
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

  @Test
  public void shouldKeepNewExercisesAddedAfterAddingThemThenRotating() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(withText(ViewHelper.str_Strong5x5WorkoutA)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).perform(click());

    rotateLandscape();

    sleepFor2Seconds();

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    ViewInteraction textView = onView(
        allOf(withId(R.id.text_view),
            childAtPosition(
                childAtPosition(
                    withId(R.id.recycler_view),
                    3),
                1)));
    textView.check(matches(withText("Alternating Dumbbell Curl")));
  }

  private void rotateLandscape() {
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
    activity[0].setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
  }

  @Test
  public void shouldSaveNewOrderIfChangedOrderThenRotated() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(withText(ViewHelper.str_Strong5x5WorkoutA)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    ViewInteraction imageView = onView(
        allOf(withId(R.id.drag_image_view),
            childAtPosition(
                childAtPosition(
                    withId(R.id.recycler_view),
                    2),
                0)));
    imageView.perform(new GeneralSwipeAction(Swipe.FAST, GeneralLocation.BOTTOM_CENTER,
        view -> {
          float[] coordinates = GeneralLocation.CENTER.calculateCoordinates(view);
          coordinates[1] = 0;
          return coordinates;
        }, Press.FINGER));

    rotateLandscape();

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
        allOf(withId(R.id.drag_image_view),
            childAtPosition(
                childAtPosition(
                    withId(R.id.recycler_view),
                    0),
                0)));
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

    onView(withText(ViewHelper.str_Strong5x5WorkoutA)).perform(click());

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

    rotateLandscape();

    sleepFor2Seconds();

    pressBack();

    onView(withId(R.id.alertTitle)).check(matches(withText("Save changes?")));
  }

  @Test
  public void shouldNotShowSaveChangesDialogIfNoChangesMade() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    pressBack();

    onView(ViewMatchers.withText(ViewHelper.str_Save)).check(doesNotExist());

    pressBack();

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void shouldShowSaveChangesDialogIfDeleteExercise() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    ViewInteraction linearLayout = onView(
        childAtPosition(
            withId(R.id.recycler_view),
            0));
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

    onView(withText(ViewHelper.str_Save)).check(matches(isDisplayed()));

    onView(withText(ViewHelper.str_Save)).perform(click());

    pressBack();

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void shouldShowSaveChangesDialogIfRearrangeOrder() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    ViewInteraction linearLayout = onView(
        childAtPosition(
            withId(R.id.recycler_view),
            0));
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

    onView(withText(ViewHelper.str_Save)).check(matches(isDisplayed()));

    onView(withText(ViewHelper.str_Save)).perform(click());

    pressBack();

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void routineShouldGetNewNameAfterExerciseInRoutineIsRenamed() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.nav_exercise)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.nav_routine)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    ViewInteraction linearLayout = onView(
        childAtPosition(
            withId(R.id.recycler_view),
            0));
    linearLayout.perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.fab_action2)).perform(click());

    pressBack();

    onView(withId(R.id.nav_exercise)).perform(click());

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.edit_linear_layout)).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("Aa"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.nav_routine)).perform(click());

    onView(withText("A")).perform(click());

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

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void shouldAddExercisesToRoutineFromClickingSaveInDiscardDialog() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    ViewInteraction linearLayout = onView(
        childAtPosition(
            withId(R.id.recycler_view),
            0));
    linearLayout.perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    pressBack();

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

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

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void shouldAddMultipleExercisesToRoutineAlphabetically() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    onView(withText("A")).perform(click());

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
        childAtPosition(
            withId(R.id.recycler_view),
            0));
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
    textView.check(matches(withText("Alternating Dumbbell Curl")));

    ViewInteraction textView2 = onView(
        allOf(withId(R.id.text_view), withText("Barbell Back Squat"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.recycler_view),
                    1),
                1),
            isDisplayed()));
    textView2.check(matches(withText("Barbell Back Squat")));

    ViewInteraction textView3 = onView(
        allOf(withId(R.id.text_view), withText("Barbell Bench Press"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.recycler_view),
                    2),
                1),
            isDisplayed()));
    textView3.check(matches(withText("Barbell Bench Press")));

    sleepFor2Seconds();

    onView(withId(R.id.fab_action2)).perform(click());

    pressBack();

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void shouldBeAbleToAddExerciseToRoutine() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    ViewInteraction linearLayout = onView(
        childAtPosition(
            withId(R.id.recycler_view),
            0));
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
    textView.check(matches(withText("Alternating Dumbbell Curl")));

    pressBack();

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void shouldBeAbleToChangeExerciseOrderInRoutine() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    ViewInteraction linearLayout = onView(
        childAtPosition(
            withId(R.id.recycler_view),
            0));
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
    textView.check(matches(withText("Barbell Bench Press")));

    ViewInteraction textView2 = onView(
        allOf(withText("Alternating Dumbbell Curl"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                        0)),
                1),
            isDisplayed()));
    textView2.check(matches(withText("Alternating Dumbbell Curl")));

    ViewInteraction textView3 = onView(
        allOf(withText("Barbell Back Squat"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                        0)),
                2),
            isDisplayed()));
    textView3.check(matches(withText("Barbell Back Squat")));

    pressBack();

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void shouldBeAbleToCreateAndDeleteRoutine() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    onView(withText("A")).check(matches(withText("A")));

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void shouldBeAbleToDeleteExerciseFromRoutineInEditRoutine() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    ViewInteraction linearLayout = onView(
        childAtPosition(
            withId(R.id.recycler_view),
            0));
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

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void shouldBeAbleToDeleteExerciseFromRoutineInViewExercises() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    ViewInteraction linearLayout = onView(
        childAtPosition(
            withId(R.id.recycler_view),
            0));
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

    onView(withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(longClick());

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

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void shouldBeAbleToSwitchToRoutineCategory() {
    onView(withId(R.id.nav_routine)).perform(click());

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

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    ViewInteraction linearLayout = onView(
        childAtPosition(
            withId(R.id.recycler_view),
            0));
    linearLayout.perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.fab_action2)).perform(click());

    sleepFor2Seconds();

    onView(withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.title)).check(matches(withText("Alternating Dumbbell Curl")));

    pressBack();

    pressBack();

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void shouldDeleteExerciseFromRoutineIfExerciseDeletedInExerciseTab() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.nav_exercise)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.nav_routine)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    ViewInteraction linearLayout = onView(
        childAtPosition(
            withId(R.id.recycler_view),
            0));
    linearLayout.perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.fab_action2)).perform(click());

    pressBack();

    onView(withId(R.id.nav_exercise)).perform(click());

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.nav_routine)).perform(click());

    onView(withText("A")).perform(click());

    onView(withText("A")).check(doesNotExist());

    pressBack();

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void shouldKeepExercisesAfterRenameRoutine() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    ViewInteraction linearLayout = onView(
        childAtPosition(
            withId(R.id.recycler_view),
            0));
    linearLayout.perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.fab_action2)).perform(click());

    pressBack();

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.edit_linear_layout)).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("Aa"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

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

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    ViewInteraction linearLayout = onView(
        childAtPosition(
            withId(R.id.recycler_view),
            0));
    linearLayout.perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    pressBack();

    onView(withText("Discard")).perform(click());

    onView(withText(ViewHelper.str_AlternatingDumbbellCurl)).check(doesNotExist());

    pressBack();

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void shouldNotBeAbleToAddExerciseToRoutineIfAlreadyInRoutine() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(ViewHelper.str_Save)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    ViewInteraction linearLayout = onView(
        childAtPosition(
            withId(R.id.recycler_view),
            0));
    linearLayout.perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.fab_action2)).perform(click());

    sleepFor2Seconds();

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    onView(allOf(withId(R.id.text_view), withText("Alternating Dumbbell Curl")))
        .check(doesNotExist());

    pressBack();

    pressBack();

    pressBack();

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

}