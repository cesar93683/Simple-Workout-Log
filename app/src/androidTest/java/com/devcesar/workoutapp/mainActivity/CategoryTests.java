package com.devcesar.workoutapp.mainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.childAtPosition;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.sleepFor2Seconds;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_AlternatingDumbbellCurl;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_Back;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_BarbellBackSquat;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_BarbellBenchPress;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_BarbellRow;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_Category;
import static com.devcesar.workoutapp.mainActivity.ViewHelper.str_Save;
import static org.hamcrest.Matchers.allOf;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import androidx.test.espresso.ViewInteraction;
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
public class CategoryTests {

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(
      MainActivity.class);

  @Test
  public void exercisesShouldRemainedCheckedAndCanBeAddedAfterRotating() {
    onView(withId(R.id.nav_category)).perform(click());

    onView(withText(str_Back)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).perform(click());

    rotateLandscape();

    sleepFor2Seconds();

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(longClick());

    onView(withText(ViewHelper.str_Yes)).perform(click());
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
  public void shouldKeepFilterAfterRotating() {
    onView(withId(R.id.nav_category)).perform(click());

    onView(withText(str_Back)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.filter_edit_text)).perform(replaceText("sq"), closeSoftKeyboard());

    rotateLandscape();

    sleepFor2Seconds();

    onView(withId(R.id.filter_edit_text)).check(matches(withText("sq")));

    ViewInteraction textView = onView(
        allOf(withId(R.id.text_view),
            childAtPosition(
                childAtPosition(
                    withId(R.id.recycler_view),
                    0),
                1)));
    textView.check(matches(withText(str_BarbellBackSquat)));
  }

  @Test
  public void exerciseThatIsCheckedThenUncheckedShouldNotBeAdded() {
    onView(withId(R.id.nav_category)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(str_Save)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(withText(str_BarbellBackSquat)).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withText(ViewHelper.str_AlternatingDumbbellCurl)).check(doesNotExist());

    pressBack();

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void canFilterExercises() {
    onView(withId(R.id.nav_category)).perform(click());

    onView(withText(str_Back)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(allOf(withId(R.id.filter_edit_text), isDisplayed()))
        .perform(replaceText("z"), closeSoftKeyboard());

    onView(withText(ViewHelper.str_AlternatingDumbbellCurl)).check(doesNotExist());
  }

  @Test
  public void canAddExerciseThatIsHiddenByFilter() {
    onView(withId(R.id.nav_category)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(str_Save)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(allOf(withId(R.id.filter_edit_text), isDisplayed()))
        .perform(replaceText("z"), closeSoftKeyboard());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(childAtPosition(withId(R.id.recycler_view), 0))
        .check(matches(withText(str_AlternatingDumbbellCurl)));

    pressBack();

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void canAddMultipleExercisesAndAreAlphabetized() {
    onView(withId(R.id.nav_category)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(allOf(withId(android.R.id.button1), withText(str_Save))).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withText(str_BarbellBackSquat)).perform(click());

    onView(withText(str_BarbellBenchPress)).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(childAtPosition(withId(R.id.recycler_view), 0))
        .check(matches(withText(str_AlternatingDumbbellCurl)));

    onView(childAtPosition(withId(R.id.recycler_view), 1))
        .check(matches(withText(str_BarbellBackSquat)));

    onView(childAtPosition(withId(R.id.recycler_view), 2))
        .check(matches(withText(str_BarbellBenchPress)));

    pressBack();

    onView(withId(R.id.nav_category)).perform(click());

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void canAddExerciseByClickingCheckBoxOrText() {
    onView(withId(R.id.nav_category)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(allOf(withId(android.R.id.button1), withText(str_Save))).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(allOf(withId(R.id.check_box), hasSibling(withText(str_AlternatingDumbbellCurl))))
        .perform(click());

    onView(withText(str_BarbellBackSquat)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(childAtPosition((withId(R.id.recycler_view)), 0))
        .check(matches(withText(str_AlternatingDumbbellCurl)));

    onView(childAtPosition((withId(R.id.recycler_view)), 1))
        .check(matches(withText(str_BarbellBackSquat)));

    pressBack();

    onView(withId(R.id.nav_category)).perform(click());

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void canVisitExerciseFromCategory() {
    onView(withId(R.id.nav_category)).perform(click());

    onView(withText(str_Back)).perform(click());

    onView(withText(str_BarbellRow)).perform(click());

    onView(withId(R.id.title)).check(matches(withText(str_BarbellRow)));

    pressBack();

    pressBack();
  }

  @Test
  public void shouldBeAbleToAddExerciseToCategory() {
    onView(withId(R.id.nav_category)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(allOf(withId(android.R.id.button1), withText(str_Save))).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(childAtPosition((withId(R.id.recycler_view)), 0))
        .check(matches(withText(str_AlternatingDumbbellCurl)));

    pressBack();

    onView(withId(R.id.nav_category)).perform(click());

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void categoryShouldGetNewNameAfterExerciseInCategoryIsRenamed() {
    onView(withId(R.id.nav_category)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(str_Save)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.nav_exercise)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("AA"), closeSoftKeyboard());

    onView(ViewMatchers.withText(str_Save)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.nav_category)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withText("AA")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    pressBack();

    onView(withId(R.id.nav_exercise)).perform(click());

    onView(withText("AA")).perform(longClick());

    onView(withId(R.id.edit_linear_layout)).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("AAa"), closeSoftKeyboard());

    onView(ViewMatchers.withText(str_Save)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.nav_category)).perform(click());

    onView(withText("A")).perform(click());

    onView(childAtPosition((withId(R.id.recycler_view)), 0)).check(matches(withText("AAa")));

    pressBack();

    onView(withId(R.id.nav_exercise)).perform(click());

    onView(withText("AAa")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.nav_category)).perform(click());

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void shouldBeAbleToSwitchToCategory() {
    onView(withId(R.id.nav_category)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(allOf(withId(android.R.id.button1), withText(str_Save))).perform(click());

    onView(withId(R.id.nav_category)).perform(click());

    onView(childAtPosition(withId(R.id.action_bar), 0)).check(matches(withText(str_Category)));

    onView(withId(R.id.nav_category)).perform(click());

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void shouldDeleteExerciseFromCategoryIfExerciseDeletedInExerciseTab() {
    onView(withId(R.id.nav_category)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(allOf(withId(android.R.id.button1), withText(str_Save))).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.nav_exercise)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("AA"), closeSoftKeyboard());

    onView(ViewMatchers.withText(str_Save)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.nav_category)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withText("AA")).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    pressBack();

    onView(withId(R.id.nav_exercise)).perform(click());

    onView(withText("AA")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());

    sleepFor2Seconds();

    onView(withId(R.id.nav_category)).perform(click());

    onView(withText("A")).perform(click());

    onView(withText("AA")).check(doesNotExist());

    pressBack();

    onView(withId(R.id.nav_category)).perform(click());

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void shouldBeAbleToDeleteExerciseFromCategoryInViewExercises() {
    onView(withId(R.id.nav_category)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(str_Save)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withText(ViewHelper.str_AlternatingDumbbellCurl)).perform(longClick());

    onView(withText(ViewHelper.str_Yes)).perform(click());

    onView(withText(ViewHelper.str_AlternatingDumbbellCurl)).check(doesNotExist());

    pressBack();

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void shouldKeepExercisesAfterRenameCategory() {
    onView(withId(R.id.nav_category)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(ViewMatchers.withText(str_Save)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

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

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }

  @Test
  public void shouldNotBeAbleToAddExerciseToCategoryIfAlreadyInCategory() {
    onView(withId(R.id.nav_category)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withId(R.id.text_input_edit_text)).perform(replaceText("A"), closeSoftKeyboard());

    onView(allOf(withId(android.R.id.button1), withText(str_Save))).perform(click());

    onView(withId(R.id.nav_category)).perform(click());

    onView(withText("A")).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).perform(click());

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    sleepFor2Seconds();

    onView(allOf(withId(R.id.fab), isDisplayed())).perform(click());

    onView(withText(str_AlternatingDumbbellCurl)).check(doesNotExist());

    pressBack();

    pressBack();

    onView(withId(R.id.nav_category)).perform(click());

    onView(withText("A")).perform(longClick());

    onView(withId(R.id.delete_linear_layout)).perform(click());

    onView(withText(ViewHelper.str_Yes)).perform(click());
  }
}