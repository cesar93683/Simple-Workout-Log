package com.example.ceamaya.workoutapp.ExerciseActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ceamaya.workoutapp.ExerciseSet;
import com.example.ceamaya.workoutapp.R;

import java.util.ArrayList;

import static com.example.ceamaya.workoutapp.MainActivity.MainActivity.exerciseDB;

public class ExerciseFragment extends Fragment {

    private static final String ARGS_EXERCISE_ID = "ARGS_EXERCISE_ID";
    private static final String ARGS_EXERCISE_NAME = "ARGS_EXERCISE_NAME";
    private Activity activity;
    private TextInputLayout repsTextInputLayout;
    private TextInputLayout weightTextInputLayout;
    private ArrayList<ExerciseSet> exerciseSets;
    private ArrayAdapter<ExerciseSet> exerciseSetAdapter;
    private int exerciseId;
    private String exerciseName;
    View fragmentView;

    public ExerciseFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(int exerciseId, String exerciseName) {
        ExerciseFragment fragment = new ExerciseFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_EXERCISE_ID, exerciseId);
        args.putString(ARGS_EXERCISE_NAME, exerciseName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            exerciseId = getArguments().getInt(ARGS_EXERCISE_ID);
            exerciseName = getArguments().getString(ARGS_EXERCISE_NAME);
        }
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        activity = getActivity();

        fragmentView = inflater.inflate(R.layout.fragment_exercise, container, false);

        repsTextInputLayout = fragmentView.findViewById(R.id.reps_text_input_layout);
        weightTextInputLayout = fragmentView.findViewById(R.id.weight_text_input_layout);

        TextView exerciseTextView = fragmentView.findViewById(R.id.exercise_text_view);
        exerciseTextView.setText(exerciseName);

        View exerciseSetEditorView = fragmentView.findViewById(R.id.exercise_set_editor);

        Button decreaseRepButton = exerciseSetEditorView.findViewById(R.id.decrease_rep_button);
        decreaseRepButton.setOnClickListener(decreaseRepButtonClickListener());

        Button increaseRepButton = exerciseSetEditorView.findViewById(R.id.increase_rep_button);
        increaseRepButton.setOnClickListener(increaseRepButtonClickListener());

        Button decreaseWeightButton = exerciseSetEditorView.findViewById(R.id.decrease_weight_button);
        decreaseWeightButton.setOnClickListener(decreaseWeightButtonClickListener());

        Button increaseWeightButton = exerciseSetEditorView.findViewById(R.id.increase_weight_button);
        increaseWeightButton.setOnClickListener(increaseWeightButtonClickListener());

        Button addSetButton = fragmentView.findViewById(R.id.add_set_button);
        addSetButton.setOnClickListener(addSetButtonClickListener());

        FloatingActionButton finishExerciseFab = fragmentView.findViewById(R.id
                .finish_exercise_fab);
        finishExerciseFab.setOnClickListener(finishExerciseFabClickListener());

        exerciseSets = new ArrayList<>();
        exerciseSetAdapter = new ArrayAdapter<>(activity, R.layout.simple_list_item,
                exerciseSets);

        ListView completedSetsListView = fragmentView.findViewById(R.id.completed_sets_list_view);
        completedSetsListView.setAdapter(exerciseSetAdapter);
        completedSetsListView.setOnItemLongClickListener(completedSetsListViewLongClickListener());

        return fragmentView;
    }

    private AdapterView.OnItemLongClickListener completedSetsListViewLongClickListener() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                                           long id) {
                createEditOrDeleteDialog(position);
                return true;
            }
        };
    }

    private void createEditOrDeleteDialog(final int setIndex) {
        ListView listView = new ListView(activity);

        final ArrayList<String> renameOrDelete = new ArrayList<>();
        final String edit = "Edit";
        final String delete = "Delete";
        renameOrDelete.add(edit);
        renameOrDelete.add(delete);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity,
                android.R.layout.simple_list_item_1, renameOrDelete);
        listView.setAdapter(arrayAdapter);

        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String action = renameOrDelete.get(position);
                if (action.equals(edit)) {
                    createEditSetDialog(setIndex);
                } else if (action.equals(delete)) {
                    createDeleteSetDialog(setIndex);
                }
                alertDialog.dismiss();
            }
        });
        alertDialog.setView(listView);
        alertDialog.show();
    }

    private void createEditSetDialog(final int setIndex) {
        final AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setMessage("Edit Set")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", null)
                .create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button= alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
            }
        });

        alertDialog.show();
    }

    private void createDeleteSetDialog(final int exerciseIndex) {
        new AlertDialog.Builder(activity)
                .setMessage("Are you sure you want to delete this set?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteExerciseSet(exerciseIndex);
                        Snackbar.make(fragmentView, "Set deleted.",
                                Snackbar.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteExerciseSet(int exerciseIndex) {
        exerciseSets.remove(exerciseIndex);
        for(int i = exerciseIndex; i < exerciseSets.size(); i++) {
            exerciseSets.get(i).setSetNumber(i + 1);
        }
        exerciseSetAdapter.notifyDataSetChanged();
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

    private View.OnClickListener addSetButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String repsString = repsTextInputLayout.getEditText().getText().toString();
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

                String weightString = weightTextInputLayout.getEditText().getText().toString();
                int weight = weightString.isEmpty() ? 0 : Integer.parseInt(weightString);
                int setNumber = exerciseSets.size() + 1;

                ExerciseSet exerciseSet = new ExerciseSet(reps, weight, exerciseId, setNumber);
                exerciseSets.add(exerciseSet);
                exerciseSetAdapter.notifyDataSetChanged();

                Snackbar.make(activity.findViewById(android.R.id.content), "Set added.",
                        Snackbar.LENGTH_SHORT).show();
            }
        };
    }

    private View.OnClickListener finishExerciseFabClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                saveSets();
                activity.finish();
            }
        };
    }

    private void saveSets() {
        for (ExerciseSet exerciseSet : exerciseSets) {
            exerciseDB.insertSet(exerciseSet);
        }
    }

    public void onBackPressed() {
        if (exerciseSets.size() == 0) {
            activity.finish();
            return;
        }
        createDiscardChangesDialog();
    }

    private void createDiscardChangesDialog() {
        new AlertDialog.Builder(activity)
                .setTitle("Discard changes?")
                .setMessage("Are you sure you want to close this exercise? Any unsaved changes " +
                        "will be lost.")
                .setNeutralButton("Cancel", null)
                .setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveSets();
                        activity.finish();
                    }
                })
                .show();
    }
}
