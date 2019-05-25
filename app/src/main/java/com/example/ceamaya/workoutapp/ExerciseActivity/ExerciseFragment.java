package com.example.ceamaya.workoutapp.ExerciseActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ceamaya.workoutapp.ExerciseSet;
import com.example.ceamaya.workoutapp.R;

import java.util.ArrayList;

import static com.example.ceamaya.workoutapp.MainActivity.MainActivity.exerciseDB;

public class ExerciseFragment extends Fragment {

    private static final String ARGS_EXERCISE_ID = "ARGS_EXERCISE_ID";
    private static final String ARGS_EXERCISE_NAME = "ARGS_EXERCISE_NAME";
    View fragmentView;
    private Activity activity;
    private ArrayList<ExerciseSet> exerciseSets;
    private ArrayAdapter<ExerciseSet> exerciseSetAdapter;
    private int exerciseId;
    private String exerciseName;

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

        TextInputLayout repsTextInputLayout = fragmentView.findViewById(R.id
                .reps_text_input_layout);
        TextInputLayout weightTextInputLayout = fragmentView.findViewById(R.id
                .weight_text_input_layout);

        TextView exerciseTextView = fragmentView.findViewById(R.id.exercise_text_view);
        exerciseTextView.setText(exerciseName);

        Button decreaseRepButton = fragmentView.findViewById(R.id.decrease_rep_button);
        decreaseRepButton.setOnClickListener(decreaseButtonClickListener(repsTextInputLayout));

        Button increaseRepButton = fragmentView.findViewById(R.id.increase_rep_button);
        increaseRepButton.setOnClickListener(increaseButtonClickListener(repsTextInputLayout));

        Button decreaseWeightButton = fragmentView.findViewById(R.id
                .decrease_weight_button);
        decreaseWeightButton.setOnClickListener(decreaseButtonClickListener(weightTextInputLayout));

        Button increaseWeightButton = fragmentView.findViewById(R.id
                .increase_weight_button);
        increaseWeightButton.setOnClickListener(increaseButtonClickListener
                (weightTextInputLayout));

        Button addSetButton = fragmentView.findViewById(R.id.add_set_button);
        addSetButton.setOnClickListener(addSetButtonClickListener(repsTextInputLayout,
                weightTextInputLayout));

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

    @NonNull
    private View.OnClickListener decreaseButtonClickListener(final TextInputLayout
                                                                     textInputLayout) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        };
    }

    @NonNull
    private View.OnClickListener increaseButtonClickListener(final TextInputLayout
                                                                        textInputLayout) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        };
    }

    private View.OnClickListener addSetButtonClickListener(
            final TextInputLayout repsTextInputLayout,
            final TextInputLayout weightTextInputLayout) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String repsString = repsTextInputLayout.getEditText().getText().toString();
                if (repsString.isEmpty()) {
                    repsTextInputLayout.setError("Please enter at least 1 rep");
                    return;
                }
                int reps = Integer.parseInt(repsString);
                if (reps == 0) {
                    repsTextInputLayout.setError("Please enter at least 1 rep");
                    return;
                }
                repsTextInputLayout.setErrorEnabled(false);

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

    private void saveSets() {
        for (ExerciseSet exerciseSet : exerciseSets) {
            exerciseDB.insertSet(exerciseSet);
        }
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
    public static void centerDialogContent(Dialog dialog) {
        ViewGroup decorView = (ViewGroup) dialog.getWindow().getDecorView();
        View content = decorView.getChildAt(0);
        FrameLayout.LayoutParams contentParams = (FrameLayout.LayoutParams)content.getLayoutParams();
        contentParams.gravity = Gravity.CENTER;
        content.setLayoutParams(contentParams);
    }

    private void createEditSetDialog(final int setIndex) {
        @SuppressLint("InflateParams") View exerciseSetEditorView =
                activity.getLayoutInflater().inflate(R.layout.exercise_set_editor, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setView(exerciseSetEditorView)
                .setMessage("Edit Set")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", null)
                .create();

        final TextInputLayout repsTextInputLayout = exerciseSetEditorView.findViewById(R.id
                .reps_text_input_layout);
        int reps = exerciseSets.get(setIndex).getReps();
        repsTextInputLayout.getEditText().setText(String.valueOf(reps));

        final TextInputLayout weightTextInputLayout = exerciseSetEditorView.findViewById(R.id
                .weight_text_input_layout);
        int weight = exerciseSets.get(setIndex).getWeight();
        weightTextInputLayout.getEditText().setText(String.valueOf(weight));

        Button decreaseRepButton = exerciseSetEditorView.findViewById(R.id.decrease_rep_button);
        decreaseRepButton.setOnClickListener(decreaseButtonClickListener(repsTextInputLayout));

        Button increaseRepButton = exerciseSetEditorView.findViewById(R.id.increase_rep_button);
        increaseRepButton.setOnClickListener(increaseButtonClickListener(repsTextInputLayout));

        Button decreaseWeightButton = exerciseSetEditorView.findViewById(R.id
                .decrease_weight_button);
        decreaseWeightButton.setOnClickListener(decreaseButtonClickListener(weightTextInputLayout));

        Button increaseWeightButton = exerciseSetEditorView.findViewById(R.id
                .increase_weight_button);
        increaseWeightButton.setOnClickListener(increaseButtonClickListener
                (weightTextInputLayout));

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editSet(repsTextInputLayout, weightTextInputLayout, alertDialog, setIndex);
                    }
                });
            }
        });
centerDialogContent(alertDialog);
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

    private void editSet(
            final TextInputLayout repsTextInputLayout,
            final TextInputLayout weightTextInputLayout, AlertDialog alertDialog, int setIndex) {
        String repsString = repsTextInputLayout.getEditText().getText().toString();
        if (repsString.isEmpty()) {
            repsTextInputLayout.setError("Please enter at least 1 rep");
            return;
        }
        int reps = Integer.parseInt(repsString);
        if (reps == 0) {
            repsTextInputLayout.setError("Please enter at least 1 rep");
            return;
        }

        String weightString = weightTextInputLayout.getEditText().getText().toString();
        int weight = weightString.isEmpty() ? 0 : Integer.parseInt(weightString);

        ExerciseSet exerciseSet = exerciseSets.get(setIndex);
        exerciseSet.setWeight(weight);
        exerciseSet.setReps(reps);
        exerciseSetAdapter.notifyDataSetChanged();

        alertDialog.dismiss();
        Snackbar.make(activity.findViewById(android.R.id.content), "Set modified.",
                Snackbar.LENGTH_SHORT).show();

    }

    private void deleteExerciseSet(int exerciseIndex) {
        exerciseSets.remove(exerciseIndex);
        for (int i = exerciseIndex; i < exerciseSets.size(); i++) {
            exerciseSets.get(i).setSetNumber(i + 1);
        }
        exerciseSetAdapter.notifyDataSetChanged();
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
