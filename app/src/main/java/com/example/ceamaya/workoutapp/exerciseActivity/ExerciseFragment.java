package com.example.ceamaya.workoutapp.exerciseActivity;

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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.ceamaya.workoutapp.ExerciseSet;
import com.example.ceamaya.workoutapp.R;
import com.example.ceamaya.workoutapp.labs.WorkoutLab;

import java.util.ArrayList;
import java.util.List;

public class ExerciseFragment extends Fragment {

    private static final String ARGS_EXERCISE_ID = "ARGS_EXERCISE_ID";
    private static final String ARGS_TIME_STAMP = "ARGS_TIME_STAMP";
    private static final long NO_TIME_STAMP = -1;
    private ArrayList<ExerciseSet> exerciseSets;
    private View fragmentView;
    private Activity activity;
    private int exerciseId;
    private long timeStamp;
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
        if (getArguments() != null) {
            exerciseId = getArguments().getInt(ARGS_EXERCISE_ID);
            timeStamp = getArguments().getLong(ARGS_TIME_STAMP);
        }
        activity = getActivity();
        hasBeenModified = false;

        exerciseSets = new ArrayList<>();
        if (timeStamp == NO_TIME_STAMP) {
            isEditing = false;
        } else {
            WorkoutLab workoutLab = WorkoutLab.get(activity);
            exerciseSets.addAll(workoutLab.getWorkout(exerciseId, timeStamp).getExerciseSets());
            isEditing = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_exercise, container, false);

        TextInputLayout repsTextInputLayout = fragmentView.findViewById(R.id
                .reps_text_input_layout);

        Button decreaseRepButton = fragmentView.findViewById(R.id.decrease_rep_button);
        decreaseRepButton.setOnClickListener(decreaseButtonClickListener(repsTextInputLayout));

        Button increaseRepButton = fragmentView.findViewById(R.id.increase_rep_button);
        increaseRepButton.setOnClickListener(increaseButtonClickListener(repsTextInputLayout));

        TextInputLayout weightTextInputLayout = fragmentView.findViewById(R.id
                .weight_text_input_layout);

        Button decreaseWeightButton = fragmentView.findViewById(R.id.decrease_weight_button);
        decreaseWeightButton.setOnClickListener(decreaseButtonClickListener(weightTextInputLayout));

        Button increaseWeightButton = fragmentView.findViewById(R.id.increase_weight_button);
        increaseWeightButton.setOnClickListener(increaseButtonClickListener
                (weightTextInputLayout));

        Button addSetButton = fragmentView.findViewById(R.id.add_set_button);
        addSetButton.setOnClickListener(addSetButtonClickListener(repsTextInputLayout,
                weightTextInputLayout));

        FloatingActionButton finishExerciseFab = fragmentView.findViewById(R.id
                .finish_exercise_fab);
        finishExerciseFab.setOnClickListener(finishExerciseFabClickListener());

        RecyclerView exerciseSetsRecyclerView =
                fragmentView.findViewById(R.id.exercise_sets_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        exerciseSetsRecyclerView.setLayoutManager(linearLayoutManager);
        exerciseSetsAdapter = new ExerciseAdapter(exerciseSets);
        exerciseSetsRecyclerView.setAdapter(exerciseSetsAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                exerciseSetsRecyclerView.getContext(), linearLayoutManager.getOrientation());
        exerciseSetsRecyclerView.addItemDecoration(dividerItemDecoration);

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

    private View.OnClickListener addSetButtonClickListener(final TextInputLayout
                                                                   repsTextInputLayout,
                                                           final TextInputLayout
                                                                   weightTextInputLayout) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateReps(repsTextInputLayout)) {
                    return;
                }

                int reps = Integer.parseInt(repsTextInputLayout.getEditText().getText().toString());
                String weightString = weightTextInputLayout.getEditText().getText().toString();
                int weight = weightString.isEmpty() ? 0 : Integer.parseInt(weightString);
                int setNumber = exerciseSets.size() + 1;

                ExerciseSet exerciseSet = new ExerciseSet(reps, weight, exerciseId, setNumber);
                exerciseSets.add(exerciseSet);
                exerciseSetsAdapter.notifyDataSetChanged();
                hasBeenModified = true;
                Snackbar.make(activity.findViewById(android.R.id.content), R.string.set_added,
                        Snackbar.LENGTH_SHORT).show();
            }
        };
    }

    private View.OnClickListener finishExerciseFabClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ((SaveSets) activity).saveSets(exerciseSets);
            }
        };
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
        @SuppressLint("InflateParams") final View dialogView =
                activity.getLayoutInflater().inflate(R.layout.dialog_edit_or_delete, null);

        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setView(dialogView);

        dialogView.findViewById(R.id.edit_linear_layout).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createEditSetDialog(position);
                        alertDialog.dismiss();
                    }
                });
        dialogView.findViewById(R.id.delete_linear_layout).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createDeleteSetDialog(position);
                        alertDialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void createEditSetDialog(final int position) {
        @SuppressLint("InflateParams") View exerciseSetEditorView =
                activity.getLayoutInflater().inflate(R.layout.exercise_set_editor, null);

        final AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setMessage(R.string.edit_set)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.save, null)
                .create();

        setViewToCenterInDialog(exerciseSetEditorView, alertDialog);

        final TextInputLayout repsTextInputLayout = exerciseSetEditorView.findViewById(R.id
                .reps_text_input_layout);
        int reps = exerciseSets.get(position).getReps();
        repsTextInputLayout.getEditText().setText(String.valueOf(reps));

        final TextInputLayout weightTextInputLayout = exerciseSetEditorView.findViewById(R.id
                .weight_text_input_layout);
        int weight = exerciseSets.get(position).getWeight();
        weightTextInputLayout.getEditText().setText(String.valueOf(weight));

        Button decreaseRepButton = exerciseSetEditorView.findViewById(R.id.decrease_rep_button);
        decreaseRepButton.setOnClickListener(decreaseButtonClickListener(repsTextInputLayout));

        Button increaseRepButton = exerciseSetEditorView.findViewById(R.id.increase_rep_button);
        increaseRepButton.setOnClickListener(increaseButtonClickListener(repsTextInputLayout));

        Button decreaseWeightButton =
                exerciseSetEditorView.findViewById(R.id.decrease_weight_button);
        decreaseWeightButton.setOnClickListener(decreaseButtonClickListener(weightTextInputLayout));

        Button increaseWeightButton =
                exerciseSetEditorView.findViewById(R.id.increase_weight_button);
        increaseWeightButton.setOnClickListener(increaseButtonClickListener
                (weightTextInputLayout));

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editSet(repsTextInputLayout, weightTextInputLayout, alertDialog, position);
                    }
                });
            }
        });
        alertDialog.show();
    }

    private void createDeleteSetDialog(final int position) {
        new AlertDialog.Builder(activity)
                .setMessage(R.string.are_you_sure_delete_set)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteExerciseSet(position);
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    private void setViewToCenterInDialog(View exerciseSetEditorView, AlertDialog alertDialog) {
        FrameLayout container = new FrameLayout(activity);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        exerciseSetEditorView.setLayoutParams(params);
        container.addView(exerciseSetEditorView);
        alertDialog.setView(container);
    }

    private void editSet(final TextInputLayout repsTextInputLayout,
                         final TextInputLayout weightTextInputLayout, AlertDialog alertDialog,
                         int position) {
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
        Snackbar.make(activity.findViewById(android.R.id.content), R.string.set_updated,
                Snackbar.LENGTH_SHORT).show();
    }

    private void deleteExerciseSet(int position) {
        exerciseSets.remove(position);
        for (int i = position; i < exerciseSets.size(); i++) {
            exerciseSets.get(i).setSetNumber(i + 1);
        }
        exerciseSetsAdapter.notifyDataSetChanged();
        hasBeenModified = true;
        Snackbar.make(fragmentView, R.string.set_deleted,
                Snackbar.LENGTH_SHORT).show();
    }

    public void onBackPressed() {
        if (!isEditing && exerciseSets.size() == 0) {
            activity.finish();
        } else if (isEditing && !hasBeenModified) {
            activity.finish();
        } else {
            createDiscardChangesDialog();
        }
    }

    private void createDiscardChangesDialog() {
        new AlertDialog.Builder(activity)
                .setTitle(R.string.discard_changes)
                .setMessage(R.string.are_you_sure_close_exercise)
                .setNeutralButton(R.string.cancel, null)
                .setNegativeButton(R.string.discard, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                })
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((SaveSets) activity).saveSets(exerciseSets);
                    }
                })
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