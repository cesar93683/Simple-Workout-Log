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

import com.example.ceamaya.workoutapp.ExerciseActivity.ExerciseActivity;
import com.example.ceamaya.workoutapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.example.ceamaya.workoutapp.MainActivity.MainActivity.exerciseDB;

public class ExerciseSelectFragment extends Fragment {
    private Activity activity;
    private ArrayList<String> filteredExercises;
    private HashMap<String, Integer> exercisesMap;
    private String exerciseFilter;
    private View fragmentView;
    private ExerciseAdapter exerciseAdapter;

    public ExerciseSelectFragment() {
        exercisesMap = exerciseDB.getExercises();

        exerciseFilter = "";

        filteredExercises = new ArrayList<>();
        filteredExercises.addAll(exercisesMap.keySet());

        Collections.sort(filteredExercises);
    }

    public static Fragment newInstance() {
        return new ExerciseSelectFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        activity = getActivity();

        fragmentView = inflater.inflate(R.layout.fragment_exercise_select, container,
                false);

        FloatingActionButton newExerciseFab = fragmentView.findViewById(R.id.new_exercise_fab);
        newExerciseFab.setOnClickListener(newExerciseFabClickListener());

        EditText filterEditText = fragmentView.findViewById(R.id.filter_edit_text);
        filterEditText.addTextChangedListener(filterEditTextListener());

        RecyclerView exerciseRecyclerView = fragmentView.findViewById(R.id.exercise_recycler_view);
        exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        exerciseAdapter = new ExerciseAdapter(filteredExercises);
        exerciseRecyclerView.setAdapter(exerciseAdapter);

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

    private void createRenameOrDeleteDialog(final String exercise) {
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

    private void createRenameExerciseDialog(final String oldExercise) {
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

    private void createDeleteExerciseDialog(final String exerciseToRemove) {
        new AlertDialog.Builder(activity)
                .setMessage("Are you sure you want to delete this exercise?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
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

    public class ExerciseHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        private String exercise;

        ExerciseHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.simple_list_item, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        void bind(String exercise) {
            this.exercise = exercise;
            ((TextView) itemView).setText(exercise);
        }

        @Override
        public void onClick(View view) {
            int exerciseId = exercisesMap.get(exercise);
            Intent intent = ExerciseActivity.newIntent(activity, exercise, exerciseId);
            startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            createRenameOrDeleteDialog(exercise);
            return true;
        }
    }

    public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseHolder> {

        private List<String> exercises;

        ExerciseAdapter(List<String> exercises) {
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
            String exercise = exercises.get(position);
            holder.bind(exercise);
        }

        @Override
        public int getItemCount() {
            return exercises.size();
        }
    }
}
