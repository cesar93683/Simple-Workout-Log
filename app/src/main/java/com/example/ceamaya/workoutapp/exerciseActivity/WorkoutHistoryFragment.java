package com.example.ceamaya.workoutapp.exerciseActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ceamaya.workoutapp.ExerciseSet;
import com.example.ceamaya.workoutapp.R;
import com.example.ceamaya.workoutapp.Workout;
import com.example.ceamaya.workoutapp.exerciseActivity.editExerciseActivity.EditExerciseActivity;
import com.example.ceamaya.workoutapp.labs.WorkoutLab;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class WorkoutHistoryFragment extends Fragment {

    private static final String ARGS_EXERCISE_ID = "ARGS_EXERCISE_ID";
    private static final String ARGS_EXERCISE_NAME = "ARGS_EXERCISE_NAME";
    private static final int REQUEST_CODE_EDIT_WORKOUT = 1;
    private int exerciseId;
    private String exerciseName;
    private View fragmentView;
    private ArrayList<Workout> workouts;
    private WorkoutHistoryAdapter workoutHistoryAdapter;
    private WorkoutLab workoutLab;

    public WorkoutHistoryFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(int exerciseId, String exerciseName) {
        WorkoutHistoryFragment fragment = new WorkoutHistoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_EXERCISE_ID, exerciseId);
        args.putString(ARGS_EXERCISE_NAME, exerciseName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            exerciseId = getArguments().getInt(ARGS_EXERCISE_ID);
            exerciseName = getArguments().getString(ARGS_EXERCISE_NAME);
        }
        workoutLab = WorkoutLab.get(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentView = getLayoutInflater().inflate(R.layout.fragment_exercise_history, container,
                false);
        workouts = workoutLab.getWorkouts(exerciseId);

        RecyclerView workoutHistoryRecyclerView =
                fragmentView.findViewById(R.id.workout_history_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        workoutHistoryRecyclerView.setLayoutManager(linearLayoutManager);
        workoutHistoryAdapter = new WorkoutHistoryAdapter(workouts);
        workoutHistoryRecyclerView.setAdapter(workoutHistoryAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                workoutHistoryRecyclerView.getContext(), linearLayoutManager.getOrientation());
        workoutHistoryRecyclerView.addItemDecoration(dividerItemDecoration);

        return fragmentView;
    }


    private void createEditOrDeleteDialog(final int position) {
        @SuppressLint("InflateParams") final View dialogView =
                getActivity().getLayoutInflater().inflate(R.layout.dialog_edit_or_delete, null);

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setView(dialogView);

        dialogView.findViewById(R.id.edit_linear_layout).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = EditExerciseActivity.newIntent(getActivity(), exerciseName,
                                exerciseId, workouts.get(position).getTimeStamp());
                        alertDialog.dismiss();
                        startActivityForResult(intent, REQUEST_CODE_EDIT_WORKOUT);
                    }
                });
        dialogView.findViewById(R.id.delete_linear_layout).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createDeleteDialog(position);
                        alertDialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void createDeleteDialog(final int position) {
        new AlertDialog.Builder(getActivity())
                .setMessage("Are you sure you want to delete this workout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        workoutLab.deleteWorkout(workouts.get(position).getTimeStamp());
                        updateWorkouts();
                        Snackbar.make(fragmentView, "Workout deleted.",
                                Snackbar.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void updateWorkouts() {
        workouts.clear();
        workouts.addAll(workoutLab.getWorkouts(exerciseId));
        workoutHistoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_EDIT_WORKOUT) {
            updateWorkouts();
            Snackbar.make(fragmentView, "Workout updated.", Snackbar.LENGTH_SHORT).show();
        }
    }

    private class WorkoutHistoryHolder extends RecyclerView.ViewHolder implements
            View.OnLongClickListener {

        private final TextView timeTextView;
        private final LinearLayout exerciseSetsContainer;
        private final TextView dateTextView;
        private int position;

        WorkoutHistoryHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.history_list_item, parent, false));
            itemView.setOnLongClickListener(this);
            dateTextView = itemView.findViewById(R.id.date_text_view);
            timeTextView = itemView.findViewById(R.id.time_text_view);
            exerciseSetsContainer = itemView.findViewById(R.id.exercise_sets_container);
        }

        void bind(Workout workout, int position) {
            this.position = position;

            String dateText = DateFormat.getDateInstance().format(workout.getTimeStamp());
            dateTextView.setText(dateText);

            String timeText = DateFormat.getTimeInstance().format(workout.getTimeStamp());
            timeTextView.setText(timeText);

            exerciseSetsContainer.removeAllViews();

            for (ExerciseSet exerciseSet : workout.getExerciseSets()) {
                @SuppressLint("InflateParams") TextView textView = (TextView) LayoutInflater.from
                        (getActivity()).inflate(R.layout.simple_list_item, null);
                textView.setText(exerciseSet.toString());
                textView.setBackground(null);
                exerciseSetsContainer.addView(textView);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            createEditOrDeleteDialog(position);
            return true;
        }
    }

    private class WorkoutHistoryAdapter extends RecyclerView.Adapter<WorkoutHistoryHolder> {

        private final List<Workout> workouts;

        WorkoutHistoryAdapter(List<Workout> workouts) {
            this.workouts = workouts;
        }

        @NonNull
        @Override
        public WorkoutHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new WorkoutHistoryHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull WorkoutHistoryHolder holder, int position) {
            Workout workout = workouts.get(position);
            holder.bind(workout, position);
        }

        @Override
        public int getItemCount() {
            return workouts.size();
        }
    }
}
