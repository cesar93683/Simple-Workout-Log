package com.example.ceamaya.workoutapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.ceamaya.workoutapp.MainActivity.exerciseDB;

public class ExerciseSelectFragment extends Fragment {

    public static final String EXTRA_EXERCISE_NAME = "EXTRA_EXERCISE_NAME";
    private Activity activity;
    private ArrayList<String> filteredExercises;
    private HashMap<String, Long> exercises;
    private ExerciseAdapter exerciseAdapter;
    private String exerciseFilter;
    private View fragmentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        activity = getActivity();

        exercises = exerciseDB.getExercises();

        exerciseFilter = "";

        filteredExercises = new ArrayList<>();
        filteredExercises.addAll(exercises.keySet());

        fragmentView = inflater.inflate(R.layout.fragment_exercise_select, container,
                false);

        FloatingActionButton fab = fragmentView.findViewById(R.id.fab);
        fab.setOnClickListener(fabListener());

        EditText filterEditText = fragmentView.findViewById(R.id.filter_edit_text);
        filterEditText.addTextChangedListener(filterEditTextListener());

        ListView exerciseListView = fragmentView.findViewById(R.id.exercise_list_view);
        exerciseAdapter = new ExerciseAdapter(activity, filteredExercises);
        exerciseListView.setAdapter(exerciseAdapter);
        exerciseListView.setOnItemClickListener(exerciseListViewClickListener());
        exerciseListView.setOnItemLongClickListener(exerciseListViewLongClickListener());

        return fragmentView;
    }

    @NonNull
    private AdapterView.OnItemLongClickListener exerciseListViewLongClickListener() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                createRenameOrDeleteDialog(position);
                return true;
            }
        };
    }

    @NonNull
    private View.OnClickListener fabListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                createNewExerciseDialog();
            }
        };
    }

    private void createRenameOrDeleteDialog(final int exerciseIndex) {
        ListView listView = new ListView(activity);

        final ArrayList<String> renameOrDelete = new ArrayList<>();
        final String rename = "Rename";
        final String delete = "Delete";
        renameOrDelete.add(rename);
        renameOrDelete.add(delete);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity,
                android.R.layout.simple_list_item_1, renameOrDelete);
        listView.setAdapter(arrayAdapter);

        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String action = renameOrDelete.get(position);
                if (action.equals(rename)) {
                    createRenameExerciseDialog(exerciseIndex);
                } else if (action.equals(delete)) {
                    createDeleteExerciseDialog(exerciseIndex);
                }
                alertDialog.dismiss();
            }
        });
        alertDialog.setView(listView);
        alertDialog.show();
    }

    private void createRenameExerciseDialog(final int exerciseIndex) {

    }

    private void createDeleteExerciseDialog(final int exerciseIndex) {
        new AlertDialog.Builder(activity)
                .setMessage("Are you sure you want to delete this exercise?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String exerciseToRemove = filteredExercises.get(exerciseIndex);
                        long id = exercises.get(exerciseToRemove);
                        exerciseDB.delete(id);
                        exercises = exerciseDB.getExercises();
                        updateFilteredExercises();
                        Snackbar.make(fragmentView, "Exercise deleted.",
                                Snackbar.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void createNewExerciseDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        final View dialogView = activity.getLayoutInflater().inflate(R.layout.dialog_new_exercise,
                null);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setMessage("New Exercise");
        alertDialogBuilder.setNegativeButton("Cancel", null);

        final EditText newExerciseEditText = dialogView.findViewById(R.id.new_exercise_edit_text);

        alertDialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String newExercise = newExerciseEditText.getText().toString().trim();
                if (newExercise.length() > 0 && exercises.containsKey(newExercise)) {
                    Snackbar.make(fragmentView, "Exercise already exists.",
                            Snackbar.LENGTH_LONG).show();
                } else if (newExercise.length() > 0) {
                    exerciseDB.insert(newExercise);
                    exercises = exerciseDB.getExercises();
                    updateFilteredExercises();
                    Snackbar.make(fragmentView, "New exercise created.",
                            Snackbar.LENGTH_LONG).show();
                }
            }
        });
        alertDialogBuilder.create().show();
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

    private void updateFilteredExercises() {
        filteredExercises.clear();
        for (String exercise : exercises.keySet()) {
            if (exercise.contains(exerciseFilter)) {
                filteredExercises.add(exercise);
            }
        }
        exerciseAdapter.notifyDataSetChanged();
    }

    @NonNull
    private AdapterView.OnItemClickListener exerciseListViewClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, ExerciseActivity.class);
                String exercise = filteredExercises.get(position);
                intent.putExtra(EXTRA_EXERCISE_NAME, exercise);
                startActivity(intent);
            }
        };
    }
}
