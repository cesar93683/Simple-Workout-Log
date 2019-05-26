package com.example.ceamaya.workoutapp.ExerciseActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ceamaya.workoutapp.R;
import com.example.ceamaya.workoutapp.Workout;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.example.ceamaya.workoutapp.MainActivity.MainActivity.exerciseDB;

public class WorkoutHistoryFragment extends Fragment {

    public static final String EXTRA_EXERCISE_ID = "EXTRA_EXERCISE_ID";
    public static final String EXTRA_TIME = "EXTRA_TIME";
    public static final String EXTRA_EXERCISE_NAME = "EXTRA_EXERCISE_NAME";
    private static final String ARGS_EXERCISE_ID = "ARGS_EXERCISE_ID";
    private static final String ARGS_EXERCISE_NAME = "ARGS_EXERCISE_NAME";
    private static final String TAG = "WorkoutHistoryFragment";
    private static final int REQUEST_CODE_EDIT_WORKOUT = 1;
    private int exerciseId;
    private String exerciseName;
    private View fragmentView;
    private ArrayList<Workout> workouts;
    private WorkoutHistoryAdapter workoutHistoryAdapter;

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
        fragmentView = getLayoutInflater().inflate(R.layout.fragment_exercise_history,
                container, false);
        workouts = exerciseDB.getWorkouts(exerciseId);

        workoutHistoryAdapter = new WorkoutHistoryAdapter
                (getActivity(), workouts);

        ListView workoutHistoryListView = fragmentView.findViewById(R.id
                .workout_history_list_view);

        workoutHistoryListView.setAdapter(workoutHistoryAdapter);

        workoutHistoryListView.setOnItemLongClickListener(workoutHistoryListViewLongClickListener
                ());

        return fragmentView;
    }

    @NonNull
    private AdapterView.OnItemLongClickListener workoutHistoryListViewLongClickListener() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long
                    id) {
                createEditOrDeleteDialog(position);
                return true;
            }
        };
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
                        Intent intent = new Intent(getActivity(), EditExerciseActivity.class);
                        intent.putExtra(EXTRA_EXERCISE_ID, exerciseId);
                        intent.putExtra(EXTRA_EXERCISE_NAME, exerciseName);
                        intent.putExtra(EXTRA_TIME, workouts.get(position).getDate().getTime());
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

    private void createDeleteDialog(final int exerciseIndex) {
        new AlertDialog.Builder(getActivity())
                .setMessage("Are you sure you want to delete this workout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        exerciseDB.deleteWorkout(workouts.get(exerciseIndex).getDate().getTime());
                        workouts.clear();
                        workouts.addAll(exerciseDB.getWorkouts(exerciseId));
                        workoutHistoryAdapter.notifyDataSetChanged();
                        Snackbar.make(fragmentView, "Workout deleted.",
                                Snackbar.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_EDIT_WORKOUT) {
            workouts.clear();
            workouts.addAll(exerciseDB.getWorkouts(exerciseId));
            workoutHistoryAdapter.notifyDataSetChanged();
            Snackbar.make(fragmentView, "Workout updated.", Snackbar.LENGTH_SHORT).show();
        }
    }
}
