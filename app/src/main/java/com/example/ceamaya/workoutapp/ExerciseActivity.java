package com.example.ceamaya.workoutapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.ceamaya.workoutapp.MainActivity.exerciseDB;

public class ExerciseActivity extends AppCompatActivity {

    private TextInputLayout repsTextInputLayout;
    private TextInputLayout weightTextInputLayout;
    private ArrayList<ExerciseSet> exerciseSets;
    private ExerciseSetAdapter exerciseSetAdapter;
    int exerciseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        exerciseId = getIntent().getIntExtra(ExerciseSelectFragment.EXTRA_EXERCISE_ID, 0);

        repsTextInputLayout = findViewById(R.id.reps_text_input_layout);
        weightTextInputLayout = findViewById(R.id.weight_text_input_layout);

        String exerciseName =
                getIntent().getStringExtra(ExerciseSelectFragment.EXTRA_EXERCISE_NAME);
        TextView exerciseTextView = findViewById(R.id.exercise_text_view);
        exerciseTextView.setText(exerciseName);

        Button decreaseRepButton = findViewById(R.id.decrease_rep_button);
        decreaseRepButton.setOnClickListener(decreaseRepButtonClickListener());

        Button increaseRepButton = findViewById(R.id.increase_rep_button);
        increaseRepButton.setOnClickListener(increaseRepButtonClickListener());

        Button increaseWeightButton = findViewById(R.id.increase_weight_button);
        increaseWeightButton.setOnClickListener(increaseWeightButtonClickListener());

        Button decreaseWeightButton = findViewById(R.id.decrease_weight_button);
        decreaseWeightButton.setOnClickListener(decreaseWeightButtonClickListener());

        Button addSetButton = findViewById(R.id.add_set_button);
        addSetButton.setOnClickListener(addSetButtonClickListener());

        FloatingActionButton finishExerciseFab = findViewById(R.id.finish_exercise_fab);
        finishExerciseFab.setOnClickListener(finishExerciseFabClickListener());

        exerciseSets = new ArrayList<>();
        exerciseSetAdapter = new ExerciseSetAdapter(this, exerciseSets);
        ListView completedSetsListView = findViewById(R.id.completed_sets_list_view);
        completedSetsListView.setAdapter(exerciseSetAdapter);
    }

    @NonNull
    private View.OnClickListener decreaseRepButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String repsString = repsTextInputLayout.getEditText().getText().toString();
                if (repsString.isEmpty()) {
                    repsTextInputLayout.getEditText().setText("0");
                } else {
                    int reps = Integer.parseInt(repsString);
                    if (reps > 0) {
                        reps--;
                    }
                    repsTextInputLayout.getEditText().setText(String.valueOf(reps));
                }
            }
        };
    }

    @NonNull
    private View.OnClickListener increaseRepButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String repsString = repsTextInputLayout.getEditText().getText().toString();
                if (repsString.isEmpty()) {
                    repsTextInputLayout.getEditText().setText("1");
                } else {
                    int reps = Integer.parseInt(repsString);
                    reps++;
                    repsTextInputLayout.getEditText().setText(String.valueOf(reps));
                }
            }
        };
    }

    private View.OnClickListener increaseWeightButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String repsString = weightTextInputLayout.getEditText().getText().toString();
                if (repsString.isEmpty()) {
                    weightTextInputLayout.getEditText().setText("1");
                } else {
                    int weight = Integer.parseInt(repsString);
                    weight++;
                    weightTextInputLayout.getEditText().setText(String.valueOf(weight));
                }
            }
        };
    }

    private View.OnClickListener decreaseWeightButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String weightString = weightTextInputLayout.getEditText().getText().toString();
                if (weightString.isEmpty()) {
                    weightTextInputLayout.getEditText().setText("0");
                } else {
                    int weight = Integer.parseInt(weightString);
                    if (weight > 0) {
                        weight--;
                    }
                    weightTextInputLayout.getEditText().setText(String.valueOf(weight));
                }
            }
        };
    }

    private View.OnClickListener addSetButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String repsString = repsTextInputLayout.getEditText().getText().toString();
                String weightString = weightTextInputLayout.getEditText().getText().toString();
                if (repsString.isEmpty()) {
                    repsTextInputLayout.setError("Please enter reps");
                    return;
                }
                int reps = Integer.parseInt(repsString);
                if (reps == 0) {
                    repsTextInputLayout.setError("Please enter at least 1 rep");
                    return;
                }
                repsTextInputLayout.setError("");
                int weight = weightString.isEmpty() ? 0 : Integer.parseInt(weightString);
                ExerciseSet exerciseSet = new ExerciseSet(reps, weight, exerciseId);
                exerciseSets.add(exerciseSet);
                exerciseSetAdapter.notifyDataSetChanged();
                Snackbar.make(findViewById(android.R.id.content), "Set added.",
                        Snackbar.LENGTH_SHORT).show();
            }
        };
    }

    private View.OnClickListener finishExerciseFabClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                saveSets();
                finish();
            }
        };
    }

    private void saveSets() {
        for (ExerciseSet exerciseSet : exerciseSets) {
            exerciseDB.insertSet(exerciseSet);
        }
    }

    @Override
    public void onBackPressed() {
        if (exerciseSets.size() == 0) {
            finish();
            return;
        }
        createDiscardChangesDialog();
    }

    private void createDiscardChangesDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Discard changes?")
                .setMessage("Are you sure you want to close this exercise? Any unsaved changes " +
                        "will be lost.")
                .setNeutralButton("Cancel", null)
                .setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveSets();
                        finish();
                    }
                })
                .show();
    }
}
