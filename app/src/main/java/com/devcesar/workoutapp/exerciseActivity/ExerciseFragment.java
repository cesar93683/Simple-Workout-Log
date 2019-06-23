package com.devcesar.workoutapp.exerciseActivity;

import static com.devcesar.workoutapp.utils.Constants.SHOULD_AUTO_START_TIMER;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.devcesar.workoutapp.R;
import com.devcesar.workoutapp.databinding.ExerciseSetEditorBinding;
import com.devcesar.workoutapp.databinding.FragmentExerciseBinding;
import com.devcesar.workoutapp.labs.WorkoutLab;
import com.devcesar.workoutapp.utils.ExerciseSet;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

public class ExerciseFragment extends Fragment {

  private static final String ARGS_EXERCISE_ID = "ARGS_EXERCISE_ID";
  private static final String ARGS_TIME_STAMP = "ARGS_TIME_STAMP";
  private static final long NO_TIME_STAMP = -1;
  private static final String EXTRA_HAS_BEEN_MODIFIED = "EXTRA_HAS_BEEN_MODIFIED";
  private static final String EXTRA_EXERCISE_SETS = "EXTRA_EXERCISE_SETS";
  private OnSaveSetsListener listener;
  private ArrayList<ExerciseSet> exerciseSets;
  private ExerciseSetAdapter exerciseSetsAdapter;
  private boolean isEditing;
  private boolean hasBeenModified;
  private CoordinatorLayout coordinatorLayout;

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

    if (savedInstanceState == null) {
      hasBeenModified = false;
      exerciseSets = new ArrayList<>();
    } else {
      hasBeenModified = savedInstanceState.getBoolean(EXTRA_HAS_BEEN_MODIFIED);
      exerciseSets = new Gson().fromJson(savedInstanceState.getString(EXTRA_EXERCISE_SETS),
          new TypeToken<ArrayList<ExerciseSet>>() {
          }.getType());
    }

    isEditing = timeStamp != NO_TIME_STAMP;

    if (isEditing) {
      exerciseSets.addAll(
          WorkoutLab.get(getActivity()).getWorkout(exerciseId, timeStamp).getExerciseSets());
    }
    exerciseSetsAdapter = new ExerciseSetAdapter(exerciseSets);
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putBoolean(EXTRA_HAS_BEEN_MODIFIED, hasBeenModified);
    outState.putString(EXTRA_EXERCISE_SETS, new Gson().toJson(exerciseSets));
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    FragmentExerciseBinding binding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_exercise, container, false);

    binding.exerciseSetEditor.decreaseRepButton
        .setOnClickListener(view -> decrease(binding.exerciseSetEditor.repsTextInputLayout));

    binding.exerciseSetEditor.increaseRepButton
        .setOnClickListener(view -> increase(binding.exerciseSetEditor.repsTextInputLayout));

    binding.exerciseSetEditor.decreaseWeightButton
        .setOnClickListener(view -> decrease(binding.exerciseSetEditor.weightTextInputLayout));

    binding.exerciseSetEditor.increaseWeightButton
        .setOnClickListener(view -> increase(binding.exerciseSetEditor.weightTextInputLayout));

    binding.addSetButton.setOnClickListener(
        view -> addSet(binding.exerciseSetEditor.repsTextInputLayout,
            binding.exerciseSetEditor.weightTextInputLayout));

    binding.finishExerciseFab.setOnClickListener(view -> saveSets());

    binding.exerciseSetsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    binding.exerciseSetsRecyclerView.addItemDecoration(
        new DividerItemDecoration(binding.exerciseSetsRecyclerView.getContext(),
            DividerItemDecoration.VERTICAL));
    binding.exerciseSetsRecyclerView.setAdapter(exerciseSetsAdapter);

    coordinatorLayout = binding.coordinatorLayout;

    return binding.getRoot();
  }

  private void decrease(final TextInputLayout textInputLayout) {
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
  }

  private void increase(final TextInputLayout textInputLayout) {
    textInputLayout.setErrorEnabled(false);
    String text = textInputLayout.getEditText().getText().toString();
    if (text.isEmpty()) {
      textInputLayout.getEditText().setText("1");
    } else {
      int val = Integer.parseInt(text);
      val++;
      textInputLayout.getEditText().setText(String.valueOf(val));
    }
  }

  private void addSet(final TextInputLayout repsTextInputLayout,
      final TextInputLayout weightTextInputLayout) {
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
    boolean shouldStartTimer = PreferenceManager.getDefaultSharedPreferences(getActivity())
        .getBoolean(SHOULD_AUTO_START_TIMER, false);
    if (!isEditing && shouldStartTimer) {
      ((ExerciseActivity) (getActivity())).startTimer();
    }
    showSnackbar(getString(R.string.set_added));
  }

  private void saveSets() {
    listener.onSaveSets(exerciseSets);
  }

  private boolean validateReps(TextInputLayout repsTextInputLayout) {
    String repsString = repsTextInputLayout.getEditText().getText().toString();
    if (repsString.isEmpty() || Integer.parseInt(repsString) == 0) {
      repsTextInputLayout.setError(getString(R.string.please_enter_at_least_1_rep));
      return false;
    }
    repsTextInputLayout.setErrorEnabled(false);
    return true;
  }

  private void showSnackbar(String text) {
    Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_SHORT).show();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    try {
      listener = (OnSaveSetsListener) context;
    } catch (ClassCastException e) {
      throw new ClassCastException(context.toString() + " must implement OnSaveSetsListener");
    }
  }

  private void showEditOrDeleteDialog(final int position) {
    @SuppressLint("InflateParams") final View dialogView = getActivity().getLayoutInflater()
        .inflate(R.layout.dialog_edit_or_delete, null);

    final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
        .setView(dialogView)
        .create();

    dialogView.findViewById(R.id.edit_linear_layout).setOnClickListener(
        view -> {
          showEditSetDialog(position);
          alertDialog.dismiss();
        });
    dialogView.findViewById(R.id.delete_linear_layout).setOnClickListener(
        view -> {
          showDeleteSetDialog(position);
          alertDialog.dismiss();
        });

    alertDialog.show();
  }

  private void showEditSetDialog(final int position) {
    final ExerciseSetEditorBinding dialogBinding = DataBindingUtil
        .inflate(LayoutInflater.from(getContext()), R.layout.exercise_set_editor, null, false);

    final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
        .setMessage(R.string.edit_set)
        .setNegativeButton(R.string.cancel, null)
        .setPositiveButton(R.string.save, null)
        .setView(dialogBinding.getRoot())
        .create();

    int reps = exerciseSets.get(position).getReps();
    dialogBinding.repsTextInputLayout.getEditText().setText(String.valueOf(reps));

    int weight = exerciseSets.get(position).getWeight();
    dialogBinding.weightTextInputLayout.getEditText().setText(String.valueOf(weight));

    dialogBinding.decreaseRepButton
        .setOnClickListener(view -> decrease(dialogBinding.repsTextInputLayout));

    dialogBinding.increaseRepButton
        .setOnClickListener(view -> increase(dialogBinding.repsTextInputLayout));

    dialogBinding.decreaseWeightButton
        .setOnClickListener(view -> decrease(dialogBinding.weightTextInputLayout));

    dialogBinding.increaseWeightButton
        .setOnClickListener(view -> increase(dialogBinding.weightTextInputLayout));

    alertDialog.setOnShowListener(
        dialogInterface -> alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
            view -> editSet(dialogBinding.repsTextInputLayout, dialogBinding.weightTextInputLayout,
                alertDialog, position)));
    alertDialog.show();
  }

  private void showDeleteSetDialog(final int position) {
    new AlertDialog.Builder(getActivity())
        .setMessage(String.format(getString(R.string.delete_item_confirmation),
            getString(R.string.set).toLowerCase()))
        .setPositiveButton(R.string.yes, (dialogInterface, i) -> deleteExerciseSet(position))
        .setNegativeButton(R.string.no, null)
        .show();
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
    showSnackbar(String.format(getString(R.string.item_updated), getString(R.string.set)));
  }

  private void deleteExerciseSet(int position) {
    exerciseSets.remove(position);
    updateExerciseSetNumbers(position);
    exerciseSetsAdapter.notifyDataSetChanged();
    hasBeenModified = true;
    showSnackbar(String.format(getString(R.string.item_deleted), getString(R.string.set)));
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
      showDiscardChangesDialog();
    }
  }

  private void showDiscardChangesDialog() {
    new AlertDialog.Builder(getActivity())
        .setTitle(R.string.discard_changes)
        .setMessage(R.string.are_you_sure_close_exercise)
        .setNeutralButton(R.string.cancel, null)
        .setNegativeButton(R.string.discard, (dialog, which) -> getActivity().finish())
        .setPositiveButton(R.string.save, (dialog, which) -> saveSets())
        .show();
  }

  // Container Activity must implement this interface
  public interface OnSaveSetsListener {

    void onSaveSets(ArrayList<ExerciseSet> exerciseSets);
  }

  class ExerciseSetHolder extends RecyclerView.ViewHolder {

    private ExerciseSet exerciseSet;

    ExerciseSetHolder(LayoutInflater inflater, ViewGroup parent) {
      super(inflater.inflate(R.layout.simple_list_item, parent, false));
      itemView.setOnLongClickListener(view -> {
        showEditOrDeleteDialog(exerciseSet.getSetNumber() - 1);
        return true;
      });
    }

    void bind(ExerciseSet exerciseSet) {
      this.exerciseSet = exerciseSet;
      ((TextView) itemView).setText(exerciseSet.toString());
    }

  }

  class ExerciseSetAdapter extends RecyclerView.Adapter<ExerciseSetHolder> {

    private final List<ExerciseSet> exerciseSets;

    ExerciseSetAdapter(List<ExerciseSet> exerciseSets) {
      this.exerciseSets = exerciseSets;
    }

    @Override
    public int getItemCount() {
      return exerciseSets.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseSetHolder holder, int position) {
      ExerciseSet exerciseSet = exerciseSets.get(position);
      holder.bind(exerciseSet);
    }

    @NonNull
    @Override
    public ExerciseSetHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
      return new ExerciseSetHolder(layoutInflater, parent);
    }
  }
}
