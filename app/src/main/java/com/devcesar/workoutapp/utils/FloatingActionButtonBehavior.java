package com.devcesar.workoutapp.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.snackbar.Snackbar.SnackbarLayout;

class FloatingActionButtonBehavior extends CoordinatorLayout.Behavior<FloatingActionsMenu> {

  public FloatingActionButtonBehavior(Context context, AttributeSet attrs) {
  }

  @Override
  public boolean layoutDependsOn(@NonNull CoordinatorLayout parent,
      @NonNull FloatingActionsMenu child, @NonNull View dependency) {
    return dependency instanceof SnackbarLayout;
  }

  @Override
  public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent,
      @NonNull FloatingActionsMenu child, @NonNull View dependency) {
    float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
    child.setTranslationY(translationY);
    return true;
  }
}