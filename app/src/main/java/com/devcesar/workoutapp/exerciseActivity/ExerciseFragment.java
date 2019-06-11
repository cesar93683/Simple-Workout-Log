package com.devcesar.workoutapp.exerciseActivity;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.databinding.ExerciseSetEditorBinding;
import com.devcesar.workoutapp.databinding.FragmentExerciseBinding;
import com.devcesar.workoutapp.labs.WorkoutLab;
import com.devcesar.workoutapp.utils.ExerciseSet;
import java.util.ArrayList;
import java.util.List;

public class ExerciseFragment extends Fragment {

  private static final String ARGS_EXERCISE_ID = "ARGS_EXERCISE_ID";
  private static final String ARGS_TIME_STAMP = "ARGS_TIME_STAMP";
  private static final long NO_TIME_STAMP = -1;
  private ArrayList<ExerciseSet> exerciseSets;
  private ExerciseAdapter exerciseSetsAdapter;
  private boolean isEditing;
  private boolean hasBeenModified;

  public ExerciseFragment() {
    // Required empty public constructor
  }

  public static Fragment newInstance(int exerciseId) {
    ExerciseFragment fragment = new ExerciseFragment();
    Bundle args = new Bundle();
    args.putInt(ARGS_EXERCISE_ID, exerciseId);
    args.putLong(ARGS_TIME_STAMP, NO_TIME_STAMP);
    fragment.setArguments(args);
    return fragment;
  }

  public static Fragment newInstance(int exerciseId, long timeStamp) {
    ExerciseFragment fragment = new ExerciseFragment();
    Bundle args = new Bundle();
    args.putInt(ARGS_EXERCISE_ID, exerciseId);
    args.putLong(ARGS_TIME_STAMP, timeStamp);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    int exerciseId = getArguments().getInt(ARGS_EXERCISE_ID);
    long timeStamp = getArguments().getLong(ARGS_TIME_STAMP);

    hasBeenModified = false;

    exerciseSets = new ArrayList<>();
    if (timeStamp == NO_TIME_STAMP) {
      isEditing = false;
    } else {
      WorkoutLab workoutLab = WorkoutLab.get(getActivity());
      exerciseSets.addAll(workoutLab.getWorkout(exerciseId, timeStamp).getExerciseSets());
      isEditing = true;
    }
    exerciseSetsAdapter = new ExerciseAdapter(exerciseSets);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    FragmentExerciseBinding binding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_exercise, container, false);

    binding.exerciseSetEditor.decreaseRepButton.setOnClickListener(
        decreaseButtonClickListener(binding.exerciseSetEditor.repsTextInputLayout));

    binding.exerciseSetEditor.increaseRepButton.setOnClickListener(
        increaseButtonClickListener(binding.exerciseSetEditor.repsTextInputLayout));

    binding.exerciseSetEditor.decreaseWeightButton.setOnClickListener(
        decreaseButtonClickListener(binding.exerciseSetEditor.weightTextInputLayout));

    binding.exerciseSetEditor.increaseWeightButton.setOnClickListener(
        increaseButtonClickListener(binding.exerciseSetEditor.weightTextInputLayout));

    binding.addSetButton.setOnClickListener(
        addSetButtonClickListener(binding.exerciseSetEditor.repsTextInputLayout,
            binding.exerciseSetEditor.weightTextInputLayout));

    binding.finishExerciseFab.setOnClickListener(v -> saveSets());

    binding.exerciseSetsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    binding.exerciseSetsRecyclerView.addItemDecoration(
        new DividerItemDecoration(binding.exerciseSetsRecyclerView.getContext(),
            DividerItemDecoration.VERTICAL));
    binding.exerciseSetsRecyclerView.setAdapter(exerciseSetsAdapter);

    return binding.getRoot();
  }

  @NonNull
  private View.OnClickListener decreaseButtonClickListener(final TextInputLayout
      textInputLayout) {
    return v -> {
      textInputLayout.setErrorEnabled(false);
      String text = textInputLayout.getEditText().getText().toString();
      if (text.isEmpty()) {
        textInputLayout.getEditText().setText("0");
      } else {
        int val = Integer.parseInt(text);
        if (val > 0) {
          val--;
        }
        textInputLayout.getEditText().setText(String.valueOf(val));
      }
    };
  }

  @NonNull
  private View.OnClickListener increaseButtonClickListener(final TextInputLayout
      textInputLayout) {
    return v -> {
      textInputLayout.setErrorEnabled(false);
      String text = textInputLayout.getEditText().getText().toString();
      if (text.isEmpty()) {
        textInputLayout.getEditText().setText("1");
      } else {
        int val = Integer.parseInt(text);
        val++;
        textInputLayout.getEditText().setText(String.valueOf(val));
      }
    };
  }

  private View.OnClickListener addSetButtonClickListener(final TextInputLayout repsTextInputLayout,
      final TextInputLayout weightTextInputLayout) {
    return v -> {
      if (!validateReps(repsTextInputLayout)) {
        return;
      }

      int reps = Integer.parseInt(repsTextInputLayout.getEditText().getText().toString());
      String weightString = weightTextInputLayout.getEditText().getText().toString();
      int weight = weightString.isEmpty() ? 0 : Integer.parseInt(weightString);
      int setNumber = exerciseSets.size() + 1;

      ExerciseSet exerciseSet = new ExerciseSet(reps, weight, setNumber);
      exerciseSets.add(exerciseSet);
      exerciseSetsAdapter.notifyDataSetChanged();
      hasBeenModified = true;
      Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.set_added,
          Snackbar.LENGTH_SHORT).show();
    };
  }

  private void saveSets() {
    ((SaveSets) getActivity()).saveSets(exerciseSets);
  }

  private boolean validateReps(TextInputLayout repsTextInputLayout) {
    String repsString = repsTextInputLayout.getEditText().getText().toString();
    if (repsString.isEmpty() || Integer.parseInt(repsString) == 0) {
      repsTextInputLayout.setError(getString(R.string.error_no_reps));
      return false;
    }
    repsTextInputLayout.setErrorEnabled(false);
    return true;
  }

  private void createEditOrDeleteDialog(final int position) {
    @SuppressLint("InflateParams") final View dialogView = getActivity().getLayoutInflater()
        .inflate(R.layout.dialog_edit_or_delete, null);

    final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
    alertDialog.setView(dialogView);

    dialogView.findViewById(R.id.edit_linear_layout).setOnClickListener(
        v -> {
          createEditSetDialog(position);
          alertDialog.dismiss();
        });
    dialogView.findViewById(R.id.delete_linear_layout).setOnClickListener(
        v -> {
          createDeleteSetDialog(position);
          alertDialog.dismiss();
        });
    alertDialog.show();
  }

  private void createEditSetDialog(final int position) {
    final ExerciseSetEditorBinding dialogBinding = DataBindingUtil
        .inflate(LayoutInflater.from(getContext()), R.layout.exercise_set_editor, null, false);

    final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
        .setMessage(R.string.edit_set)
        .setNegativeButton(R.string.cancel, null)
        .setPositiveButton(R.string.save, null)
        .create();

    setViewToCenterInDialog(dialogBinding.getRoot(), alertDialog);

    int reps = exerciseSets.get(position).getReps();
    dialogBinding.repsTextInputLayout.getEditText().setText(String.valueOf(reps));

    int weight = exerciseSets.get(position).getWeight();
    dialogBinding.weightTextInputLayout.getEditText().setText(String.valueOf(weight));

    dialogBinding.decreaseRepButton
        .setOnClickListener(decreaseButtonClickListener(dialogBinding.repsTextInputLayout));

    dialogBinding.increaseRepButton
        .setOnClickListener(increaseButtonClickListener(dialogBinding.repsTextInputLayout));

    dialogBinding.decreaseWeightButton
        .setOnClickListener(decreaseButtonClickListener(dialogBinding.weightTextInputLayout));

    dialogBinding.increaseWeightButton
        .setOnClickListener(increaseButtonClickListener(dialogBinding.weightTextInputLayout));

    alertDialog.setOnShowListener(
        dialogInterface -> alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
            view -> editSet(dialogBinding.repsTextInputLayout, dialogBinding.weightTextInputLayout,
                alertDialog, position)));
    alertDialog.show();
  }

  private void createDeleteSetDialog(final int position) {
    new AlertDialog.Builder(getActivity())
        .setMessage(R.string.are_you_sure_delete_set)
        .setPositiveButton(R.string.yes, (dialogInterface, i) -> deleteExerciseSet(position))
        .setNegativeButton(R.string.no, null)
        .show();
  }

  private void setViewToCenterInDialog(View exerciseSetEditorView, AlertDialog alertDialog) {
    FrameLayout container = new FrameLayout(getActivity());
    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    params.gravity = Gravity.CENTER_HORIZONTAL;
    exerciseSetEditorView.setLayoutParams(params);
    container.addView(exerciseSetEditorView);
    alertDialog.setView(container);
  }

  private void editSet(final TextInputLayout repsTextInputLayout,
      final TextInputLayout weightTextInputLayout, AlertDialog alertDialog, int position) {
    if (!validateReps(repsTextInputLayout)) {
      return;
    }

    int reps = Integer.parseInt(repsTextInputLayout.getEditText().getText().toString());
    String weightString = weightTextInputLayout.getEditText().getText().toString();
    int weight = weightString.isEmpty() ? 0 : Integer.parseInt(weightString);

    ExerciseSet exerciseSet = exerciseSets.get(position);
    exerciseSet.setWeight(weight);
    exerciseSet.setReps(reps);
    exerciseSetsAdapter.notifyDataSetChanged();

    alertDialog.dismiss();
    hasBeenModified = true;
    Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.set_updated,
        Snackbar.LENGTH_SHORT).show();
  }

  private void deleteExerciseSet(int position) {
    exerciseSets.remove(position);
    updateExerciseSetNumbers(position);
    exerciseSetsAdapter.notifyDataSetChanged();
    hasBeenModified = true;
    Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.set_deleted,
        Snackbar.LENGTH_SHORT).show();
  }

  private void updateExerciseSetNumbers(int position) {
    for (int i = position; i < exerciseSets.size(); i++) {
      exerciseSets.get(i).setSetNumber(i + 1);
    }
  }

  public void onBackPressed() {
    if (!isEditing && exerciseSets.size() == 0) {
      getActivity().finish();
    } else if (isEditing && !hasBeenModified) {
      getActivity().finish();
    } else {
      createDiscardChangesDialog();
    }
  }

  private void createDiscardChangesDialog() {
    new AlertDialog.Builder(getActivity())
        .setTitle(R.string.discard_changes)
        .setMessage(R.string.are_you_sure_close_exercise)
        .setNeutralButton(R.string.cancel, null)
        .setNegativeButton(R.string.discard, (dialog, which) -> getActivity().finish())
        .setPositiveButton(R.string.save, (dialog, which) -> saveSets())
        .show();
  }

  public class ExerciseHolder extends RecyclerView.ViewHolder implements View
      .OnLongClickListener {

    private ExerciseSet exerciseSet;

    ExerciseHolder(LayoutInflater inflater, ViewGroup parent) {
      super(inflater.inflate(R.layout.simple_list_item, parent, false));
      itemView.setOnLongClickListener(this);
    }

    void bind(ExerciseSet exerciseSet) {
      this.exerciseSet = exerciseSet;
      ((TextView) itemView).setText(exerciseSet.toString());
    }

    @Override
    public boolean onLongClick(View v) {
      createEditOrDeleteDialog(exerciseSet.getSetNumber() - 1);
      return true;
    }
  }

  class ExerciseAdapter extends RecyclerView.Adapter<ExerciseHolder> {

    private final List<ExerciseSet> exerciseSets;

    ExerciseAdapter(List<ExerciseSet> exerciseSets) {
      this.exerciseSets = exerciseSets;
    }

    @NonNull
    @Override
    public ExerciseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
      return new ExerciseHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseHolder holder, int position) {
      ExerciseSet exerciseSet = exerciseSets.get(position);
      holder.bind(exerciseSet);
    }

    @Override
    public int getItemCount() {
      return exerciseSets.size();
    }
  }
}
