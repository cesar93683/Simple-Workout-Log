package com.devcesar.workoutapp.mainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
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
                    IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                    2),
                1),
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
        allOf(withId(R.id.timer_start_pause), withContentDescription("Play"),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                    2),
                2),
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
                1),
            isDisplayed()));
    button.check(matches(withText("0:02")));

    ViewInteraction appCompatButton3 = onView(
        allOf(withId(R.id.timer_display), withText("0:02"),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                    2),
                1),
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

    ViewInteraction appCompatTextView2 = onView(
        allOf(withId(R.id.timer_display), withText("2:00"),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                    2),
                1),
            isDisplayed()));
    appCompatTextView2.perform(click());

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
        tp.setValue(1);
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

    ViewInteraction appCompatButton = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton.perform(scrollTo(), click());

    pressBack();

    ViewInteraction appCompatTextView3 = onView(
        allOf(withText("Barbell Back Squat"),
            childAtPosition(
                allOf(withId(R.id.recycler_view),
                    childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        1)),
                1),
            isDisplayed()));
    appCompatTextView3.perform(click());

    ViewInteraction textView = onView(
        allOf(withId(R.id.timer_display), withText("1:00"),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                    2),
                1),
            isDisplayed()));
    textView.check(matches(withText("1:00")));

    ViewInteraction appCompatTextView4 = onView(
        allOf(withId(R.id.timer_display), withText("1:00"),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                    2),
                1),
            isDisplayed()));
    appCompatTextView4.perform(click());

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

    ViewInteraction appCompatButton2 = onView(
        allOf(withId(android.R.id.button1), withText("Save"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.buttonPanel),
                    0),
                3)));
    appCompatButton2.perform(scrollTo(), click());
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
        allOf(withId(R.id.timer_start_pause), withContentDescription("Play"),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                    2),
                2),
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
                    IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
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
                1),
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
                1),
            isDisplayed()));
    button2.check(matches(withText("2:00")));
  }

  @Test
  public void canNotSetTimerWhileRunning() {
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
        allOf(withId(R.id.timer_start_pause), withContentDescription("Play"),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                    2),
                2),
            isDisplayed()));
    appCompatImageButton.perform(click());

    try {
      Thread.sleep(900);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction appCompatButton = onView(
        allOf(withId(R.id.timer_display), withText("1:58"),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                    2),
                1),
            isDisplayed()));
    appCompatButton.perform(click());

    ViewInteraction numberPicker = onView(
        allOf(childAtPosition(
            childAtPosition(
                withId(R.id.custom),
                0),
            0),
            isDisplayed()));
    numberPicker.check(doesNotExist());
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
        allOf(withId(R.id.timer_start_pause), withContentDescription("Play"),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                    2),
                2),
            isDisplayed()));
    appCompatImageButton.perform(click());

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction appCompatImageButton2 = onView(
        allOf(withId(R.id.timer_start_pause), withContentDescription("Pause"),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                    2),
                2),
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
                1),
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
        allOf(withId(R.id.timer_start_pause), withContentDescription("Play"),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                    2),
                2),
            isDisplayed()));
    appCompatImageButton.perform(click());

    ViewInteraction imageButton = onView(
        allOf(withId(R.id.timer_start_pause), withContentDescription("Pause"),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                    2),
                2),
            isDisplayed()));
    imageButton.check(matches(isDisplayed()));

    ViewInteraction appCompatImageButton2 = onView(
        allOf(withId(R.id.timer_start_pause), withContentDescription("Pause"),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                    2),
                2),
            isDisplayed()));
    appCompatImageButton2.perform(click());

    ViewInteraction imageButton2 = onView(
        allOf(withId(R.id.timer_start_pause), withContentDescription("Play"),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                    2),
                2),
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
        allOf(withId(R.id.timer_start_pause), withContentDescription("Play"),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                    2),
                2),
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
                1),
            isDisplayed()));
    button.check(matches(withText("1:57")));
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
                    IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                    2),
                1),
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
                1),
            isDisplayed()));
    button.check(matches(withText("3:59")));

    ViewInteraction button2 = onView(
        allOf(withId(R.id.timer_display),
            childAtPosition(
                childAtPosition(
                    IsInstanceOf.instanceOf(android.view.ViewGroup.class),
                    2),
                1),
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
                    IsInstanceOf.instanceOf(android.widget.LinearLayout.class),
                    2),
                1),
            isDisplayed()));
    appCompatButton.perform(click());

    ViewInteraction editText = onView(withText("2"));
    editText.check(matches(isDisplayed()));

    ViewInteraction editText2 = onView(withText("0"));
    editText2.check(matches(isDisplayed()));
  }

}
