package com.example.ceamaya.workoutapp.ExerciseActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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

import static com.example.ceamaya.workoutapp.MainActivity.MainActivity.exerciseDB;

public class WorkoutHistoryFragment extends Fragment {

    private static final String ARGS_EXERCISE_ID = "ARGS_EXERCISE_ID";
    private static final String TAG = "WorkoutHistoryFragment";
    private int exerciseId;
    private View fragmentView;
    private ArrayList<Workout> workouts;
    private WorkoutHistoryAdapter workoutHistoryAdapter;

    public WorkoutHistoryFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(int exerciseId) {
        WorkoutHistoryFragment fragment = new WorkoutHistoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_EXERCISE_ID, exerciseId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            exerciseId = getArguments().getInt(ARGS_EXERCISE_ID);
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
                        // edit
                        alertDialog.dismiss();
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
}
