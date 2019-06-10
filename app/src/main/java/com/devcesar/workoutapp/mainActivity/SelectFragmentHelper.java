package com.devcesar.workoutapp.mainActivity;

import android.app.Activity;
import android.content.Intent;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.exerciseActivity.ExerciseActivity;
import com.devcesar.workoutapp.labs.CategoryLab;
import com.devcesar.workoutapp.labs.ExerciseLab;
import com.devcesar.workoutapp.labs.NamedEntityLab;
import com.devcesar.workoutapp.labs.RoutineLab;
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
        return new SelectFragmentExercise(activity, new GoToExerciseActivity(activity));
      case Constants.TYPE_CATEGORY:
        return new SelectFragmentCategory(activity,
            new GoToViewExercisesActivity(activity, Constants.TYPE_CATEGORY));
      case Constants.TYPE_ROUTINE:
        return new SelectFragmentRoutine(activity,
            new GoToViewExercisesActivity(activity, Constants.TYPE_ROUTINE));
    }
    throw new RuntimeException("Error: type does not exists");
  }
}

abstract class SelectFragmentHelper {

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
    Intent intent = ViewExercisesActivity
        .newIntent(activity, namedEntity.getName(), namedEntity.getId(), type);
    activity.startActivity(intent);
  }
}

class GoToExerciseActivity implements SelectFragmentOnClick {

  private final Activity activity;

  GoToExerciseActivity(Activity activity) {
    this.activity = activity;
  }

  @Override
  public void onClick(final NamedEntity namedEntity) {
    Intent intent = ExerciseActivity
        .newIntent(activity, namedEntity.getName(), namedEntity.getId());
    activity.startActivity(intent);
  }
}

class SelectFragmentExercise extends SelectFragmentHelper {

  SelectFragmentExercise(Activity activity, SelectFragmentOnClick selectFragmentOnClick) {
    super(new ExerciseHelperForFragments(activity), selectFragmentOnClick);
  }
}

class SelectFragmentCategory extends SelectFragmentHelper {

  SelectFragmentCategory(Activity activity, SelectFragmentOnClick selectFragmentOnClick) {
    super(new CategoryHelperForFragments(activity), selectFragmentOnClick);
  }
}

class SelectFragmentRoutine extends SelectFragmentHelper {

  SelectFragmentRoutine(Activity activity, SelectFragmentOnClick selectFragmentOnClick) {
    super(new RoutineHelperForFragments(activity), selectFragmentOnClick);
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
    super(activity.getString(R.string.category), CategoryLab.get(activity));
  }
}

class RoutineHelperForFragments extends HelperForFragments {

  RoutineHelperForFragments(Activity activity) {
    super(activity.getString(R.string.routine), RoutineLab.get(activity));
  }
}
