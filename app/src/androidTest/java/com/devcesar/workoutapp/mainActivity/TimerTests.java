package com.devcesar.workoutapp.mainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
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
import androidx.test.rule.ActivityTestRule;
import com.devcesar.workoutapp.R;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;

public class TimerTests {

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(
      MainActivity.class);

  @Test
  public void afterTimerIsDoneShouldReset() {
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
        tp.setValue(0);
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
        tp.setValue(2);
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

    ViewInteraction appCompatImageButton = onView(
        allOf(withId(R.id.timer_play), withContentDescription("Play"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                4),
            isDisplayed()));
    appCompatImageButton.perform(click());

    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction button = onView(
        allOf(withId(R.id.timer_display),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                    2),
                2),
            isDisplayed()));
    button.check(matches(withText("0:02")));

    ViewInteraction appCompatButton3 = onView(
        allOf(withId(R.id.timer_display), withText("0:02"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                2),
            isDisplayed()));
    appCompatButton3.perform(click());

    ViewInteraction numberPicker3 = onView(
        allOf(childAtPosition(
            childAtPosition(
                withId(R.id.custom),
                0),
            0),
            isDisplayed()));
    numberPicker3.perform(new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        NumberPicker tp = (NumberPicker) view;
        tp.setValue(2);
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

    ViewInteraction numberPicker4 = onView(
        allOf(childAtPosition(
            childAtPosition(
                withId(R.id.custom),
                0),
            2),
            isDisplayed()));
    numberPicker4.perform(new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        NumberPicker tp = (NumberPicker) view;
        tp.setValue(0);
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

    ViewInteraction appCompatButton4 = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton4.perform(scrollTo(), click());
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
  public void canResetTimer() {
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
        allOf(withId(R.id.timer_play), withContentDescription("Play"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                4),
            isDisplayed()));
    appCompatImageButton.perform(click());

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction appCompatImageButton2 = onView(
        allOf(withId(R.id.timer_reset), withContentDescription("Reset"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                0),
            isDisplayed()));
    appCompatImageButton2.perform(click());

    ViewInteraction button = onView(
        allOf(withId(R.id.timer_display),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                    2),
                2),
            isDisplayed()));
    button.check(matches(withText("2:00")));

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction button2 = onView(
        allOf(withId(R.id.timer_display),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                    2),
                2),
            isDisplayed()));
    button2.check(matches(withText("2:00")));
  }

  @Test
  public void canNotIncrementDecrementOrSetTimerWhileRunning() {
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
        allOf(withId(R.id.timer_play), withContentDescription("Play"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                4),
            isDisplayed()));
    appCompatImageButton.perform(click());

    ViewInteraction appCompatImageButton2 = onView(
        allOf(withId(R.id.timer_increment), withContentDescription("Increment"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                3),
            isDisplayed()));
    appCompatImageButton2.perform(click());

    try {
      Thread.sleep(900);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction button2 = onView(
        allOf(withId(R.id.timer_display),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                    2),
                2),
            isDisplayed()));
    button2.check(matches(withText("1:58")));

    try {
      Thread.sleep(900);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction appCompatButton = onView(
        allOf(withId(R.id.timer_display), withText("1:57"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                2),
            isDisplayed()));
    appCompatButton.perform(click());

    ViewInteraction button3 = onView(
        allOf(withId(R.id.timer_display),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                    2),
                2),
            isDisplayed()));
    button3.check(matches(withText("1:57")));

    try {
      Thread.sleep(900);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction appCompatImageButton3 = onView(
        allOf(withId(R.id.timer_decrement), withContentDescription("Decrement"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                1),
            isDisplayed()));
    appCompatImageButton3.perform(click());

    ViewInteraction button4 = onView(
        allOf(withId(R.id.timer_display),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                    2),
                2),
            isDisplayed()));
    button4.check(matches(withText("1:56")));
  }

  @Test
  public void canPauseTimer() {
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
        allOf(withId(R.id.timer_play), withContentDescription("Play"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                4),
            isDisplayed()));
    appCompatImageButton.perform(click());

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction appCompatImageButton2 = onView(
        allOf(withId(R.id.timer_play), withContentDescription("Pause"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                4),
            isDisplayed()));
    appCompatImageButton2.perform(click());

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction button = onView(
        allOf(withId(R.id.timer_display),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                    2),
                2),
            isDisplayed()));
    button.check(matches(withText("1:57")));
  }

  @Test
  public void timerIconChangesWhenStartedAndStopped() {
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
        allOf(withId(R.id.timer_play), withContentDescription("Play"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                4),
            isDisplayed()));
    appCompatImageButton.perform(click());

    ViewInteraction imageButton = onView(
        allOf(withId(R.id.timer_play), withContentDescription("Pause"),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                    2),
                4),
            isDisplayed()));
    imageButton.check(matches(isDisplayed()));

    ViewInteraction appCompatImageButton2 = onView(
        allOf(withId(R.id.timer_play), withContentDescription("Pause"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                4),
            isDisplayed()));
    appCompatImageButton2.perform(click());

    ViewInteraction imageButton2 = onView(
        allOf(withId(R.id.timer_play), withContentDescription("Play"),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                    2),
                4),
            isDisplayed()));
    imageButton2.check(matches(isDisplayed()));
  }

  @Test
  public void canStartTimer() {
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
        allOf(withId(R.id.timer_play), withContentDescription("Play"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                4),
            isDisplayed()));
    appCompatImageButton.perform(click());

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction button = onView(
        allOf(withId(R.id.timer_display),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                    2),
                2),
            isDisplayed()));
    button.check(matches(withText("1:57")));
  }

  @Test
  public void canNotSetTimerAbove5959() {
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

    ViewInteraction appCompatImageButton = onView(
        allOf(withId(R.id.timer_increment), withContentDescription("Increment"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                3),
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
    button.check(matches(withText("59:59")));

    ViewInteraction appCompatButton3 = onView(
        allOf(withId(R.id.timer_display), withText("59:59"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                2),
            isDisplayed()));
    appCompatButton3.perform(click());

    ViewInteraction numberPicker3 = onView(
        allOf(childAtPosition(
            childAtPosition(
                withId(R.id.custom),
                0),
            0),
            isDisplayed()));
    numberPicker3.perform(new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        NumberPicker tp = (NumberPicker) view;
        tp.setValue(2);
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

    ViewInteraction numberPicker4 = onView(
        allOf(childAtPosition(
            childAtPosition(
                withId(R.id.custom),
                0),
            2),
            isDisplayed()));
    numberPicker4.perform(new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        NumberPicker tp = (NumberPicker) view;
        tp.setValue(0);
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
    ViewInteraction appCompatButton4 = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton4.perform(scrollTo(), click());
  }

  @Test
  public void canNotSetTimerBelowZero() {
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
        tp.setValue(0);
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
        tp.setValue(0);
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
    button.check(matches(withText("0:00")));

    ViewInteraction appCompatButton3 = onView(
        allOf(withId(R.id.timer_display), withText("0:00"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                2),
            isDisplayed()));
    appCompatButton3.perform(click());

    ViewInteraction numberPicker3 = onView(
        allOf(childAtPosition(
            childAtPosition(
                withId(R.id.custom),
                0),
            0),
            isDisplayed()));
    numberPicker3.perform(new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        NumberPicker tp = (NumberPicker) view;
        tp.setValue(2);
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

    ViewInteraction numberPicker4 = onView(
        allOf(childAtPosition(
            childAtPosition(
                withId(R.id.custom),
                0),
            2),
            isDisplayed()));
    numberPicker4.perform(new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        NumberPicker tp = (NumberPicker) view;
        tp.setValue(0);
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
    ViewInteraction appCompatButton4 = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton4.perform(scrollTo(), click());
  }

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

    ViewInteraction button2 = onView(
        allOf(withId(R.id.timer_display),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                    2),
                2),
            isDisplayed()));
    button2.perform(click());

    ViewInteraction numberPicker3 = onView(
        allOf(childAtPosition(
            childAtPosition(
                withId(R.id.custom),
                0),
            0),
            isDisplayed()));
    numberPicker3.perform(new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        NumberPicker tp = (NumberPicker) view;
        tp.setValue(2);
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

    ViewInteraction numberPicker4 = onView(
        allOf(childAtPosition(
            childAtPosition(
                withId(R.id.custom),
                0),
            2),
            isDisplayed()));
    numberPicker4.perform(new ViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        NumberPicker tp = (NumberPicker) view;
        tp.setValue(0);
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

    ViewInteraction appCompatButton3 = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton3.perform(scrollTo(), click());
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

    ViewInteraction editText = onView(withText("2"));
    editText.check(matches(isDisplayed()));

    ViewInteraction editText2 = onView(withText("0"));
    editText2.check(matches(isDisplayed()));
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

    ViewInteraction appCompatImageButton15 = onView(
        allOf(withId(R.id.timer_decrement), withContentDescription("Decrement"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                1),
            isDisplayed()));
    appCompatImageButton15.perform(click());

    ViewInteraction appCompatImageButton16 = onView(
        allOf(withId(R.id.timer_decrement), withContentDescription("Decrement"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                1),
            isDisplayed()));
    appCompatImageButton16.perform(click());

    ViewInteraction appCompatImageButton17 = onView(
        allOf(withId(R.id.timer_decrement), withContentDescription("Decrement"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                1),
            isDisplayed()));
    appCompatImageButton17.perform(click());

    ViewInteraction appCompatImageButton18 = onView(
        allOf(withId(R.id.timer_decrement), withContentDescription("Decrement"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                1),
            isDisplayed()));
    appCompatImageButton18.perform(click());

    ViewInteraction appCompatImageButton19 = onView(
        allOf(withId(R.id.timer_decrement), withContentDescription("Decrement"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                1),
            isDisplayed()));
    appCompatImageButton19.perform(click());

    ViewInteraction appCompatImageButton20 = onView(
        allOf(withId(R.id.timer_decrement), withContentDescription("Decrement"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                1),
            isDisplayed()));
    appCompatImageButton20.perform(click());

    ViewInteraction appCompatImageButton21 = onView(
        allOf(withId(R.id.timer_decrement), withContentDescription("Decrement"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                1),
            isDisplayed()));
    appCompatImageButton21.perform(click());

    ViewInteraction appCompatImageButton22 = onView(
        allOf(withId(R.id.timer_decrement), withContentDescription("Decrement"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                1),
            isDisplayed()));
    appCompatImageButton22.perform(click());

    ViewInteraction appCompatImageButton23 = onView(
        allOf(withId(R.id.timer_decrement), withContentDescription("Decrement"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                1),
            isDisplayed()));
    appCompatImageButton23.perform(click());

    ViewInteraction appCompatImageButton24 = onView(
        allOf(withId(R.id.timer_decrement), withContentDescription("Decrement"),
            childAtPosition(
                childAtPosition(
                    withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    2),
                1),
            isDisplayed()));
    appCompatImageButton24.perform(click());
  }

}
