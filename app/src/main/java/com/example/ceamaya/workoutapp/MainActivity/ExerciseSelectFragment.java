package com.example.ceamaya.workoutapp.MainActivity;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ceamaya.workoutapp.ExerciseActivity.ExerciseActivity;
import com.example.ceamaya.workoutapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static com.example.ceamaya.workoutapp.MainActivity.MainActivity.exerciseDB;

public class ExerciseSelectFragment extends Fragment {

    public static final String EXTRA_EXERCISE_NAME = "EXTRA_EXERCISE_NAME";
    public static final String EXTRA_EXERCISE_ID = "EXTRA_EXERCISE_ID";
    private Activity activity;
    private ArrayList<String> filteredExercises;
    private HashMap<String, Integer> exercisesMap;
    private ArrayAdapter<String> exerciseAdapter;
    private String exerciseFilter;
    private View fragmentView;

    public ExerciseSelectFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        return new ExerciseSelectFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        activity = getActivity();

        exercisesMap = exerciseDB.getExercises();

        exerciseFilter = "";

        filteredExercises = new ArrayList<>();
        filteredExercises.addAll(exercisesMap.keySet());

        Collections.sort(filteredExercises);

        fragmentView = inflater.inflate(R.layout.fragment_exercise_select, container,
                false);

        FloatingActionButton newExerciseFab = fragmentView.findViewById(R.id.new_exercise_fab);
        newExerciseFab.setOnClickListener(newExerciseFabClickListener());

        EditText filterEditText = fragmentView.findViewById(R.id.filter_edit_text);
        filterEditText.addTextChangedListener(filterEditTextListener());

        exerciseAdapter = new ArrayAdapter<>(activity, R.layout.simple_list_item,
                filteredExercises);
        ListView exerciseListView = fragmentView.findViewById(R.id.exercise_list_view);
        exerciseListView.setAdapter(exerciseAdapter);
        exerciseListView.setOnItemClickListener(exerciseListViewClickListener());
        exerciseListView.setOnItemLongClickListener(exerciseListViewLongClickListener());

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
                exerciseFilter = s.toString();
                updateFilteredExercises();
            }
        };
    }

    @NonNull
    private AdapterView.OnItemClickListener exerciseListViewClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, ExerciseActivity.class);
                String exercise = filteredExercises.get(position);
                int exerciseId = exercisesMap.get(exercise);
                intent.putExtra(EXTRA_EXERCISE_NAME, exercise);
                intent.putExtra(EXTRA_EXERCISE_ID, exerciseId);
                startActivity(intent);
            }
        };
    }

    @NonNull
    private AdapterView.OnItemLongClickListener exerciseListViewLongClickListener() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                                           long id) {
                createRenameOrDeleteDialog(position);
                return true;
            }
        };
    }

    private void createNewExerciseDialog() {
        @SuppressLint("InflateParams") View dialogView =
                activity.getLayoutInflater().inflate(R.layout.dialog_text_input_layout, null);

        final TextInputLayout newExerciseTextInputLayout = dialogView.findViewById(
                R.id.text_input_layout);

        final AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setView(dialogView)
                .setMessage("New Exercise")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", null)
                .create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String newExercise = newExerciseTextInputLayout.
                                        getEditText().getText().toString().trim();
                                if (newExercise.isEmpty()) {
                                    newExerciseTextInputLayout.setError("Please enter a name.");
                                } else if (exercisesMap.containsKey(newExercise)) {
                                    newExerciseTextInputLayout.setError("Exercise already exists.");
                                } else {
                                    exerciseDB.insertExercise(newExercise);
                                    exercisesMap = exerciseDB.getExercises();
                                    updateFilteredExercises();
                                    Snackbar.make(fragmentView, "New exercise created.",
                                            Snackbar.LENGTH_SHORT).show();
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
        for (String exercise : exercisesMap.keySet()) {
            if (exercise.contains(exerciseFilter)) {
                filteredExercises.add(exercise);
            }
        }
        Collections.sort(filteredExercises);
        exerciseAdapter.notifyDataSetChanged();
    }

    private void createRenameOrDeleteDialog(final int exerciseIndex) {
        @SuppressLint("InflateParams") final View dialogView =
                activity.getLayoutInflater().inflate(R.layout.dialog_edit_or_delete, null);

        TextView editTextView = dialogView.findViewById(R.id.edit_text_view);
        editTextView.setText(R.string.dialog_rename_text);

        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setView(dialogView);

        dialogView.findViewById(R.id.delete_linear_layout).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createDeleteExerciseDialog(exerciseIndex);
                        alertDialog.dismiss();
                    }
                });
        dialogView.findViewById(R.id.edit_linear_layout).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createRenameExerciseDialog(exerciseIndex);
                        alertDialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void createRenameExerciseDialog(final int exerciseIndex) {
        @SuppressLint("InflateParams") final View dialogView =
                activity.getLayoutInflater().inflate(R.layout.dialog_text_input_layout, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setView(dialogView)
                .setMessage("Edit Exercise")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", null)
                .create();

        final TextInputLayout newExerciseTextInputLayout = dialogView.findViewById(
                R.id.text_input_layout);
        final String oldExercise = filteredExercises.get(exerciseIndex);
        newExerciseTextInputLayout.getEditText().setText(oldExercise);
        final long id = exercisesMap.get(oldExercise);

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String newExercise = newExerciseTextInputLayout.
                                getEditText().getText().toString().trim();
                        if (oldExercise.equals(newExercise)) {
                            newExerciseTextInputLayout.setError("Same name.");
                        } else if (newExercise.isEmpty()) {
                            newExerciseTextInputLayout.setError("Please enter a name.");
                        } else if (exercisesMap.containsKey(newExercise)) {
                            newExerciseTextInputLayout.setError("Exercise already exists.");
                        } else {
                            exerciseDB.updateExercise(id, newExercise);
                            exercisesMap = exerciseDB.getExercises();
                            updateFilteredExercises();
                            Snackbar.make(fragmentView, "Rename successful",
                                    Snackbar.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });

        alertDialog.show();
    }

    private void createDeleteExerciseDialog(final int exerciseIndex) {
        new AlertDialog.Builder(activity)
                .setMessage("Are you sure you want to delete this exercise?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String exerciseToRemove = filteredExercises.get(exerciseIndex);
                        long id = exercisesMap.get(exerciseToRemove);
                        exerciseDB.deleteExercise(id);
                        exercisesMap = exerciseDB.getExercises();
                        updateFilteredExercises();
                        Snackbar.make(fragmentView, "Exercise deleted.",
                                Snackbar.LENGTH_SHORT).show();
                    }
                })
                .show();
    }
}
