package com.devcesar.workoutapp.mainActivity;

import static com.devcesar.workoutapp.utils.Constants.SHOULD_AUTO_START_TIMER;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.database.InitDatabase;
import com.devcesar.workoutapp.databinding.FragmentSettingsBinding;
import com.devcesar.workoutapp.labs.CategoryOrRoutineLab;
import com.devcesar.workoutapp.labs.ExerciseLab;
import com.devcesar.workoutapp.labs.WorkoutLab;
import com.google.android.material.snackbar.Snackbar;

public class SettingsFragment extends Fragment {

  private CoordinatorLayout coordinatorLayout;

  public SettingsFragment() {
    // Required empty public constructor
  }

  public static SettingsFragment newInstance() {
    return new SettingsFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    FragmentSettingsBinding binding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_settings, container, false);

    coordinatorLayout = binding.coordinatorLayout;

    binding.deleteAllWorkouts.setOnClickListener(view -> showDeleteAllWorkoutsDialog());
    ((TextView) (binding.deleteAllWorkouts)).setText(R.string.delete_all_workouts);

    binding.deleteAllItems.setOnClickListener(view -> showDeleteAllItemsDialog());
    ((TextView) (binding.deleteAllItems)).setText(R.string.delete_all_items);

    binding.importDefaultItems.setOnClickListener(view -> showImportDefaultItemsDialog());
    ((TextView) (binding.importDefaultItems)).setText(R.string.import_default_items);

    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    boolean shouldAutoStartTimer = prefs.getBoolean(SHOULD_AUTO_START_TIMER, false);

    binding.autoStartTimer.textView.setText(R.string.auto_start_timer);
    binding.autoStartTimer.checkBox.setChecked(shouldAutoStartTimer);
    binding.autoStartTimer.checkBox.setClickable(false);
    binding.autoStartTimer.constraintLayout.setOnClickListener(view -> {
      binding.autoStartTimer.checkBox.setChecked(!binding.autoStartTimer.checkBox.isChecked());
      prefs.edit().putBoolean(SHOULD_AUTO_START_TIMER, binding.autoStartTimer.checkBox.isChecked())
          .apply();
    });

    return binding.getRoot();
  }

  private void showDeleteAllWorkoutsDialog() {
    new Builder(getActivity())
        .setMessage(R.string.are_you_sure_delete_all_workouts)
        .setNegativeButton(R.string.cancel, null)
        .setPositiveButton(R.string.yes,
            (dialogInterface, i) -> {
              WorkoutLab.get(getActivity()).deleteAll();
              showSnackbar(R.string.all_workouts_deleted);
            })
        .show();
  }

  private void showDeleteAllItemsDialog() {
    new Builder(getActivity())
        .setMessage(R.string.are_you_sure_delete_all)
        .setNegativeButton(R.string.cancel, null)
        .setPositiveButton(R.string.yes,
            (dialogInterface, i) -> {
              WorkoutLab.get(getActivity()).deleteAll();
              CategoryOrRoutineLab.getCategoryLab(getActivity()).deleteAll();
              CategoryOrRoutineLab.getRoutineLab(getActivity()).deleteAll();
              ExerciseLab.get(getActivity()).deleteAll();
              ((MainActivity) getActivity()).refreshFragments();
              showSnackbar(R.string.all_exercises_categories_routines_deleted);
            })
        .show();
  }

  private void showImportDefaultItemsDialog() {
    new Builder(getActivity())
        .setMessage(R.string.are_you_sure_import_default)
        .setNegativeButton(R.string.cancel, null)
        .setPositiveButton(R.string.yes,
            (dialogInterface, i) -> {
              InitDatabase.run(getActivity());
              ((MainActivity) getActivity()).refreshFragments();
              showSnackbar(R.string.import_successful);
            })
        .show();
  }

  private void showSnackbar(int resId) {
    Snackbar.make(coordinatorLayout, resId, Snackbar.LENGTH_SHORT).show();
  }

}
