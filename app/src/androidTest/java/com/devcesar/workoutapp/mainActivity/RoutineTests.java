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
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.childAtPosition;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.sleepFor2Seconds;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_AlternatingDumbbellCurl;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_BarbellBackSquat;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_BarbellBenchPress;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_BarbellRow;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_Discard;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_Routine;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_Save;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_SaveChanges;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_Strong5x5WorkoutA;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_Yes;
import static org.hamcrest.Matchers.allOf;

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

    onView(withText(str_Strong5x5WorkoutA)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).perform(click());

    rotateLandscape();

    sleepFor2Seconds();

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(childAtPosition(childAtPosition(withId(R.id.recycler_view), 3), 1))
        .check(matches(withText(str_AlternatingDumbbellCurl)));
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

    onView(withText(str_Strong5x5WorkoutA)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    ViewInteraction imageView = onView(
        allOf(withId(R.id.drag_image_view),
            childAtPosition(
                childAtPosition(
                    withId(R.id.recycler_view),
                    2),
                0)));
    imageView.perform(dragUp());

    rotateLandscape();

    sleepFor2Seconds();

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action2)).perform(click());

    onView(childAtPosition(withId(R.id.recycler_view), 0)).check(matches(withText(str_BarbellRow)));

    onView(childAtPosition(withId(R.id.recycler_view), 1))
        .check(matches(withText(str_BarbellBackSquat)));

    onView(childAtPosition(withId(R.id.recycler_view), 2))
        .check(matches(withText(str_BarbellBenchPress)));

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

  private GeneralSwipeAction dragUp() {
    return new GeneralSwipeAction(Swipe.FAST, GeneralLocation.BOTTOM_CENTER,
        view -> {
          float[] coordinates = GeneralLocation.CENTER.calculateCoordinates(view);
          coordinates[1] = 0;
          return coordinates;
        }, Press.FINGER);
  }

  @Test
  public void shouldShowSaveChangesDialogIfModifiedThenRotatedThenPressBack() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(withText(str_Strong5x5WorkoutA)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withText(str_BarbellBackSquat)).perform(longClick());

    onView(withText(str_Yes)).perform(click());

    rotateLandscape();

    sleepFor2Seconds();

    pressBack();

    onView(withId(R.id.alertTitle)).check(matches(withText(str_SaveChanges)));
  }

  @Test
  public void shouldNotShowSaveChangesDialogIfNoChangesMade() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(str_Save)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    pressBack();

    onView(ViewMatchers.withText(str_Save)).check(doesNotExist());

    pressBack();

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(str_Yes)).perform(click());
  }

  @Test
  public void shouldShowSaveChangesDialogIfDeleteExercise() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(str_Save)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.fab_action2)).perform(click());

    sleepFor2Seconds();

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).perform(longClick());

    onView(withText(str_Yes)).perform(click());

    pressBack();

    onView(withText(str_Save)).check(matches(isDisplayed()));

    onView(withText(str_Save)).perform(click());

    pressBack();

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(str_Yes)).perform(click());
  }

  @Test
  public void shouldShowSaveChangesDialogIfRearrangeOrder() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(str_Save)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(withText(str_BarbellBackSquat)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.fab_action2)).perform(click());

    sleepFor2Seconds();

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    ViewInteraction imageView = onView(
        allOf(withId(R.id.drag_image_view),
            childAtPosition(
                childAtPosition(
                    withId(R.id.recycler_view),
                    1),
                0)));
    imageView.perform(dragUp());

    pressBack();

    sleepFor2Seconds();

    onView(withText(str_Save)).check(matches(isDisplayed()));

    onView(withText(str_Save)).perform(click());

    pressBack();

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(str_Yes)).perform(click());
  }

  @Test
  public void routineShouldGetNewNameAfterExerciseInRoutineIsRenamed() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(str_Save)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.nav_exercise)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("AA"), closeSoftKeyboard());

    onView(ViewMatchers.withText(str_Save)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.nav_routine)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    onView(withText("AA")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.fab_action2)).perform(click());

    pressBack();

    onView(withId(R.id.nav_exercise)).perform(click());

    onView(withText("AA")).perform(longClick());

    onView(withId(R.id.edit_linear_layout)).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("AAa"), closeSoftKeyboard());

    onView(ViewMatchers.withText(str_Save)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.nav_routine)).perform(click());

    onView(withText("A")).perform(click());

    onView(childAtPosition(withId(R.id.recycler_view), 0)).check(matches(withText("AAa")));

    pressBack();

    onView(withId(R.id.nav_exercise)).perform(click());

    onView(withText("AAa")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(str_Yes)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.nav_routine)).perform(click());

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(str_Yes)).perform(click());
  }

  @Test
  public void shouldAddExercisesToRoutineFromClickingSaveInDiscardDialog() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(str_Save)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    pressBack();

    onView(ViewMatchers.withText(str_Save)).perform(click());

    onView(childAtPosition(withId(R.id.recycler_view), 0))
        .check(matches(withText(str_AlternatingDumbbellCurl)));

    pressBack();

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(str_Yes)).perform(click());
  }

  @Test
  public void shouldAddMultipleExercisesToRoutineAlphabetically() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(str_Save)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(withText(str_BarbellBackSquat)).perform(click());

    onView(withText(str_BarbellBenchPress)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView((childAtPosition(childAtPosition(withId(R.id.recycler_view), 0), 1)))
        .check(matches(withText(str_AlternatingDumbbellCurl)));

    onView((childAtPosition(childAtPosition(withId(R.id.recycler_view), 1), 1)))
        .check(matches(withText(str_BarbellBackSquat)));

    onView((childAtPosition(childAtPosition(withId(R.id.recycler_view), 2), 1)))
        .check(matches(withText(str_BarbellBenchPress)));

    sleepFor2Seconds();

    onView(withId(R.id.fab_action2)).perform(click());

    pressBack();

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(str_Yes)).perform(click());
  }

  @Test
  public void shouldBeAbleToAddExerciseToRoutine() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(str_Save)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.fab_action2)).perform(click());

    onView(childAtPosition(withId(R.id.recycler_view), 0))
        .check(matches(withText(str_AlternatingDumbbellCurl)));

    pressBack();

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(str_Yes)).perform(click());
  }

  @Test
  public void shouldBeAbleToChangeExerciseOrderInRoutine() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(str_Save)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(withText(str_BarbellBackSquat)).perform(click());

    onView(withText(str_BarbellBenchPress)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    sleepFor2Seconds();

    ViewInteraction imageView = onView(
        allOf(withId(R.id.drag_image_view),
            childAtPosition(
                childAtPosition(
                    withId(R.id.recycler_view),
                    2),
                0)));
    imageView.perform(dragUp());

    onView(withId(R.id.fab_action2)).perform(click());

    onView(childAtPosition(withId(R.id.recycler_view), 0))
        .check(matches(withText(str_BarbellBenchPress)));

    onView(childAtPosition(withId(R.id.recycler_view), 1))
        .check(matches(withText(str_AlternatingDumbbellCurl)));

    onView(childAtPosition(withId(R.id.recycler_view), 2))
        .check(matches(withText(str_BarbellBackSquat)));

    pressBack();

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(str_Yes)).perform(click());
  }

  @Test
  public void shouldBeAbleToCreateAndDeleteRoutine() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(str_Save)).perform(click());

    onView(allOf(childAtPosition(withId(R.id.recycler_view), 0), isDisplayed()))
        .check(matches(withText("A")));

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(str_Yes)).perform(click());
  }

  @Test
  public void shouldBeAbleToDeleteExerciseFromRoutineInEditRoutine() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(str_Save)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).perform(longClick());

    onView(withText(str_Yes)).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).check(doesNotExist());

    onView(withId(R.id.fab_action2)).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).check(doesNotExist());

    pressBack();

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(str_Yes)).perform(click());
  }

  @Test
  public void shouldBeAbleToDeleteExerciseFromRoutineInViewExercises() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(str_Save)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.fab_action2)).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).perform(longClick());

    onView(withText(str_Yes)).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).check(doesNotExist());

    pressBack();

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(str_Yes)).perform(click());
  }

  @Test
  public void shouldBeAbleToSwitchToRoutineCategory() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(childAtPosition(withId(R.id.action_bar), 0)).check(matches(withText(str_Routine)));
  }

  @Test
  public void shouldBeAbleToVisitExerciseFromRoutine() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(str_Save)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.fab_action2)).perform(click());

    sleepFor2Seconds();

    onView(withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(withId(R.id.title)).check(matches(withText(str_AlternatingDumbbellCurl)));

    pressBack();

    pressBack();

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(str_Yes)).perform(click());
  }

  @Test
  public void shouldDeleteExerciseFromRoutineIfExerciseDeletedInExerciseTab() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(str_Save)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.nav_exercise)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("AA"), closeSoftKeyboard());

    onView(ViewMatchers.withText(str_Save)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.nav_routine)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    onView(withText("AA")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.fab_action2)).perform(click());

    pressBack();

    onView(withId(R.id.nav_exercise)).perform(click());

    onView(withText("AA")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(str_Yes)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.nav_routine)).perform(click());

    onView(withText("A")).perform(click());

    onView(withText("AA")).check(doesNotExist());

    pressBack();

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(str_Yes)).perform(click());
  }

  @Test
  public void shouldKeepExercisesAfterRenameRoutine() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(str_Save)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.fab_action2)).perform(click());

    pressBack();

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.edit_linear_layout)).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("Aa"), closeSoftKeyboard());

    onView(ViewMatchers.withText(str_Save)).perform(click());

    onView(withText("Aa")).perform(click());

    onView(childAtPosition(withId(R.id.recycler_view), 0))
        .check(matches(withText(str_AlternatingDumbbellCurl)));

    pressBack();

    onView(withText("Aa")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(str_Yes)).perform(click());
  }

  @Test
  public void shouldNotAddExercisesToRoutineFromClickingDiscardInDiscardDialog() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(str_Save)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    pressBack();

    onView(withText(str_Discard)).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).check(doesNotExist());

    pressBack();

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(str_Yes)).perform(click());
  }

  @Test
  public void shouldNotBeAbleToAddExerciseToRoutineIfAlreadyInRoutine() {
    onView(withId(R.id.nav_routine)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(str_Save)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.fab_action2)).perform(click());

    sleepFor2Seconds();

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.fab_expand_menu_button)).perform(click());

    onView(withId(R.id.fab_action1)).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).check(doesNotExist());

    pressBack();

    pressBack();

    pressBack();

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(str_Yes)).perform(click());
  }

}