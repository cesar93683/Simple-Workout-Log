package com.devcesar.workoutapp.exerciseActivity;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.databinding.DialogEditOrDeleteBinding;
import com.devcesar.workoutapp.databinding.FragmentWorkoutHistoryBinding;
import com.devcesar.workoutapp.exerciseActivity.editExerciseActivity.EditExerciseActivity;
import com.devcesar.workoutapp.labs.WorkoutLab;
import com.devcesar.workoutapp.utils.ExerciseSet;
import com.devcesar.workoutapp.utils.Workout;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class WorkoutHistoryFragment extends Fragment {

  private static final String ARGS_EXERCISE_ID = "ARGS_EXERCISE_ID";
  private static final String ARGS_EXERCISE_NAME = "ARGS_EXERCISE_NAME";
  private static final int REQUEST_CODE_EDIT_WORKOUT = 1;
  private int exerciseId;
  private String exerciseName;
  private ArrayList<Workout> workouts;
  private WorkoutHistoryAdapter workoutHistoryAdapter;

  public WorkoutHistoryFragment() {
    // Required empty public constructor
  }

  public static Fragment newInstance(int exerciseId, String exerciseName) {
    WorkoutHistoryFragment fragment = new WorkoutHistoryFragment();
    Bundle args = new Bundle();
    args.putInt(ARGS_EXERCISE_ID, exerciseId);
    args.putString(ARGS_EXERCISE_NAME, exerciseName);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    exerciseId = getArguments().getInt(ARGS_EXERCISE_ID);
    exerciseName = getArguments().getString(ARGS_EXERCISE_NAME);
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

    return binding.getRoot();
  }


  private void createEditOrDeleteDialog(final int position) {
    final DialogEditOrDeleteBinding dialogBinding = DataBindingUtil
        .inflate(LayoutInflater.from(getContext()), R.layout.dialog_edit_or_delete, null, false);

    final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
        .setView(dialogBinding.getRoot()).create();

    dialogBinding.editLinearLayout.setOnClickListener(
        v -> {
          Intent intent = EditExerciseActivity.newIntent(getActivity(), exerciseName, exerciseId,
              workouts.get(position).getTimeStamp());
          alertDialog.dismiss();
          startActivityForResult(intent, REQUEST_CODE_EDIT_WORKOUT);
        });
    dialogBinding.deleteLinearLayout.setOnClickListener(
        v -> {
          createDeleteDialog(position);
          alertDialog.dismiss();
        });
    alertDialog.show();
  }

  private void createDeleteDialog(final int position) {
    new AlertDialog.Builder(getActivity())
        .setMessage(R.string.are_you_sure_delete_workout)
        .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
          Workout workout = workouts.get(position);
          WorkoutLab.get(getActivity())
              .deleteWorkout(workout.getExerciseId(), workout.getTimeStamp());
          updateWorkouts();
          Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.workout_deleted,
              Snackbar.LENGTH_SHORT).show();
        })
        .setNegativeButton(R.string.no, null)
        .show();
  }

  private void updateWorkouts() {
    workouts.clear();
    workouts.addAll(WorkoutLab.get(getActivity()).getWorkouts(exerciseId));
    workoutHistoryAdapter.notifyDataSetChanged();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_EDIT_WORKOUT) {
      updateWorkouts();
      Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.workout_updated,
          Snackbar.LENGTH_SHORT).show();
    }
  }

  private class WorkoutHistoryHolder extends RecyclerView.ViewHolder implements
      View.OnLongClickListener {

    private final TextView timeTextView;
    private final LinearLayout exerciseSetsContainer;
    private final TextView dateTextView;
    private int position;

    WorkoutHistoryHolder(LayoutInflater inflater, ViewGroup parent) {
      super(inflater.inflate(R.layout.history_list_item, parent, false));
      itemView.setOnLongClickListener(this);
      dateTextView = itemView.findViewById(R.id.date_text_view);
      timeTextView = itemView.findViewById(R.id.time_text_view);
      exerciseSetsContainer = itemView.findViewById(R.id.exercise_sets_container);
    }

    void bind(Workout workout, int position) {
      this.position = position;

      String dateText = DateFormat.getDateInstance().format(workout.getTimeStamp());
      dateTextView.setText(dateText);

      String timeText = DateFormat.getTimeInstance().format(workout.getTimeStamp());
      timeTextView.setText(timeText);

      exerciseSetsContainer.removeAllViews();

      for (ExerciseSet exerciseSet : workout.getExerciseSets()) {
        @SuppressLint("InflateParams") TextView textView = (TextView) LayoutInflater
            .from(getActivity()).inflate(R.layout.simple_list_item, null);
        textView.setText(exerciseSet.toString());
        textView.setBackground(null);
        exerciseSetsContainer.addView(textView);
      }
    }

    @Override
    public boolean onLongClick(View v) {
      createEditOrDeleteDialog(position);
      return true;
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
      holder.bind(workout, position);
    }

    @Override
    public int getItemCount() {
      return workouts.size();
    }
  }
}
