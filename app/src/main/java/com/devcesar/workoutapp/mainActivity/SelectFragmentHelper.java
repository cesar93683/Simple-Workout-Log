package com.devcesar.workoutapp.mainActivity;

import android.app.Activity;
import android.content.Intent;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.exerciseActivity.ExerciseActivity;
import com.devcesar.workoutapp.labs.CategoryOrRoutineLab;
import com.devcesar.workoutapp.labs.ExerciseLab;
import com.devcesar.workoutapp.labs.NamedEntityLab;
import com.devcesar.workoutapp.utils.Constants;
import com.devcesar.workoutapp.utils.NamedEntity;
import com.devcesar.workoutapp.viewExercisesActivity.ViewExercisesActivity;

interface SelectFragmentOnClick {

  void onClick(final NamedEntity namedEntity);
}

class SelectFragmentFactoryHelper {

  static SelectFragmentHelper getSelectFragmentHelper(int type, Activity activity) {
    switch (type) {
      case Constants.TYPE_EXERCISE:
        return new SelectFragmentHelper(new ExerciseHelperForFragments(activity),
            new GoToExerciseActivity(activity));
      case Constants.TYPE_CATEGORY:
        return new SelectFragmentHelper(new CategoryHelperForFragments(activity),
            new GoToViewExercisesActivity(activity, type));
      case Constants.TYPE_ROUTINE:
        return new SelectFragmentHelper(new RoutineHelperForFragments(activity),
            new GoToViewExercisesActivity(activity, type));
    }
    throw new RuntimeException("Error: type does not exists");
  }
}

class SelectFragmentHelper {

  private final String name;
  private final NamedEntityLab lab;
  private final SelectFragmentOnClick selectFragmentOnClick;

  SelectFragmentHelper(HelperForFragments helperForFragments,
      SelectFragmentOnClick selectFragmentOnClick) {
    this.name = helperForFragments.getName();
    this.lab = helperForFragments.getNamedEntityLab();
    this.selectFragmentOnClick = selectFragmentOnClick;
  }

  String getName() {
    return this.name;
  }

  NamedEntityLab getLab() {
    return this.lab;
  }

  public void onClick(final NamedEntity namedEntity) {
    selectFragmentOnClick.onClick(namedEntity);
  }
}

class GoToViewExercisesActivity implements SelectFragmentOnClick {

  private final Activity activity;
  private final int type;

  GoToViewExercisesActivity(Activity activity, int type) {
    this.activity = activity;
    this.type = type;
  }

  @Override
  public void onClick(final NamedEntity namedEntity) {
    Intent intent = ViewExercisesActivity.newIntent(activity, namedEntity, type);
    activity.startActivity(intent);
  }
}

class GoToExerciseActivity implements SelectFragmentOnClick {

  private final Activity activity;

  GoToExerciseActivity(Activity activity) {
    this.activity = activity;
  }

  @Override
  public void onClick(final NamedEntity exercise) {
    Intent intent = ExerciseActivity.newIntent(activity, exercise);
    activity.startActivity(intent);
  }
}

class HelperForFragments {

  private final String name;
  private final NamedEntityLab namedEntityLab;

  HelperForFragments(String name, NamedEntityLab namedEntityLab) {
    this.name = name;
    this.namedEntityLab = namedEntityLab;
  }

  public NamedEntityLab getNamedEntityLab() {
    return namedEntityLab;
  }

  public String getName() {
    return name;
  }
}

class ExerciseHelperForFragments extends HelperForFragments {

  ExerciseHelperForFragments(Activity activity) {
    super(activity.getString(R.string.exercise), ExerciseLab.get(activity));
  }
}

class CategoryHelperForFragments extends HelperForFragments {

  CategoryHelperForFragments(Activity activity) {
    super(activity.getString(R.string.category), CategoryOrRoutineLab.getCategoryLab(activity));
  }
}

class RoutineHelperForFragments extends HelperForFragments {

  RoutineHelperForFragments(Activity activity) {
    super(activity.getString(R.string.routine), CategoryOrRoutineLab.getRoutineLab(activity));
  }
}
