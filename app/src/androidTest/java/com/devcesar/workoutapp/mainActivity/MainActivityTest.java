package com.devcesar.workoutapp.mainActivity;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.exerciseActivity.ExerciseActivity;
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
  public ActivityTestRule<ExerciseActivity> mActivityTestRule =
      new ActivityTestRule<ExerciseActivity>(
          ExerciseActivity.class) {
        @Override
        protected Intent getActivityIntent() {
          Intent intent = new Intent();
          intent.putExtra("EXTRA_EXERCISE_NAME", "name");
          intent.putExtra("EXTRA_EXERCISE_ID", 1);
          return intent;
        }
      };

  @Test
  public void ifTimerStartedThenPausedThenRotatedTimerShouldKeepSameTimeAsBefore() {
//    ViewInteraction appCompatTextView = onView(
//        allOf(withText("Alternating Dumbbell Curl"),
//            childAtPosition(
//                allOf(withId(R.id.recycler_view),
//                    childAtPosition(
//                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
//                        1)),
//                0),
//            isDisplayed()));
//    appCompatTextView.perform(click());

    ViewInteraction appCompatImageView = onView(
        allOf(withId(R.id.timer_start_pause), withContentDescription("Play"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("android.widget.LinearLayout")),
                    2),
                2),
            isDisplayed()));
    appCompatImageView.perform(click());

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction appCompatImageView2 = onView(
        allOf(withId(R.id.timer_start_pause), withContentDescription("Pause"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("android.widget.LinearLayout")),
                    2),
                2),
            isDisplayed()));
    appCompatImageView2.perform(click());

    mActivityTestRule.getActivity()
        .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction textView = onView(
        allOf(withId(R.id.timer_display), withText("1:57"),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                    2),
                1),
            isDisplayed()));
    textView.check(matches(withText("1:57")));
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
  public void ifTimerRunningShouldContinueAfterRotate() {
    ViewInteraction appCompatImageView = onView(
        allOf(withId(R.id.timer_start_pause), withContentDescription("Play"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("android.widget.LinearLayout")),
                    2),
                2),
            isDisplayed()));
    appCompatImageView.perform(click());

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    mActivityTestRule.getActivity()
        .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction textView = onView(
        allOf(withId(R.id.timer_display), withText("1:56"),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                    2),
                1),
            isDisplayed()));
    textView.check(matches(withText("1:56")));
  }
}
