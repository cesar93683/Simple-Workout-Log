package com.devcesar.workoutapp.exerciseActivity;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.databinding.DialogEditOrDeleteBinding;
import com.devcesar.workoutapp.databinding.FragmentWorkoutHistoryBinding;
import com.devcesar.workoutapp.exerciseActivity.editExerciseActivity.EditExerciseActivity;
import com.devcesar.workoutapp.labs.WorkoutLab;
import com.devcesar.workoutapp.utils.ExerciseSet;
import com.devcesar.workoutapp.utils.NamedEntity;
import com.devcesar.workoutapp.utils.Workout;
import com.google.android.material.snackbar.Snackbar;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class WorkoutHistoryFragment extends Fragment {

  private static final String ARGS_EXERCISE_ID = "ARGS_EXERCISE_ID";
  private static final String ARGS_EXERCISE_NAME = "ARGS_EXERCISE_NAME";
  private static final int REQUEST_CODE_EDIT_WORKOUT = 1;
  private ArrayList<Workout> workouts;
  private WorkoutHistoryAdapter workoutHistoryAdapter;
  private NamedEntity exercise;
  private CoordinatorLayout coordinatorLayout;

  public WorkoutHistoryFragment() {
    // Required empty public constructor
  }

  public static Fragment newInstance(NamedEntity exercise) {
    WorkoutHistoryFragment fragment = new WorkoutHistoryFragment();
    Bundle args = new Bundle();
    args.putInt(ARGS_EXERCISE_ID, exercise.getId());
    args.putString(ARGS_EXERCISE_NAME, exercise.getName());
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    int exerciseId = getArguments().getInt(ARGS_EXERCISE_ID);
    String exerciseName = getArguments().getString(ARGS_EXERCISE_NAME);
    exercise = new NamedEntity(exerciseName, exerciseId);

    workouts = WorkoutLab.get(getActivity()).getWorkouts(exerciseId);
    workoutHistoryAdapter = new WorkoutHistoryAdapter(workouts);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    FragmentWorkoutHistoryBinding binding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_workout_history, container, false);

    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    binding.recyclerView.addItemDecoration(
        new DividerItemDecoration(binding.recyclerView.getContext(),
            DividerItemDecoration.VERTICAL));
    binding.recyclerView.setAdapter(workoutHistoryAdapter);

    coordinatorLayout = binding.coordinatorLayout;

    return binding.getRoot();
  }


  private void showEditOrDeleteDialog(final Workout workout) {
    final DialogEditOrDeleteBinding dialogBinding = DataBindingUtil
        .inflate(LayoutInflater.from(getContext()), R.layout.dialog_edit_or_delete, null, false);

    final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
        .setView(dialogBinding.getRoot()).create();

    dialogBinding.editLinearLayout.setOnClickListener(
        view -> {
          Intent intent = EditExerciseActivity
              .newIntent(getActivity(), exercise, workout.getTimeStamp());
          startActivityForResult(intent, REQUEST_CODE_EDIT_WORKOUT);
          alertDialog.dismiss();
        });
    dialogBinding.deleteLinearLayout.setOnClickListener(
        view -> {
          showDeleteDialog(workout);
          alertDialog.dismiss();
        });
    alertDialog.show();
  }

  private void showDeleteDialog(final Workout workout) {
    new AlertDialog.Builder(getActivity())
        .setMessage(String.format(getString(R.string.delete_item_confirmation),
            getString(R.string.workout).toLowerCase()))
        .setPositiveButton(R.string.yes, (dialogInterface, i) -> deleteWorkout(workout))
        .setNegativeButton(R.string.no, null)
        .show();
  }

  private void deleteWorkout(Workout workout) {
    WorkoutLab.get(getActivity()).deleteWorkout(workout.getExerciseId(), workout.getTimeStamp());
    updateWorkouts();
    showSnackbar(R.string.item_deleted);
  }

  private void updateWorkouts() {
    workouts.clear();
    workouts.addAll(WorkoutLab.get(getActivity()).getWorkouts(exercise.getId()));
    workoutHistoryAdapter.notifyDataSetChanged();
  }

  private void showSnackbar(int item_action) {
    Snackbar
        .make(coordinatorLayout, String.format(getString(item_action), getString(R.string.workout)),
            Snackbar.LENGTH_SHORT).show();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_EDIT_WORKOUT) {
      updateWorkouts();
      showSnackbar(R.string.item_updated);
    }
  }

  private class WorkoutHistoryHolder extends RecyclerView.ViewHolder {

    private final TextView timeTextView;
    private final LinearLayout exerciseSetsContainer;
    private final TextView dateTextView;
    private Workout workout;

    WorkoutHistoryHolder(LayoutInflater inflater, ViewGroup parent) {
      super(inflater.inflate(R.layout.history_list_item, parent, false));
      itemView.setOnLongClickListener(view -> {
        showEditOrDeleteDialog(workout);
        return true;
      });
      dateTextView = itemView.findViewById(R.id.date_text_view);
      timeTextView = itemView.findViewById(R.id.time_text_view);
      exerciseSetsContainer = itemView.findViewById(R.id.exercise_sets_container);
    }

    void bind(Workout workout) {
      this.workout = workout;

      String dateText = DateFormat.getDateInstance().format(workout.getTimeStamp());
      dateTextView.setText(dateText);

      String timeText = DateFormat.getTimeInstance().format(workout.getTimeStamp());
      timeTextView.setText(timeText);

      exerciseSetsContainer.removeAllViews();

      for (ExerciseSet exerciseSet : workout.getExerciseSets()) {
        exerciseSetsContainer.addView(getExerciseSetView(exerciseSet));
      }
    }

    @NonNull
    private TextView getExerciseSetView(ExerciseSet exerciseSet) {
      @SuppressLint("InflateParams") TextView textView = (TextView) LayoutInflater
          .from(getActivity()).inflate(R.layout.simple_list_item, null);
      textView.setText(exerciseSet.toString());
      textView.setBackground(null);
      return textView;
    }

  }

  private class WorkoutHistoryAdapter extends RecyclerView.Adapter<WorkoutHistoryHolder> {

    private final List<Workout> workouts;

    WorkoutHistoryAdapter(List<Workout> workouts) {
      this.workouts = workouts;
    }

    @NonNull
    @Override
    public WorkoutHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
      return new WorkoutHistoryHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutHistoryHolder holder, int position) {
      Workout workout = workouts.get(position);
      holder.bind(workout);
    }

    @Override
    public int getItemCount() {
      return workouts.size();
    }
  }
}
