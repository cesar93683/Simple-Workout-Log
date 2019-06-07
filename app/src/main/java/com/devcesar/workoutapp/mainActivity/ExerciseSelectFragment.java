package com.devcesar.workoutapp.mainActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.devcesar.workoutapp.Exercise;
import com.devcesar.workoutapp.exerciseActivity.ExerciseActivity;
import com.devcesar.workoutapp.labs.ExerciseLab;
import com.devcesar.workoutapp.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExerciseSelectFragment extends Fragment {

  private Activity activity;
  private ArrayList<Exercise> filteredExercises;
  private String filter;
  private View fragmentView;
  private ExerciseAdapter exerciseAdapter;
  private ExerciseLab exerciseLab;

  public ExerciseSelectFragment() {
    // Required empty public constructor
  }

  public static Fragment newInstance() {
    return new ExerciseSelectFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
    exerciseLab = ExerciseLab.get(getActivity());

    filter = "";
    filteredExercises = new ArrayList<>();
    filteredExercises.addAll(exerciseLab.getFilteredExercise(filter));
    Collections.sort(filteredExercises);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    fragmentView = inflater.inflate(R.layout.fragment_select_with_filter, container, false);

    FloatingActionButton newExerciseFab = fragmentView.findViewById(R.id.fab);
    newExerciseFab.setOnClickListener(newExerciseFabClickListener());

    EditText filterEditText = fragmentView.findViewById(R.id.filter_edit_text);
    filterEditText.addTextChangedListener(filterEditTextListener());

    RecyclerView exerciseRecyclerView = fragmentView.findViewById(R.id.recycler_view);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    exerciseRecyclerView.setLayoutManager(linearLayoutManager);
    exerciseAdapter = new ExerciseAdapter(filteredExercises);
    exerciseRecyclerView.setAdapter(exerciseAdapter);

    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
        exerciseRecyclerView.getContext(), linearLayoutManager.getOrientation());
    exerciseRecyclerView.addItemDecoration(dividerItemDecoration);

    return fragmentView;
  }

  @NonNull
  private View.OnClickListener newExerciseFabClickListener() {
    return new View.OnClickListener() {
      @Override
      public void onClick(final View v) {
        createNewExerciseDialog();
      }
    };
  }

  @NonNull
  private TextWatcher filterEditTextListener() {
    return new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        filter = s.toString();
        updateFilteredExercises();
      }
    };
  }

  private void createNewExerciseDialog() {
    @SuppressLint("InflateParams") View dialogView = activity.getLayoutInflater()
        .inflate(R.layout.dialog_text_input_layout, null);

    final TextInputLayout newExerciseTextInputLayout = dialogView
        .findViewById(R.id.text_input_layout);

    final AlertDialog alertDialog = new AlertDialog.Builder(activity)
        .setView(dialogView)
        .setMessage(R.string.new_exercise)
        .setNegativeButton(R.string.cancel, null)
        .setPositiveButton(R.string.save, null)
        .create();

    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
      @Override
      public void onShow(DialogInterface dialogInterface) {
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                String newExercise = newExerciseTextInputLayout.getEditText().getText().toString()
                    .trim();
                if (newExercise.isEmpty()) {
                  newExerciseTextInputLayout.setError(getString(R.string
                      .error_no_name));
                } else if (exerciseLab.contains(newExercise)) {
                  newExerciseTextInputLayout.setError(getString(R.string
                      .error_exercise_already_exists));
                } else {
                  exerciseLab.insertExercise(newExercise);
                  updateFilteredExercises();
                  Snackbar.make(fragmentView, R.string.new_exercise_created, Snackbar.LENGTH_SHORT)
                      .show();
                  alertDialog.dismiss();
                }
              }
            });
      }
    });

    alertDialog.show();
  }

  private void updateFilteredExercises() {
    filteredExercises.clear();
    filteredExercises.addAll(exerciseLab.getFilteredExercise(filter));
    Collections.sort(filteredExercises);
    exerciseAdapter.notifyDataSetChanged();
  }

  private void createRenameOrDeleteDialog(final Exercise exercise) {
    @SuppressLint("InflateParams") final View dialogView =
        activity.getLayoutInflater().inflate(R.layout.dialog_edit_or_delete, null);

    TextView editTextView = dialogView.findViewById(R.id.edit_text_view);
    editTextView.setText(R.string.dialog_rename_text);

    final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
    alertDialog.setView(dialogView);

    dialogView.findViewById(R.id.edit_linear_layout).setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            createRenameExerciseDialog(exercise);
            alertDialog.dismiss();
          }
        });
    dialogView.findViewById(R.id.delete_linear_layout).setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            createDeleteExerciseDialog(exercise);
            alertDialog.dismiss();
          }
        });
    alertDialog.show();
  }

  private void createRenameExerciseDialog(final Exercise oldExercise) {
    @SuppressLint("InflateParams") final View dialogView =
        activity.getLayoutInflater().inflate(R.layout.dialog_text_input_layout, null);
    final AlertDialog alertDialog = new AlertDialog.Builder(activity)
        .setView(dialogView)
        .setMessage(R.string.edit_exercise)
        .setNegativeButton(R.string.cancel, null)
        .setPositiveButton(R.string.save, null)
        .create();

    final TextInputLayout newExerciseTextInputLayout = dialogView
        .findViewById(R.id.text_input_layout);
    newExerciseTextInputLayout.getEditText().setText(oldExercise.getExerciseName());

    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
      @Override
      public void onShow(DialogInterface dialogInterface) {
        Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            String newExercise = newExerciseTextInputLayout.getEditText().getText().toString()
                .trim();
            if (oldExercise.getExerciseName().equals(newExercise)) {
              newExerciseTextInputLayout.setError(getString(R.string
                  .error_same_name));
            } else if (newExercise.isEmpty()) {
              newExerciseTextInputLayout.setError(getString(R.string.error_no_name));
            } else if (exerciseLab.contains(newExercise)) {
              newExerciseTextInputLayout.setError(getString(R.string
                  .error_exercise_already_exists));
            } else {
              exerciseLab.updateExercise(oldExercise.getExerciseId(), newExercise);
              updateFilteredExercises();
              Snackbar.make(fragmentView, R.string.rename_successful, Snackbar.LENGTH_SHORT).show();
              alertDialog.dismiss();
            }
          }
        });
      }
    });

    alertDialog.show();
  }

  private void createDeleteExerciseDialog(final Exercise exerciseToRemove) {
    new AlertDialog.Builder(activity)
        .setMessage(R.string.are_you_sure_delete_exercise)
        .setNegativeButton(R.string.no, null)
        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            exerciseLab.deleteExercise(exerciseToRemove.getExerciseId());
            updateFilteredExercises();
            Snackbar.make(fragmentView, R.string.exercise_deleted, Snackbar.LENGTH_SHORT).show();
          }
        })
        .show();
  }

  private class ExerciseHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
      View.OnLongClickListener {

    private Exercise exercise;

    ExerciseHolder(LayoutInflater inflater, ViewGroup parent) {
      super(inflater.inflate(R.layout.simple_list_item, parent, false));
      itemView.setOnClickListener(this);
      itemView.setOnLongClickListener(this);
    }

    void bind(Exercise exercise) {
      this.exercise = exercise;
      ((TextView) itemView).setText(exercise.getExerciseName());
    }

    @Override
    public void onClick(View view) {
      Intent intent = ExerciseActivity
          .newIntent(activity, exercise.getExerciseName(), exercise.getExerciseId());
      startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v) {
      createRenameOrDeleteDialog(exercise);
      return true;
    }
  }

  private class ExerciseAdapter extends RecyclerView.Adapter<ExerciseHolder> {

    private final List<Exercise> exercises;

    ExerciseAdapter(List<Exercise> exercises) {
      this.exercises = exercises;
    }

    @NonNull
    @Override
    public ExerciseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
      return new ExerciseHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseHolder holder, int position) {
      Exercise exercise = exercises.get(position);
      holder.bind(exercise);
    }

    @Override
    public int getItemCount() {
      return exercises.size();
    }
  }
}
