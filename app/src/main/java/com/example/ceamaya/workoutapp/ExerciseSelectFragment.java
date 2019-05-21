package com.example.ceamaya.workoutapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class ExerciseSelectFragment extends Fragment {

    public static final String EXTRA_EXERCISE_NAME = "EXTRA_EXERCISE_NAME";
    private Activity activity;
    private ArrayList<String> filteredExercises;
    private ArrayList<String> exercises;
    private ExerciseAdapter exerciseAdapter;
    private View fragmentView;
    private String textFilter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        exercises = new ArrayList<>();
        exercises.add("workout 1");
        exercises.add("workout 2");

        textFilter = "";

        filteredExercises = new ArrayList<>();
        filteredExercises.addAll(exercises);

        activity = getActivity();
        fragmentView = inflater.inflate(R.layout.fragment_exercise_select, container, false);

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

    private void createRenameOrDeleteDialog(final int exerciseIndex) {
        ListView listView = new ListView(activity);
        final ArrayList<String> renameOrDelete = new ArrayList<>();
        final String rename = "Rename";
        final String delete = "Delete";
        renameOrDelete.add(rename);
        renameOrDelete.add(delete);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity,
                android.R.layout.simple_list_item_1,
                renameOrDelete);
        listView.setAdapter(arrayAdapter);
        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (renameOrDelete.get(position).equals(rename)) {
                    createRenameExerciseDialog(exerciseIndex);
                } else if (renameOrDelete.get(position).equals(delete)) {
                    createDeleteExerciseDialog(exerciseIndex);
                }
                alertDialog.dismiss();
            }
        });
        alertDialog.setView(listView);
        alertDialog.show();
    }

    private void createRenameExerciseDialog(int exerciseIndex) {
    }

    private void createDeleteExerciseDialog(final int position) {
        new AlertDialog.Builder(activity)
                .setMessage("Are you sure you want to delete this exercise?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String exerciseToRemove = filteredExercises.get(position);
                        for (int j = 0; j < exercises.size(); j++) {
                            if (exerciseToRemove.equals(exercises.get(j))) {
                                exercises.remove(j);
                                break;
                            }
                        }
                        updateFilteredExercises();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
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

    private void createNewExerciseDialog() {
        FrameLayout container = new FrameLayout(activity);
        final EditText newExerciseEditText = new EditText(activity);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(50, 0, 50, 0);

        newExerciseEditText.setLayoutParams(layoutParams);
        newExerciseEditText.setMaxLines(1);
        newExerciseEditText.setSingleLine(true);
        newExerciseEditText.setLines(1);

        newExerciseEditText.setHint("Name");

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(32);
        newExerciseEditText.setFilters(filterArray);

        container.addView(newExerciseEditText);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setView(container)
                .setMessage("New Exercise")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newExercise = newExerciseEditText.getText().toString().trim();
                        if (newExercise.length() > 0) {
                            exercises.add(newExercise);
                            Snackbar.make(fragmentView, newExercise, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            updateFilteredExercises();
                        }
                    }
                })
                .show();
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
                textFilter = s.toString();
                updateFilteredExercises();
            }
        };
    }

    private void updateFilteredExercises() {
        filteredExercises.clear();
        for (String exercise : exercises) {
            if (exercise.contains(textFilter)) {
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
                Log.d("word", exercise);
                intent.putExtra(EXTRA_EXERCISE_NAME, exercise);
                startActivity(intent);
            }
        };
    }
}
