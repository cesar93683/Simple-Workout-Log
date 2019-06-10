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
  private SelectFragmentOnClick selectFragmentOnClick;

  SelectFragmentHelper(String name, NamedEntityLab lab,
      SelectFragmentOnClick selectFragmentOnClick) {
    this.name = name;
    this.lab = lab;
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

  private Activity activity;
  private int type;

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

  private Activity activity;

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
    super(activity.getString(R.string.exercise), ExerciseLab.get(activity), selectFragmentOnClick);
  }
}

class SelectFragmentCategory extends SelectFragmentHelper {

  SelectFragmentCategory(Activity activity, SelectFragmentOnClick selectFragmentOnClick) {
    super(activity.getString(R.string.category), CategoryLab.get(activity), selectFragmentOnClick);
  }
}

class SelectFragmentRoutine extends SelectFragmentHelper {

  SelectFragmentRoutine(Activity activity, SelectFragmentOnClick selectFragmentOnClick) {
    super(activity.getString(R.string.routine), RoutineLab.get(activity), selectFragmentOnClick);
  }
}
