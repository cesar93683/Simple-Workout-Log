package com.devcesar.workoutapp.mainActivity;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

class ViewHelper {

  static final String str_AlternatingDumbbellCurl = "Alternating Dumbbell Curl";
  static final String str_Save = "Save";
  static final String str_Yes = "Yes";
  static final String str_Strong5x5WorkoutA = "Strong 5x5 - Workout A";
  static final String str_Exercise = "Exercise";

  static Matcher<View> childAtPosition(
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

  static void sleepFor2Seconds() {
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}